package com.sony.dpt.manga.persistence;

import androidx.room.Entity;

import com.sony.dpt.manga.Manga;

import java.util.List;

@Entity
public class MangaSeries {
    private String name;
    private String folderPath;
    private String description;
    private List<MangaVolume> volumes;


}
