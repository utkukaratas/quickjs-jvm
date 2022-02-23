package com.utkukaratas.qjs;

/*
 * atoms in quickjs is basically an array of values
 * (mostly string, but some enums too) indexed by and atom (basically an int).
 *
 * seriously:
 *   typedef uint32_t JSAtom;
 *
 * however this JSAtom type is used around a lot.
 *
 * ie. JSAtom func_name;
 * */

// xxx
///* Warning: 'p' is freed */
//static JSAtom JS_NewAtomStr(JSContext *ctx, JSString *p)
//{
//    JSRuntime *rt = ctx->rt;
//    uint32_t n;
//    if (is_num_string(&n, p)) {
//        if (n <= JS_ATOM_MAX_INT) {
//            js_free_string(rt, p);
//            return __JS_AtomFromUInt32(n);
//        }
//    }
//    /* XXX: should generate an exception */
//    return __JS_NewAtom(rt, p, JS_ATOM_TYPE_STRING);
//}
//
//JSAtom JS_NewAtomLen(JSContext *ctx, const char *str, size_t len)
//{
//    JSValue val;
//
//    if (len == 0 || !is_digit(*str)) {
//        JSAtom atom = __JS_FindAtom(ctx->rt, str, len, JS_ATOM_TYPE_STRING);
//        if (atom)
//            return atom;
//    }
//    val = JS_NewStringLen(ctx, str, len);
//    if (JS_IsException(val))
//        return JS_ATOM_NULL;
//    return JS_NewAtomStr(ctx, JS_VALUE_GET_STRING(val));
//}
//
//JSAtom JS_NewAtom(JSContext *ctx, const char *str)
//{
//    return JS_NewAtomLen(ctx, str, strlen(str));
//}

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

enum JSAtomKind {
    JS_ATOM_NULL("null"),
    JS_ATOM__EVAL_("<eval>"),
    JS_ATOM__ret_("<ret>"),

    JS_ATOM_CUSTOM("CUSTOM-ATOM");

    private final String name;
    private final String value;

    JSAtomKind(String name, String value) {
        this.name = name;
        this.value = value;
    }

    JSAtomKind(String name) {
        this(name, "");
    }
}

//
// so atom is just an index to an atoms table!
// we could go with an integer but let's try to go with clever usage of enums
//
class JSAtom {
    private static int counter = 0;
    public int value;

    JSAtom() {
        this.value = ++counter;
    }

    @Override
    public String toString() {
        return "JSAtom{" +
                "value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JSAtom jsAtom = (JSAtom) o;
        return value == jsAtom.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

public class Atoms {
    // xxx registrye ekle!!!
    public static final JSAtom JS_ATOM_NULL = new JSAtom();
    public static final JSAtom JS_ATOM__ret_ = new JSAtom();
    public static final JSAtom JS_ATOM__EVAL_ = new JSAtom();
    private Map<Integer, String> registry = new HashMap<>();

    public JSAtom newAtom(String s) {
        JSAtom atom = new JSAtom();
        registry.put(atom.value, s);

        return atom;
    }
}
