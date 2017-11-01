package com.example.androidtdd.ui.photo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.androidtdd.R;
import com.example.androidtdd.data.model.Photo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by quanlt on 24/01/2017.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private List<Photo> photos;

    public PhotoAdapter() {
        photos = new ArrayList<>();
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
        notifyItemRangeInserted(0, photos.size());
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(photos.get(position).getThumbnailUrl())
                .into(holder.mPhotoImageView);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_photo)
        ImageView mPhotoImageView;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
