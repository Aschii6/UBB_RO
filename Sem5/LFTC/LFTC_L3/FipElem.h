#pragma once

class FipElem {
public:
	int code;
	int TSIndex;

	FipElem(int code, int TSIndex) : code(code), TSIndex(TSIndex) {}
	FipElem(int code) : code(code), TSIndex(-1) {}
};
