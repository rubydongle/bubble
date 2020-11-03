package com.midas.comics.fragment.home.inner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.midas.comics.R;
import com.midas.comics.managers.LocalCoverHandler;
import com.midas.comics.model.Comic;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class LocalComicsAdapter extends RecyclerView.Adapter<LocalComicsAdapter.GameViewHolder> {

    private ArrayList<Comic> comics = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private Picasso mPicasso;

    public LocalComicsAdapter(Picasso picasso, OnItemClickListener listener) {
        onItemClickListener = listener;
        mPicasso = picasso;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comic, parent, false);
        GameViewHolder viewHolder = new GameViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GameViewHolder holder, final int position) {
        Comic comic = comics.get(position);
        holder.setGameTitle(comics.get(position).getFile().getName());//getCleanName());
        holder.holderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(comics.get(position));
            }
        });
        holder.moreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemMoreClick(comics.get(position));
            }
        });
        mPicasso.load(LocalCoverHandler.getComicCoverUri(comic))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return comics.size();
    }



    class GameViewHolder extends RecyclerView.ViewHolder {

        public View holderView;
        TextView textView;
        ImageView imageView;
        public ImageView moreView;
        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            holderView = itemView;
            textView = itemView.findViewById(R.id.tv_title);
            imageView = itemView.findViewById(R.id.iv_cover);
            moreView = itemView.findViewById(R.id.iv_more);
        }

        public void setGameTitle(String title) {
            textView.setText(title);
        }

    }

    public void setComics(ArrayList<Comic> comics) {
        this.comics = new ArrayList<>(comics);
        notifyDataSetChanged();
//        filterGames();
    }

    public int addGames(ArrayList<Comic> newComics) {
        for (Comic comic : newComics) {
            if (!comics.contains(comic)) {
                comics.add(comic);
            }
        }
        notifyDataSetChanged();
//        filterGames();
        return comics.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Comic comic);
        void onItemMoreClick(Comic comic);
    }
}
