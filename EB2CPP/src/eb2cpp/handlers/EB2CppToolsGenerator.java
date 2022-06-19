package eb2cpp.handlers;

import java.io.FileWriter;
import java.io.IOException;

public class EB2CppToolsGenerator {
	
	private String finalFilePath;

	public EB2CppToolsGenerator(String filePath) {
		finalFilePath = filePath;
	}
	
	public void generateCppTools() {
		try {
			StringBuilder builtLine = new StringBuilder();
			builtLine.append(finalFilePath);
			builtLine.append("EB2CppTools.h");
			
			FileWriter writer = new FileWriter(builtLine.toString());
			writer.write("""
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
					
					class EMPTY_SET;
					
					template <class T, class U>
					class RelationType;
					
					//// CLASS DEFINITION
					template <class T, class U>
					class Tuple {
					    protected:
					        T leftElement;
					        U rightElement;
					    public:
					    //Constructor
					        Tuple();
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
					
					// NotEqual operator for class
					template <class T, class U>
					bool operator!=(const Tuple<T,U> &lobj, const Tuple<T,U> &robj);
					
					
					//// CLASS DEFINITION
					class EMPTY_SET {
						public:
					
						template <class T>
						Set<T> cppUnion(Set<T> operandSet);
					
						template <class T>
						Set<T> cppIntersection(Set<T> operandSet);
					
						template <class T>
						Set<T> cppDifference(Set<T> operandSet);
					};
					
					
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
					        void operator=(const EMPTY_SET &robj);
					
					        void setInnerSet(set<T> newSet);
					        set<T> getInnerSet() const;
					
					        void setIsFinite(bool newValue);
					        bool getIsFinite() const;
					
					        void insert(T newElement);
					
					        bool contains(T element);
					
					        bool notContains(T element);
					
					        bool hasSubsetOrEqual(Set<T> otherSet);
					
					        bool hasNotSubsetOrEqual(Set<T> otherSet);
					
					        bool hasSubset(Set<T> otherSet);
					
					        bool hasNotSubset(Set<T> otherSet);
					
					        Set<T> cppUnion(Set<T> operandSet);
					        Set<T> cppIntersection(Set<T> operandSet);
					        Set<T> cppDifference(Set<T> operandSet);
					
					        INT_SET cppUnion(INT_SET operandSet);
							Set<T> cppIntersection(INT_SET operandSet);
							Set<T> cppDifference(INT_SET operandSet);
					
							NAT_SET cppUnion(NAT_SET operandSet);
							Set<T> cppIntersection(NAT_SET operandSet);
							Set<T> cppDifference(NAT_SET operandSet);
					
							NAT1_SET cppUnion(NAT1_SET operandSet);
							Set<T> cppIntersection(NAT1_SET operandSet);
							Set<T> cppDifference(NAT1_SET operandSet);
					
					        Set<Set<T>> POW(bool includeEmpty);
					
					        Set<Set<T>> powerSet();
					
					        Set<Set<T>> powerSet1();
					
					        int cardinality() const;
					
					        Set<T> generalUnion(Set<Set<T>> S);
					
					        Set<T> generalIntersection(Set<Set<T>> S);
					
					        template <class U>
					        Relation<T,U> cartesianProduct(Set<U> rightSet);
					
					        bool partition(Set<Set<T>> parts);
					
					        T min();
					        T max();
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
					
					// Equal operator for class WITH EMPTY_SET
					template <class T>
					bool operator==(const Set<T> &lobj, const EMPTY_SET &robj);
					
					template <class T>
					bool operator==(const EMPTY_SET &lobj, const Set<T> &robj);
					
					// NotEqual operator for class
					template <class T>
					bool operator!=(const Set<T> &lobj, const Set<T> &robj);
					
					// NotEqual operator for class with EMPTY_SET
					template <class T>
					bool operator!=(const Set<T> &lobj, const EMPTY_SET &robj);
					
					template <class T>
					bool operator!=(const EMPTY_SET &lobj, const Set<T> &robj);
					
					
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
					
					        void operator=(const EMPTY_SET &robj);
					
					        // Get/Set Methods
					        set<Tuple<T,U>> getInnerSet() const;
					
					        void insert(Tuple<T,U> newElement);
					
					        Relation<T,U> cppUnion(Relation<T,U> operandSet);
					
					        Relation<T,U> cppIntersection(Relation<T,U> operandSet);
					
					        Relation<T,U> cppDifference(Relation<T,U> operandSet);
					
					        Set<Relation<T,U>> POW(bool includeEmpty);
					
					        Set<Relation<T,U>> powerSet();
					
					        Set<Relation<T,U>> powerSet1();
					
					        int cardinality() const;
					
					        Relation<T,U> generalUnion(Set<Relation<T,U>> S);
					
					        Relation<T,U> generalIntersection(Set<Relation<T,U>> S);
					
					        template <class V, class W>
					        Relation<Tuple<T,U>,Tuple<V,W>> cartesianProduct(Relation<V,W> rightSet);
					
					        bool partition(Set<Relation<T,U>> parts);
					
					        Set<T> domain();
					
					        Set<U> range();
					
					        Set<U> relationalImage(Set<T> dom_set);
					
					        Relation<T,U> domainRestriction(Set<T> domSet);
					        Relation<T,U> domainSubtraction(Set<T> domSet);
					
					        Relation<T,U> rangeRestriction(Set<U> ranSet);
							Relation<T,U> rangeSubtraction(Set<U> ranSet);
					
							template <class V>
							Relation<T,V> forwardComposition(Relation<U,V> otherSet);
							template <class V>
							Relation<V,U> backwardComposition(Relation<V,T> otherSet);
					
							Relation<T,U> relationalOverride(Relation<T,U> otherSet);
					
							template <class V, class W>
							Relation<Tuple<T,V>,Tuple<U,W>> parallelProduct(Relation<V,W> rightSet);
					
							template <class V>
							Relation<T,Tuple<U,V>> directProduct(Relation<T,V>);
					
							Relation<U,T> inverse();
					
							U functionImage(T domainElement);
					
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
					
					// Equal operator for class WITH EMPTY_SET
					template <class T, class U>
					bool operator==(const Relation<T,U> &lobj, const EMPTY_SET &robj);
					
					template <class T, class U>
					bool operator==(const EMPTY_SET &lobj, const Relation<T,U> &robj);
					
					// Not-Equal operator for class
					template <class T, class U>
					bool operator!=(const Relation<T,U> &lobj, const Relation<T,U> &robj);
					
					// Not-Equal operator for class WITH EMPTY SET
					template <class T, class U>
					bool operator!=(const Relation<T,U> &lobj, const EMPTY_SET &robj);
					
					template <class T, class U>
					bool operator!=(const EMPTY_SET &lobj, const Relation<T,U> &robj);
					
					
					
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
					
							Set<int> getExcludedSet() const;
					
						    bool contains(int element);
					
						    bool notContains(int element);
					
						    bool hasSubsetOrEqual(Set<int> otherSet);
					
						    bool hasNotSubsetOrEqual(Set<int> otherSet);
					
						    bool hasSubset(Set<int> otherSet);
					
						    bool hasNotSubset(Set<int> otherSet);
					
						    INT_SET cppUnion(Set<int> operandSet);
						    Set<int> cppIntersection(Set<int> operandSet);
						    INT_SET cppDifference(Set<int> operandSet);
					
						    //template <class U>
						    //Relation<T,U> cartesianProduct(Set<U> rightSet);
					
					};
					
					// Equal operator for class WITH Set<int>
					bool operator==(const Set<int> &lobj, const INT_SET &robj);
					
					bool operator==(const INT_SET &lobj, const Set<int> &robj);
					
					
					//// SET NAT (NATURAL NUMBERS) in Event-B
					class NAT_SET {
						protected:
							Set<int> excludedSet; // The sets excluded from all of the integers in this expression
							Set<int> addedSet;	//Its possible that the model adds a couple of, say, negative numbers
						public:
							NAT_SET();
					
							Set<int> getExcludedSet() const;
					
							Set<int> getAddedSet() const;
					
						    bool contains(int element);
					
						    bool notContains(int element);
					
						    bool hasSubsetOrEqual(Set<int> otherSet);
					
						    bool hasNotSubsetOrEqual(Set<int> otherSet);
					
						    bool hasSubset(Set<int> otherSet);
					
						    bool hasNotSubset(Set<int> otherSet);
					
						    NAT_SET cppUnion(Set<int> operandSet);
						    Set<int> cppIntersection(Set<int> operandSet);
						    NAT_SET cppDifference(Set<int> operandSet);
					
						    //template <class U>
						    //Relation<T,U> cartesianProduct(Set<U> rightSet);
					
					};
					
					// Equal operator for class WITH Set<int>
					bool operator==(const Set<int> &lobj, const NAT_SET &robj);
					
					bool operator==(const NAT_SET &lobj, const Set<int> &robj);
					
					
					//// SET NAT1 (NATURAL EXCEPT 0 NUMBERS) in Event-B
					class NAT1_SET {
						protected:
							Set<int> excludedSet; // The sets excluded from all of the integers in this expression
							Set<int> addedSet;	//Its possible that the model adds a couple of, say, negative numbers
						public:
							NAT1_SET();
					
							Set<int> getExcludedSet() const;
					
							Set<int> getAddedSet() const;
					
						    bool contains(int element);
					
						    bool notContains(int element);
					
						    bool hasSubsetOrEqual(Set<int> otherSet);
					
						    bool hasNotSubsetOrEqual(Set<int> otherSet);
					
						    bool hasSubset(Set<int> otherSet);
					
						    bool hasNotSubset(Set<int> otherSet);
					
						    NAT1_SET cppUnion(Set<int> operandSet);
						    Set<int> cppIntersection(Set<int> operandSet);
						    NAT1_SET cppDifference(Set<int> operandSet);
					
						    //template <class U>
						    //Relation<T,U> cartesianProduct(Set<U> rightSet);
					
					};
					
					// Equal operator for class WITH Set<int>
					bool operator==(const Set<int> &lobj, const NAT1_SET &robj);
					
					bool operator==(const NAT1_SET &lobj, const Set<int> &robj);
					
					
					//// BASIC RELATION TYPE (A <-> B in Event-B)
					template <class T, class U>
					class RelationType {
						protected:
							string type; // Is it a basic, total, surjective, or total surjective relation?
							T domainSet;
							U rangeSet;
						public:
							RelationType(string t, T dom, U ran);
					
							T getDomainSet();
							U getRangeSet();
					
							template <class V, class W>
							bool contains(Relation<V,W> otherRelation);
					};
					
					
					
					
					//// CLASS IMPLEMENTATION
					// TUPLE
					
					//Constructor
					template <class T, class U>
					Tuple<T,U>::Tuple() {}
					
					template <class T, class U>
					Tuple<T,U>::Tuple(T elementA, U elementB) {leftElement = elementA; rightElement = elementB;}
					
					// Get/Set Methods
					
					template <class T, class U>
					T Tuple<T,U>::getLeft() const {return leftElement;}
					
					template <class T, class U>
					U Tuple<T,U>::getRight() const {return rightElement;}
					
					
					// Print function for class
					template <class T, class U>
					ostream& operator<<(ostream& os, const Tuple<T,U>& tp)
					{
					    os << '(' << tp.getLeft() << ',' << tp.getRight() << ')';
					    return os;
					}
					
					// Less operator for class
					template <class T, class U>
					bool operator<(const Tuple<T,U> &lobj, const Tuple<T,U> &robj){
					            bool result;
					            if (lobj.getLeft() == robj.getLeft()) {
					            	result = lobj.getRight() < robj.getRight();
					            }
					            else
					            	result = lobj.getLeft() < robj.getLeft();
					            return result;
					        }
					
					// Equal operator for class
					template <class T, class U>
					bool operator==(const Tuple<T,U> &lobj, const Tuple<T,U> &robj){
						bool result;
						result = (lobj.getLeft() == robj.getLeft()) && (lobj.getRight() == robj.getRight());
						return result;
					}
					
					// NotEqual operator for class
					template <class T, class U>
					bool operator!=(const Tuple<T,U> &lobj, const Tuple<T,U> &robj){
						bool result;
						result = !(lobj == robj);
						return result;
					}
					
					
					//// CLASS IMPLEMENTATION
					// SET
					
					template <class T>
					Set<T>::Set(){isFinite = false;}
					
					template <class T>
					Set<T>::Set(set<T> startSet) {innerSet = startSet; isFinite = false;}
					
					template <class T>
					void Set<T>::operator=(const EMPTY_SET &robj) {innerSet = {};}
					
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
					bool Set<T>::contains(T element) { // O(log n) n=innerSet.size
					    bool elementBelongs;
					
					    auto itr = innerSet.find(element);
					
					    if (itr == innerSet.end())
					        elementBelongs = false;
					    else
					        elementBelongs = true;
					
					    return elementBelongs;
					}
					
					template <class T>
					bool Set<T>::notContains(T element) { // O(log n) n=innerSet.size
					    return !(contains(element));
					}
					
					template <class T>
					bool Set<T>::hasSubsetOrEqual(Set<T> otherSet) { // O(n+m)
					    bool result = false;
					
					    Set<T> intersection = cppIntersection(otherSet);
					
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
					Set<T> Set<T>::cppUnion(Set<T> operandSet) { // O(n+m)
					    set<T> unionResult;
					
					    set<T> secondSet = operandSet.getInnerSet();
					
					    set_union(innerSet.begin(), innerSet.end(),
					                secondSet.begin(),secondSet.end(),
					                inserter(unionResult, unionResult.begin()));
					
					    Set<T> result(unionResult);
					    return result;
					}
					
					template <class T>
					Set<T> Set<T>::cppIntersection(Set<T> operandSet) { // O(n+m)
					    set<T> intersectionResult;
					
					    set<T> secondSet = operandSet.getInnerSet();
					
					    set_intersection(innerSet.begin(), innerSet.end(),
					                secondSet.begin(),secondSet.end(),
					                inserter(intersectionResult, intersectionResult.begin()));
					
					    Set<T> result(intersectionResult);
					    return result;
					}
					
					template <class T>
					Set<T> Set<T>::cppDifference(Set<T> operandSet) { // O(n+m)
					    set<T> subtractResult;
					
					    set<T> secondSet = operandSet.getInnerSet();
					
					    set_difference(innerSet.begin(), innerSet.end(),
					                secondSet.begin(),secondSet.end(),
					                inserter(subtractResult, subtractResult.begin()));
					
					    Set<T> result(subtractResult);
					    return result;
					}
					
					// Union, intersection and difference with INT_SET
					template <class T>
					INT_SET Set<T>::cppUnion(INT_SET operandSet) {
						return operandSet.cppUnion(*this);
					};
					
					template <class T>
					Set<T> Set<T>::cppIntersection(INT_SET operandSet) {
						return operandSet.cppIntersection(*this);
					};
					
					template <class T>
					Set<T> Set<T>::cppDifference(INT_SET operandSet) {
						return Set<T>();
					};
					
					// Union, intersection and difference with NAT_SET
					template <class T>
					NAT_SET Set<T>::cppUnion(NAT_SET operandSet) {
						return operandSet.cppUnion(*this);
					};
					
					template <class T>
					Set<T> Set<T>::cppIntersection(NAT_SET operandSet) {
						return operandSet.cppIntersection(*this);
					};
					
					template <class T>
					Set<T> Set<T>::cppDifference(NAT_SET operandSet) {
						return Set<T>();
					};
					
					// Union, intersection and difference with NAT1_SET
					template <class T>
					NAT1_SET Set<T>::cppUnion(NAT1_SET operandSet) {
						return operandSet.cppUnion(*this);
					};
					
					template <class T>
					Set<T> Set<T>::cppIntersection(NAT1_SET operandSet) {
						return operandSet.cppIntersection(*this);
					};
					
					template <class T>
					Set<T> Set<T>::cppDifference(NAT1_SET operandSet) {
						return Set<T>();
					};
					
					
					
					template <class T>
					Set<Set<T>> Set<T>::POW(bool includeEmpty) { // O( ((2^n)-2)*n )  n: innerSet size
					    Set<Set<T>> result;
					
					    int innerSetSize = innerSet.size();
					
					    // Num of elements that the powerSet will have
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
					Set<Set<T>> Set<T>::powerSet() {
					    return POW(true);
					}
					
					template <class T>
					Set<Set<T>> Set<T>::powerSet1() {// Not including the empty subset
					    return POW(false);
					}
					
					template <class T>
					int Set<T>::cardinality() const { // O(1)
					    return innerSet.size();
					}
					
					template <class T>
					Set<T> Set<T>::generalUnion(Set<Set<T>> S) {
					    Set<T> result;
					
					    set<Set<T>> otherSet = S.getInnerSet();
					
					    // Iterating through the inn
					    for (auto itr = otherSet.begin(); itr != otherSet.end(); itr++) {
					        result = result.cppUnion(*itr);
					    }
					
					    return result;
					}
					
					template <class T>
					Set<T> Set<T>::generalIntersection(Set<Set<T>> S) {
					    Set<T> result;
					
					    set<Set<T>> otherSet = S.getInnerSet();
					
					    // Iterating through the inn
					    for (auto itr = otherSet.begin(); itr != otherSet.end(); itr++) {
					        if (itr == otherSet.begin())
					            result = (*itr);
					        else
					            result = result.cppIntersection(*itr);
					    }
					
					    return result;
					}
					
					template <class T>
					template <class U>
					Relation<T,U> Set<T>::cartesianProduct(Set<U> rightSet) { // O(n*m) n,m: set sizes
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
					bool Set<T>::partition(Set<Set<T>> parts) {
					    bool isPartitioned;
					
					    // To check if union of elements equals this set
					    Set<T> combinedSet = combinedSet.generalUnion(parts);
					
					    if (combinedSet == *this) {
					        combinedSet = combinedSet.generalIntersection(parts);
					
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
					
					template <class T>
					T Set<T>::min() { // O(1)
						// Because the implementation of Set uses the less than operator
						// to order the elements of the set, if this is indeed a set of integers,
						// then, the first element of the set will always the minimum
						// Therefore, we just need to straightforwardly return the first element of the set
						auto itr = innerSet.begin();
						return (*itr);
					}
					
					template <class T>
					T Set<T>::max() { // O(1)
						// The vice-versa of the above explanation also shows that
						// the maximum is at the end of the set.
						auto itr = innerSet.rbegin();
						return (*itr);
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
					
					// Equal operator for class WITH EMPTY_SET
					template <class T>
					bool operator==(const Set<T> &lobj, const EMPTY_SET &robj) {
						bool result = false;
					
						if (lobj == Set<T>())
							result = true;
					
						return result;
					}
					
					template <class T>
					bool operator==(const EMPTY_SET &lobj, const Set<T> &robj) {
						return robj == lobj;
					}
					
					// NotEqual operator for class
					template <class T>
					bool operator!=(const Set<T> &lobj, const Set<T> &robj){
					            bool result;
					            result = !(lobj == robj);
					            return result;
					        }
					
					// NotEqual operator for class with EMPTY_SET
					template <class T>
					bool operator!=(const Set<T> &lobj, const EMPTY_SET &robj) {
						return !(lobj == robj);
					}
					
					template <class T>
					bool operator!=(const EMPTY_SET &lobj, const Set<T> &robj) {
						return !(robj == lobj);
					}
					
					
					// Function to generate a range of numbers as a set
					inline Set<int> numbersRange(int start, int end) {
						Set<int> result;
					
						for (int i = start; i <= end; i++)
							result.insert(i);
					
						return result;
					}
					
					inline int powINT(int base, int exp) {
						int result{ 1 }; //{ 1 }
						while (exp)
						{
							if (exp & 1)
								result *= base;
							exp >>= 1;
							base *= base;
						}
					
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
					
					template <class T, class U>
					void Relation<T,U>::operator=(const EMPTY_SET &robj) {innerSet = {};}
					
					// Get/Set Methods
					template <class T, class U>
					set<Tuple<T,U>> Relation<T,U>::getInnerSet() const {return innerSet;}
					
					template <class T, class U>
					void Relation<T,U>::insert(Tuple<T,U> newElement) {
					    innerSet.insert(newElement);
					}
					
					template <class T, class U>
					Relation<T,U> Relation<T,U>::cppUnion(Relation<T,U> operandSet) { // O(n+m)
					    set<Tuple<T,U>> unionResult;
					
					    set<Tuple<T,U>> secondSet = operandSet.getInnerSet();
					
					    set_union(innerSet.begin(), innerSet.end(),
					                secondSet.begin(),secondSet.end(),
					                inserter(unionResult, unionResult.begin()));
					
					    Relation<T,U> result(unionResult);
					    return result;
					}
					
					template <class T, class U>
					Relation<T,U> Relation<T,U>::cppIntersection(Relation<T,U> operandSet) { // O(n+m) n:first set size
					    set<Tuple<T,U>> intersectionResult;
					
					    set<Tuple<T,U>> secondSet = operandSet.getInnerSet();
					
					    set_intersection(innerSet.begin(), innerSet.end(),
					                secondSet.begin(),secondSet.end(),
					                inserter(intersectionResult, intersectionResult.begin()));
					
					    Relation<T,U> result(intersectionResult);
					    return result;
					}
					
					template <class T, class U>
					Relation<T,U> Relation<T,U>::cppDifference(Relation<T,U> operandSet) { // O(n+m)
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
					
					    // Num of elements that the powerSet will have
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
					Set<Relation<T,U>> Relation<T,U>::powerSet() {
					    return POW(true);
					}
					
					template <class T, class U>
					Set<Relation<T,U>> Relation<T,U>::powerSet1() {// Not including the empty subset
					    return POW(false);
					}
					
					template <class T, class U>
					int Relation<T,U>::cardinality() const { // O(1)
					    return innerSet.size();
					}
					
					template <class T, class U>
					Relation<T,U> Relation<T,U>::generalUnion(Set<Relation<T,U>> S) {
						Relation<T,U> result;
					
					    set<Relation<T,U>> otherSet = S.getInnerSet();
					
					    // Iterating through the inn
					    for (auto itr = otherSet.begin(); itr != otherSet.end(); itr++) {
					        result = result.cppUnion(*itr);
					    }
					
					    return result;
					}
					
					template <class T, class U>
					Relation<T,U> Relation<T,U>::generalIntersection(Set<Relation<T,U>> S) {
						Relation<T,U> result;
					
					    set<Relation<T,U>> otherSet = S.getInnerSet();
					
					    // Iterating through the inn
					    for (auto itr = otherSet.begin(); itr != otherSet.end(); itr++) {
					        if (itr == otherSet.begin())
					            result = (*itr);
					        else
					            result = result.cppIntersection(*itr);
					    }
					
					    return result;
					}
					
					template <class T, class U>
					template <class V, class W>
					Relation<Tuple<T,U>,Tuple<V,W>> Relation<T,U>::cartesianProduct(Relation<V,W> rightSet) { // O(n*m) n,m: set sizes
					    Relation<Tuple<T,U>,Tuple<V,W>> result;
					
					    set<Tuple<V,W>> rightInnerSet = rightSet.getInnerSet();
					
					    for (auto leftItr = innerSet.begin(); leftItr != innerSet.end(); leftItr++) {
					        for (auto rightItr = rightInnerSet.begin(); rightItr != rightInnerSet.end(); rightItr++) {
					        	cout << "Entered loop" << endl;
					            result.insert(Tuple<Tuple<T,U>,Tuple<V,W>>((*leftItr), (*rightItr)));
					        }
					    }
					
					    return result;
					}
					
					template <class T, class U>
					bool Relation<T,U>::partition(Set<Relation<T,U>> parts) {
					    bool isPartitioned;
					
					    // To check if union of elements equals this set
					    Relation<T,U> combinedSet = combinedSet.generalUnion(parts);
					
					    if (combinedSet == *this) {
					        combinedSet = combinedSet.generalIntersection(parts);
					
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
					Set<T> Relation<T,U>::domain() {
						Set<T> result;
					
						for (auto itr = innerSet.begin(); itr != innerSet.end(); itr++) {
							result.insert((*itr).getLeft());
						}
					
						return result;
					}
					
					template <class T, class U>
					Set<U> Relation<T,U>::range() {
						Set<U> result;
					
						for (auto itr = innerSet.begin(); itr != innerSet.end(); itr++) {
							result.insert((*itr).getRight());
						}
					
						return result;
					}
					
					template <class T, class U>
					Set<U> Relation<T,U>::relationalImage(Set<T> dom_set) {
						Set<U> result;
					
						for (auto itr = innerSet.begin(); itr != innerSet.end(); itr++) {
							// See if the iterated element of the domain is in the set
							// being queried for its relational image
							if ( dom_set.contains( (*itr).getLeft() ) ) {
								result.insert(*itr.getRight());
							}
						}
					
						return result;
					}
					
					template <class T, class U>
					Relation<T,U> Relation<T,U>::domainRestriction(Set<T> domSet) {
						Relation<T,U> result;
					
						for (auto itr = innerSet.begin(); itr != innerSet.end(); itr++) {
							if ( domSet.contains( (*itr).getLeft() ) )
								result.insert(*itr);
						}
					
						return result;
					}
					template <class T, class U>
					Relation<T,U> Relation<T,U>::domainSubtraction(Set<T> domSet) {
						Relation<T,U> result;
					
						for (auto itr = innerSet.begin(); itr != innerSet.end(); itr++) {
							if ( domSet.notContains( (*itr).getLeft() ) )
								result.insert(*itr);
						}
					
						return result;
					}
					
					template <class T, class U>
					Relation<T,U> Relation<T,U>::rangeRestriction(Set<U> ranSet) {
						Relation<T,U> result;
					
						for (auto itr = innerSet.begin(); itr != innerSet.end(); itr++) {
							if ( ranSet.contains( (*itr).getRight() ) )
								result.insert(*itr);
						}
					
						return result;
					}
					template <class T, class U>
					Relation<T,U> Relation<T,U>::rangeSubtraction(Set<U> ranSet) {
						Relation<T,U> result;
					
						for (auto itr = innerSet.begin(); itr != innerSet.end(); itr++) {
							if ( ranSet.notContains( (*itr).getRight() ) )
								result.insert(*itr);
						}
					
						return result;
					}
					
					template <class T, class U>
					template <class V>
					Relation<T,V> Relation<T,U>::forwardComposition(Relation<U,V> otherSet) {
						Relation<T,V> result;
					
						set<Tuple<U,V>> secondSet = otherSet.getInnerSet();
					
						for (auto itr = innerSet.begin(); itr != innerSet.end(); itr++) {
							for (auto otherItr = secondSet.begin(); otherItr != secondSet.end(); otherItr++) {
								if ( (*itr).getRight() == (*otherItr).getLeft() )
									result.insert(Tuple<T,V>( (*itr).getLeft() , (*otherItr).getRight() ));
							}
						}
					
						return result;
					}
					
					template <class T, class U>
					template <class V>
					Relation<V,U> Relation<T,U>::backwardComposition(Relation<V,T> otherSet) {
						return otherSet.forwardComposition(*this);
					}
					
					template <class T, class U>
					Relation<T,U> Relation<T,U>::relationalOverride(Relation<T,U> otherSet) {
						return otherSet.cppUnion( (*this).domainSubtraction(otherSet.domain()) );
					}
					
					template <class T, class U>
					template <class V, class W>
					Relation<Tuple<T,V>,Tuple<U,W>> Relation<T,U>::parallelProduct(Relation<V,W> rightSet) {
						Relation<Tuple<T,V>,Tuple<U,W>> result;
						set<Tuple<V,W>> secondSet = rightSet.getInnerSet();
					
						for (auto itr = innerSet.begin(); itr != innerSet.end(); itr++) {
							for (auto oItr = secondSet.begin(); oItr != secondSet.end(); oItr++) {
								Tuple<T,V> firstElement( (*itr).getLeft() , (*oItr).getLeft() );
								Tuple<U,W> secondElement( (*itr).getRight() , (*oItr).getRight() );
					
								result.insert(Tuple<Tuple<T,V>,Tuple<U,W>>(firstElement, secondElement));
							}
						}
					
						return result;
					}
					
					template <class T, class U>
					template <class V>
					Relation<T,Tuple<U,V>> Relation<T,U>::directProduct(Relation<T,V> rightSet) {
						Relation<T,Tuple<U,V>> result;
					
						set<Tuple<T,V>> secondSet = rightSet.getInnerSet();
					
						for (auto itr = innerSet.begin(); itr != innerSet.end(); itr++) {
							T x = (*itr).getLeft();
							U y = (*itr).getRight();
							for (auto oItr = secondSet.begin(); oItr != secondSet.end(); oItr++) {
								V z = (*oItr).getRight();
								if (x == (*oItr).getLeft())
									result.insert(Tuple<T,Tuple<U,V>>( x , Tuple<U,V>(y,z) ));
							}
						}
					
						return result;
					}
					
					template <class T, class U>
					Relation<U,T> Relation<T,U>::inverse() { // O(n) n:set size
						Relation<U,T> result;
					
						for (auto itr = innerSet.begin(); itr != innerSet.end(); itr++) {
							result.insert(Tuple<U,T>( (*itr).getRight() , (*itr).getLeft() ));
						}
					
						return result;
					}
					
					template <class T, class U>
					U Relation<T,U>::functionImage(T domainElement) { // O(n) n: set size
						U result;
					
						bool foundElement = false;
					
						for (auto itr = innerSet.begin(); itr != innerSet.end() && !foundElement; itr++) {
							if ( (*itr).getLeft() == domainElement ) {
								foundElement = true;
								result = (*itr).getRight();
							}
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
					
					// Equal operator for class WITH EMPTY_SET
					template <class T, class U>
					bool operator==(const Relation<T,U> &lobj, const EMPTY_SET &robj) {
						bool result = false;
					
						if (lobj == Relation<T,U>())
							result = true;
					
						return result;
					}
					
					template <class T, class U>
					bool operator==(const EMPTY_SET &lobj, const Relation<T,U> &robj) {
						return robj == lobj;
					}
					
					// Not-Equal operator for class
					template <class T, class U>
					bool operator!=(const Relation<T,U> &lobj, const Relation<T,U> &robj) {
						return !(lobj == robj);
					}
					
					// Not-Equal operator for class WITH EMPTY SET
					template <class T, class U>
					bool operator!=(const Relation<T,U> &lobj, const EMPTY_SET &robj) {
						return !(lobj == robj);
					}
					
					template <class T, class U>
					bool operator!=(const EMPTY_SET &lobj, const Relation<T,U> &robj) {
						return !(robj == lobj);
					}
					
					
					////// CLASS IMPLEMENTATION
					//// SET BOOL in Event-B
					inline BOOL_SET::BOOL_SET() {
					            innerSet = {true,false};
					        }
					
					
					////// CLASS IMPLEMENTATION
					//// SET P (INTEGER NUMBERS) in Event-B
					inline INT_SET::INT_SET() {
						excludedSet = Set<int>();
					}
					
					inline Set<int> INT_SET::getExcludedSet() const {return excludedSet;}
					
					inline bool INT_SET::contains(int element) {
						bool result = true;
					
						if (excludedSet.contains(element))
							result = false;
					
						return result;
					}
					
					inline bool INT_SET::notContains(int element) {
						return !(contains(element));
					}
					
					inline bool INT_SET::hasSubsetOrEqual(Set<int> otherSet) {
						bool result = true;
					
						if ( otherSet.cppIntersection(excludedSet) != Set<int>() )
							result = false;
					
						return result;
					}
					
					inline bool INT_SET::hasNotSubsetOrEqual(Set<int> otherSet) {
						return !(hasSubsetOrEqual(otherSet));
					}
					
					inline bool INT_SET::hasSubset(Set<int> otherSet) {
						return hasSubsetOrEqual(otherSet);
					}
					
					inline bool INT_SET::hasNotSubset(Set<int> otherSet) {
						return !(hasSubset(otherSet));
					}
					
					inline INT_SET INT_SET::cppUnion(Set<int> operandSet) {
						// It doesn't matter what set of integers you use
						// its still just a subset of INT_SET.
						// INT_SET therefore is the result of the union
						// UNLESS, the set being added reintroduces elements
						// that have been included from the excludedSet
						excludedSet = excludedSet.cppDifference(operandSet);
						return *this;
					}
					inline Set<int> INT_SET::cppIntersection(Set<int> operandSet) {
						// A subset intersected with its encompassing set equals said subset
						// But the elements said subset may have been removed from excludedSet.
						Set<int> result = operandSet.cppDifference(excludedSet);
						return result;
					}
					inline INT_SET INT_SET::cppDifference(Set<int> operandSet) {
						// If you take the INT_SET and subtract {1} from it
						// the resulting set is now all of the integers except {1}
						// Thats why we use excludedSet.
						excludedSet = excludedSet.cppUnion(operandSet);
						return (*this);
					}
					
					// Equal operator for class WITH Set<int>
					inline bool operator==(const Set<int> &lobj, const INT_SET &robj) {
						return false;
					}
					
					inline bool operator==(const INT_SET &lobj, const Set<int> &robj) {
						return false;
					}
					
					//template <class U>
					//Relation<T,U> cartesianProduct(Set<U> rightSet);
					
					
					////// CLASS IMPLEMENTATION
					//// SET NAT (NATURAL NUMBERS) in Event-B
					inline NAT_SET::NAT_SET() {
						excludedSet = Set<int>();
						addedSet = Set<int>();
					}
					
					inline Set<int> NAT_SET::getExcludedSet() const {
						return excludedSet;
					}
					
					inline Set<int> NAT_SET::getAddedSet() const {
						return addedSet;
					}
					
					inline bool NAT_SET::contains(int element) {
						bool result = false;
					
						if (excludedSet.contains(element))
							result = false;
						else if (element >= 0 || addedSet.contains(element))
							result = true;
					
						return result;
					}
					
					inline bool NAT_SET::notContains(int element) {
						return !(contains(element));
					}
					
					inline bool NAT_SET::hasSubsetOrEqual(Set<int> otherSet) {
						bool result = false;
					
						if ( otherSet.cppIntersection(excludedSet) != Set<int>() )
							result = false;
						else {
							set<int> otherInnerSet = otherSet.getInnerSet();
							auto itr = otherInnerSet.begin();
							result = true;
							while (itr != otherInnerSet.end() && result) {
								if ((*this).notContains(*itr))
									result = false;
								itr++;
							}
						}
					
						return result;
					}
					
					inline bool NAT_SET::hasNotSubsetOrEqual(Set<int> otherSet) {
						return !(hasSubsetOrEqual(otherSet));
					}
					
					inline bool NAT_SET::hasSubset(Set<int> otherSet) {
						return hasSubsetOrEqual(otherSet);
					}
					
					inline bool NAT_SET::hasNotSubset(Set<int> otherSet) {
						return !(hasSubset(otherSet));
					}
					
					inline NAT_SET NAT_SET::cppUnion(Set<int> operandSet) {
						// It doesn't matter what set of integers you use
						// its still just a subset of INT_SET.
						// INT_SET therefore is the result of the union
						// UNLESS, the set being added reintroduces elements
						// that have been included from the excludedSet
						excludedSet = excludedSet.cppDifference(operandSet);
						addedSet = addedSet.cppUnion(operandSet);
						return *this;
					}
					inline Set<int> NAT_SET::cppIntersection(Set<int> operandSet) {
						// A subset intersected with its encompassing set equals said subset
						// But the elements said subset may have been removed from excludedSet.
						Set<int> preResult = operandSet.cppDifference(excludedSet);
						Set<int> result;
					
						set<int> preResultSet = preResult.getInnerSet();
					
						for (auto itr = preResultSet.begin(); itr != preResultSet.end(); itr++) {
							if ((*this).contains(*itr))
								result.insert(*itr);
						}
						return result;
					}
					inline NAT_SET NAT_SET::cppDifference(Set<int> operandSet) {
						// If you take the INT_SET and subtract {1} from it
						// the resulting set is now all of the integers except {1}
						// Thats why we use excludedSet.
						excludedSet = excludedSet.cppUnion(operandSet);
						addedSet = addedSet.cppDifference(operandSet);
						return (*this);
					}
					// Equal operator for class WITH Set<int>
					inline bool operator==(const Set<int> &lobj, const NAT_SET &robj) {
						return false;
					}
					
					inline bool operator==(const NAT_SET &lobj, const Set<int> &robj) {
						return false;
					}
					
					
					////// CLASS IMPLEMENTATION
					//// SET NAT1 (NATURAL EXCEPT 0 NUMBERS) in Event-B
					inline NAT1_SET::NAT1_SET() {
						excludedSet = Set<int>({0});
						addedSet = Set<int>();
					}
					
					inline Set<int> NAT1_SET::getExcludedSet() const {
						return excludedSet;
					}
					
					inline Set<int> NAT1_SET::getAddedSet() const {
						return addedSet;
					}
					
					inline bool NAT1_SET::contains(int element) {
						bool result = false;
					
						if (excludedSet.contains(element))
							result = false;
						else if (element >= 0 || addedSet.contains(element))
							result = true;
					
						return result;
					}
					
					inline bool NAT1_SET::notContains(int element) {
						return !(contains(element));
					}
					
					inline bool NAT1_SET::hasSubsetOrEqual(Set<int> otherSet) {
						bool result = false;
					
						if ( otherSet.cppIntersection(excludedSet) != Set<int>() )
							result = false;
						else {
							set<int> otherInnerSet = otherSet.getInnerSet();
							auto itr = otherInnerSet.begin();
							result = true;
							while (itr != otherInnerSet.end() && result) {
								if ((*this).notContains(*itr))
									result = false;
								itr++;
							}
						}
					
						return result;
					}
					
					inline bool NAT1_SET::hasNotSubsetOrEqual(Set<int> otherSet) {
						return !(hasSubsetOrEqual(otherSet));
					}
					
					inline bool NAT1_SET::hasSubset(Set<int> otherSet) {
						return hasSubsetOrEqual(otherSet);
					}
					
					inline bool NAT1_SET::hasNotSubset(Set<int> otherSet) {
						return !(hasSubset(otherSet));
					}
					
					inline NAT1_SET NAT1_SET::cppUnion(Set<int> operandSet) {
						// It doesn't matter what set of integers you use
						// its still just a subset of INT_SET.
						// INT_SET therefore is the result of the union
						// UNLESS, the set being added reintroduces elements
						// that have been included from the excludedSet
						excludedSet = excludedSet.cppDifference(operandSet);
						addedSet = addedSet.cppUnion(operandSet);
						return *this;
					}
					inline Set<int> NAT1_SET::cppIntersection(Set<int> operandSet) {
						// A subset intersected with its encompassing set equals said subset
						// But the elements said subset may have been removed from excludedSet.
						Set<int> preResult = operandSet.cppDifference(excludedSet);
						Set<int> result;
					
						set<int> preResultSet = preResult.getInnerSet();
					
						for (auto itr = preResultSet.begin(); itr != preResultSet.end(); itr++) {
							if ((*this).contains(*itr))
								result.insert(*itr);
						}
						return result;
					}
					inline NAT1_SET NAT1_SET::cppDifference(Set<int> operandSet) {
						// If you take the INT_SET and subtract {1} from it
						// the resulting set is now all of the integers except {1}
						// Thats why we use excludedSet.
						excludedSet = excludedSet.cppUnion(operandSet);
						addedSet = addedSet.cppDifference(operandSet);
						return (*this);
					}
					// Equal operator for class WITH Set<int>
					inline bool operator==(const Set<int> &lobj, const NAT1_SET &robj) {
						return false;
					}
					
					inline bool operator==(const NAT1_SET &lobj, const Set<int> &robj) {
						return false;
					}
					
					
					////// BASIC RELATION TYPE (A <-> B in Event-B)
					//template <class T, class U>
					//class RelationType {
					//	protected:
					//		T domainSet;
					//		U rangeSet;
					//	public:
					//		RelationType(T dom, U ran);
					//
					//		template <class V, class W>
					//		bool contains(Relation<V,W> otherRelation);
					//};
					
					//// BASIC RELATION TYPE (A <-> B in Event-B)
					// CLASS IMPLEMENTATION
					template <class T, class U>
					RelationType<T,U>::RelationType(string t, T dom, U ran) {type = t; domainSet = dom; rangeSet = ran;}
					
					template <class T, class U>
					T RelationType<T,U>::getDomainSet() {return domainSet;}
					template <class T, class U>
					U RelationType<T,U>::getRangeSet() {return rangeSet;}
					
					template <class T, class U>
					template <class V, class W>
					bool RelationType<T,U>::contains(Relation<V,W> otherRelation) { // O(2n) n: set size
						bool result = false;
					
						Set<V> otherDomain = otherRelation.domain();
						Set<W> otherRange = otherRelation.range();
					
						// Switches in C++ can only be used for integers
						if (type == "Basic") { // Relation
							if ( domainSet.hasSubsetOrEqual(otherDomain) ) {
								if ( rangeSet.hasSubsetOrEqual(otherRange) ) {
									result = true;
								}
							}
						}
						else if (type == "Total") { // TotalRelation
							if ( domainSet == otherDomain ) {
								if ( rangeSet.hasSubsetOrEqual(otherRange) ) {
									result = true;
								}
							}
						}
						else if (type == "Surjective") { // Surjective Relation
							if ( rangeSet == otherRange ) {
								if ( domainSet.hasSubsetOrEqual(otherDomain) ) {
									result = true;
								}
							}
						}
						else if (type == "TotalSurjective") { // Total Surjective Relation
							if ( domainSet == otherDomain ) {
								if ( rangeSet == otherRange ) {
									result = true;
								}
							}
						}
						else if (type == "PartialFunction") { // Partial Function
							// If a relation's cardinality is not equal to the cardinality of its domain
							// its because there are pairs in the relation that have the same first element
							// and as a result, when you calculate the domain, these pairs will add up to only one
							// element in the calculated domain (a set can't have duplicates).
							// This allows us to see if the relation is a function
							// We choose to do this to diminish COMPUTATION COMPLEXITY
							if ( otherDomain.cardinality() == otherRelation.cardinality() ) {
								if ( domainSet.hasSubsetOrEqual(otherDomain) ) {
									if ( rangeSet.hasSubsetOrEqual(otherRange) ) {
										result = true;
									}
								}
							}
						}
						else if (type == "TotalFunction") {
							if ( otherDomain.cardinality() == otherRelation.cardinality() ) {
								if ( domainSet == otherDomain ) {
									if ( rangeSet.hasSubsetOrEqual(otherRange) ) {
										result = true;
									}
								}
							}
						}
						else if (type == "PartialInjection") {
							if ( otherDomain.cardinality() == otherRelation.cardinality() ) {
								// Injective means that two pairs cant have the same second element
								// Means that the inverse of the function is ALSO a function
								if ( otherRange.cardinality() == otherRelation.cardinality() ) {
									if ( domainSet.hasSubsetOrEqual(otherDomain) ) {
										if ( rangeSet.hasSubsetOrEqual(otherRange) ) {
											result = true;
										}
									}
								}
							}
						}
						else if (type == "TotalInjection") {
							if ( otherDomain.cardinality() == otherRelation.cardinality() ) {
								// Injective means that two pairs cant have the same second element
								// Means that the inverse of the function is ALSO a function
								if ( otherRange.cardinality() == otherRelation.cardinality() ) {
									if ( domainSet == otherDomain ) {
										if ( rangeSet.hasSubsetOrEqual(otherRange) ) {
											result = true;
										}
									}
								}
							}
						}
						else if (type == "PartialSurjection") {
							if ( otherDomain.cardinality() == otherRelation.cardinality() ) {
								if ( rangeSet == otherRange ) {
									if ( domainSet.hasSubsetOrEqual(otherDomain) ) {
										result = true;
									}
								}
							}
						}
						else if (type == "TotalSurjection") {
							if ( otherDomain.cardinality() == otherRelation.cardinality() ) {
								if ( rangeSet == otherRange ) {
									if ( domainSet == otherDomain ) {
										result = true;
									}
								}
							}
						}
						else if (type == "TotalBijection") {
							if ( otherDomain.cardinality() == otherRelation.cardinality() ) {
								// Injective means that two pairs cant have the same second element
								// Means that the inverse of the function is ALSO a function
								if ( otherRange.cardinality() == otherRelation.cardinality() ) {
									if ( domainSet == otherDomain ) {
										if ( rangeSet == otherRange ) {
											result = true;
										}
									}
								}
							}
						}
					
						return result;
					}
					
					
					//#include "EB2CppTools.cpp"
					
					#endif
					""");
			writer.close();
		}
		catch (IOException e) {
			System.out.println("Can't create file");
			e.printStackTrace();
		}
	}

}
