package com.efindus.utils;

import java.util.function.Supplier;

public class RingList<T> {
    T[] elements;
    int top;
    int size;
    Supplier<T> tSupplier;

    public RingList(int capacity, Supplier<T> tSupplier) {
        this(capacity, 0, tSupplier);
    }

    @SuppressWarnings("unchecked")
    public RingList(int capacity, int initialSize, Supplier<T> tSupplier) {
        elements = (T[]) new Object[capacity];
        top = 0;
        size = initialSize;
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

    public T get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return elements[(top + index) % elements.length];
    }

    private T _scroll(T replacement) {
        if (size == 0) return null;

        T element = elements[top];
        elements[top] = null;
        elements[(top + size) % elements.length] = replacement;

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
