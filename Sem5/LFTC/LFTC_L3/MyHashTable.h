#pragma once

#include <string>
#include <vector>

#include "HashNode.h"

using namespace std;

class MyHashTable {
	vector<HashNode*> bucketVector;

	int numBuckets;
	int size;
	int position;

	int getHashCode(string key) {
		int hash = 641;
		int c;

		for (int i = 0; i < key.length(); i++) {
			c = key[i];
			hash = hash * 13 + c;
		}

		return hash;
	}

	int getBucketIndex(string key) {
		int hash = getHashCode(key);
		return hash % numBuckets;
	}

	void addAux(string key, string value, int position) {
		int bucketIndex = getBucketIndex(key);
		int hashCode = getHashCode(key);
		HashNode* head = bucketVector[bucketIndex];

		while (head != nullptr) {
			if (head->key == key) {
				head->value = value;
				return;
			}
			head = head->next;
		}

		size++;
		head = bucketVector[bucketIndex];

		HashNode* newNode = new HashNode(key, value, position, hashCode);
		newNode->next = head;

		bucketVector[bucketIndex] = newNode;

		if ((1.0 * size) / numBuckets >= 0.7) {
			rehash();
		}
	}

	void rehash() {
		vector<HashNode*> temp = bucketVector;
		bucketVector.clear();

		numBuckets *= 2;
		bucketVector = vector<HashNode*>(numBuckets, nullptr);
		size = 0;

		for (HashNode* head : temp) {
			HashNode* current = head;
			while (current != nullptr) {
				addAux(current->key, current->value, current->position);
				current = current->next;
			}
		}
	}
public:
	MyHashTable() : numBuckets(10), size(0), position(0) {
		bucketVector = vector<HashNode*>(numBuckets, nullptr);
	}

	void add(string key, string value) {
		addAux(key, value, position++);
	}

	HashNode* getNode(string key) {
		int bucketIndex = getBucketIndex(key);
		HashNode* head = bucketVector[bucketIndex];

		while (head != nullptr) {
			if (head->key == key) {
				return head;
			}
			head = head->next;
		}

		return nullptr;
	}

	vector<HashNode*> getItems() {
		vector<HashNode*> items;

		for (HashNode* head : bucketVector) {
			HashNode* current = head;
			while (current != nullptr) {
				items.push_back(current);
				current = current->next;
			}
		}

		return items;
	}

	int getSize() {
		return size;
	}
};