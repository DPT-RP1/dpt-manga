package com.sony.dpt.manga.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MangaDao {

    @Insert
    void insert(MangaVolume mangaVolume);

    @Query("SELECT * FROM MangaVolume WHERE path = :path")
    MangaVolume findByPath(String path);

    @Query("UPDATE MangaVolume SET current_page = :page WHERE path = :path")
    void moveToPage(String path, int page);

    @Query("SELECT * FROM MangaVolume")
    List<MangaVolume> findAll();

    @Query("SELECT path FROM MangaVolume")
    List<String> indexedPaths();

    @Query("DELETE FROM MangaVolume WHERE path = :path")
    void deleteByPath(String path);
}
