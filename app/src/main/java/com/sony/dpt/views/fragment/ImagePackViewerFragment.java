package com.sony.dpt.views.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sony.dpt.R;
import com.sony.dpt.controller.MangaViewerController;
import com.sony.dpt.controller.menu.MangaMenuController;
import com.sony.dpt.views.ImagePackImageView;

public class ImagePackViewerFragment extends Fragment {

    private ImagePackImageView viewer;

    private View menu;
    private View layout;
    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.image_pack_viewer, container, false);
        viewer = layout.findViewById(R.id.image_pack_view);
        menu = layout.findViewById(R.id.manga_menu);

        assert getArguments() != null;
        String path = ImagePackViewerFragmentArgs.fromBundle(getArguments()).getImagePackURI();

        MangaViewerController mangaViewerController = new MangaViewerController(context, viewer, menu);
        mangaViewerController.setup();
        mangaViewerController.bootstrap(path);
        return layout;
    }

}
