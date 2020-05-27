package com.sony.dpt.controller;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.room.Room;

import com.sony.dpt.controller.menu.MangaMenuController;
import com.sony.dpt.manga.persistence.MangaDatabase;
import com.sony.dpt.manga.persistence.MangaVolume;
import com.sony.dpt.media.ImagePackFactory;
import com.sony.dpt.views.ImagePackImageView;

import java.io.IOException;

public class MangaViewerController {

    private final Context context;
    private final ImagePackImageView viewer;
    private GestureDetector gestureDetector;
    private MangaDatabase mangaDatabase;
    private MangaVolume currentVolume;
    private final ImagePackFactory imagePackFactory;
    private final View menu;
    private final MangaMenuController mangaMenuController;

    public MangaViewerController(final Context context, final ImagePackImageView viewer, final View menu) {
        this.context = context;
        this.viewer = viewer;
        this.menu = menu;
        mangaDatabase = Room.databaseBuilder(
                context,
                MangaDatabase.class,
                "manga-database"
        ).allowMainThreadQueries().build();
        imagePackFactory = new ImagePackFactory();

        mangaMenuController = new MangaMenuController(menu, viewer);
    }

    public void setup() {
        setupGestureDetector(context);
        viewer.setOnTouchListener((v, event) -> {
            if (event.getToolType(0) == MotionEvent.TOOL_TYPE_FINGER && gestureDetector != null) {
                return gestureDetector.onTouchEvent(event);
            }
            return false;
        });

        mangaMenuController.setup();
    }

    public void bootstrap(String path) {
        currentVolume = mangaDatabase.mangaDao().findByPath(path);
        try {
            viewer.setImagePack(imagePackFactory.load(path));
            viewer.display(currentVolume.currentPage);
            moveToPage(currentVolume.currentPage);
        } catch (IOException ignored) { }
    }

    public void moveToPage(int page) {
        mangaDatabase.mangaDao().moveToPage(currentVolume.path, page);
        mangaMenuController.moveToPage(page, currentVolume.pageCount);
    }

    private void setupGestureDetector(Context context) {
        this.gestureDetector = new DptGestureDetector(context, new DptGestureListener() {
            @Override
            public void onFlingRight() {
                try {
                    moveToPage(viewer.flipNext());
                } catch (IOException ignored) { }
            }

            @Override
            public void onFlingLeft()  {
                try {
                    moveToPage(viewer.flipPrevious());
                } catch (IOException ignored) { }
            }

            @Override
            public void onSingleTap() {
                switch (menu.getVisibility()) {
                    case View.VISIBLE:
                        menu.setVisibility(View.INVISIBLE);
                        break;
                    case View.GONE:
                    case View.INVISIBLE:
                        menu.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }
}
