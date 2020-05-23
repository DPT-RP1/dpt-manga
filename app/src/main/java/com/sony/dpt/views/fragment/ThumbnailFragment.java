package com.sony.dpt.views.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.sony.dpt.R;
import com.sony.dpt.manga.Manga;
import com.sony.dpt.manga.MangaScanner;
import com.sony.dpt.manga.MangaVolume;
import com.sony.dpt.views.ThumbnailView;

import java.util.List;

import static com.sony.dpt.views.fragment.ThumbnailFragmentDirections.actionThumbnailFragmentToImagePackViewerFragment;

public class ThumbnailFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.thumbnails, container, false);
        final ThumbnailView thumbnailView = view.findViewById(R.id.thumbnail);

        MangaScanner mangaScanner = new MangaScanner("/data/manga");

        final List<Manga> mangas = mangaScanner.scan();

        if (mangas != null) {
            for (Manga manga : mangas) {
                if (!manga.isSeries()) { // TODO: support series
                    thumbnailView.addThumbnail(manga.getThumbnail());
                }
            }
        }

        thumbnailView.setOnItemClickListener((parent, view1, position, id) -> Navigation
                .findNavController(view1)
                .navigate(
                    actionThumbnailFragmentToImagePackViewerFragment(((MangaVolume) mangas.get(position)).getContent().path())
                ));

        return view;
    }

}
