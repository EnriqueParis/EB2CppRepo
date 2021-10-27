/*
 * EB2CppTools.h
 *
 *  Created on: Oct 23, 2021
 *      Author: ejpar
 */

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

template<class T, class U>
class Tuple;

template <class T>
class Set;

template <class T, class U>
class Relation;

class INT_SET;

class NAT_SET;

class NAT1_SET;

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
        T getLeft() const;
        U getRight() const;

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

        bool hasSubsetOrEqual(Set<T> otherSet);

        bool hasNotSubsetOrEqual(Set<T> otherSet);

        bool hasSubset(Set<T> otherSet);

        bool hasNotSubset(Set<T> otherSet);

        Set<T> CppUnion(Set<T> operandSet);
        Set<T> CppIntersection(Set<T> operandSet);
        Set<T> CppDifference(Set<T> operandSet);

        INT_SET CppUnion(INT_SET operandSet);
		Set<T> CppIntersection(INT_SET operandSet);
		Set<T> CppDifference(INT_SET operandSet);

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

        Relation<T,U> CppDifference(Relation<T,U> operandSet);

        Set<Relation<T,U>> POW(bool includeEmpty);

        Set<Relation<T,U>> PowerSet();

        Set<Relation<T,U>> PowerSet1();

        int Cardinality() const;

        Relation<T,U> GeneralUnion(Set<Relation<T,U>> S);

        Relation<T,U> GeneralIntersection(Set<Relation<T,U>> S);

        template <class V, class W>
        Relation<Tuple<T,U>,Tuple<V,W>> CartesianProduct(Relation<V,W> rightSet);

        bool Partition(Set<Relation<T,U>> parts);

        Set<T> Domain();

        Set<U> Range();

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


//// SET BOOL in Event-B
class BOOL_SET : public Set<bool> {
    public:
        BOOL_SET();
};


//// SET P (INTEGER NUMBERS) in Event-B
class INT_SET {
	protected:
		Set<int> excludedSet; // The sets excluded from all of the integers in this expression
	public:
		INT_SET();

	    bool Contains(int element);

	    bool NotContains(int element);

	    bool hasSubsetOrEqual(Set<int> otherSet);

	    bool hasNotSubsetOrEqual(Set<int> otherSet);

	    bool hasSubset(Set<int> otherSet);

	    bool hasNotSubset(Set<int> otherSet);

	    INT_SET CppUnion(Set<int> operandSet);
	    Set<int> CppIntersection(Set<int> operandSet);
	    INT_SET CppDifference(Set<int> operandSet);

	    //template <class U>
	    //Relation<T,U> CartesianProduct(Set<U> rightSet);

};

//// SET NAT (NATURAL NUMBERS) in Event-B
class NAT_SET {
	protected:
		Set<int> excludedSet; // The sets excluded from all of the integers in this expression
		Set<int> addedSet;	//Its possible that the model adds a couple of, say, negative numbers
	public:
		NAT_SET();

	    bool Contains(int element);

	    bool NotContains(int element);

	    bool hasSubsetOrEqual(Set<int> otherSet);

	    bool hasNotSubsetOrEqual(Set<int> otherSet);

	    bool hasSubset(Set<int> otherSet);

	    bool hasNotSubset(Set<int> otherSet);

	    NAT_SET CppUnion(Set<int> operandSet);
	    Set<int> CppIntersection(Set<int> operandSet);
	    NAT_SET CppDifference(Set<int> operandSet);

	    //template <class U>
	    //Relation<T,U> CartesianProduct(Set<U> rightSet);

};

//// SET NAT1 (NATURAL EXCEPT 0 NUMBERS) in Event-B
class NAT1_SET {
	protected:
		Set<int> excludedSet; // The sets excluded from all of the integers in this expression
	public:
		NAT1_SET();

	    bool Contains(int element);

	    bool NotContains(int element);

	    bool hasSubsetOrEqual(Set<int> otherSet);

	    bool hasNotSubsetOrEqual(Set<int> otherSet);

	    bool hasSubset(Set<int> otherSet);

	    bool hasNotSubset(Set<int> otherSet);

