package com.example.madcampserverapp.server;

import android.content.ContentValues;

import com.example.madcampserverapp.ThreadTask;

public class NetworkTask extends ThreadTask<Void, byte[]> {

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
    protected byte[] doInBackground(Void arg) {
        RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();

        return requestHttpURLConnection.request(mUrl, mValues);
    }

    @Override
    protected void onPostExecute(byte[] result) {
        System.out.println(result);
    }
}