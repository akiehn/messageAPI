package org.example;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {

    private static AtomicInteger id = new AtomicInteger();

    public int createId() {
        return id.getAndIncrement();
    }
}
