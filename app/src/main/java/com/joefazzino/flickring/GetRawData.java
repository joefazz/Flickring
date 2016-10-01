package com.joefazzino.flickring;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by joefazzino on 10/08/2016.
 */

// Provides status updates on the download state
enum DownloadStatus { IDLE, PROCESSING, NOT_INTITIALISED, FAILED_OR_EMPTY, OK }

public class GetRawData {
    private String LOG_TAG = GetRawData.class.getSimpleName();
    private String mRawURL;
    private String mData;
    private DownloadStatus mDownloadStatus;

    // The url is the argument passed to the GetRawData function
    public GetRawData(String mRawURL) {
        this.mRawURL = mRawURL;
        this.mDownloadStatus = DownloadStatus.IDLE;
    }

    // Can be called from a class which extends this class to remake the url
    public void setmRawURL(String mRawURL) {
        this.mRawURL = mRawURL;
    }

    // Completely reset the data and url
    public void Reset() {
        this.mDownloadStatus = DownloadStatus.IDLE;
        this.mRawURL = null;
        this.mData= null;
    }

    // Find out the current state of the download
    public DownloadStatus getmDownloadStatus() {
        return mDownloadStatus;
    }

    // Begins the processing of the data and the async task
    public void execute() {
        mDownloadStatus = DownloadStatus.PROCESSING;
        DownloadRawData downloadRawData = new DownloadRawData();
        downloadRawData.execute(mRawURL);
    }

    // Can be called to verify data output
    public String getmData() {
        return mData;
    }

    // Class which handles the asyncronous downloading
    public class DownloadRawData extends AsyncTask<String, Void, String> {

        // After the download is finished, save the output as mData and log if the output is correct
        // If there is a problem change the download status to reflect the nature of the problem
        @Override
        protected void onPostExecute(String webData) {
            mData = webData;
            Log.v(LOG_TAG, "Data Returned: " + mData);

            if (mData == null) {
                if (mRawURL == null) {
                    mDownloadStatus = DownloadStatus.NOT_INTITIALISED;
                } else {
                    mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
                }
            } else {
                mDownloadStatus = DownloadStatus.OK;
            }
        }

        // Where the data is downloaded from
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null; // Used to make an outside connection
            BufferedReader reader = null; // Will read the file

            if (params == null) // If no url has been passed then just stop trying
                return null;


            // If a url has been passed save it as a URL object, encased in try in case of exception while downloading
            try {

                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection(); // Connect to the URL
                urlConnection.setRequestMethod("GET"); // We want to request the data so GET is used
                urlConnection.connect();

                // If the connection is empty after connecting there is a problem with the URL provided and return null;
                if (urlConnection == null)
                    return null;

                // Create the inputstream so the file is ready to be read
                InputStream inputStream = urlConnection.getInputStream();

                // String buffer just stores long strings
                StringBuffer buffer = new StringBuffer();

                // Initialise the BufferedReader object with a new InputStream reader object which
                // will read the stream from the URL's input stream
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                // While we aren't at the end of the document keep reading every line and then append it to the StringBuffer
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                // Return the output
                return buffer.toString();

            } catch (IOException e) {

                Log.d(LOG_TAG, "ERROR CONNECTING", e);
                return null;

            } finally {
                // Disconnect from the URL
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

                try {
                    // Attempt to disconnect the reader
                    if (reader != null) {
                        reader.close();
                    }

                } catch (final IOException e) {
                    Log.d(LOG_TAG, "ERROR CLOSING READING STREAM", e);
                }
            }

        }
    }


}