	    NAT1_SET CppUnion(Set<int> operandSet);
	    Set<int> CppIntersection(Set<int> operandSet);
	    NAT1_SET CppDifference(Set<int> operandSet);

	    //template <class U>
	    //Relation<T,U> CartesianProduct(Set<U> rightSet);

};



//// CLASS IMPLEMENTATION
// TUPLE

//Constructor
template <class T, class U>
Tuple<T,U>::Tuple(T elementA, U elementB) {p = make_pair(elementA,elementB);}

// Get/Set Methods
template <class T, class U>
pair<T,U> Tuple<T,U>::getPair() const {return p;}

template <class T, class U>
T Tuple<T,U>::getLeft() const {return p.first;}

template <class T, class U>
U Tuple<T,U>::getRight() const {return p.second;}


// Print function for class
template <class T, class U>
ostream& operator<<(ostream& os, const Tuple<T,U>& tp)
{
    pair<T,U> pairObj = tp.getPair();
    os << '(' << pairObj.first << ',' << pairObj.second << ')';
    return os;
}

// Less operator for class
template <class T, class U>
bool operator<(const Tuple<T,U> &lobj, const Tuple<T,U> &robj){
            bool result;
            pair<T,U> firstPair = lobj.getPair();
            pair<T,U> secondPair = robj.getPair();
            result = firstPair < secondPair;
            return result;
        }

// Equal operator for class
template <class T, class U>
bool operator==(const Tuple<T,U> &lobj, const Tuple<T,U> &robj){
            bool result;
            pair<T,U> firstPair = lobj.getPair();
            pair<T,U> secondPair = robj.getPair();
            result = firstPair == secondPair;
            return result;
        }


//// CLASS IMPLEMENTATION
// SET

template <class T>
Set<T>::Set(){isFinite = false;}

template <class T>
Set<T>::Set(set<T> startSet) {innerSet = startSet; isFinite = false;}

template <class T>
void Set<T>::setInnerSet(set<T> newSet) {innerSet = newSet;}

template <class T>
set<T> Set<T>::getInnerSet() const {return innerSet;}

template <class T>
void Set<T>::setIsFinite(bool newValue) {isFinite = newValue;}

template <class T>
bool Set<T>::getIsFinite() const {return isFinite;}

template <class T>
void Set<T>::insert(T newElement) {
    innerSet.insert(newElement);
}

template <class T>
bool Set<T>::Contains(T element) { // O(log n) n=innerSet.size
    bool elementBelongs;

    auto itr = innerSet.find(element);

    if (itr == innerSet.end())
        elementBelongs = false;
    else
        elementBelongs = true;

    return elementBelongs;
}

template <class T>
bool Set<T>::NotContains(T element) { // O(log n) n=innerSet.size
    return !(Contains(element));
}

template <class T>
bool Set<T>::hasSubsetOrEqual(Set<T> otherSet) { // O(n+m)
    bool result = false;

    Set<T> intersection = CppIntersection(otherSet);

    if (intersection == otherSet)
        result = true;

    return result;
}

template <class T>
bool Set<T>::hasNotSubsetOrEqual(Set<T> otherSet) { // O(n+m)
    return !(isSubsetOrEqual(otherSet));
}

template <class T>
bool Set<T>::hasSubset(Set<T> otherSet) { // O(n+m)
    bool result;

    if ((*this) == otherSet)
        result = false;
    else {
        result = isSubsetOrEqual(otherSet);
    }

    return result;
}

template <class T>
bool Set<T>::hasNotSubset(Set<T> otherSet) {
    return !(isSubset(otherSet));
}

template <class T>
Set<T> Set<T>::CppUnion(Set<T> operandSet) { // O(n+m)
    set<T> unionResult;

    set<T> secondSet = operandSet.getInnerSet();

    set_union(innerSet.begin(), innerSet.end(),
                secondSet.begin(),secondSet.end(),
                inserter(unionResult, unionResult.begin()));

    Set<T> result(unionResult);
    return result;
}

template <class T>
Set<T> Set<T>::CppIntersection(Set<T> operandSet) { // O(n+m)
    set<T> intersectionResult;

    set<T> secondSet = operandSet.getInnerSet();

    set_intersection(innerSet.begin(), innerSet.end(),
                secondSet.begin(),secondSet.end(),
                inserter(intersectionResult, intersectionResult.begin()));

    Set<T> result(intersectionResult);
    return result;
}

