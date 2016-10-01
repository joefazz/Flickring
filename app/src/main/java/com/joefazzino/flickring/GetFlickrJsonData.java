package com.joefazzino.flickring;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joefazzino on 10/08/2016.
 */

// Extend the GetRawData class because we need to translate it from the JSON format
public class GetFlickrJsonData extends GetRawData {
    private String LOG_TAG = GetFlickrJsonData.class.getSimpleName();
    private List<Photo> mPhotos; // For all the items in the file make a list of them as Photo objects
    private Uri mDestinationUri; // URI is fancier URL with additional info

    // When initialised take the thing to search for and if we should search for all queries or any of them
    public GetFlickrJsonData(String searchCriteria, boolean matchAll) {
        super(null);

        createAndUpdateUri(searchCriteria, matchAll); // Call function with parameters
        mPhotos = new ArrayList<Photo>(); // Get mPhotos ready to take Photo objects
    }

    public List<Photo> getPhotos() {
        return mPhotos;
    }

    // When the execute funciton is called set the URL for processing as the new URI and print the new URI
    public void execute() {
        super.setmRawURL(mDestinationUri.toString());
        DownloadJsonData downloadJsonData = new DownloadJsonData();
        Log.v(LOG_TAG, "BUILT URI = " + mDestinationUri.toString());
        downloadJsonData.execute(mDestinationUri.toString());
    }

    // Takes the base url and then adds all the seperate parameters like tags and format so we can make it
    // JSON and dictate what we want to search for
    public boolean createAndUpdateUri(String searchCriteria, boolean matchAll) {
        final String FLICKR_BASE_API_URL = "https://api.flickr.com/services/feeds/photos_public.gne";
        final String TAGS_PARAM = "tags";
        final String TAGMODE_PARAM = "tagmode";
        final String FORMAT_PARAM = "format";
        final String NOJSONCALLBACK_PARAM = "nojsoncallback";

        // Sets up what the URI will be
        mDestinationUri = Uri.parse(FLICKR_BASE_API_URL).buildUpon() // Start with the base
                .appendQueryParameter(TAGS_PARAM,searchCriteria) // Add the search parameters passed
                .appendQueryParameter(TAGMODE_PARAM, matchAll ? "all" : "any") // Check to see what the match parameter is
                .appendQueryParameter(FORMAT_PARAM, "json") // Make sure the format is in JSON
                .appendQueryParameter(NOJSONCALLBACK_PARAM, "1") // Make sure NOJSONCALLBACK is on otherwise JSON is invalid
                .build(); // Create the URI

        return mDestinationUri != null; // Return false if the URI isn't empty otherwise return true
    }

    // Make sure the download status is ok and then get ready for the JSON output
    public void processResult() {
        if (getmDownloadStatus() != DownloadStatus.OK) {
            Log.e(LOG_TAG, "ERROR PROCESSING RESULT UNSUCCESSFUL DOWNLOAD");
            return;
        }

        // List of JSON elements and arrays
        final String ITEMS = "items";
        final String TITLE = "title";
        final String MEDIA = "media";
        final String PHOTO_URL = "m";
        final String AUTHOR = "author";
        final String AUTHOR_ID = "author_id";
        final String LINK = "link";
        final String TAGS = "tags";

        try {

            JSONObject jsonData = new JSONObject(getmData()); // Grab all the data and store it as a JSON object
            JSONArray itemsArray = jsonData.getJSONArray(ITEMS); // As items contains all the photos it is an array JSON object

            // While there are still items inside the JSON items array keep going through
            for (int i=0; i < itemsArray.length(); i++) {
                JSONObject jsonPhoto = itemsArray.getJSONObject(i); // Store each object scrolled through as the current jsonPhoto

                String title = jsonPhoto.getString(TITLE); // Grab the current title for the photo
                String author = jsonPhoto.getString(AUTHOR); // Author grabbed
                String author_id = jsonPhoto.getString(AUTHOR_ID); // Author ID grabbed
//                String link = jsonPhoto.getString(LINK); // Link to post grabbed
                String tags = jsonPhoto.getString(TAGS); // All tags grabbed

                JSONObject jsonMedia = jsonPhoto.getJSONObject(MEDIA); // Store the media as a seperate JSON object
                String photoUrl = jsonMedia.getString(PHOTO_URL); // Grab the photo url from the JSON media object
                String link = photoUrl.replaceFirst("_m.","_b.");

                Photo photoObject = new Photo(title, author, author_id, link, tags, photoUrl); // Create a new Photo everytime for loop loops

                this.mPhotos.add(photoObject); // Add the photoObject as a Photo into the mPhotos list of Photo objects
            }

            // For every individual photo inside the Photo list
            for (Photo singlePhoto : mPhotos) {
                // Print it in the console in a readable format for verification purposes
                Log.v(LOG_TAG, singlePhoto.toString());
            }

            // If there's a problem processing the JSON print it here
            } catch (JSONException jsone) {
                jsone.printStackTrace();
                Log.e(LOG_TAG, "ERROR PROCESSING JSON");
            }
    }

    // Make some changes to the DownloadRawData class so that the result is processing into JSON
    public class DownloadJsonData extends DownloadRawData {
        @Override
        protected void onPostExecute(String webData) {
            super.onPostExecute(webData);
            processResult();
        }

        // Make the parameter that is passed the new URI that is created
        @Override
        protected String doInBackground(String... params) {
            String[] par = { mDestinationUri.toString() };

            // Make sure all the things that have already been written in GetRawData happens but pass the new URI
            return super.doInBackground(par);
        }
    }

}
