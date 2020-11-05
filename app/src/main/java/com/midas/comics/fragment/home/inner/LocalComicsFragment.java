package com.midas.comics.fragment.home.inner;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.midas.comics.R;
import com.midas.comics.activity.HomeActivity;
import com.midas.comics.activity.ReaderActivity;
import com.midas.comics.fragment.ReaderFragment;
import com.midas.comics.model.Comic;
import com.midas.comics.model.Storage;

import java.util.ArrayList;

public class LocalComicsFragment extends Fragment implements LocalComicsAdapter.OnItemClickListener/*, RomScanner.OnRomsFinderListener*/ {

    public static final int LOCAL_COMIC_TYPE_ALL = 0;
    public static final int LOCAL_COMIC_TYPE_READING = 1;
    public static final int LOCAL_COMIC_TYPE_UNREAD = 2;
    public static final int LOCAL_COMIC_TYPE_READ = 3;
    public static final int LOCAL_COMIC_TYPE_FAVOURITE = 4;

    int type = LOCAL_COMIC_TYPE_ALL;
    private static final String TAG = "LocalGamesFragment";
    LocalComicsAdapter comicsAdapter;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    public static LocalComicsFragment newInstance(int type) {
        LocalComicsFragment instance = new LocalComicsFragment();
        instance.type = type;
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_local_comics, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

        comicsAdapter = new LocalComicsAdapter(((HomeActivity)getActivity()).getPicasso(), this);
        recyclerView.setAdapter(comicsAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadComics();
    }

    private void loadComics() {
        ArrayList<Comic> allComics = Storage.getStorage(requireContext()).listComics();
        switch (type) {
            case LOCAL_COMIC_TYPE_ALL:
                comicsAdapter.addComics(allComics);
                break;
            case LOCAL_COMIC_TYPE_READING:
                ArrayList<Comic> readingComics = new ArrayList<>();
                for (Comic comic : allComics) {
                    if (comic.getCurrentPage() != 0) {
                        readingComics.add(comic);
                    }
                }
                comicsAdapter.addComics(readingComics);

                break;
            case LOCAL_COMIC_TYPE_UNREAD:
                ArrayList<Comic> unreadComics = new ArrayList<>();
                for (Comic comic : allComics) {
                    if (comic.getCurrentPage() == 0) {
                        unreadComics.add(comic);
                    }
                }
                comicsAdapter.addComics(unreadComics);
                break;
            case LOCAL_COMIC_TYPE_READ:
                ArrayList<Comic> readComics = new ArrayList<>();
                for (Comic comic : allComics) {
                    if (comic.getCurrentPage() == comic.getTotalPages()) {
                        readComics.add(comic);
                    }
                }
                comicsAdapter.addComics(readComics);
                break;
            case LOCAL_COMIC_TYPE_FAVOURITE:
                break;
        }

    }


    public void openComic(Comic comic) {
        if (!comic.getFile().exists()) {
            Snackbar.make(getView(), "Missing file. please update the library", Snackbar.LENGTH_SHORT).show();
//            Toast.makeText(
//                    getActivity(),
//                    R.string.warning_missing_file,
//                    Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(getActivity(), ReaderActivity.class);
        intent.putExtra(ReaderFragment.PARAM_HANDLER, comic.getId());
        intent.putExtra(ReaderFragment.PARAM_MODE, ReaderFragment.Mode.MODE_LIBRARY);
        startActivity(intent);
    }

    @Override
    public void onItemClick(Comic comic) {
        openComic(comic);
    }

    @Override
    public void onItemMoreClick(Comic game) {
//        ToastUtils.showLong("item more clicked");
    }
}
