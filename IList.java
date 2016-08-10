// to represent a generic list
interface IList<T> {

}

// to represent an empty generic list
class MtList<T> implements IList<T> {
    
}

// to represent a non-empty generic list
class ConsList<T> implements IList<T> {
    T first;
    IList<T> rest;
    
    ConsList(T first, IList<T> rest) {
        this.first = first;
        this.rest = rest;
    }
}