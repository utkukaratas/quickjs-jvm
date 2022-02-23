package com.utkukaratas.qjs;

import java.util.ArrayList;
import java.util.EnumSet;

enum EvalFlag {
    JS_EVAL_TYPE_GLOBAL, /* global code (default) */
    JS_EVAL_TYPE_DIRECT, /* direct call (internal use) */
    JS_EVAL_TYPE_INDIRECT, /* indirect call (internal use) */
    JS_EVAL_TYPE_MASK,

    JS_EVAL_FLAG_STRICT, /* force 'strict' mode */
    JS_EVAL_FLAG_STRIP, /* force 'strip' mode */

    JS_EVAL_FLAG_COMPILE_ONLY,
    JS_EVAL_FLAG_BACKTRACE_BARRIER
}

enum JSModeFlag {
    JS_MODE_STRICT,
    JS_MODE_STRIP,
    JS_MODE_MATH
};

class JSParseState {
//    JSContext *ctx;
//    int last_line_num;  /* line number of last token */
//    int line_num;       /* line number of current offset */
//    const char *filename;
//    JSToken token;
//    BOOL got_lf; /* true if got line feed before the current token */
//    const uint8_t *last_ptr;
//    const uint8_t *buf_ptr;
//    const uint8_t *buf_end;
//
//    /* current function code */
//    JSFunctionDef *cur_func;
//    BOOL is_module; /* parsing a module */
//    BOOL allow_html_comments;
//    BOOL ext_json; /* true if accepting JSON superset */

    JSContext ctx;
    int last_line_num;  /* line number of last token */
    int line_num;       /* line number of current offset */
    String filename;
    String buffer; // xxx bu benden

    // refactor: pointers are just string indexes for us.
    int last_ptr;
    int buf_ptr;
    int buf_end;

    boolean got_lf;

    JSFunctionDef cur_func;
    boolean allow_html_comments;
    JSToken token;

    public JSParseState(JSContext ctx, String buf, String filename) {
        token = new JSToken();
        this.ctx = ctx;
        this.buffer = buf;
        this.filename = filename;
    }
}

