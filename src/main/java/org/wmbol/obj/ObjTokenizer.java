package org.wmbol.obj;

final class ObjTokenizer {

    private final String source;
    private final int sourceLength;
    private int nextIndex = 0;

    ObjTokenizer(String source) {
        this.source = source;
        this.sourceLength = source.length();
    }

    StringTokenStream nextLine(IWmbObjLoadConsumer consumer) {
        if (nextIndex >= sourceLength)
            return null;

        final StringBuilder tokenBuilder = new StringBuilder();
        final StringTokenStream tokens = new StringTokenStream();
        boolean inComment = false;

        int currentIndex;
        while ((currentIndex = nextIndex++) < sourceLength) {
            final char currentChar = source.charAt(currentIndex);

            if (currentChar == '/') {
                if (!tokenBuilder.isEmpty()) {
                    tokens.append(tokenBuilder.toString());
                    tokenBuilder.setLength(0);
                }

                tokens.append("/");
                continue;
            }

            if (charIsWhitespace(currentChar)) {
                if (!tokenBuilder.isEmpty()) {
                    tokens.append(tokenBuilder.toString());
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

            consumer.consumeWarning("Unexpected char '" + currentChar + "'");
        }

        // If string ended we have not added the token yet
        if (!tokenBuilder.isEmpty())
            tokens.append(tokenBuilder.toString());

        return tokens;
    }

    private boolean charIsWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\r' || c == '\n';
    }

    private boolean charCanBeToken(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') ||
                c == '.' || c == '-' || c =='_';
    }
}
