#pragma once

#include <iostream>
#include <string>
#include <vector>
#include <map>
#include <algorithm>

#include "Token.h"
#include "FipElem.h"
#include "HashNode.h"
#include "MyHashTable.h"

using namespace std;

class Parser {
	map<string, int> atomsCode = {
		{"IDENTIFIER", 0},
		{"CONSTANT", 1},
		{"main", 2},
		{"int", 3},
		{"float", 4},
		{"struct", 5},
		{"cin", 6},
		{"cout", 7},
		{"if", 8},
		{"else", 9},
		{"while", 10},
		{"{", 11},
		{"}", 12},
		{"(", 13},
		{")", 14},
		{";", 15},
		{",", 16},
		{"+", 17},
		{"-", 18},
		{"*", 19},
		{"/", 20},
		{"%", 21},
		{"<<", 22},
		{">>", 23},
		{"==", 24},
		{"!=", 25},
		{"<=", 26},
		{"<", 27},
		{">=", 28},
		{">", 29},
		{"&&", 30},
		{"||", 31},
		{"=", 32},
		{"cattimp", 33},
		{"executa", 34},
		{"sfcattimp", 35},
	};

	vector<FipElem> fip;
	MyHashTable symbolTable;

public:
	Parser() {}

	void parse(vector<Token> tokens) {
		for (Token token : tokens) {
			string value = token.value;
			if (token.type == IDENTIFIER || token.type == CONSTANT) {
				if (symbolTable.getNode(value) == nullptr)
					symbolTable.add(value, "");

				HashNode* node = symbolTable.getNode(value);
				int position = node->position;

				int code = token.type == IDENTIFIER ? atomsCode["IDENTIFIER"] : atomsCode["CONSTANT"];

				FipElem elem(code, position);
				fip.push_back(elem);
			}
			else {
				if (atomsCode.find(value) != atomsCode.end()) {
					int code = atomsCode[value];
					FipElem elem(code);
					fip.push_back(elem);
				}
				else {
					cout << "Parser error: unknown token: " << value << "\n";
					return;
				}
			}
		}
	}

	vector<FipElem> getFip() {
		return fip;
	}

	vector<HashNode*> getSymbolTableItems() {
		auto items = symbolTable.getItems();

		sort(items.begin(), items.end(), [](HashNode* a, HashNode* b) {
			return a->position < b->position;
			});

		return items;
	}
};