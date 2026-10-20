package org.wmbol.obj;

/**
 * An interface to process loaded obj data. The callbacks are called in the right order as defined in the obj source.
 */
public interface IWmbObjLoadConsumer {

    /**
     * Callback when an error occurs during loading. The loader will stop loading after this call.
     *
     * @param message The error message.
     */
    void consumeError(String message);

    /**
     * Callback when a warning occurs during loading. The loader will continue loading.
     *
     * @param message The warning message.
     */
    void consumeWarning(String message);

    /**
     * Callback when vertex geometry data is loaded.
     *
     * @param x The vertex x coordinate.
     * @param y The vertex y coordinate.
     * @param z The vertex z coordinate.
     * @param w The vertex w coordinate. Optional and defaults to 0.
     */
    void consumeV(float x, float y, float z, float w);

    /**
     * Callback when vertex texture coordinate data is loaded.
     *
     * @param u The texture u coordinate.
     * @param v The texture v coordinate. Optional and defaults to 0.
     * @param w The texture w coordinate. Optional and defaults to 0.
     */
    void consumeVT(float u, float v, float w);

    /**
     * Callback when vertex normal data is loaded.
     *
     * @param x The normal x coordinate.
     * @param y The normal y coordinate.
     * @param z The normal z coordinate.
     */
    void consumeVN(float x, float y, float z);

    /**
     * Callback when face data is loaded. Gives indices to previously loaded vertex data. If texture or normal indices
     * are not given hey are negative. This method assumes triangulated faces, thus data for 3 vertices is given.
     * The indices are made 0-based, not 1-based as in the raw {@code .obj} data.
     *
     * @param aV  Index to vertex geometry data for vertex A. Always present.
     * @param aVT Index to texture data for vertex A. {@code < 0} if not given.
     * @param aVN Index to normal data for vertex A. {@code < 0} if not given.
     * @param bV  Index to vertex geometry data for vertex B. Always present.
     * @param bVT Index to texture data for vertex B. {@code < 0} if not given.
     * @param bVN Index to normal data for vertex B. {@code < 0} if not given.
     * @param cV  Index to vertex geometry data for vertex C. Always present.
     * @param cVT Index to texture data for vertex C. {@code < 0} if not given.
     * @param cVN Index to normal data for vertex C. {@code < 0} if not given.
     */
    void consumeF(int aV, int aVT, int aVN,
                  int bV, int bVT, int bVN,
                  int cV, int cVT, int cVN);
}
