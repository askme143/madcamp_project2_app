package com.example.madcampserverapp.server;

import android.content.ContentValues;

import java.io.BufferedReader;
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

                subParams.append(key).append("=").append(value);

                if (!moreThanTwo && pParams.size() > 1)
                    moreThanTwo = true;
                if (moreThanTwo)
                    subParams.append("&");
            }
        }

        System.out.println(subParams.toString());

        /* Get data */
        try {
            URL url = new URL(pUrl);
            urlConnection = (HttpURLConnection) url.openConnection();

            /* urlConnection setting */
            urlConnection.setRequestMethod("GET");
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
            if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;

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
