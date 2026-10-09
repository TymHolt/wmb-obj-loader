package org.wmbol.obj;

import java.util.ArrayList;
import java.util.List;

final class ObjTokenizer {

    private final String source;
    private final int sourceLength;
    private int nextIndex = 0;

    ObjTokenizer(String source) {
        this.source = source;
        this.sourceLength = source.length();
    }

    /**
     * Tokenizes the next line of the source. May produce a {@link RuntimeException} when tokenization fails.
     *
     * @return A list containing the tokens. Can be {@code null} when there is no source lef to parse.
     */
    List<String> nextLine() {
        if (nextIndex >= sourceLength)
            return null;

        final StringBuilder tokenBuilder = new StringBuilder();
        final List<String> tokens = new ArrayList<>();
        boolean inComment = false;

        int currentIndex;
        while ((currentIndex = nextIndex++) < sourceLength) {
            final char currentChar = source.charAt(currentIndex);

            if (charIsWhitespace(currentChar)) {
                if (!tokenBuilder.isEmpty()) {
                    tokens.add(tokenBuilder.toString());
                    tokenBuilder.setLength(0);
                }

                if (currentChar == '\n')
                    return tokens;
                else
                    continue;
            }

            if (inComment || currentChar == '#') {
                inComment = true;
                continue;
            }

            if (charCanBeToken(currentChar)) {
                tokenBuilder.append(currentChar);
                continue;
            }

            throw new RuntimeException("Unexpected char '" + currentChar + "'"); // TODO Give Line/Column

        }

        // If string ended we have not added the token yet
        if (!tokenBuilder.isEmpty())
            tokens.add(tokenBuilder.toString());

        return tokens;
    }

    private boolean charIsWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\r' || c == '\n';
    }

    private boolean charCanBeToken(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '.';
    }
}
