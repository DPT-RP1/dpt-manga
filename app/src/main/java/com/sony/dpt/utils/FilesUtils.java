package com.sony.dpt.utils;

import java.io.File;

public class FilesUtils {

    public interface FileVisitor {
        void visit(File file);
    }

    public static void walk( String path, FileVisitor fileVisitor ) {

        File root = new File( path );
        File[] list = root.listFiles();

        if (list == null) return;

        for ( File f : list ) {
            fileVisitor.visit(f);
            if ( f.isDirectory() ) {
                walk( f.getAbsolutePath(), fileVisitor );
            }
        }
    }

}
