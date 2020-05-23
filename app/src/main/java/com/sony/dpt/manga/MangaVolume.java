package com.sony.dpt.manga;

import com.sony.dpt.media.ImagePack;

import java.io.IOException;

public class MangaVolume implements Manga {
    private String name;
    private String path;
    private ImagePack content;
    private MangaSeries series;
    private String description;

    public MangaVolume() {
    }

    public String getName() {
        return name;
    }

    @Override
    public byte[] getThumbnail() {
        try {
            return content.cover();
        } catch (IOException ignored) { } // TODO: default ?
        return null;
    }

    @Override
    public boolean isSeries() {
        return false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ImagePack getContent() {
        return content;
    }

    public void setContent(ImagePack content) {
        this.content = content;
    }

    public MangaSeries getSeries() {
        return series;
    }

    public void setSeries(MangaSeries series) {
        this.series = series;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
