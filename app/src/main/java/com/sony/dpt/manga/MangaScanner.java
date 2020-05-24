package com.sony.dpt.manga;

import android.content.Context;

import androidx.room.Room;

import com.sony.dpt.manga.persistence.MangaDatabase;
import com.sony.dpt.manga.persistence.MangaVolume;
import com.sony.dpt.media.ImagePack;
import com.sony.dpt.media.ImagePackFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MangaScanner {

    private final String rootPath;
    private final ImagePackFactory imagePackFactory;
    private final MangaDatabase mangaDatabase;

    public MangaScanner(Context context, String rootPath) {
        this.rootPath = rootPath;
        this.imagePackFactory = new ImagePackFactory();

        this.mangaDatabase = Room.databaseBuilder(
                context,
                MangaDatabase.class,
                "manga-database"
        ).allowMainThreadQueries().build();

    }

    // TODO: support series
    public void scan() throws IOException {
        List<String> indexedPaths = mangaDatabase.mangaDao().indexedPaths();

        File root = new File(rootPath);
        File[] files = root.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (!file.isDirectory()) {
                if (indexedPaths.contains(file.getAbsolutePath())) {
                    indexedPaths.remove(file.getAbsolutePath());
                } else {
                    ImagePack imagePack = imagePackFactory.load(file);
                    MangaVolume mangaVolume = new MangaVolume();
                    mangaVolume.name = imagePack.name();
                    mangaVolume.currentPage = 0;
                    mangaVolume.pageCount = imagePack.pageCount();
                    mangaVolume.volumeIndex = 0; // TODO: support series
                    mangaVolume.path = file.getAbsolutePath();
                    mangaVolume.thumbnail = imagePack.cover();

                    imagePack.close();
                    mangaDatabase.mangaDao().insert(mangaVolume);
                }

            }
        }

        // We delete whatever file disappeared from the db
        for (String path : indexedPaths) {
            mangaDatabase.mangaDao().deleteByPath(path);
        }
    }

}
