package com.example.authapp3.control;

import android.annotation.SuppressLint;
import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.authapp3.R;
import com.example.authapp3.entity.Photo;
import com.example.authapp3.entity.PlacesResult;

import java.io.InputStream;
import java.util.List;

public class PlacesListAdapter extends ArrayAdapter<PlacesResult> {

    private final Context context;
    private final List<PlacesResult> placesResults;
    Bitmap photo;

    public PlacesListAdapter(Context context, List<PlacesResult> placesResults) {
        super(context, R.layout.place_row_layout, placesResults);
        this.context = context;
        this.placesResults = placesResults;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        try {
            ViewHolder viewHolder;
            if(view == null) {
                viewHolder = new ViewHolder();
                view = LayoutInflater.from(context).inflate(R.layout.place_row_layout,null);
                viewHolder.textViewName = view.findViewById(R.id.textViewName);
                viewHolder.textViewAddress = view.findViewById(R.id.textViewAddress);
                viewHolder.imageViewPhoto = view.findViewById(R.id.imageViewPhoto);
                view.setTag(viewHolder);
            } else  {
                viewHolder = (ViewHolder) view.getTag();
            }
            PlacesResult placesResult = placesResults.get(position);
            viewHolder.textViewName.setText(placesResult.getName());
            viewHolder.textViewAddress.setText(placesResult.getVicinity());
            Photo photoarray = placesResult.getPhotos().get(0);
            String photourl = "https://maps.googleapis.com/maps/api/place/photo" +"?maxwidth=400" +
                    "&photo_reference="+ photoarray.getPhotoReference() + "&key=" + "AIzaSyB4jNfDzwogpxS5Q3lZKNdVlGzA_ipiOCQ";
            photo = new ImageRequestAsk().execute(photourl).get();
            if(photo == null){
                photo = new ImageRequestAsk().execute(placesResult.getIcon()).get();
                }
            viewHolder.imageViewPhoto.setImageBitmap(photo);
            return view;
        } catch (Exception e) {
                return view;
        }
    }

    public static class ViewHolder {
        public TextView textViewName;
        public TextView textViewAddress;
        public ImageView imageViewPhoto;
    }

    private static class ImageRequestAsk extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                InputStream inputStream = new java.net.URL(params[0]).openStream();
                return BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                return null;
            }
        }

    }
}