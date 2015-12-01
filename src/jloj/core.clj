(ns jloj.core
  (:require [clojure.string :as string]
            [clojure.reflect :as reflect]
            [instaparse.core :as insta])
  (:import [jloj.lang Jloj Func]))

;; TODO: This is totally broken - symbol escaping in the proto-sigs breaks in the syntax quote
(defn emit-protocol
    ([iface]
     (emit-protocol iface "" ""))
    ([iface doc-str]
     (emit-protocol iface doc-str ""))
    ([iface doc-str ns-str]
  (let [current-ns *ns*
        iface-reflected (reflect/reflect iface)]
    (when-let [meths (and (:interface (:flags iface-reflected))
                          (seq (filter #(not= (:flags %)
                                              #{:public :static}) (:members iface-reflected))))]
      (let [proto-tags (string/split (subs (str iface) 10) #"\.")
            proto-name (last proto-tags)
            ns-str (if (empty? ns-str) (string/join "." (butlast proto-tags)) ns-str)
            proto-sym (symbol ns-str proto-name)
            proto-meths (group-by :name meths)
            proto-args (reduce (fn [acc [meth-name meth-objs]]
                                 (assoc acc meth-name (into #{} (map :parameter-types meth-objs))))
                               {} proto-meths)
            proto-sigs (reduce (fn [acc [n args]]
                                 (assoc acc
                                        (keyword n) {:name (symbol n)
                                                     :arglists (mapv #(mapv gensym %) args)
                                                     :doc nil}))
                               {}
                               proto-args)
            meth-map (reduce (fn [acc k]
                               (assoc acc k k)) {} (keys proto-sigs))]
        (in-ns (symbol ns-str))
        (eval `(def ~(symbol proto-name) ~doc-str
                 {:on ~(symbol (str ns-str "." proto-name))
                  :on-interface ~iface
                  :var (var ~proto-sym)
                  :sigs ~proto-sigs
                  :method-map ~meth-map
                  ;; TODO: Figure out the best way to make the method-builders
                  :method-builders {}}))
        (in-ns (symbol (str current-ns)))
        (eval `(var ~proto-sym)))))))

; StaticSig = <'static '> space* BaseName space* OptionalBinding space* [ReturnType] space* JavaBody space*
;; TODO: Add ",\n" to Value, allowing newlines to be grouped ONLY if there's a trailing comma
(def grammar "Program = [Comment] [Package] Segment*
             Comment = <'/*'> (CommentText | space)* <'*/'> space*
             CommentText = #'((?!\\*/).)*'
             Package = <'package '> PackageName space*
             BaseName = #'[\\w<>]+'
             PackageName = #'[A-Za-z0-9\\._]+'
             Segment = Comment | Import | Protocol | LambdaType | Class
             Import = <'import '> PackageName space*
             Protocol = <'protocol '> BaseName space* <'{'> space* [ProtocolPiece space*]* <'}'> space*
             ProtocolPiece = Comment | Assignment | Sig | Function | DefSig
             Assignment = <'val '> BaseName space* [ReturnType] space* <'='> space* Value
             ReturnType = <':'> space* BaseName
             Value = #'[A-Za-z0-9_\"\\./\\(\\)\\+\\-\\*%=:<>?!{}\\[\\]|`, ]+'
             Sig = <'sig '> BaseName space* Binding+ space* [ReturnType]
             Binding = <'['> space* BaseName space* BaseName space* [<','> space* BaseName space* BaseName space*]* <']'> space*
             OptionalBinding = <'['> space* [BaseName space* BaseName space* [<','> space* BaseName space* BaseName space*]*]* <']'> space*
             JavaBody = <'{'> [space* #'((?!\\}).)*']* <'}'>
             DefSig = <'def '> space* BaseName space* OptionalBinding space* [ReturnType] space* JavaBody space*
             LambdaType = <'lambda-type '> space* ['protocol '] space* BaseName space* OptionalBinding space* [ReturnType] space*
             Class = <'class '> space* BaseName space* [OptionalBinding space* Implements space*] <'{'> space* [ClassPiece space*]* <'}'> space*
             Implements = <'implements '> space* BaseName space* [BaseName space*]*
             ClassPiece = Comment | Assignment | Function | DefSig
             Function = <'fn '> space* BaseName space* OptionalBinding space* [ReturnType] space* JavaBody
             <space> = <#'\\s*'>")

(def parser (insta/parser grammar))

(def transform-opts {})

(defmulti handle-segment
  (fn [[maybe-segment & segment-parts]]
    (if (= :Segment maybe-segment)
      (ffirst segment-parts)
      maybe-segment)))

(defmethod handle-segment :Package [[_ & packages]]
  (first (keep (fn [[tag package-name]]
                    (when (= tag :PackageName)
                      [:Package (str "package " package-name ";") {}])) packages)))

(defmethod handle-segment :OptionalBinding [[_ & all-parameters]]
  (let [params (map (fn [[t v]] (str "final "(if (= t "Self") "Object" t) " " v))
                    (partition 2 (map second all-parameters)))]
    (str "(" (string/join ", " params) ")")))

(defmethod handle-segment :Binding [[_ & all-parameters]]
  (let [params (map (fn [[t v]] (str "final "(if (= t "Self") "Object" t) " " v))
                    (partition 2 (map second all-parameters)))]
    (str "(" (string/join ", " params) ")")))

(defmethod handle-segment :Comment [params]
  ;; TODO: This should really be StringBuilder
  (let [[_ [_ & maybe-comments]] params
        comments (if (string? (first maybe-comments))
                   (rest params)
                   maybe-comments)
        comment-text (string/join "\n" (keep (fn [[tag c]]
                                                 (when (= tag :CommentText)
                                                   c))
                                               comments))]
    [:Comment (str "/* " comment-text " */")
   {}]))

(defmethod handle-segment :Import [[_ [_ & imports]]]
  [:Import (str "import " (string/join ";\nimport "
                                       (keep (fn [[tag c]]
                                               (when (= tag :PackageName)
                                                 c))
                                             imports)) ";")
   {}])

(defmethod handle-segment :LambdaType [[_ [_ & args]]]
  (let [result-tag (if (= (first args) "protocol ") :LambaTypeProtocol :LambdaType)
        args (if (= (first args) "protocol ") (rest args) args)
        [_ i-name] (first args)
        params (second args)
        maybe-return (last args)
        return-type (if (= (first maybe-return) :ReturnType) (-> maybe-return second second) "Object")]
    [result-tag (str "@FunctionalInterface\npublic interface " i-name "{\npublic " return-type " " (string/lower-case i-name)
                     (handle-segment params) ";\n}")
     {:file (str i-name ".java")}]))

(defn smallest-numeric-type [x]
  (let [num-x (if (string? x) (read-string x) x)]
    (if (float? num-x)
      (cond
        (or (= Double/NEGATIVE_INFINITY num-x) (= Double/POSITIVE_INFINITY num-x)) "BigDecimal"
        :else "double")
      (cond
        (< -128 num-x 127) "byte"
        (< -32768 num-x 32767) "short"
        (< -2147483648 num-x 2147483647) "int"
        (< -9223372036854775808 num-x 9223372036854775807) "long"
        :else "BigInteger"))))

(defmethod handle-segment :Assignment [[_ & args]]
  (let [args (vec args) ;; So `get-in` will work
        [_ v-name] (first args)
        [_ value] (last args)
        value (string/trim value)
        return-type (if (= (get-in args [1 0]) :ReturnType)
                      (get-in args [1 1 1])
                      (cond
                        (string/starts-with? value "\"") "String"
                        (re-find #" " value) (throw (ex-info "Value has no return type can can't be inferred." {:value value}))
                        (re-find #"^[-]?(\d*[.])?\d+$" value) (smallest-numeric-type value)
                        :else "Object"))]
    [:Assignment (str "public static final " return-type " " v-name " = " value ";") {}]))

(defmethod handle-segment :Sig [[_ & args]]
  (let [[[_ func-name] & binds] args
        maybe-ret (last args)
        ret-type (if (= (first maybe-ret) :ReturnType)
                   (get-in maybe-ret [1 1])
                   "Object")
        base-str (str "public " ret-type " " func-name)
        sigs (map #(str base-str (handle-segment %)";")
                  (filter #(= :Binding (first %))
                          binds))]
    [:Sig (string/join "\n" sigs) {}]))

(defmethod handle-segment :Function [[_ & args]]
  (let [[[_ func-name] opt-bind maybe-ret] args
        params (handle-segment opt-bind)
        ret-type (if (= (first maybe-ret) :ReturnType)
                   (get-in maybe-ret [1 1])
                   "Object")
        body (string/join "\n" (rest (last args)))]
    [:Function (str "public static " ret-type " " func-name params " {\n" body "\n}") {}]))

(defmethod handle-segment :Protocol [[_ [_ & args]]]
  (let [[_ iface-name] (first args)
        parts (mapv (fn [[_ part]]
                      (if (= :DefSig (first part))
                        (let [[[_ func-name] opt-bind maybe-ret] (rest part)
                              params (handle-segment opt-bind)
                              ret-type (if (= (first maybe-ret) :ReturnType)
                                         (get-in maybe-ret [1 1])
                                         "Object")
                              body (string/join "\n" (rest (last (rest part))))]
                          (str "default " ret-type " " func-name params " {\n" body "\n}"))
                        (second (handle-segment part))))
                    (rest args))]
    [:Protocol
     (str "public interface " iface-name
          " {\npublic static Object _proto = Jloj.defineProtocol(this);\n"
          (string/join "\n" parts) "\n}")
     {}]))

(comment


  (emit-protocol Func)

  (def example (slurp "example.jloj"))
  (def parse-res (parser example))
  (handle-segment (nth parse-res 1))
  (handle-segment (nth parse-res 2))
  (handle-segment (nth parse-res 4))
  (handle-segment (nth parse-res 10))
  (handle-segment (nth parse-res 11))
  (handle-segment (nth parse-res 13))
  (handle-segment (get-in (nth parse-res 18) [1 6 1]))
  (handle-segment (get-in (nth parse-res 10) [1 3 1]))
  (handle-segment (get-in (nth parse-res 10) [1 4 1]))
  (handle-segment (get-in (nth parse-res 10) [1 5 1]))
  (handle-segment (get-in (nth parse-res 10) [1 6 1]))
  (handle-segment (get-in (nth parse-res 10) [1 8 1]))
  (handle-segment (get-in (nth parse-res 10) [1 10 1]))
  ;; TODO: VVVV
  (handle-segment (nth parse-res 16))
  (handle-segment (nth parse-res 18))
  (def parse-transform (insta/transform transform-opts parse-res))

  (some-> 1
          inc
          inc)
  (Jloj/pipe 1 [inc inc])
  (Jloj/pipe 1 (seq [inc inc]))

  (.orElse (Jloj/pipe 1) 5)
  (.orElse (Jloj/pipe nil) 5)

  (or (some-> 1
          inc
          ((fn [x] nil))
          inc)
      5)
  (.orElse (Jloj/pipe 1 [inc (fn [x] nil) inc]) 5)


  (Jloj/apply inc 1)
  (Jloj/apply inc [1])
  (Jloj/apply inc [1 2])
  (apply + (object-array [1 2 3]))
  (Jloj/apply + (object-array [1 2 3]))

  (Jloj/and [true [1] inc])
  (Jloj/and [true true true])
  (Jloj/and [true false true])
  (Jloj/and [true nil true])

  (.orElse (Jloj/pipe (Jloj/and [true nil true])) 42)
  (Jloj/or [(Jloj/and [true nil true]) 42])
  (= (Jloj/or [(Jloj/and [true nil true]) false nil "x"])
     (Jloj/or [(Jloj/and [true nil true]) "x" false nil]))


  (Jloj/some even? [1 3 5 7 10])
  (Jloj/every even? [1 3 5 7 10])
  (Jloj/every odd? [1 3 5 7])


  (Jloj/cond [false 1
              nil 2
              true 3])
  (Jloj/cond [true 1
              nil 2
              true 3])

  )
