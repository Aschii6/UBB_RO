#pragma once

#include <string>

enum TokenType
{
	KEYWORD = 0,
	IDENTIFIER = 1,
	CONSTANT = 2,
	SEPARATOR = 3,
	ARITHMETIC_OPERATOR = 4,
	IO_OPERATOR = 5,
	COMPARATOR = 6,
	LOGICAL_OPERATOR = 7,
	ASSIGNMENT_OPERATOR = 8,
};

const std::string tokenTypes[] = {
	"KEYWORD",
	"IDENTIFIER",
	"CONSTANT",
	"SEPARATOR",
	"ARITHMETIC_OPERATOR",
	"IO_OPERATOR",
	"COMPARATOR",
	"LOGICAL_OPERATOR",
	"ASSIGNMENT_OPERATOR",
};

class Token {
public:
	TokenType type;
	std::string value;
	Token(TokenType type, std::string value) : type(type), value(value) {}
};