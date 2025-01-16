%{
    #include "stdio.h"
    #include "stdlib.h"
    #include "string.h"

    #include "spec.tab.h"

    extern int yylineno;  
%}

%option noyywrap

WHITESPACE [ \t\r\n]

INTEGER [+-]?[0-9]+
IDENTIFIER [a-zA-Z][a-zA-Z0-9]*

%%

{WHITESPACE} { 
    if (strcmp(yytext, "\n") == 0) {
        yylineno++;
    }
 }

"main" { return MAIN; }
"(" { return LPAREN; }
")" { return RPAREN; }
"{" { return LBRACE; }
"}" { return RBRACE; }
";" { return SEMICOLON; }

"int" { return INT; }

"=" { return EQUALS; }

"cin" { return CIN; }
">>" { return IN_OP; }

"cout" { return COUT; }
"<<" { return OUT_OP; }

{INTEGER} { yylval.sVal = strdup(yytext); return INTEGER; }

{IDENTIFIER} { yylval.sVal = strdup(yytext); return IDENTIFIER; }

"+" { return PLUS; }
"-" { return MINUS; }
"*" { return TIMES; }

. { fprintf(stderr, "Unrecognized character: %s at line %d\n", yytext, yylineno); exit(1); }

%%
