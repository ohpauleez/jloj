
package jloj.lang;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Arrays;
import java.util.stream.Stream;
import java.util.stream.IntStream;
import java.util.stream.Collectors;
import java.util.function.Supplier;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.Comparator;

// For Clojure/Java API usage
import clojure.lang.IFn;
import clojure.lang.Symbol;
import clojure.lang.Var;
import clojure.java.api.Clojure;

// For Clojure interop usage
import clojure.lang.Ref;
import clojure.lang.Atom;
import clojure.lang.IAtom;
import clojure.lang.IDeref;
import clojure.lang.Settable;
import clojure.lang.IPersistentMap;
import clojure.lang.PersistentHashMap;
import clojure.lang.IPersistentVector;
import clojure.lang.PersistentVector;
import clojure.lang.ISeq;
import clojure.lang.ArraySeq;
import clojure.lang.RT;

// Jloj's foundations
import jloj.lang.Func;

public class Jloj {

    public static IFn REQUIRE;
    public static IFn SYMBOL;
    public static IFn READ;
    public static IFn PR_STR;
    public static IFn EVAL;
    public static IFn APPLY;
    public static IFn IN_NS;
    public static IFn ALL_NS;
    public static IFn READ_EDN_STRING;
    public static IFn AND;
    public static IFn OR;
    public static IFn INTO;
    public static IFn EMIT_PROTOCOL;
    static {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            ClassLoader loader = Jloj.class.getClassLoader();
            Thread.currentThread().setContextClassLoader(loader);
            // We need to bootstrap Clojure's internal runtime `core`; Grabbing an internal class will do this
            Class c = Class.forName("clojure.java.api.Clojure");
            REQUIRE = Clojure.var("clojure.core", "require");
            SYMBOL = Clojure.var("clojure.core", "symbol");
            READ = Clojure.var("clojure.core", "read");
            PR_STR = Clojure.var("clojure.core", "pr-str");
            EVAL = Clojure.var("clojure.core", "eval");
            APPLY = Clojure.var("clojure.core", "apply");
            IN_NS = Clojure.var("clojure.core", "in-ns");
            ALL_NS = Clojure.var("clojure.core", "all-ns");
            //REQUIRE.invoke(SYMBOL.invoke("clojure.edn"));
            READ_EDN_STRING = Clojure.var("clojure.edn", "read-string");
            AND = Clojure.var("clojure.core", "and");
            OR = Clojure.var("clojure.core", "or");
            INTO = Clojure.var("clojure.core", "into");
            EMIT_PROTOCOL = Clojure.var("jloj.core", "emit-protocol");
        } catch (Exception e) {
            System.out.println("Bootstrapping failed!!!");
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }

    private Jloj() {};

    /* Like `comp`, but composes left to right.  The list in order */
    public static Function xomp(final Function... fns) {
        return Arrays.stream(fns)
                     .reduce(Function::andThen)
                     .orElse(Function.identity());
    }
    public static Function xomp(final Object... fns) {
        return Arrays.stream(fns)
                     .map(Jloj::ensureFunction)
                     .reduce(Function::andThen)
                     .orElse(Function.identity());
    }
    public static Function xomp(final List<Object> fns) {
        return fns.stream()
                  .map(Jloj::ensureFunction)
                  .reduce(Function::andThen)
                  .orElse(Function.identity());
    }

    /* Composes like Clojure's `comp`, right to left, args in reverse order */
    public static Function comp(final Function... fns) {
        return Arrays.stream(fns)
                     .reduce(Function::compose)
                     .orElse(Function.identity());
    }
    public static Function comp(final Object... fns) {
        return Arrays.stream(fns)
                     .map(Jloj::ensureFunction)
                     .reduce(Function::compose)
                     .orElse(Function.identity());
    }
    public static Function comp(final List<Object> fns) {
        return fns.stream()
                  .map(Jloj::ensureFunction)
                  .reduce(Function::compose)
                  .orElse(Function.identity());
    }

