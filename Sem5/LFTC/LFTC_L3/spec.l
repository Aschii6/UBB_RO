%{
	#include <iostream>
	#include <fstream>
	#include <vector>
	#include <string>

	#include <stdio.h>

	#include "Token.h"
	#include "FipElem.h"
	#include "MyHashTable.h"
	#include "Parser.h"

	using namespace std;

	vector<Token> tokens;

	void addToken(TokenType type, string value) {
	    Token token(type, value);
		tokens.push_back(token);
	}

	int myLineno = 1;
%}

%option noyywrap

WHITESPACE [ \t\n\r]+
CONST [+-]?(0|[1-9][0-9]*)(\.[0-9]+)?
ID [a-zA-Z][a-zA-Z0-9]*
KEYWORD "main"|"int"|"float"|"struct"|"cin"|"cout"|"if"|"else"|"while"|"cattimp"|"executa"|"sfcattimp"
SEPARATOR "{"|"}"|"("|")"|";"|","
ARITHMETIC_OPERATOR "+"|"-"|"*"|"/"|"%"
IO_OPERATOR "<<"|">>"
COMPARATOR "=="|"!="|"<="|"<"|">="|">"
LOGICAL_OPERATOR "&&"|"||"
ASSIGNMENT_OPERATOR "="

%%

{WHITESPACE} {
	while (*yytext) {
		if (*yytext == '\n') {
			myLineno++;
		}
		yytext++;
	}
}
{KEYWORD} {
	addToken(KEYWORD, yytext);
}

{ID}({WHITESPACE}*){ARITHMETIC_OPERATOR} {
    string copy = yytext;
	string id = copy.substr(0, copy.find_first_of("+-*/% \t\r\n"));

	char op = copy.back();

	addToken(IDENTIFIER, id);
	addToken(ARITHMETIC_OPERATOR, string(1, op));
}

{CONST}({WHITESPACE}*){ARITHMETIC_OPERATOR} {
    string copy = yytext;
	string constant = copy.substr(0, copy.find_first_of("+-*/% \t\r\n"));

	char op = copy.back();

	addToken(CONSTANT, constant);
	addToken(ARITHMETIC_OPERATOR, string(1, op));
}

{ID} {
	addToken(IDENTIFIER, yytext);
}
{CONST} {
	addToken(CONSTANT, yytext);
}
{SEPARATOR} {
	addToken(SEPARATOR, yytext);
}
{ARITHMETIC_OPERATOR} {
	addToken(ARITHMETIC_OPERATOR, yytext);
}
{IO_OPERATOR} {
	addToken(IO_OPERATOR, yytext);
}
{COMPARATOR} {
	addToken(COMPARATOR, yytext);
}
{LOGICAL_OPERATOR} {
	addToken(LOGICAL_OPERATOR, yytext);
}
{ASSIGNMENT_OPERATOR} {
	addToken(ASSIGNMENT_OPERATOR, yytext);
}
. {
	cout << "Unknown token: " << yytext << " at line " << myLineno << endl;
	return 1;
}

%%

int main(int argc, char *argv[]) {
	if (argc < 2) {
		cout << "Usage: " << argv[0] << " <input_file>" << endl;
		return 1;
	}

	ifstream inputFile(argv[1]);

	FlexLexer *lexer = new yyFlexLexer();

	lexer->switch_streams(&inputFile, &cout);

	lexer->yylex();

	ofstream outputFile("tokens.txt");
	for (Token token : tokens) {
		outputFile << tokenTypes[token.type] << " " << token.value << "\n";
	}
	outputFile.close();

	Parser parser;

	parser.parse(tokens);

	vector<FipElem> fip = parser.getFip();
	ofstream fipFile("fip.txt");
	for (FipElem elem : fip) {
		fipFile << elem.code << " " << elem.TSIndex << "\n";
	}
	fipFile.close();

	vector<HashNode*> ts = parser.getSymbolTableItems();
	ofstream tsFile("ts.txt");
	for (HashNode* node : ts) {
		tsFile << node->position << " " << node->key << "\n";
	}

	return 0;
}