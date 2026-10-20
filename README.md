# wmb-obj-loader

The utility library for loading **Wavefront .obj** files in Java for the 
[World Map Builder](https://github.com/TymHolt/world-map-builder) Project, contained as a dependency in the
[wmb-graphics-framework](https://github.com/TymHolt/wmb-graphics-framework).

## Install

To use this library for other projects, use **Maven**. Run
> mvn install

to have it built and installed as a usable dependency. 

```XML
<dependencies>
    <dependency>
        <groupId>org.wmbol</groupId>
        <artifactId>wmb-obj-loader</artifactId>
        <version>0.1.0</version>
    </dependency>
</dependencies>
```

## Build

This project can be built using **Maven**. Run
> mvn clean package

## Usage

Use an ```WmbObjLoader``` to parse a given ```String``` containing the raw OBJ content. The loader takes an
```IWmbObjLoadConsumer```, which serves as a callback for the parsed data. Every OBJ data point will be parsed
and given to the callback in the order they are defined in the source.

```java
WmbObjLoader loader = new WmbObjLoader(new IWmbObjLoadConsumer() {
    
        @Override
        public void consumeError(String message) {
            // An error occurred during parsing, parsing stops after this call
        }

        @Override
        public void consumeWarning(String message) {
            // A warning occurred during parsing, parsing continues
        }

        @Override
        public void consumeV(float x, float y, float z, float w) {
            // Vertex geometry was parsed
        }

        @Override
        public void consumeVT(float u, float v, float w) {
            // Vertex texture coordinates were parsed
        }

        @Override
        public void consumeVN(float x, float y, float z) {
            // Vertex normal was parsed
        }

        @Override
        public void consumeF(int aV, int aVT, int aVN, int bV, int bVT, int bVN, int cV, int cVT, int cVN) {
            // A face was parsed
        }
    });

loader.load("...");
```