    public static Stream concat(final Stream... streams) {
        return (Stream) Arrays.stream(streams)
                              .reduce(Stream::concat)
                              .orElseGet(Stream::empty);
    }
    public static Stream concat(final List<Stream> streams) {
        return (Stream) streams.stream()
                               .reduce(Stream::concat)
                               .orElseGet(Stream::empty);
    }

    // TODO: Implement `map` across ISeq, List, Array, and Stream.
    // TODO: Consider making `Seam` - the union of ISeq and Stream, like `Func`


    /* Like `some->` from Clojure, but with Lamdas/Methods and Clojure Functions
     * This can also be used like `maybe` */
    public static Optional pipe(final Object x) {
        return (x instanceof Optional) ? (Optional) x : Optional.ofNullable(x);
    }
    public static Optional pipe(final Object x, final Function... fns) {
        // Note: xomp does not correctly nil pun or short-circuit via Optional
        Optional maybeX = (x instanceof Optional) ? (Optional) x : Optional.ofNullable(x);
        Optional result = maybeX;
        for (Function fn : fns) {
            result = result.map(fn);
        }
        return result;
    }
    public static Optional pipe(final Object x, final Object... fns) {
        //return pipe(x, Arrays.asList(fns)); // Save some allocations...
        Optional maybeX = (x instanceof Optional) ? (Optional) x : Optional.ofNullable(x);
        Optional result = maybeX;
        for (Object fn : fns) {
            result = result.map(ensureFunction(fn));
        }
        return result;
    }
    public static Optional pipe(final Object x, final List<Object> fns) {
        Optional maybeX = (x instanceof Optional) ? (Optional) x : Optional.ofNullable(x);
        Optional result = maybeX;
        for (Object fn : fns) {
            result = result.map(ensureFunction(fn));
        }
        return result;
    }

    public static BiFunction toBiFunction(final IFn fn) {
        return (fn != null) ? (x, y) -> fn.invoke(x, y) : null;
    }
    public static BiFunction toBiFunction(final Optional<IFn> optFn) {
        if (optFn.isPresent()) {
            IFn fn = optFn.get();
            return (x, y) -> fn.invoke(x, y);
        } else {
            return null;
        }
    }

    public static Function toFunction(final IFn fn) {
        return (fn != null) ? (x) -> fn.invoke(x) : null;
    }
    public static Function toFunction(final Optional<IFn> optFn) {
        if (optFn.isPresent()) {
            IFn fn = optFn.get();
            return (x) -> fn.invoke(x);
        } else {
            return null;
        }
    }

    public static Supplier toSupplier(final IFn fn) {
        return (fn != null) ? () -> fn.invoke() : null;
    }
    public static Supplier toSupplier(final Optional<IFn> optFn) {
        if (optFn.isPresent()) {
            IFn fn = optFn.get();
            return () -> fn.invoke();
        } else {
            return null;
        }
    }

    public static Func toFunc(final IFn fn) {
        return (fn != null) ? Func.of(fn) : null;
    }
    public static Func toFunc(final Optional<IFn> optFn) {
        if (optFn.isPresent()) {
            IFn fn = optFn.get();
            return Func.of(fn);
        } else {
            return null;
        }
    }

    public static Function ensureFunction(final Object fn) {
        if (fn instanceof Func) {
            return (Func) fn;
        } else if (fn instanceof Function) {
            return (Function) fn;
        } else if (fn instanceof IFn) {
            return toFunction((IFn) fn);
        } else {
            // TODO: We should make ensureFunction a protocol, to allow others to participate in this dispatch
            throw new IllegalArgumentException("Could not coerce fn Function: "+fn.toString());
        }
    }
    public static Supplier ensureSupplier(final Object fn) {
        if (fn instanceof Func) {
            return (Func) fn;
        } else if (fn instanceof Supplier) {
            return (Supplier) fn;
        } else if (fn instanceof IFn) {
            return toSupplier((IFn) fn);
        } else {
            // TODO: We should make ensureSupplier a protocol, to allow others to participate in this dispatch
            throw new IllegalArgumentException("Could not coerce fn to Supplier: "+fn.toString());
        }
    }

