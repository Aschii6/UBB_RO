#pragma once

#include <string>

using namespace std;

class HashNode {
public:
	string key;
	string value;
	int position;
	int hashCode;
	HashNode* next;

	HashNode(string key, string value, int position, int hashCode) : key(key), value(value), 
		position(position), hashCode(hashCode), next(nullptr) {}
};