template <class T>
Set<T> Set<T>::CppDifference(Set<T> operandSet) { // O(n+m)
    set<T> subtractResult;

    set<T> secondSet = operandSet.getInnerSet();

    set_difference(innerSet.begin(), innerSet.end(),
                secondSet.begin(),secondSet.end(),
                inserter(subtractResult, subtractResult.begin()));

    Set<T> result(subtractResult);
    return result;
}

template <class T>
INT_SET Set<T>::CppUnion(INT_SET operandSet) {
	return operandSet.CppUnion(*this);
};

template <class T>
Set<T> Set<T>::CppIntersection(INT_SET operandSet) {
	return operandSet.CppIntersection(*this);
};

template <class T>
Set<T> Set<T>::CppDifference(INT_SET operandSet) {
	return Set<T>();
};

template <class T>
Set<Set<T>> Set<T>::POW(bool includeEmpty) { // O( ((2^n)-2)*n )  n: innerSet size
    Set<Set<T>> result;

    int innerSetSize = innerSet.size();

    // Num of elements that the PowerSet will have
    int powerSetSize = pow(2,innerSetSize);

    int bytesToPick = 0;

    Set<T> setToAdd;

    if (includeEmpty) {
        // The first set to add to the power set is the empty set
        // Lets do that before the loop to avoid a useless iteration
        result.insert(setToAdd);
    }

    // The same for the subset that equals the entire innerSet
    result.insert(innerSet);

    // Because we did this first, we start the loop with PElementIndex = 1
    // and also stop PElementIndex one step before the end.
    // PElementIndex == powerSetSize-1 would be the step to add the subset
    //   that has all of the values of the original set

    for (int PElementIndex = 1; PElementIndex < (powerSetSize-1); PElementIndex++) {
        // We are handling a set, so we must use iterators
        // To do the trick of using bytes & and shifts
        // we need an int iterator that will also rise
        bytesToPick = 0;
        setToAdd = Set<T>();

        for (auto itr = innerSet.begin(); itr != innerSet.end(); itr++) {
            if (PElementIndex & (1 << bytesToPick)) {

                setToAdd.insert(*itr);
            }

            bytesToPick += 1;
        }

        result.insert(setToAdd);
    }

    return result;
}

template <class T>
Set<Set<T>> Set<T>::PowerSet() {
    return POW(true);
}

template <class T>
Set<Set<T>> Set<T>::PowerSet1() {// Not including the empty subset
    return POW(false);
}

template <class T>
int Set<T>::Cardinality() const { // O(1)
    return innerSet.size();
}

template <class T>
Set<T> Set<T>::GeneralUnion(Set<Set<T>> S) {
    Set<T> result;

    set<Set<T>> otherSet = S.getInnerSet();

    // Iterating through the inn
    for (auto itr = otherSet.begin(); itr != otherSet.end(); itr++) {
        result = result.CppUnion(*itr);
    }

    return result;
}

template <class T>
Set<T> Set<T>::GeneralIntersection(Set<Set<T>> S) {
    Set<T> result;

    set<Set<T>> otherSet = S.getInnerSet();

    // Iterating through the inn
    for (auto itr = otherSet.begin(); itr != otherSet.end(); itr++) {
        if (itr == otherSet.begin())
            result = (*itr);
        else
            result = result.CppIntersection(*itr);
    }

    return result;
}

template <class T>
template <class U>
Relation<T,U> Set<T>::CartesianProduct(Set<U> rightSet) { // O(n*m) n,m: set sizes
    Relation<T,U> result;

    set<U> rightInnerSet = rightSet.getInnerSet();

    for (auto leftItr = innerSet.begin(); leftItr != innerSet.end(); leftItr++) {
        for (auto rightItr = rightInnerSet.begin(); rightItr != rightInnerSet.end(); rightItr++) {
            result.insert(Tuple<T,U>((*leftItr), (*rightItr)));
        }
    }

    return result;
}