    public static Object apply(final Supplier fn) {
        return fn.get();
    }
    public static Object apply(final Function fn, final Object x) {
        return fn.apply(x);
    }
    public static Object apply(final Function fn, final Object... xs) {
        return fn.apply(xs);
    }
    public static Object apply(final Function fn, final List xs) {
        return fn.apply(xs);
    }
    public static Object apply(final Function fn, Stream xs) {
        return fn.apply(xs);
    }
    public static Object apply(final IFn fn) {
        return fn.invoke();
    }
    public static Object apply(final IFn fn, final Object x) {
        return fn.invoke(x);
    }
    public static Object apply(final IFn fn, final Object... xs) {
        return APPLY.invoke(fn, xs);
    }
    public static Object apply(final IFn fn, final List xs) {
        return APPLY.invoke(fn, xs);
    }
    public static Object apply(final IFn fn, Stream xs) {
        return APPLY.invoke(fn, xs.collect(Collectors.toList()));
    }
    /* Define behaviors for Funcs, since it's ambiguous, and default to Clojure function behavior */
    public static Object apply(final Func fn) {
        return fn.invoke();
    }
    public static Object apply(final Func fn, final Object x) {
        return fn.invoke(x);
    }
    public static Object apply(final Func fn, final Object... xs) {
        return APPLY.invoke(fn, xs);
    }
    public static Object apply(final Func fn, final List xs) {
        return APPLY.invoke(fn, xs);
    }
    public static Object apply(final Func fn, Stream xs) {
        return APPLY.invoke(fn, xs.collect(Collectors.toList()));
    }
    public static Object apply(final BiFunction fn, final Object x, final Object y) {
        return fn.apply(x, y);
    }
    public static Object apply(final BiFunction fn, final Object... xs) {
        if (xs.length == 2) {
            return fn.apply(xs[0], xs[1]);
        } else {
            throw new IllegalArgumentException("Can only apply two arguments to BiFunction: "+fn.toString());
        }
    }
    public static Object apply(final BiFunction fn, final List xs) {
        if (xs.size() == 2) {
            return fn.apply(xs.get(0), xs.get(1));
        } else {
            throw new IllegalArgumentException("Can only apply two arguments to BiFunction: "+fn.toString());
        }
    }
    public static Object apply(final BiFunction fn, Stream xs) {
        return apply(fn, xs.collect(Collectors.toList()));
    }
    public static Object apply(final Comparator cmp, final Object lhs, final Object rhs) {
        return cmp.compare(lhs, rhs);
    }
    public static Object apply(final Comparator cmp, final Object... xs) {
        if (xs.length == 2) {
            return cmp.compare(xs[0], xs[1]);
        } else {
            throw new IllegalArgumentException("Can only apply two arguments to Comparator: "+cmp.toString());
        }
    }
    public static Object apply(final Comparator cmp, final List xs) {
        if (xs.size() == 2) {
            return cmp.compare(xs.get(0), xs.get(1));
        } else {
            throw new IllegalArgumentException("Can only apply two arguments to Comparator: "+cmp.toString());
        }
    }
    public static Object apply(final Comparator cmp, Stream xs) {
        return apply(cmp, xs.collect(Collectors.toList()));
    }

    public static Object readEdn(final String ednString) {
        return (ednString != null) ? READ_EDN_STRING.invoke(ednString) : null;
    }
    public static Object readEdn(final Optional<String> ednString) {
        return ednString.isPresent() ? READ_EDN_STRING.invoke(ednString.get()) : null;
    }
    public static Object readEdn(final Object opts, final String ednString){
        return (ednString != null) ? READ_EDN_STRING.invoke(opts, ednString) : null;
    }
    public static Object readEdn(final Object opts, final Optional<String> ednString) {
        return ednString.isPresent() ? READ_EDN_STRING.invoke(opts, ednString.get()) : null;
    }

