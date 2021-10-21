#include <iostream>
#include <set>
#include <iterator>
#include <utility>
#include <algorithm>
#include <string>
#include <vector>
#include <math.h>

#ifndef EB2CPPTOOLS_H
#define EB2CPPTOOLS_H

using namespace std;

/* HOW TO PRINT CarrierSet with example PEOPLE Carrier Set

ostream& operator<<(ostream& os, const PEOPLE value){
    const char* s = 0;
    #define PROCESS_VAL(p) case(p): s = #p; break;
        switch(value){
            PROCESS_VAL(PEOPLE01);     
            PROCESS_VAL(PEOPLE02);     
            PROCESS_VAL(PEOPLE03);
        }
    #undef PROCESS_VAL

    return os << s;
}

*/

template<class T, class U>
class Tuple;

template <class T>
class Set;

template <class T, class U>
class Relation;

//// CLASS DEFINITION
template <class T, class U>
class Tuple {
    protected:
        pair<T,U> p;
    public:
    //Constructor
        Tuple(T elementA, U elementB);
        
        // Get/Set Methods
        pair<T,U> getPair() const;
        
};

// Print function for class
template <class T, class U>
ostream& operator<<(ostream& os, const Tuple<T,U>& tp);

// Less operator for class
template <class T, class U>
bool operator<(const Tuple<T,U> &lobj, const Tuple<T,U> &robj);

// Equal operator for class
template <class T, class U>
bool operator==(const Tuple<T,U> &lobj, const Tuple<T,U> &robj);

////////// AS A SPECIALIZATION OF SET CLASS !!!!!!!!!
//// CLASS DEFINITION
template <class T, class U>
class Relation {
    protected:
        set<Tuple<T,U>> innerSet;
    public:
        // Constructor
        Relation();
        Relation(set<Tuple<T,U>> startSet);
        
        // Get/Set Methods
        set<Tuple<T,U>> getInnerSet() const;
        

        void insert(Tuple<T,U> newElement);

        Relation<T,U> CppUnion(Relation<T,U> operandSet);

        Relation<T,U> CppIntersection(Relation<T,U> operandSet);

        Relation<T,U> CppSubtract(Relation<T,U> operandSet);
   
};

// Print function for class
template <class T, class U>
ostream& operator<<(ostream& os, const Relation<T,U>& s);

// Less operator for class
template <class T, class U>
bool operator<(const Relation<T,U> &lobj, const Relation<T,U> &robj);

// Equal operator for class
template <class T, class U>
bool operator==(const Relation<T,U> &lobj, const Relation<T,U> &robj);

//// CLASS DEFINITION
template <class T>
class Set {
    protected:
        set<T> innerSet;
        bool isFinite; // Will not be used for anything in current version
                        // but for possible future use.
    public:
    // Constructors
        Set();
        Set(set<T> startSet);
        
        void setInnerSet(set<T> newSet);
        set<T> getInnerSet() const;

        void setIsFinite(bool newValue);
        bool getIsFinite() const;
        
        void insert(T newElement);

        bool Contains(T element);

        bool NotContains(T element);

        bool isSubsetOrEqual(Set<T> otherSet);

        bool isNotSubsetOrEqual(Set<T> otherSet);

        bool isSubset(Set<T> otherSet);

        bool isNotSubset(Set<T> otherSet);

        Set<T> CppUnion(Set<T> operandSet);
        Set<T> CppIntersection(Set<T> operandSet);
        Set<T> CppDifference(Set<T> operandSet);

        Set<Set<T>> POW(bool includeEmpty);

        Set<Set<T>> PowerSet();

        Set<Set<T>> PowerSet1();

        int Cardinality() const;

        Set<T> GeneralUnion(Set<Set<T>> S);

        Set<T> GeneralIntersection(Set<Set<T>> S);

        template <class U>
        Relation<T,U> CartesianProduct(Set<U> rightSet);

        bool Partition(Set<Set<T>> parts);

};

// Print function for class
template <class T>
ostream& operator<<(ostream& os, const Set<T>& s);

// Less operator for class
template <class T>
bool operator<(const Set<T> &lobj, const Set<T> &robj);

// Equal operator for class
template <class T>
bool operator==(const Set<T> &lobj, const Set<T> &robj);
        
// NotEqual operator for class
template <class T>
bool operator!=(const Set<T> &lobj, const Set<T> &robj);

//// SET BOOL in Event-B
class BOOL_SET : public Set<bool> {
    public:
        BOOL_SET();
};

#include "EB2CppTools.cpp"

#endif

//////////////////////////
// ABOUT LINKING ERRORS //
//////////////////////////
// The line #include "EB2CppTools.cpp" must also be in every
// Context/Machine translated Cpp file, not just the header
// This is to avoid a linking error
// Its the Method 2 explained here:
// https://www.codeproject.com/Articles/48575/How-to-Define-a-Template-Class-in-a-h-File-and-Imp