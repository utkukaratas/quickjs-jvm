package com.utkukaratas.qjs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.EnumSet;

public class qjs {
    public static void main(String[] args) throws Exception {
        System.out.println(">>> QJS main");

        types t;

        JSRuntime rt = new JSRuntime();
        JSContext ctx = newCustomContext(rt);

        // xxx
//        if (expr) {
//            if (eval_buf(ctx, expr, strlen(expr), "<cmdline>", 0))
//                goto fail;
//        } else
//        if (optind >= argc) {
//            /* interactive mode */
//            interactive = 1;
//        } else {
//            const char *filename;
        String filename = args[0];
        int module = -1;
        try {
            if (evalFile(ctx, filename, module) != 0) {
                // xxx goto fail.. which is finally
            }

        } finally {
            // xxx
//            JS_FreeContext(ctx);
//            JS_FreeRuntime(rt);
        }

    }

    public static JSContext newCustomContext(JSRuntime rt) {
        JSContext ctx = new JSContext(rt);

        // xxx
//        js_init_module_std(ctx, "std");
//        js_init_module_os(ctx, "os");

        return ctx;
    }

    public static int evalFile(JSContext ctx, String filename, int module) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filename)));
        return evalBuf(ctx, content, filename);
    }

    public static int evalBuf(JSContext ctx, String buf, String filename) {
        JSValue val;
        int ret;

        val = quickjs.JS.eval(ctx, buf, filename, EnumSet.of(EvalFlag.JS_EVAL_TYPE_GLOBAL));

        if (quickjs.JS.isException(val)) {
//            js_std_dump_error(ctx); xxx
            ret = -1;
        } else {
            ret = 0;
        }

        // JS_FreeValue(ctx, val); xxx

        return ret;
    }

}
