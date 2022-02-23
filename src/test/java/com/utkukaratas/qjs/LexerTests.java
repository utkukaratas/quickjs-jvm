package com.utkukaratas.qjs;

import static org.junit.jupiter.api.Assertions.*;

class LexerTests {

    @org.junit.jupiter.api.Test
    void test_next_token() {

        var code = "console.log(11 * 23 + 45);";
        var rt = new JSRuntime();
        var ctx = new JSContext(rt);
        var pstate = new JSParseState(ctx, code, "<lexer-test>");

        var t = Lexer.next_token(pstate);

        assertEquals(0, t);
        assertEquals(Tokens.TOK_IDENT, pstate.token.val);
        assertEquals(1, pstate.token.line_num);

        t = Lexer.next_token(pstate);
        assertEquals(0, t);
        assertEquals(Tokens.TOK_DOT, pstate.token.val);

        t = Lexer.next_token(pstate);
        assertEquals(0, t);
        assertEquals(Tokens.TOK_IDENT, pstate.token.val);


    }
}