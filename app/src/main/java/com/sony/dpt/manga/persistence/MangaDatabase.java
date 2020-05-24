package com.sony.dpt.manga.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {MangaVolume.class}, version = 1, exportSchema = false)
public abstract class MangaDatabase extends RoomDatabase {
    public abstract MangaDao mangaDao();
}
