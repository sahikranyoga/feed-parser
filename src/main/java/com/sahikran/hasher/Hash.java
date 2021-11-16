package com.sahikran.hasher;

public interface Hash<T> {
    long generateHash(T t);
}
