package com.rock.okhttp3_demo.utils.listener;

import java.util.List;

/**
 * Created by Rock on 2018/6/25.
 */

public interface DataIO<T> {

    void add(T elem);

    void addAt(int location, T elem);

    void addAll(List<T> elements);

    void addAllAt(int location, List<T> elements);

    void remove(T elem);

    void removeAll(List<T> elements);

    void removeAt(int index);

    void clear();

    void replace(T oldElem, T newElem);

    void replaceAt(int index, T elem);

    void replaceAll(List<T> elements);

    void notifyData(List<T> elements);

    List<T> getAll();

    T get(int position);

    int getSize();

    boolean contains(T elem);
}
