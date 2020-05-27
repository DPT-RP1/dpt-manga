package com.sony.dpt.controller.menu;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.sony.dpt.App;
import com.sony.dpt.R;
import com.sony.dpt.views.ImagePackImageView;

import java.io.IOException;

import static com.sony.dpt.views.fragment.ImagePackViewerFragmentDirections.actionImagePackViewerFragmentToThumbnailFragment;

public class MangaMenuController {

    private View menu;
    private ImagePackImageView viewer;
    private View backButton;
    private ProgressBar progressBar;
    private TextView progressBarText;

    public MangaMenuController(View menu, ImagePackImageView viewer) {
        this.menu = menu;
        this.viewer = viewer;
    }

    public void setup() {
        backButton = menu.findViewById(R.id.back);
        backButton.setOnClickListener(v ->
                Navigation
                        .findNavController(viewer)
                        .navigate(actionImagePackViewerFragmentToThumbnailFragment())
        );

        progressBar = menu.findViewById(R.id.page_progress_bar);
        progressBar.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                int page = (int) ((event.getX() / (float) v.getWidth()) * progressBar.getMax());
                try {
                    viewer.display(page);
                } catch (IOException ignored) { }
                moveToPage(
                       page,
                       progressBar.getMax());
            }
            return true;
        });

        progressBarText = menu.findViewById(R.id.page_progress_bar_text);
    }

    public void moveToPage(int page, int pageCount) {
        progressBar.setMax(pageCount);
        progressBar.setProgress(page);
        progressBarText.setText(App.getAppResources().getString(R.string.progress_text, page, pageCount));
    }
}
