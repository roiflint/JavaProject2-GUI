package com;

/** functional interface of a Consumer with two types of objects,
 *  the function accept has been altered to accept two types of
 *  variables according to the signature
 * */

@FunctionalInterface
public interface Buyer<T, S> {
    void accept(T t, S s);
}
