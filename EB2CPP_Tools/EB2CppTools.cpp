#include <iostream>
#include <set>
#include <iterator>
#include <utility>
#include <algorithm>
#include <string>
#include <vector>
#include <math.h>

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

//// CLASS DEFINITION
template <class T, class U>
class Tuple {
    protected:
        pair<T,U> p;
    public:
    //Constructor
        Tuple(T elementA, U elementB) {p = make_pair(elementA,elementB);}
        
        // Get/Set Methods
        pair<T,U> getPair() const {return p;}
        
};

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

////////// AS A SPECIALIZATION OF SET CLASS !!!!!!!!!
//// CLASS DEFINITION
template <class T, class U>
class Relation {
    protected:
        set<Tuple<T,U>> innerSet;
    public:
        // Constructor
        Relation() {}
        Relation(set<Tuple<T,U>> startSet) {
            innerSet = startSet;
        }
        
        // Get/Set Methods
        set<Tuple<T,U>> getInnerSet() const {return innerSet;}
        

        void insert(Tuple<T,U> newElement) {
            innerSet.insert(newElement);
        }

        Relation<T,U> CppUnion(Relation<T,U> operandSet) { // O(n+m)
            set<Tuple<T,U>> unionResult;

            set<Tuple<T,U>> secondSet = operandSet.getInnerSet();

            set_union(innerSet.begin(), innerSet.end(),
                        secondSet.begin(),secondSet.end(),
                        inserter(unionResult, unionResult.begin()));

            Relation<T,U> result(unionResult);
            return result;
        }

        Relation<T,U> CppIntersection(Relation<T,U> operandSet) { // O(n+m) n:first set size
            set<Tuple<T,U>> intersectionResult;

            set<Tuple<T,U>> secondSet = operandSet.getInnerSet();

            set_intersection(innerSet.begin(), innerSet.end(),
                        secondSet.begin(),secondSet.end(),
                        inserter(intersectionResult, intersectionResult.begin()));

            Relation<T,U> result(intersectionResult);
            return result;
        }

        Relation<T,U> CppSubtract(Relation<T,U> operandSet) { // O(n+m)
            set<Tuple<T,U>> subtractResult;

            set<Tuple<T,U>> secondSet = operandSet.getInnerSet();

            set_difference(innerSet.begin(), innerSet.end(),
                        secondSet.begin(),secondSet.end(),
                        inserter(subtractResult, subtractResult.begin()));

            Relation<T,U> result(subtractResult);
            return result;
        }

        
};

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

//// CLASS DEFINITION
template <class T>
class Set {
    protected:
        set<T> innerSet;
        bool isFinite; // Will not be used for anything in current version
                        // but for possible future use.
    public:
    // Constructors
        Set(){}
        Set(set<T> startSet) {innerSet = startSet;}
        
        void setInnerSet(set<T> newSet) {innerSet = newSet;}
        set<T> getInnerSet() const {return innerSet;}

        void setIsFinite(bool newValue) {isFinite = newValue;}
        bool getIsFinite() const {return isFinite;}
        
        void insert(T newElement) {
            innerSet.insert(newElement);
        }

        bool Contains(T element) { // O(log n) n=innerSet.size
            bool elementBelongs;

            auto itr = innerSet.find(element);

            if (itr == innerSet.end())
                elementBelongs = false;
            else
                elementBelongs = true;

            return elementBelongs;
        }

        bool NotContains(T element) { // O(log n) n=innerSet.size
            return !(Contains(element));
        }

        bool isSubsetOrEqual(Set<T> otherSet) { // O(n+m)
            bool result = false;

            Set<T> intersection = CppIntersection(otherSet);

            if (intersection == (*this))
                result = true;

            return result;
        }

        bool isNotSubsetOrEqual(Set<T> otherSet) { // O(n+m)
            return !(isSubsetOrEqual(otherSet));
        }

        bool isSubset(Set<T> otherSet) { // O(n+m)
            bool result;

            if ((*this) == otherSet)
                result = false;
            else {
                result = isSubsetOrEqual(otherSet);
            }

            return result;
        }

