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

    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.image_pack_viewer, container, false);
        ImagePackImageView viewer = layout.findViewById(R.id.image_pack_view);
        View menu = layout.findViewById(R.id.manga_menu);

        View optionsMenu = layout.findViewById(R.id.viewer_options_menu);

        assert getArguments() != null;
        String path = ImagePackViewerFragmentArgs.fromBundle(getArguments()).getImagePackURI();

        MangaViewerController mangaViewerController = new MangaViewerController(
                context,
                viewer,
                menu,
                optionsMenu);
        mangaViewerController.setup();
        mangaViewerController.bootstrap(path);
        return layout;
    }

}
