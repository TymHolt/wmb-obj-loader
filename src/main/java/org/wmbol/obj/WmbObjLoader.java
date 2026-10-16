package org.wmbol.obj;

import java.util.ArrayList;
import java.util.List;

/**
 * A loader class for wavefront obj files/sources.
 */
public final class WmbObjLoader {

    /** A list containing warnings encountered while loading. */
    public final List<String> warnings = new ArrayList<>();

    /**
     * Loads the given {@link String} as obj data.
     *
     * @param sourceString The string .obj source.
     */
    public void load(String sourceString) {
        final ObjTokenizer tokenizer = new ObjTokenizer(sourceString);
        List<String> tokens;
        while ((tokens = tokenizer.nextLine(this.warnings)) != null)
            handleLine(tokens);
    }

    private void handleLine(List<String> tokens) {
        if (tokens.isEmpty())
            return;

        final String type = tokens.getFirst();
        switch (type) {
            case "v":
            case "vt":
            case "vn":
            case "f":
            case "o":
            case "mtllib":
            case "usemtl":
            case "s":
                warnings.add("Type not implemented: " + type);
                break;
            default:
                warnings.add("Unknown type: " + type);
                break;
        }
    }
}
