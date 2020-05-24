package com.sony.dpt.manga.persistence;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.sony.dpt.manga.Manga;

@Entity
public class MangaVolume implements Manga {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "path")
    public String path;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "volume_index")
    public int volumeIndex;

    @ColumnInfo(name = "page_count")
    public int pageCount;

    @ColumnInfo(name = "current_page")
    public int currentPage;

    @ColumnInfo(name = "thumbnail", typeAffinity = ColumnInfo.BLOB)
    public byte[] thumbnail;

    @ColumnInfo(name = "description")
    public String description;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public byte[] getThumbnail() {
        return thumbnail;
    }

    @Override
    public boolean isSeries() {
        return false;
    }


}
