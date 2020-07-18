package com.example.madcampserverapp.server;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class RequestHttpURLConnection {
    public String request(String pUrl, ContentValues pParams) {
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
            URL url = new URL(pUrl);
            urlConnection = (HttpURLConnection) url.openConnection();

            /* urlConnection setting */
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
            urlConnection.setRequestProperty("Context_Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");

            /* Parameter passing */
            String strParams = subParams.toString();
            OutputStream outputStream = urlConnection.getOutputStream();
            outputStream.write(strParams.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();

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

    public String request(String pUrl, JSONObject jsonObject) {
        HttpURLConnection urlConnection = null;

        /* Get data */
        try {
            URL url = new URL(pUrl);
            urlConnection = (HttpURLConnection) url.openConnection();

            /* urlConnection setting */
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("Content-Type", "application/json");

            /* Convert JSON to String*/
            String jsonString = jsonObject.toString();

            System.out.println(jsonString.getBytes("UTF-8"));

            /* Parameter passing */
            OutputStream outputStream = urlConnection.getOutputStream();
            outputStream.write(jsonString.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();

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
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        return null;
    }

    public String upload(String pUrl, Bitmap bitmap, ContentValues contentValues) {
        /* Get facebook id and file name */
        String fb_id = contentValues.getAsString("fb_id");
        String filename = contentValues.getAsString("file_name");

        if (filename.length() == 0)
            filename = "userfile.jpg";

        /* String constants */
        String name = "userfile";
        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary =  "----WebKitFormBoundaryQGvWeNAiOE4g2VM5";

        HttpURLConnection urlConnection = null;

        /* Get data */
        try {
            URL url = new URL(pUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);

            /* urlConnection setting */
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Cache-Control", "no-cache");
            urlConnection.setRequestProperty(
                    "Content-Type", "multipart/form-data;boundary=" + boundary);

            /* Start content wrapper*/
            DataOutputStream request = new DataOutputStream(
                    urlConnection.getOutputStream());

            request.writeBytes(twoHyphens + boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"" +
                    name + "\";filename=\"" +
                    filename + "\"" + crlf +
                    "Content-Type: image/jpg" + crlf);
            request.writeBytes(crlf);

            /* Convert bitmap to byte array, and write */
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
            byte[] pixels = stream.toByteArray();

            request.write(pixels);

            /* End content wrapper */
            request.writeBytes(crlf);
            request.writeBytes(twoHyphens + boundary +
                    twoHyphens + crlf);
            request.flush();
            request.close();

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
            if (urlConnection != null)
                urlConnection.disconnect();
        }

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
}
