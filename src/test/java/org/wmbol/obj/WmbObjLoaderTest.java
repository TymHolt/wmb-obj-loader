package org.wmbol.obj;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Tests for the {@link WmbObjLoader} class.
 */
class WmbObjLoaderTest {

    private static String loadResource(String path) {
        try (InputStream is = WmbObjLoaderTest.class.getResourceAsStream(path)) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Test all possible and implemented types for correct identification by the parser.
     */
    @Test
    void parsePossibleTypes() {
        new WmbObjLoader(new IWmbObjLoadConsumer() {

            @Override
            public void consumeError(String message) {
                Assertions.fail("Error during parsing: " + message);
            }

            @Override
            public void consumeWarning(String message) {
                Assertions.fail("Warning during parsing: " + message);
            }

            @Override
            public void consumeV(float x, float y, float z, float w) {
                Assertions.assertEquals(0.0f, x);
                Assertions.assertEquals(1.0f, y);
                Assertions.assertEquals(2.0f, z);
                Assertions.assertEquals(0.0f, w);
            }

            @Override
            public void consumeVT(float u, float v, float w) {
                Assertions.assertEquals(3.0f, u);
                Assertions.assertEquals(4.0f, v);
                Assertions.assertEquals(0.0f, w);
            }

            @Override
            public void consumeVN(float x, float y, float z) {
                Assertions.assertEquals(5.0f, x);
                Assertions.assertEquals(6.0f, y);
                Assertions.assertEquals(7.0f, z);
            }

            @Override
            public void consumeF(int aV, int aVT, int aVN, int bV, int bVT, int bVN, int cV, int cVT, int cVN) {
                Assertions.assertEquals(0, aV);
                Assertions.assertEquals(0, aVT);
                Assertions.assertEquals(0, aVN);

                Assertions.assertEquals(0, bV);
                Assertions.assertEquals(0, bVT);
                Assertions.assertEquals(0, bVN);

                Assertions.assertEquals(0, cV);
                Assertions.assertEquals(0, cVT);
                Assertions.assertEquals(0, cVN);
            }
        }).load(loadResource("/org/wmbol/obj/parsePossibleTypes.obj"));
    }
}