        bool isNotSubset(Set<T> otherSet) {
            return !(isSubset(otherSet));
        }

        Set<T> CppUnion(Set<T> operandSet) { // O(n+m)
            set<T> unionResult;
            
            set<T> secondSet = operandSet.getInnerSet();

            set_union(innerSet.begin(), innerSet.end(),
                        secondSet.begin(),secondSet.end(),
                        inserter(unionResult, unionResult.begin()));

            Set<T> result(unionResult);
            return result;
        }
        Set<T> CppIntersection(Set<T> operandSet) { // O(n+m)
            set<T> intersectionResult;
            
            set<T> secondSet = operandSet.getInnerSet();

            set_intersection(innerSet.begin(), innerSet.end(),
                        secondSet.begin(),secondSet.end(),
                        inserter(intersectionResult, intersectionResult.begin()));

            Set<T> result(intersectionResult);
            return result;
        }
        Set<T> CppDifference(Set<T> operandSet) { // O(n+m)
            set<T> subtractResult;
            
            set<T> secondSet = operandSet.getInnerSet();

            set_difference(innerSet.begin(), innerSet.end(),
                        secondSet.begin(),secondSet.end(),
                        inserter(subtractResult, subtractResult.begin()));

            Set<T> result(subtractResult);
            return result;
        }

        Set<Set<T>> POW(bool includeEmpty) { // O( ((2^n)-2)*n )  n: innerSet size
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

        Set<Set<T>> PowerSet() {
            return POW(true);
        }

        Set<Set<T>> PowerSet1() {// Not including the empty subset
            return POW(false);
        }

        int Cardinality() const { // O(1)
            return innerSet.size();
        }

        Set<T> GeneralUnion(Set<Set<T>> S) {
            Set<T> result;

            set<Set<T>> otherSet = S.getInnerSet();

            // Iterating through the inn
            for (auto itr = otherSet.begin(); itr != otherSet.end(); itr++) {
                result = result.CppUnion(*itr);
            }

            return result;
        }

        Set<T> GeneralIntersection(Set<Set<T>> S) {
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

        template <class U>
        Relation<T,U> CartesianProduct(Set<U> rightSet) { // O(n*m) n,m: set sizes
            Relation<T,U> result;
            
            set<U> rightInnerSet = rightSet.getInnerSet();
            
            for (auto leftItr = innerSet.begin(); leftItr != innerSet.end(); leftItr++) {
                for (auto rightItr = rightInnerSet.begin(); rightItr != rightInnerSet.end(); rightItr++) {
                    result.insert(Tuple<T,U>((*leftItr), (*rightItr)));
                }
            }
            
            return result;
        }

        bool Partition(Set<Set<T>> parts) {
            bool isPartitioned;

            // To check if union of elements equals this set
            Set<T> combinedSet = combinedSet.GeneralUnion(parts);

            if (combinedSet == *this) {
                combinedSet = combinedSet.GeneralIntersection(parts);
                
                // The intersection of parts must be empty
                if (combinedSet == Set<T>())
                    isPartitioned = true;
                else
                    isPartitioned - false;
            }
            else
                isPartitioned = false;

            return isPartitioned;
        }

};

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

//// SET BOOL in Event-B
class BOOL_SET : public Set<bool> {
    public:
        BOOL_SET() {
            innerSet = {true,false};
        }
};


/*
int main()
{
    
    Relation<int,int> r1( {} );
    r1.insert(Tuple<int,int>(2,3));
    r1.insert(Tuple<int,int>(2,2));
    r1.insert(Tuple<int,int>(1,44));
    r1.insert(Tuple<int,int>(5,17));
    
    cout << r1 << endl;
    
    Relation<PEOPLE,int> r2({});
    r2.insert(Tuple<PEOPLE,int>(PEOPLE02,15));
    r2.insert(Tuple<PEOPLE,int>(PEOPLE01,2));
    
    set_union(test1.begin(), test1.end(), test2.begin(), test2.end(),
                inserter(resultU, resultU.begin()));


    cout << r2 << endl;

    return 0;
}
*/