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

enum COLOURS_CS {
    // Constants that belong to this carrier set:
    yellow,
    red,
    green
    // End of constants
};

// SET THAT CONTAINS ALL ELEMENTS OF COLOURS
Set<COLOURS_CS> COLOURS({yellow, red, green});

int main() {

	cout << "Hello World!!!" << endl; // prints Hello World!!!

	Set<COLOURS_CS> s1({yellow,red});

	Relation<COLOURS_CS,int> r1({Tuple<COLOURS_CS,int>(yellow,2),Tuple<COLOURS_CS,int>(red,20)});

	Relation<int,int> r2({Tuple<int,int>(0,50), Tuple<int,int>(1,80), Tuple<int,int>(2,10)});
	Relation<int,int> r3({ Tuple<int,int>(80,100), Tuple<int,int>(10,700) });

	Relation<int,int> r4({ Tuple<int,int>(1,4764), Tuple<int,int>(8,918) });

	cout << "Set r2: " << r2 << endl;
	cout << "Set r3: " << r3 << endl;

	Set<int> s3({0,1,100});

	cout << "Forward Composition r2;r3: " << r2.ForwardComposition(r3) << endl;

	cout << "Backward Composition r2(circ)r3: " << r3.BackwardComposition(r2) << endl;

	cout << "RelationalOverride r2<+r4: " << r2.RelationalOverride(r4) << endl;

	cout << r2.parallelProduct(r3);

	return 0;
}
