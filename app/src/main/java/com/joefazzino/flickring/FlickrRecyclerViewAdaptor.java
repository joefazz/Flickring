package com.joefazzino.flickring;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by joefazzino on 10/08/2016.
 */
public class FlickrRecyclerViewAdaptor extends RecyclerView.Adapter<FlickrImageViewHolder> {
    private List<Photo> mPhotosList;
    private Context mContext;
    private final String LOG_TAG = FlickrRecyclerViewAdaptor.class.getSimpleName();

    public FlickrRecyclerViewAdaptor(List<Photo> photosList, Context context) {
        this.mPhotosList = photosList;
        this.mContext = context;
    }

    @Override
    public FlickrImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, null);
        FlickrImageViewHolder flickrImageViewHolder = new FlickrImageViewHolder(view);


        return flickrImageViewHolder;
    }

    @Override
    public int getItemCount() {
        return (null != mPhotosList ? mPhotosList.size() : 0 );
    }

    public Photo getPhoto(int position) {
        return (null != mPhotosList ? mPhotosList.get(position) : null);
    }

    @Override
    public void onBindViewHolder(FlickrImageViewHolder holder, int position) {
        Photo photoItem = mPhotosList.get(position);
        Log.d(LOG_TAG, "PROCESSING: " + photoItem.getTitle() + " ----> " + Integer.toString(position));
        Picasso.with(mContext).load(photoItem.getImage())
                .error(R.drawable.placekitten)
                .placeholder(R.drawable.placekitten)
                .into(holder.thumbnail);

        holder.title.setText(photoItem.getTitle());
    }

    public void loadNewData(List<Photo> newPhotos) {
        mPhotosList = newPhotos;
        notifyDataSetChanged();
    }
}
