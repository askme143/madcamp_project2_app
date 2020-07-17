package com.example.madcampserverapp.server;

import android.content.ContentValues;

import com.example.madcampserverapp.ThreadTask;

public class NetworkTask extends ThreadTask<Void, String> {

    private String mUrl;
    private ContentValues mValues;

    public NetworkTask(String url, ContentValues values) {
        mUrl = url;
        mValues = values;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(Void arg) {
        String result;
        RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();

        result = requestHttpURLConnection.request(mUrl, mValues);
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println(result);
    }
}