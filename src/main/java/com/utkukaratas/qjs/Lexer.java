package com.utkukaratas.qjs;

// xxx offffffff
class Tokens {
    public static final int TOK_IDENT = 0;

    public static final int TOK_NULL = 10000;
    public static final int TOK_EOF = 3123;
    public static final short TOK_DOT = 23234;
}

// original lexer of quickjs is several large functions behaving like a class
// with all the goto branching sprinkled around. so let's make it a real one:
public class Lexer {
    static int p;
    static char c;

    static boolean redo = false; // xxx what a hack!!!

    static int redo(JSParseState s) {
        redo = true;
        return next_token(s);
    }

    static JSAtom parse_ident(JSParseState s) {
        // todo: skips unicode
        // todo: escaped identifiers
        // todo: private idents ###
        char c;
        String identifier = "";
        JSAtom atom;

        c = s.buffer.charAt(p);
        for (; ; ) {
            if (Character.isLetterOrDigit(c)) {
                identifier += c;
            }

            // todo: daha kisa?
            c = s.buffer.charAt(++p);
            if (!Character.isLetterOrDigit(c))
                break;
        }
        atom = quickjs.JS.newAtom(s.ctx, identifier);

        System.out.println(">>> parse_ident:" + atom);

        return atom;
    }

    static int next_token(JSParseState s) {
        boolean fail = false;
        boolean ident_has_escape;
        JSAtom atom;

        ///free_token(s, &s->token);
        if (!redo) {
            p = s.last_ptr = s.buf_ptr;
            s.got_lf = false;
            s.last_line_num = s.token.line_num;
        }
        redo = false;

        ///redo:
        s.token.line_num = s.line_num;
        s.token.ptr = p;

        c = s.buffer.charAt(p);
        char c1 = s.buffer.charAt(p + 1);
        char c2 = s.buffer.charAt(p + 2);

        switch (c) {
            case 0:
                quickjs.notImplemented("lexer: null char");
                break;
            case '`':
                quickjs.notImplemented("lexer: `");
                break;
            case '\'':
            case '\"':
                quickjs.notImplemented("lexer: string");
                break;
            case '\r': /* accept DOS and MAC newline sequences */
                if (c1 == '\n') p++;
                // fall through
            case '\n':
                p++;
                s.got_lf = true;
                s.line_num++;
                redo(s);
            case '\f':
                /// case '\v':
            case ' ':
            case '\t':
                p++;
                redo(s);
            case '/':
                quickjs.notImplemented("lexer: comment");
                break;
            case '\\':
                quickjs.notImplemented("lexer: escape");
                break;
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
            case 'g':
            case 'h':
            case 'i':
            case 'j':
            case 'k':
            case 'l':
            case 'm':
            case 'n':
            case 'o':
            case 'p':
            case 'q':
            case 'r':
            case 's':
            case 't':
            case 'u':
            case 'v':
            case 'w':
            case 'x':
            case 'y':
            case 'z':
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
            case '_':
            case '$':
//                p++;
//                ident_has_escape = FALSE;
//                has_ident:
//                atom = parse_ident(s, &p, &ident_has_escape, c, FALSE);
//                if (atom == JS_ATOM_NULL)
//            goto fail;
//                s->token.u.ident.atom = atom;
//                s->token.u.ident.has_escape = ident_has_escape;
//                s->token.u.ident.is_reserved = FALSE;
//                if (s->token.u.ident.atom <= JS_ATOM_LAST_KEYWORD ||
//                        (s->token.u.ident.atom <= JS_ATOM_LAST_STRICT_KEYWORD &&
//                                (s->cur_func->js_mode & JS_MODE_STRICT)) ||
//                        (s->token.u.ident.atom == JS_ATOM_yield &&
//                                ((s->cur_func->func_kind & JS_FUNC_GENERATOR) ||
//                                        (s->cur_func->func_type == JS_PARSE_FUNC_ARROW &&
//                                                !s->cur_func->in_function_body && s->cur_func->parent &&
//                                                        (s->cur_func->parent->func_kind & JS_FUNC_GENERATOR)))) ||
//                        (s->token.u.ident.atom == JS_ATOM_await &&
//                                (s->is_module ||
//                                        (((s->cur_func->func_kind & JS_FUNC_ASYNC) ||
//                                                (s->cur_func->func_type == JS_PARSE_FUNC_ARROW &&
//                                                        !s->cur_func->in_function_body && s->cur_func->parent &&
//                                                                (s->cur_func->parent->func_kind & JS_FUNC_ASYNC))))))) {
//                    if (ident_has_escape) {
//                        s->token.u.ident.is_reserved = TRUE;
//                        s->token.val = TOK_IDENT;
//                    } else {
//                        /* The keywords atoms are pre allocated */
//                        s->token.val = s->token.u.ident.atom - 1 + TOK_FIRST_KEYWORD;
//                    }
//                } else {
//                    s->token.val = TOK_IDENT;
//                }

                p++;
                //
                atom = parse_ident(s);

                if (atom.equals(Atoms.JS_ATOM_NULL))
                     fail = true;

                s.token.u_ident_atom = atom;
//                s.token.u_ident_has_escape = ident_has_escape;
                s.token.u_ident_is_reserved = false;

                s.token.val = Tokens.TOK_IDENT;

                break;
            case '#':
                quickjs.notImplemented("lexer: private #");
                break;
            case '.':
                quickjs.notImplemented("lexer: dot");
                break;
            case '0':
                quickjs.notImplemented("lexer: number");
                break;
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                quickjs.notImplemented("lexer: number");
                break;
            case '*':
                quickjs.notImplemented("lexer: *");
                break;
            case '%':
                quickjs.notImplemented("lexer: %");
                break;
            case '+':
                quickjs.notImplemented("lexer: +");
                break;
            case '-':
                quickjs.notImplemented("lexer: -");
                break;
            case '<':
                quickjs.notImplemented("lexer: <");
                break;
            case '>':
                quickjs.notImplemented("lexer: >");
                break;
            case '=':
                quickjs.notImplemented("lexer: =");
                break;
            case '!':
                quickjs.notImplemented("lexer: !");
                break;
            case '&':
                quickjs.notImplemented("lexer: &");
                break;
            case '^':
                quickjs.notImplemented("lexer: ^");
                break;
            case '|':
                quickjs.notImplemented("lexer: |");
                break;
            case '?':
                quickjs.notImplemented("lexer: ?");
                break;
            default:
                if (c >= 128) {
                    // unicode value
                    quickjs.notImplemented("lexer: number");
                    break;
                }

                ///s.token.val = c;
                p++;

                break;
        }

        s.buf_ptr = p;

        // System.out.println(">>> nextToken:" + atom);

        if (fail) {
            System.out.println("xxx next_token fail");
            return 1;
        }

        return 0;
    }
}
