package com.sahikran.service;

import java.util.List;

public interface StorageService<T> {
    String save(T t);
    boolean save(List<T> ts);
}
