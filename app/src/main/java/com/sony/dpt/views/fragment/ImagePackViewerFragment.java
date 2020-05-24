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

public class ImagePackViewerFragment extends Fragment {

    private ImagePackImageView currentView;
    private GestureDetector gestureDetector;
    private ImagePackFactory imagePackFactory;
    private MangaDatabase mangaDatabase;
    private MangaVolume currentVolume;

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
        View view = inflater.inflate(R.layout.image_pack_viewer, container, false);
        ImagePackImageView imagePackImageView = view.findViewById(R.id.image_pack_view);

        assert getArguments() != null;
        String path = ImagePackViewerFragmentArgs.fromBundle(getArguments()).getImagePackURI();
        currentVolume = mangaDatabase.mangaDao().findByPath(path);

        try {
            imagePackImageView.setImagePack(imagePackFactory.load(path));
            imagePackImageView.display(currentVolume.currentPage);
        } catch (IOException ignored) {
            System.out.println(ignored);
        }


        currentView = imagePackImageView;
        currentView.setOnTouchListener((v, event) -> {
            if (event.getToolType(0) == MotionEvent.TOOL_TYPE_FINGER && gestureDetector != null) {
                return gestureDetector.onTouchEvent(event);
            }
            return false;
        });
        return view;
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
        });
    }

}
