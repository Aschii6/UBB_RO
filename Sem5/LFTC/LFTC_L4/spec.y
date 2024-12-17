%{
#include <stdio.h>
#include <stdlib.h>

extern int yylineno;

extern int yylex();
void yyerror(const char *s);
%}

%debug

%union {
	int iVal;
	float fVal;
	char *sVal;
}

%token <sVal> IDENTIFIER
%token <iVal> INTEGER_CONSTANT
%token <fVal> FLOAT_CONSTANT

%token ADD SUB MUL DIV MOD
%token LOGICAL_OPERATOR ASSIGNMENT_OPERATOR IN_OPERATOR OUT_OPERATOR RELATIONAL_OPERATOR
%token TYPE_KEYWORD PROGRAM_START IF ELSE WHILE CIN COUT STRUCT
%token L_PARENTHESIS R_PARENTHESIS L_BRACE R_BRACE SEMICOLON COMMA

%left LOGICAL_OPERATOR
%left RELATIONAL_OPERATOR
%left ADD SUB
%left MUL DIV MOD

%start program

%%
program: PROGRAM_START L_PARENTHESIS R_PARENTHESIS L_BRACE instruction_list R_BRACE
	;

instruction_list: instruction instruction_list
	| instruction
	;

instruction: declaration SEMICOLON
	| struct_declaration SEMICOLON
	| assignment SEMICOLON
	| io SEMICOLON
	| control
	| repetition
	;

declaration: TYPE_KEYWORD IDENTIFIER
	| TYPE_KEYWORD IDENTIFIER ASSIGNMENT_OPERATOR expression
	;

struct_declaration: STRUCT IDENTIFIER L_BRACE declaration_list R_BRACE IDENTIFIER
	;

declaration_list: declaration SEMICOLON declaration_list
	| declaration SEMICOLON
	;

assignment: IDENTIFIER ASSIGNMENT_OPERATOR expression
	;

expression: expression ADD expression
	| expression SUB expression
	| expression MUL expression
	| expression DIV expression
	| expression MOD expression
	| IDENTIFIER
	| INTEGER_CONSTANT
	| FLOAT_CONSTANT
	;

io: CIN IN_OPERATOR IDENTIFIER
	| COUT OUT_OPERATOR expression
	;

control: IF L_PARENTHESIS logical_expression R_PARENTHESIS L_BRACE instruction_list R_BRACE
	| IF L_PARENTHESIS logical_expression R_PARENTHESIS L_BRACE instruction_list R_BRACE ELSE L_BRACE instruction_list R_BRACE
	;

logical_expression: expression RELATIONAL_OPERATOR expression
	| logical_expression LOGICAL_OPERATOR logical_expression
	;

repetition: WHILE L_PARENTHESIS logical_expression R_PARENTHESIS L_BRACE instruction_list R_BRACE
	;

%%

void yyerror(const char *s) {
	fprintf(stderr, "ERROR: %s at line %d\n", s, yylineno);
}

int main() {
	yydebug = 1;
	return yyparse();
}