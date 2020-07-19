package com.example.madcampserverapp.server;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

public class RequestHttpURLConnection {
    private static final String TAG = "hello";

    private static final String crlf = "\r\n";
    private static final String twoHyphens = "--";
    private static final String boundary =  "----WebKitFormBoundaryQGvWeNAiOE4g2VM5";

    /* application/x-www-form-urlencoded */
    public String request(String pUrl, ContentValues pParams) {
        Log.e(TAG, "application/x-www-form-urlencoded");
        HttpURLConnection urlConnection = null;

        /* Make query sub parameters */
        StringBuilder subParams = new StringBuilder();
        if (pParams == null)
            subParams.append("");
        else {
            boolean moreThanTwo = false;
            String key;
            String value;

            for (Map.Entry<String, Object> parameter : pParams.valueSet()) {
                key = parameter.getKey();
                value = parameter.getValue().toString();

                if (moreThanTwo)
                    subParams.append("&");

                subParams.append(key).append("=").append(value);

                if (!moreThanTwo && pParams.size() > 1)
                    moreThanTwo = true;
            }
        }

        /* Get data */
        try {
            Log.e("hello", pUrl+ " " + subParams.toString());
            URL url = new URL(pUrl);
            urlConnection = (HttpURLConnection) url.openConnection();

            /* urlConnection setting */
            urlConnectionSetting(urlConnection, "application/x-www-form-urlencoded");

            /* Parameter passing */
            String strParams = subParams.toString();
            OutputStream outputStream = urlConnection.getOutputStream();
            outputStream.write(strParams.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();

            return getResponse(urlConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (urlConnection != null)
            urlConnection.disconnect();

        return null;
    }

    public String request(String pUrl, JSONObject jsonObject) {
        Log.e(TAG, "application/json");
        HttpURLConnection urlConnection = null;

        /* Get data */
        try {
            URL url = new URL(pUrl);
            urlConnection = (HttpURLConnection) url.openConnection();

            /* urlConnection setting */
            urlConnectionSetting(urlConnection, "application/json");

            /* Convert JSON to String*/
            String jsonString = jsonObject.toString();

            /* Parameter passing */
            OutputStream outputStream = urlConnection.getOutputStream();
            outputStream.write(jsonString.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();

            /* Get Response */
            return getResponse(urlConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (urlConnection != null)
            urlConnection.disconnect();

        return null;
    }

    public String uploadImage(String pUrl, Bitmap bitmap, ContentValues contentValues) {
        Log.e(TAG, "multipart");
        HttpURLConnection urlConnection = null;

        /* Get facebook id and file name */
        String fb_id = contentValues.getAsString("fb_id");
        String filename = contentValues.getAsString("file_name");
        if (filename.length() == 0)
            filename = "userfile.jpg";

        /* Get data */
        try {
            URL url = new URL(pUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);

            /* urlConnection setting */
            urlConnectionSetting(urlConnection, "multipart/form-data");

            DataOutputStream request = new DataOutputStream(
                    urlConnection.getOutputStream());

            /* Start writing image */
            request.writeBytes(twoHyphens + boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"" +
                    "image" + "\";filename=\"" +
                    filename + "\"" + crlf +
                    "Content-Type: image/jpg" + crlf);
            request.writeBytes(crlf);

            /* Write image */
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
            byte[] pixels = stream.toByteArray();
            request.write(pixels);
            request.writeBytes(crlf);

            /* Start writing facebook id */
            request.writeBytes(twoHyphens + boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"" +
                    "fb_id" + "\";filename=\"" +
                    fb_id + "\"" + crlf +
                    "Content-Type: text/plain" + crlf);
            request.writeBytes(crlf);

            /* Write empty file */
            request.writeBytes(crlf);

            /* End */
            request.writeBytes(twoHyphens + boundary +
                    twoHyphens + crlf);
            request.flush();
            request.close();

            return getResponse(urlConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (urlConnection != null)
            urlConnection.disconnect();

        return null;
    }

    public String get(String pUrl, ContentValues pParams) {
        HttpURLConnection urlConnection = null;
        StringBuilder subParams = new StringBuilder();

        /* Make query sub parameters */
        if (pParams == null)
            subParams.append("");
        else {
            boolean moreThanTwo = false;
            String key;
            String value;

            for (Map.Entry<String, Object> parameter : pParams.valueSet()) {
                key = parameter.getKey();
                value = parameter.getValue().toString();

                if (moreThanTwo)
                    subParams.append("&");

                subParams.append(key).append("=").append(value);

                if (!moreThanTwo && pParams.size() > 1)
                    moreThanTwo = true;
            }
        }

        /* Get data */
        try {
            URL url = new URL(pUrl + "?" + subParams.toString());
            urlConnection = (HttpURLConnection) url.openConnection();

            /* urlConnection setting */
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
            urlConnection.setRequestProperty("Context_Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");

            /* Check response code */
            if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            /* Read and make string value PAGE */
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

            String line;
            String page = "";
            while ((line = reader.readLine()) != null)
                page += line;

            return page;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void urlConnectionSetting(HttpURLConnection urlConnection, String type) throws ProtocolException {
        switch (type) {
            case "application/x-www-form-urlencoded":
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
                urlConnection.setRequestProperty("Context_Type",
                        "application/x-www-form-urlencoded;charset=UTF-8");
                break;
            case "application/json":
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                break;
            case "multipart/form-data":
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Cache-Control", "no-cache");
                urlConnection.setRequestProperty(
                        "Content-Type", "multipart/form-data;boundary=" + boundary);
                break;
        }
    }

    private String getResponse(HttpURLConnection urlConnection) throws IOException {
        /* Check response code */
        if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            return null;
        }

        /* Read and make string value PAGE */
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

        String line;
        String page = "";
        while ((line = reader.readLine()) != null)
            page += line;

        return page;
    }

    private Bitmap getBitmap(HttpURLConnection urlConnection) throws IOException {
        /* Check response code */
        if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            return null;
        }

        /* Read and make string value PAGE */
        InputStream inputStream = urlConnection.getInputStream();

        byte[] buffer = new byte[8000];
        int bytesRead = 0;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while ((bytesRead = inputStream.read(buffer)) != -1 ) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        System.out.println(imageByteArray);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);

        return bitmap;
    }
}