template <class T>
bool Set<T>::Partition(Set<Set<T>> parts) {
    bool isPartitioned;

    // To check if union of elements equals this set
    Set<T> combinedSet = combinedSet.GeneralUnion(parts);

    if (combinedSet == *this) {
        combinedSet = combinedSet.GeneralIntersection(parts);

        // The intersection of parts must be empty
        if (combinedSet == Set<T>())
            isPartitioned = true;
        else
            isPartitioned = false;
    }
    else
        isPartitioned = false;

    return isPartitioned;
}


// Print function for class
template <class T>
ostream& operator<<(ostream& os, const Set<T>& s)
{
    set<T> usedSet = s.getInnerSet();

    os << '{';
    for (auto itr = usedSet.begin(); itr != usedSet.end() ; itr++){
        if (itr != usedSet.begin())
            os << ',';
        os << (*itr);
    }
    os << '}';
    return os;
}

// Less operator for class
template <class T>
bool operator<(const Set<T> &lobj, const Set<T> &robj) {
            set<T> firstSet = lobj.getInnerSet();
            set<T> secondSet = robj.getInnerSet();
            bool result;
            result = firstSet < secondSet;
            return result;
        }

// Equal operator for class
template <class T>
bool operator==(const Set<T> &lobj, const Set<T> &robj){
            set<T> firstSet = lobj.getInnerSet();
            set<T> secondSet = robj.getInnerSet();
            bool result;
            result = firstSet == secondSet;
            return result;
        }

// NotEqual operator for class
template <class T>
bool operator!=(const Set<T> &lobj, const Set<T> &robj){
            bool result;
            result = !(lobj == robj);
            return result;
        }


////////// AS A SPECIALIZATION OF SET CLASS !!!!!!
        // Specialization will not help
//// CLASS IMPLEMENTATION
// RELATION

// Constructor
template <class T, class U>
Relation<T,U>::Relation() {}
template <class T, class U>
Relation<T,U>::Relation(set<Tuple<T,U>> startSet) {
    innerSet = startSet;
}

// Get/Set Methods
template <class T, class U>
set<Tuple<T,U>> Relation<T,U>::getInnerSet() const {return innerSet;}

template <class T, class U>
void Relation<T,U>::insert(Tuple<T,U> newElement) {
    innerSet.insert(newElement);
}

template <class T, class U>
Relation<T,U> Relation<T,U>::CppUnion(Relation<T,U> operandSet) { // O(n+m)
    set<Tuple<T,U>> unionResult;

    set<Tuple<T,U>> secondSet = operandSet.getInnerSet();

    set_union(innerSet.begin(), innerSet.end(),
                secondSet.begin(),secondSet.end(),
                inserter(unionResult, unionResult.begin()));

    Relation<T,U> result(unionResult);
    return result;
}

template <class T, class U>
Relation<T,U> Relation<T,U>::CppIntersection(Relation<T,U> operandSet) { // O(n+m) n:first set size
    set<Tuple<T,U>> intersectionResult;

    set<Tuple<T,U>> secondSet = operandSet.getInnerSet();

    set_intersection(innerSet.begin(), innerSet.end(),
                secondSet.begin(),secondSet.end(),
                inserter(intersectionResult, intersectionResult.begin()));

    Relation<T,U> result(intersectionResult);
    return result;
}

template <class T, class U>
Relation<T,U> Relation<T,U>::CppDifference(Relation<T,U> operandSet) { // O(n+m)
    set<Tuple<T,U>> subtractResult;

    set<Tuple<T,U>> secondSet = operandSet.getInnerSet();

    set_difference(innerSet.begin(), innerSet.end(),
                secondSet.begin(),secondSet.end(),
                inserter(subtractResult, subtractResult.begin()));

    Relation<T,U> result(subtractResult);
    return result;
}

