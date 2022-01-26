package com.sahikran.service.storage;

import java.util.List;
import java.util.Set;

public interface StorageService<T, ReturnType> {
    ReturnType save(T t);
    Set<ReturnType> save(List<T> ts);
}
