package com.joefazzino.flickring;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by joefazzino on 11/08/2016.
 */

// Base class is extended from instead of AppCompat simply to make modifications to the toolbar
public class BaseActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    public static final String FLICKR_QUERY = "FLICKR_QUERY";
    public static final String PHOTO_TRANSFER = "PHOTO_TRANSFER";

    protected Toolbar activateToolbar() {
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.app_bar);

            if (mToolbar != null) {
                setSupportActionBar(mToolbar);
            }

        }
        return mToolbar;
    }

    protected Toolbar activateToolbarWithHomeEnabled() {

        activateToolbar();
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        return mToolbar;

    }

}
