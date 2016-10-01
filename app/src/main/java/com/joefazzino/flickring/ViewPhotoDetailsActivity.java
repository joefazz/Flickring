package com.joefazzino.flickring;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ViewPhotoDetailsActivity extends BaseActivity {

    private TextView photoTitle, photoAuthor, photoTags;
    private ImageView photoImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_details);
        activateToolbarWithHomeEnabled();

        Intent intent = getIntent();
        Photo photo = (Photo) intent.getSerializableExtra(PHOTO_TRANSFER);

        if (photo.getTitle() != null) {
            photoTitle = (TextView) findViewById(R.id.photo_title);
            photoTitle.setText(photo.getTitle());
        }

        photoAuthor = (TextView) findViewById(R.id.photo_author);
        photoAuthor.setText(photo.getAuthor());

        photoTags = (TextView) findViewById(R.id.photo_tags);
        photoTags.setText("Tags: " + photo.getTags());

        photoImage = (ImageView) findViewById(R.id.photo_image);
        Picasso.with(this).load(photo.getLink()).error(R.drawable.placekitten).placeholder(R.drawable.placekitten).into(photoImage);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
