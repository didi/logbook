package com.didiglobal.common.web.filterchain.list;


import com.didiglobal.common.web.filterchain.ProxiedFilterChain;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.Filter;
import javax.servlet.FilterChain;


public class SimpleNamedFilterList implements NamedFilterList {

    private String name;
    private List<Filter> backingList;

    public SimpleNamedFilterList(String name) {
        this(name, new ArrayList<Filter>());
    }

    public SimpleNamedFilterList(String name, List<Filter> backingList) {
        if (backingList == null) {
            throw new NullPointerException("backingList constructor argument cannot be null.");
        }
        this.backingList = backingList;
        setName(name);
    }

    protected void setName(String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public FilterChain proxy(FilterChain orig) {
        return new ProxiedFilterChain(orig, this);
    }

    public boolean add(Filter filter) {
        return this.backingList.add(filter);
    }

    public void add(int index, Filter filter) {
        this.backingList.add(index, filter);
    }

    public boolean addAll(Collection<? extends Filter> c) {
        return this.backingList.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends Filter> c) {
        return this.backingList.addAll(index, c);
    }

    public void clear() {
        this.backingList.clear();
    }

    public boolean contains(Object o) {
        return this.backingList.contains(o);
    }

    public boolean containsAll(Collection<?> c) {
        return this.backingList.containsAll(c);
    }

    public Filter get(int index) {
        return this.backingList.get(index);
    }

    public int indexOf(Object o) {
        return this.backingList.indexOf(o);
    }

    public boolean isEmpty() {
        return this.backingList.isEmpty();
    }

    public Iterator<Filter> iterator() {
        return this.backingList.iterator();
    }

    public int lastIndexOf(Object o) {
        return this.backingList.lastIndexOf(o);
    }

    public ListIterator<Filter> listIterator() {
        return this.backingList.listIterator();
    }

    public ListIterator<Filter> listIterator(int index) {
        return this.backingList.listIterator(index);
    }

    public Filter remove(int index) {
        return this.backingList.remove(index);
    }

    public boolean remove(Object o) {
        return this.backingList.remove(o);
    }

    public boolean removeAll(Collection<?> c) {
        return this.backingList.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return this.backingList.retainAll(c);
    }

    public Filter set(int index, Filter filter) {
        return this.backingList.set(index, filter);
    }

    public int size() {
        return this.backingList.size();
    }

    public List<Filter> subList(int fromIndex, int toIndex) {
        return this.backingList.subList(fromIndex, toIndex);
    }

    public Object[] toArray() {
        return this.backingList.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return this.backingList.toArray(a);
    }
}
