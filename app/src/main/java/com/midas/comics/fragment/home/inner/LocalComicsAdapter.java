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


public class LocalComicsAdapter extends RecyclerView.Adapter<LocalComicsAdapter.ComicViewHolder> {

    private ArrayList<Comic> comics = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private Picasso mPicasso;

    public LocalComicsAdapter(Picasso picasso, OnItemClickListener listener) {
        onItemClickListener = listener;
        mPicasso = picasso;
    }

    @NonNull
    @Override
    public ComicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comic, parent, false);
        ComicViewHolder viewHolder = new ComicViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ComicViewHolder holder, final int position) {
        Comic comic = comics.get(position);
        holder.setComicTitle(comic.getFile().getName());//getCleanName());
        holder.pathTextView.setText(comic.getFile().getPath());
        holder.pageTextView.setText(comic.getTotalPages() + "页");
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



    class ComicViewHolder extends RecyclerView.ViewHolder {

        public View holderView;
        TextView titleTextView;
        public TextView pathTextView;
        public TextView pageTextView;
        ImageView imageView;
        public ImageView moreView;
        public ComicViewHolder(@NonNull View itemView) {
            super(itemView);
            holderView = itemView;
            titleTextView = itemView.findViewById(R.id.tv_title);
            pathTextView = itemView.findViewById(R.id.tv_path);
            pageTextView = itemView.findViewById(R.id.tv_page);
            imageView = itemView.findViewById(R.id.iv_cover);
            moreView = itemView.findViewById(R.id.iv_more);
        }

        public void setComicTitle(String title) {
            titleTextView.setText(title);
        }

    }

    public void setComics(ArrayList<Comic> comics) {
        this.comics = new ArrayList<>(comics);
        notifyDataSetChanged();
//        filterGames();
    }

    public int addComics(ArrayList<Comic> newComics) {
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
