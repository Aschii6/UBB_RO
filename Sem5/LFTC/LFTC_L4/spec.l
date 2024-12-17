%{
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "spec.tab.h"

extern int yylineno;
%}

%option noyywrap

WHITESPACE [ \t\r\n]

INT [+-]?[1-9][0-9]*
FLOAT [+-]?[0-9]+\.[0-9]+
ID [a-zA-Z][a-zA-Z0-9]*

LOGICAL_OPERATOR "&&"|"||"
RELATIONAL_OPERATOR "=="|"!="|"<="|">="|"<"|">"

%%

{WHITESPACE} {
	if (strchr(yytext, '\n')) {
		yylineno++;
	}
}

"int main" { return PROGRAM_START; }

"cin" { return CIN; }
"cout" { return COUT; }
">>" { return IN_OPERATOR; }
"<<" { return OUT_OPERATOR; }

"int" { return TYPE_KEYWORD; }
"float" { return TYPE_KEYWORD; }

"if" { return IF; }
"else" { return ELSE; }
"while" { return WHILE; }
"struct" { return STRUCT; }

{INT} { yylval.iVal = atoi(yytext); return INTEGER_CONSTANT; }
{FLOAT} { yylval.fVal = atof(yytext); return FLOAT_CONSTANT; }
{ID} { yylval.sVal = strdup(yytext); return IDENTIFIER; }

"(" { return L_PARENTHESIS; }
")" { return R_PARENTHESIS; }
"{" { return L_BRACE; }
"}" { return R_BRACE; }
";" { return SEMICOLON; }
"," { return COMMA; }

{LOGICAL_OPERATOR} { return LOGICAL_OPERATOR; }
{RELATIONAL_OPERATOR} { return RELATIONAL_OPERATOR; }

"=" { return ASSIGNMENT_OPERATOR; }

"+" { return ADD; }
"-" { return SUB; }
"*" { return MUL; }
"/" { return DIV; }
"%" { return MOD; }


. { fprintf(stderr, "Unknown character: %s at line %d\n", yytext, yylineno); exit(1); }

%%
