package com.sony.dpt.media;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This represent a flippable image group, the original intent being
 * to abstract cbz/cbr files
 */
public interface ImagePack {

    Map<String, ImagePack> registry = new HashMap<String, ImagePack>();

    byte[] page(int i) throws IOException;

    int pageCount();

    int currentPage();

    void close() throws IOException;

    byte[] cover() throws IOException;

    String name();

    String path();
}
