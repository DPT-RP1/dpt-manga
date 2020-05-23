package com.sony.dpt.manga;

import java.util.List;

public class MangaSeries implements Manga {
    private String name;
    private String folderPath;
    private String description;
    private List<MangaVolume> volumes;

    public MangaSeries() {
    }

    public String getName() {
        return name;
    }

    @Override
    public byte[] getThumbnail() {
        return new byte[0];
    }

    @Override
    public boolean isSeries() {
        return true;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<MangaVolume> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<MangaVolume> volumes) {
        this.volumes = volumes;
    }
}
