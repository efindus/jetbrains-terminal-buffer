package com.efindus.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RingListTest {
    @Test
    void constructorBounds() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RingList<Integer>(-1, () -> null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new RingList<Integer>(0, 10, () -> null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new RingList<Integer>(0, -1, () -> null);
        });
    }

    @Test
    void zeroCapacity() {
        RingList<Integer> rl = new RingList<>(0, 0, () -> null);
        rl.clear();
        assertNull(rl.scroll());
        rl.insert(10);
        assertEquals(0, rl.size());
    }

    @Test
    void getBounds() {
        RingList<Integer> rl = new RingList<>(10, () -> null);
        rl.insert(10);

        assertEquals(10, rl.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> {
            rl.get(-1);
        });
        assertThrows(IndexOutOfBoundsException.class, () -> {
            rl.get(2);
        });
    }
}