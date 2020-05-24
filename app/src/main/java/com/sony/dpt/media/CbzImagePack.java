package com.sony.dpt.media;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class CbzImagePack implements ImagePack {

    private final String path;
    private Map<Integer, byte[]> prefetched;

    private final Map<Integer, String> pageToFilename;

    private final ZipFile cbzFile;
    private int currentPage = 0;
    private int pageCount;

    public static boolean canLoad(String path) {
        return path.endsWith("cbz");
    }

    private CbzImagePack(String path) throws IOException {
        this.path = path;
        this.prefetched = new HashMap<>();
        this.cbzFile = new ZipFile(new File(path));
        this.pageToFilename = new HashMap<>();
        scanForPages();
        registry.put(path, this);
    }

    public static ImagePack open(String path) throws IOException {
        return new CbzImagePack(path);
    }

    private void scanForPages() {
        List<String> fileNames = new ArrayList<String>();
        Enumeration<? extends ZipEntry> entries = cbzFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry current = entries.nextElement();
            if (!current.isDirectory()) fileNames.add(current.getName());
        }

        // TODO: revisit if needed
        Collections.sort(fileNames, String::compareTo);

        this.pageCount = 0;
        for (String fileName : fileNames) {
            pageToFilename.put(pageCount, fileName);
            pageCount += 1;
        }
        this.pageCount += 1;
    }

    private void prefetch() {

    }

    public byte[] fetch(int i) throws IOException {
        ZipEntry zipEntry = cbzFile.getEntry(pageToFilename.get(i));
        InputStream zipInputStream = cbzFile.getInputStream(zipEntry);

        byte[] buffer = new byte[(int) zipEntry.getSize()];
        new DataInputStream(zipInputStream).readFully(buffer);

        prefetched.put(i, buffer);
        return buffer;
    }

    @Override
    public byte[] page(int i) throws IOException {
        currentPage = i;
        return fetch(i);
    }


    @Override
    public int pageCount() {
        return pageCount;
    }

    @Override
    public int currentPage() {
        return currentPage;
    }

    @Override
    public void close() throws IOException {
        cbzFile.close();
        prefetched.clear();
        prefetched = null;
        registry.remove(path);
    }

    @Override
    public byte[] cover() throws IOException {
        if (prefetched.containsKey(0)) {
            return prefetched.get(0);
        }
        return fetch(0);
    }

    @Override
    public String name() {
        return cbzFile.getName();
    }

    @Override
    public String path() {
        return path;
    }
}
