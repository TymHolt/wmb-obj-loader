package org.wmbol.obj;

import java.util.Objects;

/**
 * A loader class for wavefront obj files/sources.
 */
public final class WmbObjLoader {

    private final IWmbObjLoadConsumer consumer;
    private int geometryDataCount, textureDataCount, normalDataCount;

    /**
     * Initialize the loader.
     *
     * @param consumer The consumer, or handler, for the loaded data.  Must not be {@code null}.
     */
    public WmbObjLoader(IWmbObjLoadConsumer consumer) {
        Objects.requireNonNull(consumer);
        this.consumer = consumer;
    }

    /**
     * Loads the given {@link String} as obj data.
     *
     * @param sourceString The string .obj source. Must not be {@code null}.
     */
    public void load(String sourceString) {
        try {
            this.geometryDataCount = 0;
            this.textureDataCount = 0;
            this.normalDataCount = 0;

            final ObjTokenizer tokenizer = new ObjTokenizer(sourceString);
            StringTokenStream tokens;
            while ((tokens = tokenizer.nextLine(this.consumer)) != null) {
                if (tokens.isEmpty())
                    continue;

                final String type = tokens.consume();
                switch (type) {
                    case "v":
                        handleV(tokens);
                        break;
                    case "vt":
                        handleVT(tokens);
                        break;
                    case "vn":
                        handleVN(tokens);
                        break;
                    case "f":
                        handleF(tokens);
                        break;
                    case "vp":
                    case "o":
                    case "mtllib":
                    case "usemtl":
                    case "l":
                    case "s":
                    case "g":
                        this.consumer.consumeWarning("Type not implemented: " + type);
                        break;
                    default:
                        this.consumer.consumeWarning("Unknown type: " + type);
                        break;
                }
            }
        } catch (ObjErrorException exception) {
            this.consumer.consumeError(exception.getMessage());
        }
    }

    private void handleV(StringTokenStream tokens) throws ObjErrorException {
        // v [x] [y] [z] ([w]) -> 3 - 4 Tokens

        final int tokenCount = tokens.getRemaining();
        if (tokenCount < 3 || tokenCount > 4)
            throw new ObjErrorException("Malformed 'v' type, length " + tokenCount);

        this.geometryDataCount++;

        final float x = parseFloat(tokens.consume());
        final float y = parseFloat(tokens.consume());
        final float z = parseFloat(tokens.consume());
        final float w = !tokens.isEmpty() ? parseFloat(tokens.consume()) : 0.0f;

        this.consumer.consumeV(x, y, z, w);
    }

    private void handleVT(StringTokenStream tokens) throws ObjErrorException {
        // vt [u] ([v]) ([w]) -> 1 - 3 Tokens

        final int tokenCount = tokens.getRemaining();
        if (tokenCount < 1 || tokenCount > 3)
            throw new ObjErrorException("Malformed 'vt' type, length " + tokenCount);

        this.textureDataCount++;

        final float u = parseFloat(tokens.consume());
        final float v = !tokens.isEmpty() ? parseFloat(tokens.consume()) : 0.0f;
        final float w = !tokens.isEmpty() ? parseFloat(tokens.consume()) : 0.0f;

        this.consumer.consumeVT(u, v, w);
    }

    private void handleVN(StringTokenStream tokens) throws ObjErrorException {
        // vn [x] [y] [z] -> 4 Tokens

        final int tokenCount = tokens.getRemaining();
        if (tokenCount != 3)
            throw new ObjErrorException("Malformed 'vn' type, length " + tokenCount);

        this.normalDataCount++;

        final float x = parseFloat(tokens.consume());
        final float y = parseFloat(tokens.consume());
        final float z = parseFloat(tokens.consume());

        this.consumer.consumeVN(x, y, z);
    }

    private void handleF(StringTokenStream tokens) throws ObjErrorException {
        // f [aV]             [bV]             [cV]             -> 3 Tokens
        // f [aV]/[aVT]       [bV]/[bVT]       [cV]/[cVT]       -> 9 Tokens
        // f [aV]//[aVN]      [bV]//[bVN]      [cV]//[cVN]      -> 12 Tokens
        // f [aV]/[aVT]/[aVN] [bV]/[bVT]/[bVN] [cV]/[cVT]/[cVN] -> 15 Tokens

        final int tokenCount = tokens.getRemaining();
        if ((tokenCount != 3) && (tokenCount != 9) && (tokenCount != 12) && (tokenCount != 15))
            throw new ObjErrorException("Malformed 'f' type, length " + tokenCount);

        final int[] result = new int[3];
        handleFVertex(tokens, result);
        final int aV = result[0], aVT = result[1], aVN = result[2];
        handleFVertex(tokens, result);
        final int bV = result[0], bVT = result[1], bVN = result[2];
        handleFVertex(tokens, result);
        final int cV = result[0], cVT = result[1], cVN = result[2];

        this.consumer.consumeF(aV, aVT, aVN, bV, bVT, bVN, cV, cVT, cVN);
    }

    private void handleFVertex(StringTokenStream tokens, int[] result) throws ObjErrorException {
        int v = 0, vt = 0, vn = 0;

        v = parseInt(tokens.consume());
        if (tokens.peek().equals("/")) {
            tokens.consume();

            if (tokens.peek().equals("/")) {
                tokens.consume();
                vn = parseInt(tokens.consume());
            } else {
                tokens.consume();
                vt = parseInt(tokens.consume());

                if (tokens.peek().equals("/")) {
                    tokens.consume();
                    vn = parseInt(tokens.consume());
                }
            }
        }

        // OBJ indices are 1-based -> We want 0-based
        v -= 1;
        vt -= 1;
        vn -= 1;

        if (v >= this.geometryDataCount || vt >= this.textureDataCount || vn >= this.normalDataCount || v < 0)
            throw new ObjErrorException(String.format("Vertex indices out of bounds: %d %d %d", v, vt, vn));

        result[0] = v;
        result[1] = vt;
        result[2] = vn;
    }

    private static float parseFloat(String string) throws ObjErrorException {
        try {
            return Float.parseFloat(string);
        } catch (Exception exception) {
            throw new ObjErrorException(exception.getMessage());
        }
    }

    private static int parseInt(String string) throws ObjErrorException {
        try {
            return Integer.parseInt(string);
        } catch (Exception exception) {
            throw new ObjErrorException(exception.getMessage());
        }
    }

    private static class ObjErrorException extends Exception {

        ObjErrorException(String message) {
            super(message);
        }
    }
}
