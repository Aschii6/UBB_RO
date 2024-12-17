/* A Bison parser, made by GNU Bison 3.8.2.  */

/* Bison interface for Yacc-like parsers in C

   Copyright (C) 1984, 1989-1990, 2000-2015, 2018-2021 Free Software Foundation,
   Inc.

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <https://www.gnu.org/licenses/>.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.

   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */

/* DO NOT RELY ON FEATURES THAT ARE NOT DOCUMENTED in the manual,
   especially those whose name start with YY_ or yy_.  They are
   private implementation details that can be changed or removed.  */

#ifndef YY_YY_SPEC_TAB_H_INCLUDED
# define YY_YY_SPEC_TAB_H_INCLUDED
/* Debug traces.  */
#ifndef YYDEBUG
# define YYDEBUG 1
#endif
#if YYDEBUG
extern int yydebug;
#endif

/* Token kinds.  */
#ifndef YYTOKENTYPE
# define YYTOKENTYPE
  enum yytokentype
  {
    YYEMPTY = -2,
    YYEOF = 0,                     /* "end of file"  */
    YYerror = 256,                 /* error  */
    YYUNDEF = 257,                 /* "invalid token"  */
    IDENTIFIER = 258,              /* IDENTIFIER  */
    INTEGER_CONSTANT = 259,        /* INTEGER_CONSTANT  */
    FLOAT_CONSTANT = 260,          /* FLOAT_CONSTANT  */
    ADD = 261,                     /* ADD  */
    SUB = 262,                     /* SUB  */
    MUL = 263,                     /* MUL  */
    DIV = 264,                     /* DIV  */
    MOD = 265,                     /* MOD  */
    LOGICAL_OPERATOR = 266,        /* LOGICAL_OPERATOR  */
    ASSIGNMENT_OPERATOR = 267,     /* ASSIGNMENT_OPERATOR  */
    IN_OPERATOR = 268,             /* IN_OPERATOR  */
    OUT_OPERATOR = 269,            /* OUT_OPERATOR  */
    RELATIONAL_OPERATOR = 270,     /* RELATIONAL_OPERATOR  */
    TYPE_KEYWORD = 271,            /* TYPE_KEYWORD  */
    PROGRAM_START = 272,           /* PROGRAM_START  */
    IF = 273,                      /* IF  */
    ELSE = 274,                    /* ELSE  */
    WHILE = 275,                   /* WHILE  */
    CIN = 276,                     /* CIN  */
    COUT = 277,                    /* COUT  */
    STRUCT = 278,                  /* STRUCT  */
    L_PARENTHESIS = 279,           /* L_PARENTHESIS  */
    R_PARENTHESIS = 280,           /* R_PARENTHESIS  */
    L_BRACE = 281,                 /* L_BRACE  */
    R_BRACE = 282,                 /* R_BRACE  */
    SEMICOLON = 283,               /* SEMICOLON  */
    COMMA = 284                    /* COMMA  */
  };
  typedef enum yytokentype yytoken_kind_t;
#endif

/* Value type.  */
#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
union YYSTYPE
{
#line 13 "spec.y"

	int iVal;
	float fVal;
	char *sVal;

#line 99 "spec.tab.h"

};
typedef union YYSTYPE YYSTYPE;
# define YYSTYPE_IS_TRIVIAL 1
# define YYSTYPE_IS_DECLARED 1
#endif


extern YYSTYPE yylval;


int yyparse (void);


#endif /* !YY_YY_SPEC_TAB_H_INCLUDED  */