template <class T, class U>
Set<Relation<T,U>> Relation<T,U>::POW(bool includeEmpty) { // O( ((2^n)-2)*n )  n: innerSet size
    Set<Relation<T,U>> result;

    int innerSetSize = innerSet.size();

    // Num of elements that the PowerSet will have
    int powerSetSize = pow(2,innerSetSize);

    int bytesToPick = 0;

    Relation<T,U> setToAdd;

    if (includeEmpty) {
        // The first set to add to the power set is the empty set
        // Lets do that before the loop to avoid a useless iteration
        result.insert(setToAdd);
    }

    // The same for the subset that equals the entire innerSet
    result.insert(*this);

    // Because we did this first, we start the loop with PElementIndex = 1
    // and also stop PElementIndex one step before the end.
    // PElementIndex == powerSetSize-1 would be the step to add the subset
    //   that has all of the values of the original set

    for (int PElementIndex = 1; PElementIndex < (powerSetSize-1); PElementIndex++) {
        // We are handling a set, so we must use iterators
        // To do the trick of using bytes & and shifts
        // we need an int iterator that will also rise
        bytesToPick = 0;
        setToAdd = Relation<T,U>();

        for (auto itr = innerSet.begin(); itr != innerSet.end(); itr++) {
            if (PElementIndex & (1 << bytesToPick)) {

                setToAdd.insert(*itr);
            }

            bytesToPick += 1;
        }

        result.insert(setToAdd);
    }

    return result;
}

template <class T, class U>
Set<Relation<T,U>> Relation<T,U>::PowerSet() {
    return POW(true);
}

template <class T, class U>
Set<Relation<T,U>> Relation<T,U>::PowerSet1() {// Not including the empty subset
    return POW(false);
}

template <class T, class U>
int Relation<T,U>::Cardinality() const { // O(1)
    return innerSet.size();
}

template <class T, class U>
Relation<T,U> Relation<T,U>::GeneralUnion(Set<Relation<T,U>> S) {
	Relation<T,U> result;

    set<Relation<T,U>> otherSet = S.getInnerSet();

    // Iterating through the inn
    for (auto itr = otherSet.begin(); itr != otherSet.end(); itr++) {
        result = result.CppUnion(*itr);
    }

    return result;
}

template <class T, class U>
Relation<T,U> Relation<T,U>::GeneralIntersection(Set<Relation<T,U>> S) {
	Relation<T,U> result;

    set<Relation<T,U>> otherSet = S.getInnerSet();

    // Iterating through the inn
    for (auto itr = otherSet.begin(); itr != otherSet.end(); itr++) {
        if (itr == otherSet.begin())
            result = (*itr);
        else
            result = result.CppIntersection(*itr);
    }

    return result;
}

template <class T, class U>
template <class V, class W>
Relation<Tuple<T,U>,Tuple<V,W>> Relation<T,U>::CartesianProduct(Relation<V,W> rightSet) { // O(n*m) n,m: set sizes
    Relation<Tuple<T,U>,Tuple<V,W>> result;

    set<Tuple<V,W>> rightInnerSet = rightSet.getInnerSet();

    for (auto leftItr = innerSet.begin(); leftItr != innerSet.end(); leftItr++) {
        for (auto rightItr = rightInnerSet.begin(); rightItr != rightInnerSet.end(); rightItr++) {
            result.insert(Tuple<Tuple<T,U>,Tuple<V,W>>((*leftItr), (*rightItr)));
        }
    }

    return result;
}

template <class T, class U>
bool Relation<T,U>::Partition(Set<Relation<T,U>> parts) {
    bool isPartitioned;

    // To check if union of elements equals this set
    Relation<T,U> combinedSet = combinedSet.GeneralUnion(parts);

    if (combinedSet == *this) {
        combinedSet = combinedSet.GeneralIntersection(parts);

        // The intersection of parts must be empty
        if (combinedSet == Relation<T,U>())
            isPartitioned = true;
        else
            isPartitioned = false;
    }
    else
        isPartitioned = false;

    return isPartitioned;
}

template <class T, class U>
Set<T> Relation<T,U>::Domain() {
	Set<T> result;

	for (auto itr = innerSet.begin(); itr != innerSet.end(); itr++) {
		result.insert((*itr).getLeft());
	}

	return result;
}

template <class T, class U>
Set<U> Relation<T,U>::Range() {
	Set<U> result;

	for (auto itr = innerSet.begin(); itr != innerSet.end(); itr++) {
		result.insert((*itr).getRight());
	}

	return result;
}


