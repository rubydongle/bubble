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

public class LocalComicsFragment extends Fragment implements LocalComicsAdapter.OnItemClickListener/*, RomScanner.OnRomsFinderListener*/ {

    private static final String TAG = "LocalGamesFragment";
    LocalComicsAdapter comicsAdapter;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

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
        comicsAdapter.addGames(Storage.getStorage(requireContext()).listComics());
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
