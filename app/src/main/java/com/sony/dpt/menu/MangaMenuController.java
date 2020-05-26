package com.sony.dpt.menu;

import android.view.View;

import androidx.navigation.Navigation;

import com.sony.dpt.R;

import static com.sony.dpt.views.fragment.ImagePackViewerFragmentDirections.actionImagePackViewerFragmentToThumbnailFragment;

public class MangaMenuController {

    private View menu;
    private View viewer;

    public MangaMenuController(View menu, View viewer) {
        this.menu = menu;
        this.viewer = viewer;
    }

    public void setup() {
        View backButton = menu.findViewById(R.id.back);
        backButton.setOnClickListener(v ->
                Navigation
                        .findNavController(viewer)
                        .navigate(actionImagePackViewerFragmentToThumbnailFragment())
        );
    }
}
