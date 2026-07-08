package org.wmb.loader.obj;

import java.util.Objects;

final class ObjStringSource {

    private final String sourceString;
    private final int sourceStringLength;
    private int index = -1;
    private char currentChar = 0;

    ObjStringSource(String sourceString) {
        Objects.requireNonNull(sourceString, "Source string is null");
        this.sourceString = sourceString;
        this.sourceStringLength = sourceString.length();

    }

    void forward() {
        this.index++;
        if (hasCurrentChar())
            this.currentChar = this.sourceString.charAt(this.index);
    }

    boolean hasCurrentChar() {
        return this.index < this.sourceStringLength;
    }

    char getCurrentChar() {
        return this.currentChar;
    }
}
