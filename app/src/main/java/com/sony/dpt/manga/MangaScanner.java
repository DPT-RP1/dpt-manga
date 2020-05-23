package com.sony.dpt.manga;

import com.sony.dpt.media.ImagePack;
import com.sony.dpt.media.ImagePackFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MangaScanner {

    private final String rootPath;
    private final ImagePackFactory imagePackFactory;

    public MangaScanner(String rootPath) {
        this.rootPath = rootPath;
        this.imagePackFactory = new ImagePackFactory();
    }

    public List<Manga> scan() {
        // We have only 2 possible levels: folders at the root with volumes inside, or volumes at the root
        List<Manga> mangas = new ArrayList<>();
        File root = new File(rootPath);
        File[] files = root.listFiles();
        if (files == null) return mangas;

        for (File file : files) {
            if (!file.isDirectory()) {
                try {
                    ImagePack imagePack = imagePackFactory.load(file);
                    if (imagePack != null) {
                        MangaVolume mangaVolume = new MangaVolume();
                        mangaVolume.setContent(imagePack);
                        mangaVolume.setName(imagePack.name());
                        mangaVolume.setPath(file.getAbsolutePath());
                        mangas.add(mangaVolume);
                    }
                } catch (IOException ignored) {}
            } else {
                MangaSeries mangaSeries = new MangaSeries();
                mangaSeries.setFolderPath(file.getAbsolutePath());
                mangaSeries.setName(file.getName());
                try { mangaSeries.setVolumes(scanSeries(mangaSeries)); } catch (IOException ignored) {}
                mangas.add(mangaSeries);
            }
        }
        return mangas;
    }

    private List<MangaVolume> scanSeries(MangaSeries series) throws IOException {
        List<MangaVolume> volumes = new ArrayList<>();
        File root = new File(series.getFolderPath());

        File[] files = root.listFiles();
        if (files == null) return volumes;

        for (File file : files) {
            if (!file.isDirectory()) {
                ImagePack imagePack = imagePackFactory.load(file);
                if (imagePack != null) {
                    MangaVolume mangaVolume = new MangaVolume();
                    mangaVolume.setContent(imagePack);
                    mangaVolume.setName(imagePack.name());
                    mangaVolume.setPath(file.getAbsolutePath());
                    mangaVolume.setSeries(series);
                    volumes.add(mangaVolume);
                }
            }
        }
        return volumes;
    }

}