// Print function for class
template <class T, class U>
ostream& operator<<(ostream& os, const Relation<T,U>& s)
{
    set<Tuple<T,U>> sSet = s.getInnerSet();

    os << '{';
    for (auto itr = sSet.begin(); itr != sSet.end() ; itr++){
        if (itr != sSet.begin())
            os << ',';
        os << (*itr);
    }
    os << '}';
    return os;
}

// Less operator for class
template <class T, class U>
bool operator<(const Relation<T,U> &lobj, const Relation<T,U> &robj){
            bool result;
            set<Tuple<T,U>> leftSet = lobj.getInnerSet();
            set<Tuple<T,U>> rightSet = robj.getInnerSet();
            result = leftSet < rightSet;
            return result;
        }

// Equal operator for class
template <class T, class U>
bool operator==(const Relation<T,U> &lobj, const Relation<T,U> &robj){
            bool result;
            set<Tuple<T,U>> leftSet = lobj.getInnerSet();
            set<Tuple<T,U>> rightSet = robj.getInnerSet();
            result = leftSet == rightSet;
            return result;
        }

////// CLASS IMPLEMENTATION
//// SET BOOL in Event-B
BOOL_SET::BOOL_SET() {
            innerSet = {true,false};
        }


////// CLASS IMPLEMENTATION
//// SET P (INTEGER NUMBERS) in Event-B
INT_SET::INT_SET() {
	excludedSet = Set<int>();
}

bool INT_SET::Contains(int element) {
	bool result = false;

	if (excludedSet.Contains(element))
		result = false;
	else if (typeid(element) == typeid(int))
		result = true;

	return result;
}

bool INT_SET::NotContains(int element) {
	return !(Contains(element));
}

bool INT_SET::hasSubsetOrEqual(Set<int> otherSet) {
	bool result = false;

	if ( otherSet.CppIntersection(excludedSet) != Set<int>() )
		result = false;
	else if (typeid(Set<int>) == typeid(otherSet))
		result = true;

	return result;
}

bool INT_SET::hasNotSubsetOrEqual(Set<int> otherSet) {
	return !(hasSubsetOrEqual(otherSet));
}

bool INT_SET::hasSubset(Set<int> otherSet) {
	return hasSubsetOrEqual(otherSet);
}

bool INT_SET::hasNotSubset(Set<int> otherSet) {
	return !(hasSubset(otherSet));
}

INT_SET INT_SET::CppUnion(Set<int> operandSet) {
	// It doesn't matter what set of integers you use
	// its still just a subset of INT_SET.
	// INT_SET therefore is the result of the union
	// UNLESS, the set being added reintroduces elements
	// that have been included from the excludedSet
	excludedSet.CppDifference(operandSet);
	return *this;
}
Set<int> INT_SET::CppIntersection(Set<int> operandSet) {
	// A subset intersected with its encompassing set equals said subset
	// But the elements said subset may have been removed from excludedSet.
	Set<int> result = operandSet.CppDifference(excludedSet);
	return result;
}
INT_SET INT_SET::CppDifference(Set<int> operandSet) {
	// If you take the INT_SET and subtract {1} from it
	// the resulting set is now all of the integers except {1}
	// Thats why we use excludedSet.
	excludedSet = excludedSet.CppUnion(operandSet);
	return (*this);
}

//template <class U>
//Relation<T,U> CartesianProduct(Set<U> rightSet);


////// CLASS IMPLEMENTATION
//// SET NAT (NATURAL NUMBERS) in Event-B
NAT_SET::NAT_SET() {
	excludedSet = Set<int>();
	addedSet = Set<int>();
}

bool NAT_SET::Contains(int element) {
	bool result = false;

	if (excludedSet.Contains(element))
		result = false;
	else if ((typeid(element) == typeid(int)) && (element >= 0 || addedSet.Contains(element)) )
		result = true;

	return result;
}

bool NAT_SET::NotContains(int element) {
	return !(Contains(element));
}

bool NAT_SET::hasSubsetOrEqual(Set<int> otherSet) {
	bool result = false;

	if ( otherSet.CppIntersection(excludedSet) != Set<int>() )
		result = false;
	else if (typeid(Set<int>) == typeid(otherSet)) {
		set<int> otherInnerSet = otherSet.getInnerSet();
		auto itr = otherInnerSet.begin();
		result = true;
		while (itr != otherInnerSet.end() && result) {
			if ((*this).NotContains(*itr))
				result = false;
			itr++;
		}
	}

	return result;
}

