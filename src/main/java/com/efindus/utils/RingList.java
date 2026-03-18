package com.efindus.utils;

import java.util.function.Supplier;

public class RingList<T> {
    private final T[] elements;
    private int top;
    private int size;
    private final int initialSize;
    private final Supplier<T> tSupplier;

    public RingList(int capacity, Supplier<T> tSupplier) {
        this(capacity, 0, tSupplier);
    }

    @SuppressWarnings("unchecked")
    public RingList(int capacity, int initialSize, Supplier<T> tSupplier) {
        if (capacity < 0) throw new IllegalArgumentException("Capacity must be >= 0");
        if (initialSize < 0 || initialSize > capacity)
            throw new IllegalArgumentException("initialSize must be between 0 and capacity");

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
        if (capacity() == 0) return;

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
        if (size == 0) return null;
        return _scroll(tSupplier.get());
    }

    /**
     * If capacity is 0 inserts are discarded
     */
    public void insert(T element) {
        if (elements.length == size) {
            _scroll(element);
        } else {
            elements[(top + size) % elements.length] = element;
            size++;
        }
    }

    public T poll() {
        if (size == 0) return null;
        T element = elements[top];
        top = (top + 1) % elements.length;
        size--;
        return element;
    }
}
