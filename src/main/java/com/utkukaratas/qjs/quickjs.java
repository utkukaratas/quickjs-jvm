/*
 * plan:
 * - skip anything "module"
 * - skip anything "strict mode"
 *
 *
 *
 * */
package com.utkukaratas.qjs;

import java.util.EnumSet;

public class quickjs {

    // xxx define better
    static final JSValue JS_EXCEPTION = null;

    //
    //
    // internal methods
    //
    //
    static Object notImplemented() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[2];
        String methodName = e.getMethodName();

        System.out.println(methodName + " not implemented!");
        return null;
    }

    static Object notImplemented(String s) {
        System.out.println(s + " not implemented");
        return null;
    }

    static int add_closure_variables(JSContext ctx, JSFunctionDef s,
                                     JSFunctionBytecode b, int scope_idx) {
        notImplemented();
        return 0;
    }

    static int push_scope(JSParseState s) {
        notImplemented();
        return 0;
    }

    static void fail1() {
        throw new RuntimeException("xxx goto alternative lol");
    }

    public static void js_parseInit(JSContext ctx, JSParseState s, String buf, String filename) {
        s.ctx = ctx;
        s.filename = filename;
        s.line_num = 1;
        s.buf_ptr = 0; // refactor simply index instead of pointer
        s.buf_end = buf.length() - 1;
        s.token.val = Tokens.TOK_NULL; // og: s.token.val = ' ';
        s.token.line_num = 1;
    }

    // xxx constructor?
    static JSFunctionDef js_newFunctionDef(JSContext ctx, JSFunctionDef parent, boolean is_eval, boolean is_func_expr, String filename, int line_num) {
        JSFunctionDef fd = new JSFunctionDef();
        fd.ctx = ctx;
        fd.parent = parent;
        fd.is_eval = is_eval;
        fd.is_func_expr = is_func_expr;

        fd.func_name = Atoms.JS_ATOM_NULL;

        fd.line_num = line_num;

        return fd;
    }

    static void js_freeFunctionDef() {
        notImplemented();
    }

    /* return the variable index or -1 if error */
    static int add_var(JSContext ctx, JSFunctionDef fd, JSAtom name) {
        return 0;
    }

    static int next_token(JSParseState s) {
        return Lexer.next_token(s);
    }

    static int js_parse_directives(JSParseState s) {
        return 0;
    }

    static int js_parse_source_element(JSParseState s) {
        return 0;
    }

    static int js_parseProgram(JSParseState s) {
        JSFunctionDef fd = s.cur_func;
        int idx;

        if (next_token(s) != 0)
            return -1;

        if (js_parse_directives(s) != 0)
            return -1;

        fd.is_global_var = (fd.eval_type.equals(EvalFlag.JS_EVAL_TYPE_GLOBAL)) ||
                !(fd.js_mode.contains(JSModeFlag.JS_MODE_STRICT));

        /* hidden variable for the return value */
        fd.eval_ret_idx = idx = add_var(s.ctx, fd, Atoms.JS_ATOM__ret_);
        if (idx < 0)
            return -1;

        while (s.token.val != Tokens.TOK_EOF) {
            if (js_parse_source_element(s) != 0)
                return -1;
        }

        // xxx
        /* return the value of the hidden variable eval_ret_idx  */
//            emit_op(s, OP_get_loc);
//            emit_u16(s, fd.eval_ret_idx);
//            emit_op(s, OP_return);

        return 0;
    }

    static JSValue js_createFunction(JSContext ctx, JSFunctionDef fd) {
        notImplemented();
        return null;
    }


    static class JS {

        static JSAtom newAtom(JSContext ctx, String v) {
            return ctx.rt.atom_array.newAtom(v);
        }

        static void skipShebang(JSParseState s) {
            notImplemented();
        }

        static JSAtom dupAtom(JSContext ctx, JSAtom v) {
            notImplemented();
            return null;
        }

        static void freeToken() {
            notImplemented();
        }

        static void dupValue() {
            notImplemented();
        }

        // the real deal
        public static JSValue __evalInternal(JSContext ctx,
                                             JSValue thisObj,
                                             String buf,
                                             String filename,
                                             EnumSet<EvalFlag> flags,
                                             int scopeIdx) {
            JSParseState s = new JSParseState(ctx, buf, filename);
            JSParseState s1 = s; // og: *s = &s1;
            int err;
            EnumSet<JSModeFlag> js_mode = EnumSet.noneOf(JSModeFlag.class);
            EnumSet<EvalFlag> eval_type = EnumSet.noneOf(EvalFlag.class);
            JSValue fun_obj, ret_val;
            JSStackFrame sf = new JSStackFrame(); // og: not here?
            JSVarRef[] var_refs = new JSVarRef[]{}; // og: **var_refs
            JSFunctionBytecode b = new JSFunctionBytecode(); // og: nope
            JSFunctionDef fd;

            js_parseInit(ctx, s, buf, filename);
            skipShebang(s);

            eval_type.add(EvalFlag.JS_EVAL_TYPE_MASK);

            if (eval_type.equals(EnumSet.of(EvalFlag.JS_EVAL_TYPE_DIRECT))) {
                notImplemented();
            } else {
                sf = null;
                b = null;
                var_refs = null;
                js_mode = EnumSet.noneOf(JSModeFlag.class);
                if (flags.contains(EvalFlag.JS_EVAL_FLAG_STRICT))
                    js_mode.add(JSModeFlag.JS_MODE_STRICT);
                if (flags.contains(EvalFlag.JS_EVAL_FLAG_STRIP))
                    js_mode.add(JSModeFlag.JS_MODE_STRIP);
            }

            try {
                fd = js_newFunctionDef(ctx, null, true, false, filename, 1);
                if (fd == null) fail1();
                s.cur_func = fd;
                fd.eval_type = eval_type;
                fd.has_this_binding = !eval_type.equals(EnumSet.of(EvalFlag.JS_EVAL_TYPE_DIRECT));
                fd.backtrace_barrier = flags.contains(EvalFlag.JS_EVAL_FLAG_BACKTRACE_BARRIER);
                if (eval_type.equals(EnumSet.of(EvalFlag.JS_EVAL_TYPE_DIRECT))) {
                    fd.new_target_allowed = b.new_target_allowed;
                    fd.super_call_allowed = b.super_call_allowed;
                    fd.super_allowed = b.super_allowed;
                    fd.arguments_allowed = b.arguments_allowed;
                } else {
                    fd.new_target_allowed = false;
                    fd.super_call_allowed = false;
                    fd.super_allowed = false;
                    fd.arguments_allowed = true;
                }
                fd.js_mode = js_mode;
                fd.func_name = dupAtom(ctx, Atoms.JS_ATOM__EVAL_);
                if (b != null) {
                    if (add_closure_variables(ctx, fd, b, scopeIdx) != 0) fail1();
                }

                push_scope(s); /* body scope */
                fd.body_scope = fd.scope_level;

                err = js_parseProgram(s);
                if (err != 0) {
                    notImplemented();
                    // xxx
//                    fai:
//                    freeToken(s, & s.token);
//                    js.freeFunctionDef(ctx, fd);
//                    fail1();
                }

                /* create the function object and all the enclosed functions */
                fun_obj = js_createFunction(ctx, fd);
                if (isException(fun_obj)) fail1();
                /* Could add a flag to avoid resolution if necessary */
                if (flags.contains(EvalFlag.JS_EVAL_FLAG_COMPILE_ONLY)) {
                    ret_val = fun_obj;
                } else {
                    ret_val = evalFunctionInternal(ctx, fun_obj, thisObj, var_refs, sf);
                }
                return ret_val;
            } catch (RuntimeException e) {
                System.out.println("xxx exc in eval" + e);
                notImplemented();
            } finally {
                return JS_EXCEPTION;
            }
        }

        public static JSValue evalFunctionInternal(JSContext ctx,
                                                   JSValue fun_obj,
                                                   JSValue this_obj,
                                                   JSVarRef[] var_refs,
                                                   JSStackFrame sf) {
            notImplemented();
            return null;
        }

        public static JSValue evalInternal(JSContext ctx,
                                           JSValue thisObj,
                                           String buf,
                                           String filename,
                                           EnumSet<EvalFlag> evalFlags,
                                           int scopeIdx) {
//        if (unlikely(!ctx->eval_internal)) {
//            return JS_ThrowTypeError(ctx, "eval is not supported");
//        }
//        return ctx->eval_internal(ctx, this_obj, input, input_len, filename,
//                flags, scope_idx);
            return __evalInternal(ctx, thisObj, buf, filename, evalFlags, scopeIdx);
        }

        public static JSValue evalThis(JSContext ctx,
                                       JSValue thisObj,
                                       String buf,
                                       String filename,
                                       EnumSet<EvalFlag> evalFlags) {

            // xxx
//        int eval_type = eval_flags & JS_EVAL_TYPE_MASK;
//        JSValue ret;
//
//        assert(eval_type == JS_EVAL_TYPE_GLOBAL ||
//                eval_type == JS_EVAL_TYPE_MODULE);
//        ret = JS_EvalInternal(ctx, this_obj, input, input_len, filename,
//                eval_flags, -1);

            return evalInternal(ctx, thisObj, buf, filename, evalFlags, -1);
        }


        public static JSValue eval(JSContext ctx, String buf, String filename, EnumSet<EvalFlag> evalFlags) {
            System.out.println(">>> eval:" + buf);
            return evalThis(ctx, ctx.globalObj, buf, filename, evalFlags);
        }

        // xxx
        public static boolean isException(JSValue val) {
            return false;
        }
    }

}