bool NAT_SET::hasNotSubsetOrEqual(Set<int> otherSet) {
	return !(hasSubsetOrEqual(otherSet));
}

bool NAT_SET::hasSubset(Set<int> otherSet) {
	return hasSubsetOrEqual(otherSet);
}

bool NAT_SET::hasNotSubset(Set<int> otherSet) {
	return !(hasSubset(otherSet));
}

NAT_SET NAT_SET::CppUnion(Set<int> operandSet) {
	// It doesn't matter what set of integers you use
	// its still just a subset of INT_SET.
	// INT_SET therefore is the result of the union
	// UNLESS, the set being added reintroduces elements
	// that have been included from the excludedSet
	excludedSet.CppDifference(operandSet);
	return *this;
}
Set<int> NAT_SET::CppIntersection(Set<int> operandSet) {
	// A subset intersected with its encompassing set equals said subset
	// But the elements said subset may have been removed from excludedSet.
	Set<int> result = operandSet.CppDifference(excludedSet);
	return result;
}
NAT_SET NAT_SET::CppDifference(Set<int> operandSet) {
	// If you take the INT_SET and subtract {1} from it
	// the resulting set is now all of the integers except {1}
	// Thats why we use excludedSet.
	excludedSet = excludedSet.CppUnion(operandSet);
	return (*this);
}


////// CLASS IMPLEMENTATION
//// SET NAT1 (NATURAL EXCEPT 0 NUMBERS) in Event-B
NAT1_SET::NAT1_SET() {
	excludedSet = Set<int>();
}

bool NAT1_SET::Contains(int element) {
	bool result = false;

	if (excludedSet.Contains(element))
		result = false;
	else if (typeid(element) == typeid(int))
		result = true;

	return result;
}

bool NAT1_SET::NotContains(int element) {
	return !(Contains(element));
}

bool NAT1_SET::hasSubsetOrEqual(Set<int> otherSet) {
	bool result = false;

	if ( otherSet.CppIntersection(excludedSet) != Set<int>() )
		result = false;
	else if (typeid(Set<int>) == typeid(otherSet))
		result = true;

	return result;
}

bool NAT1_SET::hasNotSubsetOrEqual(Set<int> otherSet) {
	return !(hasSubsetOrEqual(otherSet));
}

bool NAT1_SET::hasSubset(Set<int> otherSet) {
	return hasSubsetOrEqual(otherSet);
}

bool NAT1_SET::hasNotSubset(Set<int> otherSet) {
	return !(hasSubset(otherSet));
}

NAT1_SET NAT1_SET::CppUnion(Set<int> operandSet) {
	// It doesn't matter what set of integers you use
	// its still just a subset of INT_SET.
	// INT_SET therefore is the result of the union
	// UNLESS, the set being added reintroduces elements
	// that have been included from the excludedSet
	excludedSet.CppDifference(operandSet);
	return *this;
}
Set<int> NAT1_SET::CppIntersection(Set<int> operandSet) {
	// A subset intersected with its encompassing set equals said subset
	// But the elements said subset may have been removed from excludedSet.
	Set<int> result = operandSet.CppDifference(excludedSet);
	return result;
}
NAT1_SET NAT1_SET::CppDifference(Set<int> operandSet) {
	// If you take the INT_SET and subtract {1} from it
	// the resulting set is now all of the integers except {1}
	// Thats why we use excludedSet.
	excludedSet = excludedSet.CppUnion(operandSet);
	return (*this);
}


//#include "EB2CppTools.cpp"

#endif

//////////////////////////
// ABOUT LINKING ERRORS //
//////////////////////////
// The line #include "EB2CppTools.cpp" must also be in every
// Context/Machine translated Cpp file, not just the header
// This is to avoid a linking error
// Its the Method 2 explained here:
// https://www.codeproject.com/Articles/48575/How-to-Define-a-Template-Class-in-a-h-File-and-Imp
// This... causes another bug, with the implementation of BOOL_SET
