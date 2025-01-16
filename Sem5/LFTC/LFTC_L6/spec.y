%{
    #include "stdio.h"
    #include "stdlib.h"
    #include "string.h"

    extern int yylineno;

    extern int yylex();
    void yyerror(const char *s);

    #include <iostream>
    #include <fstream>
    #include <sstream>
    #include <vector>
    #include <string>
    #include <map>

    #include "expression_utils.cpp"

    using namespace std;

    map<string, int> symbolTable;

    vector<string> code_segment;
%}

%union {
    char* sVal;
}

%token <sVal> IDENTIFIER
%token <sVal> INTEGER

%type <sVal> expression term factor

%token PLUS MINUS
%token TIMES

%token MAIN LPAREN RPAREN LBRACE RBRACE SEMICOLON

%token INT EQUALS 

%token CIN IN_OP COUT OUT_OP

%%
program: MAIN LPAREN RPAREN LBRACE instruction_list RBRACE
    ;

instruction_list: instruction instruction_list
    | instruction
    ;

instruction: declaration SEMICOLON
    | assignment SEMICOLON
    | input SEMICOLON
    | output SEMICOLON
    ;

declaration: INT IDENTIFIER { symbolTable[$2] = 0; }

assignment: IDENTIFIER EQUALS expression { 
        if (symbolTable.find($1) == symbolTable.end()) {
            cerr << "Error: variable " << $1 << " not declared" << endl;
            exit(1);
        }

        vector<string> polishPostix = infixToPostfix($3);

        for (auto s : polishPostix) {
            cout << s << " ";
        }
        cout << endl;

        vector<string> asmCode = generateAsmCode(polishPostix, string($1));

        for (auto s : asmCode) {
            code_segment.push_back(s);
        }
    }

expression: term { $$ = strdup($1); }
    | expression PLUS term { 
        string infix = std::string($1) + " + " + string($3);
        $$ = strdup(infix.c_str());
    }
    | expression MINUS term { 
        string infix = std::string($1) + " - " + string($3);
        $$ = strdup(infix.c_str());
    }
    ;

term: factor { $$ = strdup($1); }
    | term TIMES factor {
        string infix = string($1) + " * " + string($3);
        $$ = strdup(infix.c_str());
    }
    ;

factor: INTEGER { $$ = strdup($1); }
    | IDENTIFIER { $$ = strdup($1); }
    | LPAREN expression RPAREN { string infix = "(" + string($2) + ")"; $$ = strdup(infix.c_str()); }
    ;

input: CIN IN_OP IDENTIFIER {
        stringstream ss;

        ss << "push dword " << $3;
        code_segment.push_back(ss.str());
        ss.str("");

        ss << "push dword input_format";
        code_segment.push_back(ss.str());
        ss.str("");

        ss << "call scanf";
        code_segment.push_back(ss.str()); 
        ss.str("");

        ss << "add esp, 4 * 2";
        code_segment.push_back(ss.str());

        code_segment.push_back("");
    }
    ;

output: COUT OUT_OP IDENTIFIER {
        stringstream ss;

        ss << "push dword [" << $3 << "]";
        code_segment.push_back(ss.str());
        ss.str("");

        ss << "push dword output_format";
        code_segment.push_back(ss.str());
        ss.str("");

        ss << "call printf";
        code_segment.push_back(ss.str());
        ss.str("");

        ss << "add esp, 4 * 2";
        code_segment.push_back(ss.str());

        code_segment.push_back("");
    }
    ;

%%
void yyerror(const char *s) {
    cerr << "Error: " << s << " at line " << yylineno << endl;
    exit(1);
}

int main() {
    yyparse();

    string filename = "output.asm";
    ofstream fout(filename);

    fout << "bits 32" << endl;
    fout << endl;

    fout << "global start" << endl;
    fout << endl;

    fout << "extern exit" << endl;
    fout << "extern scanf" << endl;
    fout << "extern printf" << endl;
    fout << endl;

    fout << "section .data" << endl;

    for (auto it = symbolTable.begin(); it != symbolTable.end(); it++) {
        fout << '\t' << it->first << " dd 0" << endl;
    }

    fout << '\t' << "input_format db \"%d\", 0" << endl;
    fout << '\t' << "output_format db \"%d\", 10, 0" << endl;
    fout << endl;

    fout << "section .text" << endl;
    fout << endl;

    fout << "start:" << endl;

    for (auto it = code_segment.begin(); it != code_segment.end(); it++) {
        fout << '\t' << *it << endl;
    }

    fout << '\t' << "push dword 0" << endl;
    fout << '\t' << "call exit" << endl;

    fout.close();

    return 0;
}