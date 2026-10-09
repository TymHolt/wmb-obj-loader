package org.wmbol.obj;

import java.util.List;

/**
 * A loader class for wavefront obj files/sources.
 */
public final class WmbObjLoader {

    /**
     * Loads the given {@link String} as obj data.
     *
     * @param sourceString The string .obj source.
     */
    public void load(String sourceString) {
        final ObjTokenizer tokenizer = new ObjTokenizer(sourceString);
        List<String> tokens;
        while ((tokens = tokenizer.nextLine()) != null) {
            for (String token : tokens)
                System.out.println(token);

            System.out.println("-----------------");
        }

        handleLine(tokens);
    }

    private void handleLine(List<String> tokens) {

    }
}
