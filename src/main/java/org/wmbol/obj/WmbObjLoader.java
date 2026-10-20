package org.wmbol.obj;

import java.util.List;
import java.util.Objects;

/**
 * A loader class for wavefront obj files/sources.
 */
public final class WmbObjLoader {

    private final IWmbObjLoadConsumer consumer;

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
            final ObjTokenizer tokenizer = new ObjTokenizer(sourceString);
            List<String> tokens;
            while ((tokens = tokenizer.nextLine(this.consumer)) != null) {
                if (tokens.isEmpty())
                    continue;

                final String type = tokens.getFirst();
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
                    case "vp":
                    case "o":
                    case "mtllib":
                    case "usemtl":
                    case "l":
                    case "s":
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

    private void handleV(List<String> tokens) throws ObjErrorException {
        // v [x] [y] [z] ([w]) -> 4 - 5 Tokens

        final int tokenCount = tokens.size();
        if (tokenCount < 4 || tokenCount > 5)
            throw new ObjErrorException("Malformed 'v' type, length " + tokenCount);

        final float x = parseFloat(tokens.get(1));
        final float y = parseFloat(tokens.get(2));
        final float z = parseFloat(tokens.get(3));
        final float w = tokenCount == 5 ? parseFloat(tokens.get(4)) : 0.0f;

        this.consumer.consumeV(x, y, z, w);
    }

    private void handleVT(List<String> tokens) throws ObjErrorException {
        // vt [u] ([v]) ([w]) -> 2 - 4 Tokens

        final int tokenCount = tokens.size();
        if (tokenCount < 2 || tokenCount > 4)
            throw new ObjErrorException("Malformed 'vt' type, length " + tokenCount);

        final float u = parseFloat(tokens.get(1));
        final float v = tokenCount >= 3 ? parseFloat(tokens.get(2)) : 0.0f;
        final float w = tokenCount == 4 ? parseFloat(tokens.get(3)) : 0.0f;

        this.consumer.consumeVT(u, v, w);
    }

    private void handleVN(List<String> tokens) throws ObjErrorException {
        // vn [x] [y] [z] -> 4 Tokens

        final int tokenCount = tokens.size();
        if (tokenCount != 4)
            throw new ObjErrorException("Malformed 'vn' type, length " + tokenCount);

        final float x = parseFloat(tokens.get(1));
        final float y = parseFloat(tokens.get(2));
        final float z = parseFloat(tokens.get(3));

        this.consumer.consumeVN(x, y, z);
    }

    private static float parseFloat(String string) throws ObjErrorException {
        try {
            return Float.parseFloat(string);
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
