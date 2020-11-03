package com.nkanaev.comics.fragment.home.inner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.nkanaev.comics.R;
import com.nkanaev.comics.model.Comic;
import com.nkanaev.comics.model.Storage;

public class LocalComicsFragment extends Fragment implements LocalComicsAdapter.OnItemClickListener/*, RomScanner.OnRomsFinderListener*/ {

    private static final String TAG = "LocalGamesFragment";
    LocalComicsAdapter gamesAdapter;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_local_comics, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

        gamesAdapter = new LocalComicsAdapter(this);
        recyclerView.setAdapter(gamesAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gamesAdapter.addGames(Storage.getStorage(requireContext()).listComics());
    }


//    protected Set<String> exts;
//    protected Set<String> inZipExts;
//    protected boolean reloadGames = true;
//    protected boolean reloading = false;
//    private RomScanner romsFinder = null;
//    private DatabaseHelper dbHelper = null;
//    ProgressDialog searchDialog = null;
//    private boolean importing = false;
//
//
//    protected void reloadGames(boolean searchNew, File selectedFolder) {
//        if (romsFinder == null) {
//            reloadGames = false;
//            reloading = searchNew;
//            romsFinder = new RomScanner(exts, inZipExts, this, this, searchNew, selectedFolder);
//            romsFinder.start();
//        }
//    }
//
//    protected void stopRomsFinding() {
//        if (romsFinder != null) {
//            romsFinder.stopSearch();
//        }
//    }
//
//    private void showSearchProgressDialog(boolean zipMode) {
//        if (searchDialog == null) {
//            searchDialog = new ProgressDialog(requireContext());
//            searchDialog.setMax(100);
//            searchDialog.setCancelable(false);
//            searchDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            searchDialog.setIndeterminate(true);
//            searchDialog.setProgressNumberFormat("");
//            searchDialog.setProgressPercentFormat(null);
//            searchDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel),
//                    (dialog, which) -> stopRomsFinding());
//        }
//        searchDialog.setMessage(getString(zipMode ?
//                R.string.gallery_zip_search_label
//                : R.string.gallery_sdcard_search_label));
//        DialogUtils.show(searchDialog, false);
//
//    }
//
//    public void setLastGames(ArrayList<GameDescription> games) {
//        gamesAdapter.setComics(games);
//    }
//
//    public void setNewGames(ArrayList<GameDescription> games) {
//        boolean isListEmpty = gamesAdapter.addGames(games) == 0;
//    }
//
//    public void onSearchingEnd(final int count, final boolean showToast) {
//        requireActivity().runOnUiThread(() -> {
//            if (searchDialog != null) {
//                searchDialog.dismiss();
//                searchDialog = null;
//            }
//            if (showToast) {
//                if (count > 0) {
//                    Snackbar.make(recyclerView, getString(R.string.gallery_count_of_found_games, count),
//                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                }
//            }
//        });
//    }

//    void runOnCreate() {
//        exts = Constants.getFCGameExtensions();//getRomExtensions();
//        exts.addAll(Constants.getArchiveExtensions());//getArchiveExtensions());
//        dbHelper = new DatabaseHelper(requireContext());
//        SharedPreferences pref = requireActivity().getSharedPreferences("android50comp", Context.MODE_PRIVATE);
//        String androidVersion = Build.VERSION.RELEASE;
//
//        if (!pref.getString("androidVersion", "").equals(androidVersion)) {
//            SQLiteDatabase db = dbHelper.getWritableDatabase();
//            dbHelper.onUpgrade(db, Integer.MAX_VALUE - 1, Integer.MAX_VALUE);
//            db.close();
//            SharedPreferences.Editor editor = pref.edit();
//            editor.putString("androidVersion", androidVersion);
//            editor.apply();
//        }
//        inZipExts = Constants.getFCGameExtensions();//getRomExtensions();
//        reloadGames = true;
//    }

//    void runOnResume() {
//        gamesAdapter.notifyDataSetChanged();
//        if (reloadGames && !importing) {
//            boolean isDBEmpty = dbHelper.countObjsInDb(GameDescription.class, null) == 0;
//            reloadGames(isDBEmpty, null);
//        }
//    }

//    private void setupViewPager(ViewPager mViewPager) {
//        adapter = new GalleryPagerAdapter(requireActivity(), this);
//        mViewPager.setAdapter(adapter);
//    }

//    public boolean onGameSelected(Comic game, int slot) {
//        Intent intent = new Intent(requireContext(), getEmulatorActivityClass());
//        intent.putExtra(EmulatorActivity.EXTRA_GAME, game);
//        intent.putExtra(EmulatorActivity.EXTRA_SLOT, slot);
//        intent.putExtra(EmulatorActivity.EXTRA_FROM_GALLERY, true);
//        startActivity(intent);
//        return true;
//    }

    @Override
    public void onItemClick(Comic game) {
//        File gameFile = new File(game.getFile()path);
////        NLog.i(TAG, "select " + game);
//
//        if (game.isInArchive()) {
//            gameFile = new File(requireActivity().getExternalCacheDir(), game.checksum);
//            game.path = gameFile.getAbsolutePath();
//            ZipRomFile zipRomFile = dbHelper.selectObjFromDb(ZipRomFile.class,
//                    "WHERE _id=" + game.zipfile_id, false);
//            File zipFile = new File(zipRomFile.path);
//            if (!gameFile.exists()) {
//                try {
//                    EmuUtils.extractFile(zipFile, game.name, gameFile);
//                } catch (IOException e) {
//                    LogUtils.e(TAG, "", e);
//                }
//            }
//        }
//
//        if (gameFile.exists()) {
//            game.lastGameTime = System.currentTimeMillis();
//            game.runCount++;
//            dbHelper.updateObjToDb(game, new String[]{"lastGameTime", "runCount"});
//            onGameSelected(game, 0);
//        } else {
//            LogUtils.w(TAG, "rom file:" + gameFile.getAbsolutePath() + " does not exist");
//            AlertDialog dialog = new AlertDialog.Builder(requireContext())
//                    .setMessage(getString(R.string.gallery_rom_not_found))
//                    .setTitle(R.string.error)
//                    .setPositiveButton(R.string.gallery_rom_not_found_reload, (dialog1, which)
//                            -> reloadGames(true, null))
//                    .setCancelable(false)
//                    .create();
//            dialog.setOnDismissListener(dialog12 ->
//                    reloadGames(true, null));
//            dialog.show();
//        }

    }

    @Override
    public void onItemMoreClick(Comic game) {
//        ToastUtils.showLong("item more clicked");
    }
}
