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


	return 0;
}