class JSFunctionDef {
//    JSContext *ctx;
//    struct JSFunctionDef *parent;
//    int parent_cpool_idx; /* index in the constant pool of the parent
//                             or -1 if none */
//    int parent_scope_level; /* scope level in parent at point of definition */
//    struct list_head child_list; /* list of JSFunctionDef.link */
//    struct list_head link;
//
//    BOOL is_eval; /* TRUE if eval code */
//    int eval_type; /* only valid if is_eval = TRUE */
//    BOOL is_global_var; /* TRUE if variables are not defined locally:
//                           eval global, eval module or non strict eval */
//    BOOL is_func_expr; /* TRUE if function expression */
//    BOOL has_home_object; /* TRUE if the home object is available */
//    BOOL has_prototype; /* true if a prototype field is necessary */
//    BOOL has_simple_parameter_list;
//    BOOL has_parameter_expressions; /* if true, an argument scope is created */
//    BOOL has_use_strict; /* to reject directive in special cases */
//    BOOL has_eval_call; /* true if the function contains a call to eval() */
//    BOOL has_arguments_binding; /* true if the 'arguments' binding is
//                                   available in the function */
//    BOOL has_this_binding; /* true if the 'this' and new.target binding are
//                              available in the function */
//    BOOL new_target_allowed; /* true if the 'new.target' does not
//                                throw a syntax error */
//    BOOL super_call_allowed; /* true if super() is allowed */
//    BOOL super_allowed; /* true if super. or super[] is allowed */
//    BOOL arguments_allowed; /* true if the 'arguments' identifier is allowed */
//    BOOL is_derived_class_constructor;
//    BOOL in_function_body;
//    BOOL backtrace_barrier;
//    JSFunctionKindEnum func_kind : 8;
//    JSParseFunctionEnum func_type : 8;
//    uint8_t js_mode; /* bitmap of JS_MODE_x */
//    JSAtom func_name; /* JS_ATOM_NULL if no name */
//
//    JSVarDef *vars;
//    int var_size; /* allocated size for vars[] */
//    int var_count;
//    JSVarDef *args;
//    int arg_size; /* allocated size for args[] */
//    int arg_count; /* number of arguments */
//    int defined_arg_count;
//    int var_object_idx; /* -1 if none */
//    int arg_var_object_idx; /* -1 if none (var object for the argument scope) */
//    int arguments_var_idx; /* -1 if none */
//    int arguments_arg_idx; /* argument variable definition in argument scope,
//                              -1 if none */
//    int func_var_idx; /* variable containing the current function (-1
//                         if none, only used if is_func_expr is true) */
//    int eval_ret_idx; /* variable containing the return value of the eval, -1 if none */
//    int this_var_idx; /* variable containg the 'this' value, -1 if none */
//    int new_target_var_idx; /* variable containg the 'new.target' value, -1 if none */
//    int this_active_func_var_idx; /* variable containg the 'this.active_func' value, -1 if none */
//    int home_object_var_idx;
//    BOOL need_home_object;
//
//    int scope_level;    /* index into fd->scopes if the current lexical scope */
//    int scope_first;    /* index into vd->vars of first lexically scoped variable */
//    int scope_size;     /* allocated size of fd->scopes array */
//    int scope_count;    /* number of entries used in the fd->scopes array */
//    JSVarScope *scopes;
//    JSVarScope def_scope_array[4];
//    int body_scope; /* scope of the body of the function or eval */
//
//    int global_var_count;
//    int global_var_size;
//    JSGlobalVar *global_vars;
//
//    DynBuf byte_code;
//    int last_opcode_pos; /* -1 if no last opcode */
//    int last_opcode_line_num;
//    BOOL use_short_opcodes; /* true if short opcodes are used in byte_code */
//
//    LabelSlot *label_slots;
//    int label_size; /* allocated size for label_slots[] */
//    int label_count;
//    BlockEnv *top_break; /* break/continue label stack */
//
//    /* constant pool (strings, functions, numbers) */
//    JSValue *cpool;
//    int cpool_count;
//    int cpool_size;
//
//    /* list of variables in the closure */
//    int closure_var_count;
//    int closure_var_size;
//    JSClosureVar *closure_var;
//
//    JumpSlot *jump_slots;
//    int jump_size;
//    int jump_count;
//
//    LineNumberSlot *line_number_slots;
//    int line_number_size;
//    int line_number_count;
//    int line_number_last;
//    int line_number_last_pc;
//
//    /* pc2line table */
//    JSAtom filename;
//    int line_num;
//    DynBuf pc2line;
//
//    char *source;  /* raw source, utf-8 encoded */
//    int source_len;
//
//    JSModuleDef *module; /* != NULL when parsing a module */

    JSContext ctx;
    JSFunctionDef parent;

    EnumSet<EvalFlag> eval_type;
    EnumSet<JSModeFlag> js_mode;
    boolean has_this_binding;
    boolean backtrace_barrier;

    boolean new_target_allowed;
    boolean super_call_allowed;
    boolean super_allowed;
    boolean arguments_allowed;

    JSAtom func_name; /* JS_ATOM_NULL if no name */

    int body_scope; /* scope of the body of the function or eval */
    int scope_level;

    int line_num;

    boolean is_eval;
    boolean is_func_expr;
    boolean is_global_var; /* TRUE if variables are not defined locally:
                           eval global, eval module or non strict eval */
    int eval_ret_idx; /* variable containing the return value of the eval, -1 if none */
}

//
//
// internal types
//
//

class JSContext {
    JSRuntime rt;
    JSValue globalObj;

    JSContext(JSRuntime rt) {
        this.rt = rt;
    }
}

class JSRuntime {
    Atoms atom_array = new Atoms();
}

class JSValue {
}

class JSStackFrame {

}

class JSVarRef {

}

class JSFunctionBytecode {
    boolean new_target_allowed;
    boolean super_call_allowed;
    boolean super_allowed;
    boolean arguments_allowed;
}

class JSToken {
    int val;
    int line_num;   /* line number of token start */
    int ptr;

    // og: we use concatenated field names instead of unions
    // str
    JSValue u_str_str;
    int u_str_sep;

    // num
    JSValue u_num_val;

    // ident
    JSAtom u_ident_atom;
    boolean u_ident_has_escape;
    boolean u_ident_is_reserved;

    // regexp
    JSValue u_regexp_body;
    JSValue u_regexp_flags;
}

public class types {
    // xxx seems useful
}
