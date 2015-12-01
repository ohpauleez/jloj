package jloj.lang;

import java.util.function.Supplier;
import java.util.function.Function;
import java.util.function.BiFunction;
import clojure.lang.IFn;
import clojure.lang.ISeq;
import clojure.lang.Var;
import clojure.lang.ArityException;

public interface Func extends IFn, Supplier, Function, BiFunction {

    //public static Var _proto = Jloj.defineProtocol(this, "jloj.lang", "A protocol from this interface");

    default void run() {
        invoke();
    }
    default Object call() {
        return invoke();
    }
    default Object get() {
        return invoke();
    }
    default Object apply() {
        return invoke();
    }
    default Object apply(final Object x) {
        return invoke(x);
    }
    default Object apply(final Object x, final Object y) {
        return invoke(x, y);
    }
    default Func andThen(final Func after) {
        return Func.of(after.compose(this));
    }
    default Func andThen(final Function after) {
        return Func.of(after.compose(this));
    }

    public static Func of(final Supplier fn) {
        return new Func() {
            public void run() {
                fn.get();
            }
            public Object call() {
                return fn.get();
            }
            public Object get() {
                return fn.get();
            }
            public Object apply() {
                return fn.get();
            }
            public Object applyTo(ISeq arglist) {
                int c = arglist.count();
                if (c == 0) {
                    return fn.get();
                } else {
                    return new ArityException(c, fn.toString());
                }
            }
            public Object invoke() {
                return fn.get();
            }
            public Object invoke(Object arg1) {
                return new ArityException(1, fn.toString());
            }
            public Object invoke(Object arg1, Object arg2) {
                return new ArityException(2, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3) {
                return new ArityException(3, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4) {
                return new ArityException(4, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) {
                return new ArityException(5, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6) {
                return new ArityException(6, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7) {
                return new ArityException(7, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8) {
                return new ArityException(8, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9) {
                return new ArityException(9, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10) {
                return new ArityException(10, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11) {
                return new ArityException(11, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12) {
                return new ArityException(12, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13) {
                return new ArityException(13, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14) {
                return new ArityException(14, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15) {
                return new ArityException(15, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16) {
                return new ArityException(16, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16, Object arg17) {
                return new ArityException(17, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16, Object arg17, Object arg18) {
                return new ArityException(18, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16, Object arg17, Object arg18, Object arg19) {
                return new ArityException(19, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16, Object arg17, Object arg18, Object arg19, Object arg20) {
                return new ArityException(20, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16, Object arg17, Object arg18, Object arg19, Object arg20,
                    Object... args) {
                return new ArityException(args.length+20, fn.toString());
            }
        };
    }
    public static Func of(final Function fn) {
        return new Func() {
            public Object apply(Object x) {
                return fn.apply(x);
            }
            public Object applyTo(ISeq arglist) {
                int c = arglist.count();
                if (c == 1) {
                    return fn.apply(arglist.first());
                } else {
                    return new ArityException(c, fn.toString());
                }
            }
            public Object invoke() {
                return new ArityException(0, fn.toString());
            }
            public Object invoke(Object arg1) {
                return fn.apply(arg1);
            }
            public Object invoke(Object arg1, Object arg2) {
                return new ArityException(2, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3) {
                return new ArityException(3, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4) {
                return new ArityException(4, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) {
                return new ArityException(5, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6) {
                return new ArityException(6, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7) {
                return new ArityException(7, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8) {
                return new ArityException(8, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9) {
                return new ArityException(9, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10) {
                return new ArityException(10, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11) {
                return new ArityException(11, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12) {
                return new ArityException(12, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13) {
                return new ArityException(13, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14) {
                return new ArityException(14, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15) {
                return new ArityException(15, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16) {
                return new ArityException(16, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16, Object arg17) {
                return new ArityException(17, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16, Object arg17, Object arg18) {
                return new ArityException(18, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16, Object arg17, Object arg18, Object arg19) {
                return new ArityException(19, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16, Object arg17, Object arg18, Object arg19, Object arg20) {
                return new ArityException(20, fn.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16, Object arg17, Object arg18, Object arg19, Object arg20,
                    Object... args) {
                return new ArityException(args.length+20, fn.toString());
            }
        };
    }
    public static Func of(final BiFunction fn) {
        return new Func() {
            public Object apply(Object x, Object y) {
                return fn.apply(x, y);
            }
            public Object applyTo(ISeq arglist) {
                int c = arglist.count();
                if (c == 2) {
                    return fn.apply(arglist.first(), arglist.next().first());
                } else {
                    return new ArityException(c, fn.toString());
                }
            }
            public Object invoke() {
                return new ArityException(0, fn.toString());
            }
            public Object invoke(Object arg1) {
                return new ArityException(1, fn.toString());
            }
            public Object invoke(Object arg1, Object arg2) {
                return fn.apply(arg1, arg2);
            }
            public Object invoke(Object arg1, Object arg2, Object arg3) {
                return new ArityException(3, fn.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4) {
                return new ArityException(4, fn.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) {
                return new ArityException(5, fn.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6) {
                return new ArityException(6, fn.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7) {
                return new ArityException(7, fn.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8) {
                return new ArityException(8, fn.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9) {
                return new ArityException(9, fn.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10) {
                return new ArityException(10, fn.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11) {
                return new ArityException(11, fn.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12) {
                return new ArityException(12, fn.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13) {
                return new ArityException(13, fn.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14) {
                return new ArityException(14, fn.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15) {
                return new ArityException(15, fn.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16) {
                return new ArityException(16, fn.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16, Object arg17) {
                return new ArityException(17, fn.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16, Object arg17, Object arg18) {
                return new ArityException(18, fn.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16, Object arg17, Object arg18, Object arg19) {
                return new ArityException(19, fn.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16, Object arg17, Object arg18, Object arg19, Object arg20) {
                return new ArityException(20, fn.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16, Object arg17, Object arg18, Object arg19, Object arg20,
                    Object... args) {
                return new ArityException(args.length+20, fn.toString());
            }
        };
    }
    public static Func of(final IFn fn) {
        return new Func() {
            public Object invoke() {
                return fn.invoke();
            }
            public Object invoke(Object arg1) {
                return fn.invoke(arg1);
            }

            public Object invoke(Object arg1, Object arg2) {
                return fn.invoke(arg1, arg2);
            }

            public Object invoke(Object arg1, Object arg2, Object arg3) {
                return fn.invoke(arg1, arg2, arg3);
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4) {
                return fn.invoke(arg1, arg2, arg3, arg4);
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) {
                return fn.invoke(arg1, arg2, arg3, arg4, arg5);
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6) {
                return fn.invoke(arg1, arg2, arg3, arg4, arg5, arg6);
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7) {
                return fn.invoke(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8) {
                return fn.invoke(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9) {
                return fn.invoke(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10) {
                return fn.invoke(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11) {
                return fn.invoke(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12) {
                return fn.invoke(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13) {
                return fn.invoke(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13);
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14) {
                return fn.invoke(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15) {
                return fn.invoke(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16) {
                return fn.invoke(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16, Object arg17) {
                return fn.invoke(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17);
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16, Object arg17, Object arg18) {
                return fn.invoke(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18);
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16, Object arg17, Object arg18, Object arg19) {
                return fn.invoke(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19);
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16, Object arg17, Object arg18, Object arg19, Object arg20) {
                return fn.invoke(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20);
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16, Object arg17, Object arg18, Object arg19, Object arg20,
                    Object... args) {
                return fn.invoke(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, args);
            }

            public Object applyTo(ISeq arglist) {
                return fn.applyTo(arglist);
            }
        };
    }
    public static Func of(final Supplier fn1, final Function fn2) {
        return new Func() {
            public void run() {
                fn1.get();
            }
            public Object call() {
                return fn1.get();
            }
            public Object get() {
                return fn1.get();
            }
            public Object apply() {
                return fn1.get();
            }
            public Object apply(Object x) {
                return fn2.apply(x);
            }
            public Object applyTo(ISeq arglist) {
                int c = arglist.count();
                if (c == 0) {
                    return fn1.get();
                } else if (c == 1) {
                    return fn2.apply(arglist.first());
                } else {
                    return new ArityException(0, fn1.toString());
                }
            }
            public Object invoke() {
                return fn1.get();
            }
            public Object invoke(Object arg1) {
                return fn2.apply(arg1);
            }
            public Object invoke(Object arg1, Object arg2) {
                return new ArityException(2, this.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3) {
                return new ArityException(3, fn1.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4) {
                return new ArityException(4, fn1.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) {
                return new ArityException(5, fn1.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6) {
                return new ArityException(6, fn1.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7) {
                return new ArityException(7, fn1.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8) {
                return new ArityException(8, fn1.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9) {
                return new ArityException(9, fn1.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10) {
                return new ArityException(10, fn1.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11) {
                return new ArityException(11, fn1.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12) {
                return new ArityException(12, fn1.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13) {
                return new ArityException(13, fn1.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14) {
                return new ArityException(14, fn1.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15) {
                return new ArityException(15, fn1.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16) {
                return new ArityException(16, fn1.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16, Object arg17) {
                return new ArityException(17, fn1.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16, Object arg17, Object arg18) {
                return new ArityException(18, fn1.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16, Object arg17, Object arg18, Object arg19) {
                return new ArityException(19, fn1.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16, Object arg17, Object arg18, Object arg19, Object arg20) {
                return new ArityException(20, fn1.toString());
            }

            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16, Object arg17, Object arg18, Object arg19, Object arg20,
                    Object... args) {
                return new ArityException(args.length+20, fn1.toString());
            }
        };

    }
    public static Func of(final Supplier fn1, final Function fn2, final BiFunction fn3) {
        return new Func() {
            public void run() {
                fn1.get();
            }
            public Object call() {
                return fn1.get();
            }
            public Object get() {
                return fn1.get();
            }
            public Object apply() {
                return fn1.get();
            }
            public Object apply(Object x) {
                return fn2.apply(x);
            }
            public Object apply(Object x, Object y) {
                return fn3.apply(x, y);
            }
            public Object applyTo(ISeq arglist) {
                int c = arglist.count();
                if (c == 0) {
                    return fn1.get();
                } else if (c == 1) {
                    return fn2.apply(arglist.first());
                } else if (c == 2) {
                    return fn3.apply(arglist.first(), arglist.next().first());
                } else {
                    return new ArityException(c, fn1.toString());
                }
            }
            public Object invoke() {
                return fn1.get();
            }
            public Object invoke(Object arg1) {
                return fn2.apply(arg1);
            }
            public Object invoke(Object arg1, Object arg2) {
                return fn3.apply(arg1, arg2);
            }
            public Object invoke(Object arg1, Object arg2, Object arg3) {
                return new ArityException(3, fn1.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4) {
                return new ArityException(4, fn1.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) {
                return new ArityException(5, fn1.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6) {
                return new ArityException(6, fn1.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7) {
                return new ArityException(7, fn1.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8) {
                return new ArityException(8, fn1.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9) {
                return new ArityException(9, fn1.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10) {
                return new ArityException(10, fn1.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11) {
                return new ArityException(11, fn1.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12) {
                return new ArityException(12, fn1.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13) {
                return new ArityException(13, fn1.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14) {
                return new ArityException(14, fn1.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15) {
                return new ArityException(15, fn1.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16) {
                return new ArityException(16, fn1.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16, Object arg17) {
                return new ArityException(17, fn1.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16, Object arg17, Object arg18) {
                return new ArityException(18, fn1.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16, Object arg17, Object arg18, Object arg19) {
                return new ArityException(19, fn1.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16, Object arg17, Object arg18, Object arg19, Object arg20) {
                return new ArityException(20, fn1.toString());
            }
            public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                    Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                    Object arg15, Object arg16, Object arg17, Object arg18, Object arg19, Object arg20,
                    Object... args) {
                return new ArityException(args.length+20, fn1.toString());
            }
        };
    }
}
