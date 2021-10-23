//============================================================================
// Name        : EB2CppToolsMain.cpp
// Author      : Enrique Paris
// Version     :
// Copyright   : Your copyright notice
// Description : Hello World in C++, Ansi-style
//============================================================================

#include <iostream>
#include "EB2CppTools.h"
using namespace std;

int main() {
	cout << "Hello World!!!" << endl; // prints Hello World!!!

	Set<int> s1({1,2,3});

	cout << s1 << endl;

	cout << BOOL_SET() << endl;

	return 0;
}