    public static String asString(final Object edn) {
        return (String)PR_STR.invoke(edn);
    }
    public static String asString(final Optional<Object> edn) {
        return edn.isPresent() ? (String)PR_STR.invoke(edn.get()) : (String)PR_STR.invoke(null);
    }

    public static Object into(final Object to, final Object from) {
        return INTO.invoke(to, from);
    }
    public static Object into(final Object to, final Optional<Object> from) {
        return from.isPresent() ? INTO.invoke(to, from.get()) : to;
    }
    public static Object into(final Object to, Stream from) {
        if (to instanceof List) {
            return INTO.invoke(to, from.collect(Collectors.toList()));
        } else {
            return INTO.invoke(to, from);
        }
    }

    public static Object when(final boolean test, final Supplier body) {
        return test ? body.get() : null;
    }
    /*public static Object when(final Boolean test, final Supplier body) {
        return test ? body.get() : null;
    }*/
    public static Object when(final Object test, final Supplier body) {
        return (test == null || ((test instanceof Boolean) && ((Boolean)test == false))) ? null : body.get();
    }
    public static Object when(final Optional test, final Supplier body) {
        if ((test == null) || !test.isPresent() || (test.isPresent() && (test.get() instanceof Boolean) && ((Boolean)test.get() == false))) {
            return null;
        } else {
            return body.get();
        }
    }
    public static Object when(final Supplier test, final Supplier body) {
        Object temp = test != null ? test.get() : false;
        return ((temp instanceof Boolean) && ((Boolean)temp == false)) ? null : body.get();
    }

    public static Object cond(final List<Object> params) {
        if (params.size() % 2 != 0) {
            throw new IllegalArgumentException("cond requires an even number of parameters.");
        }
        List<Integer> indexes = IntStream.range(0, params.size()/2)
                                         .map(x -> x*2)
                                         .boxed()
                                         .collect(Collectors.toList());
        for (int idx : indexes) {
            if (truthyBool(params.get(idx))) {
                Object ret = params.get(idx+1);
                if (ret instanceof Supplier) {
                    return ((Supplier)ret).get();
                } else {
                    return ret;
                }
            }
        }
        return null;
    }

    public Object _do(Supplier body) {
        return body != null ? body.get() : null;
    }
    public Object _do(Optional<Supplier> body) {
        return (body != null) && body.isPresent() ? body.get().get() : null;
    }

    public static Object truthy(final Object test) {
        if (test == null) {
            return null;
        } else if ((test instanceof Boolean) && ((Boolean)test == false)) {
            return false;
        } else if (test instanceof Optional) {
            Optional opttest = (Optional)test;
            Object optvalue = opttest.isPresent() ? opttest.get() : false;
            if ((optvalue instanceof Boolean) && ((Boolean)optvalue == false)) {
                return false;
            } else {
                return optvalue;
            }
        } else if (test instanceof Supplier) {
            Supplier suptest = (Supplier)test;
            Object temp = suptest.get();
            if ((temp instanceof Boolean) && ((Boolean)temp == false)) {
                return false;
            } else if (temp == null) {
                return null;
            } else {
                return temp;
            }
        } else {
            return test;
        }
    }

