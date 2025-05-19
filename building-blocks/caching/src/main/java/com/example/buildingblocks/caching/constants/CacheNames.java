package com.example.buildingblocks.caching.constants;

import java.util.UUID;

public final class CacheNames {
    public static final String BOOKS = "books";

    public static String getBookKey(UUID bookId) {
        return BOOKS + "::" + bookId;
    }
}
