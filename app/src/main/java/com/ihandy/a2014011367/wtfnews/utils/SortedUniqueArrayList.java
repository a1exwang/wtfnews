package com.ihandy.a2014011367.wtfnews.utils;

import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SortedUniqueArrayList<T extends Comparable<T> & Indexable> implements List<T> {
    private ArrayList<T> data = new ArrayList<>();

    public static class ElementAlreadyExistException extends Exception {
    }
    public interface FindCallback<T1> {
        boolean isTarget(T1 t);
    }

    public int findBy(FindCallback<T> cb) {
        for (int i = 0; i < data.size(); ++i) {
            if (cb.isTarget(data.get(i))) {
                return i;
            }
        }
        return -1;
    }
    public int addSortedUnique(T t) throws ElementAlreadyExistException {
        for (int i = 0; i < data.size(); ++i) {
            int result = t.compareTo(data.get(i));
            if (result == 0) {
                throw new ElementAlreadyExistException();
            }
            else if (result < 0) {
                data.add(i + 1, t);
                t.setIndex(i + 1);
                return i + 1;
            }
        }
        t.setIndex(data.size());
        data.add(t);
        return data.size() - 1;
    }
    public T last() {
        if (data.size() == 0)
            return null;
        return data.get(data.size() - 1);
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return data.contains(o);
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return data.iterator();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return data.toArray();
    }

    @NonNull
    @Override
    public <T1> T1[] toArray(T1[] t1s) {
        return data.toArray(t1s);
    }

    @Override
    public boolean add(T t) {
        return data.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return data.remove(o);
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        return data.containsAll(collection);
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends T> collection) {
        return data.addAll(collection);
    }

    @Override
    public boolean addAll(int i, @NonNull Collection<? extends T> collection) {
        return data.addAll(i, collection);
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        return removeAll(collection);
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        return data.retainAll(collection);
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public T get(int i) {
        return data.get(i);
    }

    @Override
    public T set(int i, T t) {
        return data.set(i, t);
    }

    @Override
    public void add(int i, T t) {
        data.add(i, t);
    }

    @Override
    public T remove(int i) {
        return data.remove(i);
    }

    @Override
    public int indexOf(Object o) {
        return data.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return data.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return data.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator(int i) {
        return data.listIterator(i);
    }

    @NonNull
    @Override
    public List<T> subList(int i, int i1) {
        return data.subList(i, i1);
    }
}