    public static boolean truthyBool(final Object test) {
        Object x = truthy(test);
        if ((x == null) || ((x instanceof Boolean) && (Boolean)x == false)) {
            return false;
        } else {
            return true;
        }
    }
    /* Like Clojure's `and`, but understands Optional and Supplier */
    public static Object and(final Object... tests) {
        return and(Arrays.asList(tests));
    }
    public static Object and(final List<Object> tests) {
        Object result = false;
        for (Object test : tests) {
            result = truthy(test);
            if (result == null) {
                return null;
            } else if ((result instanceof Boolean) && (Boolean)result == false) {
                return false;
            }
        }
        return result;
    }
    /* Like Clojure's `or`, but understands Optional and Supplier */
    public static Object or(final Object... tests) {
        return or(Arrays.asList(tests));
    }
    public static Object or(final List<Object> tests) {
        Object result = false;
        for (Object test : tests) {
            result = truthy(test);
            if (result == null) {
            } else if ((result instanceof Boolean) && (Boolean)result == false) {
            } else {
                return result;
            }
        }
        return result;
    }

    /* Like Clojure's `some`.  If you want a boolean, prefer `Stream.anyMatch`*/
    public static Optional some(final IFn pred, Object... coll) {
        return some(pred, Arrays.asList(coll));
    }
    public static Optional some(final IFn pred, List<Object> coll) {
        return some(pred, coll.stream());
    }
    public static Optional some(final IFn pred, Stream<Object> coll) {
        return some(x -> truthyBool(pred.invoke(x)), coll);
    }
    public static Optional some(final Func pred, Object... coll) {
        return some(pred, Arrays.asList(coll));
    }
    public static Optional some(final Func pred, List<Object> coll) {
        return some(pred, coll.stream());
    }
    public static Optional some(final Func pred, Stream<Object> coll) {
        return some(x -> truthyBool(pred.invoke(x)), coll);
    }
    public static Optional some(final Predicate pred, Object... coll) {
        return some(pred, Arrays.stream(coll));
    }
    public static Optional some(final Predicate pred, List<Object> coll) {
        return some(pred, coll.stream());
    }
    public static Optional some(final Predicate pred, Stream<Object> coll) {
        return coll.filter(pred)
                   .findFirst();
    }

    /* Like Clojure's `every?` */
    public static boolean every(final IFn pred, Object... coll) {
        return every(pred, Arrays.stream(coll));
    }
    public static boolean every(final IFn pred, List<Object> coll) {
        return every(pred, coll.stream());
    }
    public static boolean every(final IFn pred, Stream<Object> coll) {
        return every(x -> truthyBool(pred.invoke(x)), coll);
    }
    public static boolean every(final Func pred, Object... coll) {
        return every(pred, Arrays.asList(coll));
    }
    public static boolean every(final Func pred, List<Object> coll) {
        return every(pred, coll.stream());
    }
    public static boolean every(final Func pred, Stream<Object> coll) {
        return every(x -> truthyBool(pred.invoke(x)), coll);
    }
    public static boolean every(final Predicate pred, Object... coll) {
        return Arrays.stream(coll)
                     .allMatch(pred);
    }
    public static boolean every(final Predicate pred, List<Object> coll) {
        return coll.stream()
                   .allMatch(pred);
    }
    public static boolean every(final Predicate pred, Stream<Object> coll) {
        return coll.allMatch(pred);
    }

    public static Var defineProtocol(Class iface) {
        return (Var) EMIT_PROTOCOL.invoke(iface);
    }
    public static Var defineProtocol(Class iface, String doc) {
        return (Var) EMIT_PROTOCOL.invoke(iface, doc);
    }
    public static Var defineProtocol(Class iface, String doc, String ns_str) {
        return (Var) EMIT_PROTOCOL.invoke(iface, doc, ns_str);
    }

    public static Object cljRequire(final String ns) {
        return REQUIRE.invoke(SYMBOL.invoke(ns));
    }
    public static Object cljRequire(final Symbol ns) {
        return REQUIRE.invoke(ns);
    }

    public static Func cljFn(final Var v) {
        return Func.of(v);
    }
    public static Func cljFn(final String symbol) {
        return Func.of(Clojure.var("clojure.core", symbol));
    }
    public static Func cljFn(final String ns, final String symbol) {
        return Func.of(Clojure.var(ns, symbol));
    }

