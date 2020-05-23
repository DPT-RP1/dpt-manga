package com.sony.dpt.media;

import java.io.File;
import java.io.IOException;

public class ImagePackFactory {

    public ImagePack load(String path) throws IOException {
        if (CbzImagePack.canLoad(path)) {
            return CbzImagePack.open(path);
        }
        return null;
    }

    public ImagePack load(File file) throws IOException {
        return load(file.getAbsolutePath());
    }

}
