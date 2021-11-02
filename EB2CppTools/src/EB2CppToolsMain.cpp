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

	Set<int> s1({0,1,2,3,4,-2});

	cout << s1 << endl;

	NAT_SET NAT;

	cout << NAT.Contains(-2) << endl;
	cout << NAT.Contains(3) << endl;
	cout << NAT.hasSubset(s1) << endl;

	NAT = NAT.CppDifference(Set<int>({3}));

	cout << NAT.getExcludedSet() << endl;

	cout << NAT.Contains(-2) << endl;
	cout << NAT.Contains(3) << endl;
	cout << NAT.hasSubset(s1) << endl;

	NAT = NAT.CppUnion(Set<int>({-2}));

	cout << NAT.getAddedSet() << endl;

	cout << NAT.Contains(-2) << endl;
	cout << NAT.Contains(3) << endl;
	cout << NAT.hasSubset(s1) << endl;

	cout << NAT.CppIntersection(s1);

	return 0;
}