    public static ISeq seq(ISeq coll) {
        return coll;
    }
    public static ISeq seq(Object coll) {
        return RT.seq(coll);
    }
    public static ISeq seq(Optional coll) {
        return RT.seq(coll.orElse(null));
    }
    public static ISeq seq(Stream stream) {
        return RT.seq(stream.toArray());
    }
    public static ISeq seq(Object... coll) {
        return ArraySeq.create(coll);
    }

    // Data collections
    // TODO: Create Optional-aware versions of RT's mapUniqueKeys, set, subvec
    public static IPersistentMap persistentMap(final Object... init) {
        return RT.map(init);
    }
    public static IPersistentMap persistentMap(final Optional init) {
        return RT.map(init.orElse(null));
    }
    public static IPersistentMap persistentMap(final Map init) {
        return PersistentHashMap.create(init);
    }

    public static IPersistentVector persistentVec(final Object... init) {
        return RT.vector(init);
    }
    public static IPersistentVector persistentVec(final Optional init) {
        return RT.vector(init.orElse(null));
    }
    public static IPersistentVector persistentVec(final List init) {
        return PersistentVector.create(init);
    }
    public static IPersistentVector persistentVec(final Stream init) {
        return RT.vector(init.toArray());
    }

    // Mutable/reference types
    // -----------------------
    public static Ref ref() {
        return new Ref(null);
    }
    public static Ref ref(final Object init) {
        return new Ref(init);
    }
    public static Ref ref(final Optional init) {
        return new Ref(init.orElse(null));
    }
    public static Ref ref(final Object init, final IPersistentMap meta) {
        return new Ref(init, meta);
    }
    public static Ref ref(final Optional init, final IPersistentMap meta) {
        return new Ref(init.orElse(null), meta);
    }

    public static Object commute(final Ref ref, final IFn fn, final Object... args) {
        return ref.commute(fn, seq(args));
    }
    public static Object commute(final Ref ref, final IFn fn, final List args) {
        return ref.commute(fn, seq(args));
    }
    public static Object commute(final Ref ref, final IFn fn, final Optional args) {
        return ref.commute(fn, seq(args.orElse(null)));
    }
    public static Object commute(final Ref ref, final IFn fn, final ISeq args) {
        return ref.commute(fn, args);
    }
    // We can commute with Function/BiFunction, but we're going to have to pack args up differently.
    // This will make semantics wonky, but it'll behave as expected if you're using Lambdas
    public static Object commute(final Ref ref, final Function fn) {
        return ref.commute(Func.of(fn), null);
    }
    public static Object commute(final Ref ref, final BiFunction fn, final Object arg) {
        return ref.commute(Func.of(fn), seq(new Object[]{arg}));
    }
    public static Object commute(final Ref ref, final BiFunction fn, final Optional arg) {
        return ref.commute(Func.of(fn), arg.isPresent() ? seq(new Object[]{arg.get()}) : null);
    }

    public static Object alter(final Ref ref, final IFn fn, final Object... args) {
        return ref.alter(fn, seq(args));
    }
    public static Object alter(final Ref ref, final IFn fn, final List args) {
        return ref.alter(fn, seq(args));
    }
    public static Object alter(final Ref ref, final IFn fn, final Optional args) {
        return ref.alter(fn, seq(args.orElse(null)));
    }
    public static Object alter(final Ref ref, final IFn fn, final ISeq args) {
        return ref.alter(fn, args);
    }
    // We can alter with Function/BiFunction, but we're going to have to pack args up differently.
    // This will make semantics wonky, but it'll behave as expected if you're using Lambdas
    public static Object alter(final Ref ref, final Function fn) {
        return ref.alter(Func.of(fn), null);
    }
    public static Object alter(final Ref ref, final BiFunction fn, final Object arg) {
        return ref.alter(Func.of(fn), seq(new Object[]{arg}));
    }
    public static Object alter(final Ref ref, final BiFunction fn, final Optional arg) {
        return ref.alter(Func.of(fn), arg.isPresent() ? seq(new Object[]{arg.get()}) : null);
    }

