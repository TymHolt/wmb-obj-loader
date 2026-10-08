package org.wmbol.obj;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ObjLoader {

    public ObjLoader() {

    }

    public void load(String sourceString) throws IOException {
        final ObjStringSource source = new ObjStringSource(sourceString);
        final List<String> lineTokens = new ArrayList<>();
        final StringBuilder tokenBuilder = new StringBuilder();
        boolean inComment = false;

        while (source.hasCurrentChar()) {
            final char currentChar = source.getCurrentChar();
            source.forward();

            if (charIsWhitespace(currentChar)) {
                if (!tokenBuilder.isEmpty()) {
                    lineTokens.add(tokenBuilder.toString());
                    tokenBuilder.setLength(0);
                }

                if (currentChar == '\n') {
                    handleLine(lineTokens);
                    lineTokens.clear();
                    inComment = false;
                }
            }

            if (inComment || currentChar == '#') {
                inComment = true;
                continue;
            }

            if (charCanBeToken(currentChar)) {
                tokenBuilder.append(currentChar);
                continue;
            }

            throw new IOException("Unexpected char '" + currentChar + "'"); // TODO Give Line/Column
        }

        // Handle end of file. If no \n at the end we might miss a token or line
        if (!tokenBuilder.isEmpty())
            lineTokens.add(tokenBuilder.toString());

        if (!lineTokens.isEmpty())
            handleLine(lineTokens);
    }

    private void handleLine(List<String> tokens) {

    }

    private boolean charIsWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\r' || c == '\n';
    }

    private boolean charCanBeToken(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '.';
    }
}
