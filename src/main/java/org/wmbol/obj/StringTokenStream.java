package org.wmbol.obj;

import java.util.ArrayList;
import java.util.List;

final class StringTokenStream {

    private final List<String> tokenList = new ArrayList<>();

    String consume() {
        final String token = this.tokenList.getFirst();
        this.tokenList.removeFirst();
        return token;
    }

    String peek() {
        return this.tokenList.getFirst();
    }

    void append(String token) {
        this.tokenList.add(token);
    }

    boolean isEmpty() {
        return this.tokenList.isEmpty();
    }

    int getRemaining() {
        return this.tokenList.size();
    }
}