    public static Atom atom() {
        return new Atom(null);
    }
    public static Atom atom(final Object init) {
        return new Atom(init);
    }
    public static Atom atom(final Optional init) {
        return new Atom(init.orElse(null));
    }
    public static Atom atom(final Object init, final IPersistentMap meta) {
        return new Atom(init, meta);
    }
    public static Atom atom(final Optional init, final IPersistentMap meta) {
        return new Atom(init.orElse(null), meta);
    }

    public static Object swap(IAtom atom, IFn fn) {
        return atom.swap(fn);
    }
    public static Object swap(IAtom atom, Function fn) {
        return atom.swap(Func.of(fn));
    }
    public static Object swap(IAtom atom, IFn fn, Object arg) {
        return atom.swap(fn, arg);
    }
    public static Object swap(IAtom atom, IFn fn, Optional arg) {
        if (arg.isPresent()) {
            return atom.swap(fn, arg.get());
        } else {
            return null;
        }
    }
    public static Object swap(IAtom atom, BiFunction fn, Object arg) {
        return atom.swap(Func.of(fn), arg);
    }
    public static Object swap(IAtom atom, BiFunction fn, Optional arg) {
        if (arg.isPresent()) {
            return atom.swap(Func.of(fn), arg.get());
        } else {
            return null;
        }
    }
    public static Object swap(IAtom atom, IFn fn, Object arg1, Object arg2) {
        return atom.swap(fn, arg1, arg2);
    }
    public static Object swap(IAtom atom, IFn fn, Object arg1, Optional arg2) {
        if (arg2.isPresent()) {
            return atom.swap(fn, arg1, arg2.get());
        } else {
            return null;
        }
    }
    public static Object swap(IAtom atom, IFn fn, Object arg1, Object arg2, Object... args) {
        return atom.swap(fn, arg1, arg2, seq(args));
    }

    public static boolean compareAndSet(IAtom atom, Object oldv, Object newv) {
        return atom.compareAndSet(oldv, newv);
    }
    public static boolean compareAndSet(IAtom atom, Optional oldv, Object newv) {
        return atom.compareAndSet(oldv.orElse(null), newv);
    }
    public static boolean compareAndSet(IAtom atom, Object oldv, Optional newv) {
        return atom.compareAndSet(oldv, newv.orElse(null));
    }
    public static boolean compareAndSet(IAtom atom, Optional oldv, Optional newv) {
        return atom.compareAndSet(oldv.orElse(null), newv.orElse(null));
    }


    // UberReset - this set method should work across all types
    public static Object reset(final Ref ref, final Object val) {
        return ref.set(val);
    }
    public static Object reset(final Ref ref, final Optional val) {
        if (val.isPresent()) {
            return ref.set(val.get());
        } else {
            return null;
        }
    }
    public static Object reset(final Settable settable, final Object val) {
        return settable.doReset(val);
    }
    public static Object reset(final Settable settable, final Optional val) {
        if (val.isPresent()) {
            return settable.doReset(val.get());
        } else {
            return null;
        }
    }
    public static Object reset(final IAtom atom, final Object val) {
        return atom.reset(val);
    }
    public static Object reset(final IAtom atom, final Optional val) {
        if (val.isPresent()) {
            return atom.reset(val.get());
        } else {
            return null;
        }
    }
    // This works, but it feels totally gross
    /*public static Object reset(Optional opt, final Object val) {
        opt = Optional.ofNullable(val);
        return opt;
    }
    public static Object reset(Optional opt, final Optional val) {
        opt = val;
        return opt;
    }*/

    // UberDeref
    public static Object deref(final IDeref derefable) {
        return derefable.deref();
    }
    public static Object deref(final Optional opt) {
        return opt.orElse(null);
    }

}

