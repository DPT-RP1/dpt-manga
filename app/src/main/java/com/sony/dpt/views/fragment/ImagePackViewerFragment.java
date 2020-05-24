package com.sony.dpt.views.fragment;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.room.Room;

import com.sony.dpt.R;
import com.sony.dpt.controller.DptGestureDetector;
import com.sony.dpt.controller.DptGestureListener;
import com.sony.dpt.manga.persistence.MangaDatabase;
import com.sony.dpt.manga.persistence.MangaVolume;
import com.sony.dpt.media.CbzImagePack;
import com.sony.dpt.media.ImagePack;
import com.sony.dpt.media.ImagePackFactory;
import com.sony.dpt.views.ImagePackImageView;
import com.sony.dpt.views.ThumbnailView;

import java.io.IOException;

import static com.sony.dpt.views.fragment.ImagePackViewerFragmentDirections.actionImagePackViewerFragmentToThumbnailFragment;
import static com.sony.dpt.views.fragment.ThumbnailFragmentDirections.actionThumbnailFragmentToImagePackViewerFragment;

public class ImagePackViewerFragment extends Fragment {

    private ImagePackImageView currentView;
    private GestureDetector gestureDetector;
    private ImagePackFactory imagePackFactory;
    private MangaDatabase mangaDatabase;
    private MangaVolume currentVolume;
    private View viewer;
    private View menu;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        setupGestureDetector(context);
        imagePackFactory = new ImagePackFactory();
        mangaDatabase = Room.databaseBuilder(
                context,
                MangaDatabase.class,
                "manga-database"
        ).allowMainThreadQueries().build();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewer = inflater.inflate(R.layout.image_pack_viewer, container, false);
        currentView = viewer.findViewById(R.id.image_pack_view);
        menu = viewer.findViewById(R.id.manga_menu);

        View backButton = menu.findViewById(R.id.back);
        backButton.setOnClickListener(v ->
            Navigation
                    .findNavController(currentView)
                    .navigate(actionImagePackViewerFragmentToThumbnailFragment())
        );

        assert getArguments() != null;
        String path = ImagePackViewerFragmentArgs.fromBundle(getArguments()).getImagePackURI();
        currentVolume = mangaDatabase.mangaDao().findByPath(path);

        try {
            currentView.setImagePack(imagePackFactory.load(path));
            currentView.display(currentVolume.currentPage);
        } catch (IOException ignored) {
            System.out.println(ignored);
        }

        currentView.setOnTouchListener((v, event) -> {
            if (event.getToolType(0) == MotionEvent.TOOL_TYPE_FINGER && gestureDetector != null) {
                return gestureDetector.onTouchEvent(event);
            }
            return false;
        });
        return viewer;
    }

    private void setupGestureDetector(Context context) {
        this.gestureDetector = new DptGestureDetector(context, new DptGestureListener() {
            @Override
            public void onFlingRight() {
                try {
                    mangaDatabase.mangaDao()
                            .moveToPage(
                                    currentVolume.path,
                                    currentView.flipNext()
                            );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFlingLeft()  {
                try {
                    mangaDatabase.mangaDao().moveToPage(
                            currentVolume.path,
                            currentView.flipPrevious()
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
