package com.efindus.utils;

import java.util.function.Supplier;

public class RingList<T> {
    T[] elements;
    int top;
    int size;
    int initialSize;
    Supplier<T> tSupplier;

    public RingList(int capacity, Supplier<T> tSupplier) {
        this(capacity, 0, tSupplier);
    }

    @SuppressWarnings("unchecked")
    public RingList(int capacity, int initialSize, Supplier<T> tSupplier) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be greater than zero");

        elements = (T[]) new Object[capacity];
        top = 0;
        this.initialSize = size = initialSize;
        this.tSupplier = tSupplier;

        for (int i = 0; i < size; i++)
            elements[i] = tSupplier.get();
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return elements.length;
    }

    public void clear() {
        size = initialSize;
        top = 0;

        for (int i = 0; i < size; i++)
            elements[i] = tSupplier.get();

        for (int i = size; i < elements.length; i++)
             elements[i] = null;
    }

    public T get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return elements[(top + index) % elements.length];
    }

    private T _scroll(T newLast) {
        if (size == 0) return null;

        T element = elements[top];
        elements[top] = null;
        elements[(top + size) % elements.length] = newLast;

        top = (top + 1) % elements.length;
        return element;
    }

    public T scroll() {
        return _scroll(tSupplier.get());
    }

    public void insert(T element) {
        if (elements.length == size) {
            _scroll(element);
        } else {
            elements[(top + size) % elements.length] = element;
            size++;
        }
    }
}
