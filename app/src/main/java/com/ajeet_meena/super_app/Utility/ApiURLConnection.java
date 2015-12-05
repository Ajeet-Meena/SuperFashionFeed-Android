package com.ajeet_meena.super_app.Utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ajeet Kumar Meena on 06-11-2015.
 */

public class ApiURLConnection {

    public static int GET_REQUEST = 0;
    public static int POST_REQUEST = 1;

    private static OnRequestCompleteListener onRequestCompleteListener;
    private static Boolean hasListener = false;

    public static void registerOnRequestCompleteListener(OnRequestCompleteListener onRequestCompleteListener) {
        ApiURLConnection.onRequestCompleteListener = onRequestCompleteListener;
        ApiURLConnection.hasListener = true;
    }

    public ApiURLConnection() {

    }

    public String httpPost(String URL, HashMap<String, String> param) {
        URL url;
        String response = "";

        HttpURLConnection urlConnection;
        BufferedWriter writer;
        OutputStream os;

        try {
            url = new URL(URL);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            os = urlConnection.getOutputStream();
            writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(param));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = urlConnection.getResponseCode();
            if( responseCode == HttpURLConnection.HTTP_OK ) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                while ( (line = br.readLine()) != null ) {
                    response += line;
                }
            } else {
                response = "";
            }
        } catch (Exception e) {

        }

        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for ( Map.Entry<String, String> entry : params.entrySet() ) {
            if( first )
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }


        return result.toString();
    }

    public String httpGet(String stringUrl, HashMap<String, String> params) {
        HttpURLConnection urlConnection;
        URL url;
        InputStream inStream;
        String contentAsString = null;

        try {
            if( params != null ) {
                stringUrl = stringUrl + "?" + getPostDataString(params);
            }
            url = new URL(stringUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);


            // Starts the query
            urlConnection.connect();
            int response = urlConnection.getResponseCode();

            inStream = urlConnection.getInputStream();

            // Convert the InputStream into a string
            contentAsString = readInstream(inStream);

            if( inStream != null ) {
                inStream.close();
            }


        } catch (MalformedURLException e) {


        } catch (IOException e) {


        } finally {

        }
        return contentAsString;
    }

    public String readInstream(InputStream stream) {
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));

            StringBuilder sb = new StringBuilder();
            String line;

            while ( (line = reader.readLine()) != null ) {
                sb.append(line + "\n");
            }
            reader.close();
            return sb.toString();


        } catch (UnsupportedEncodingException e) {


        } catch (IOException e) {

        }

        return "error";
    }

    public static class HttpRequester extends AsyncTask<String, Void, Boolean> {
        ApiURLConnection apiURLConnection;

        Boolean hasProgressDialog;
        MaterialDialog progressDialog = null;
        Context context;
        String URL;
        HashMap<String, String> hashMap;
        String result;
        Boolean networkAvailable;
        int requstCode;
        private Boolean hasPrivateListener = false;
        private OnRequestCompleteListener privateOnRequestCompleteListener;

        public void registerOnRequestCompleteListener(OnRequestCompleteListener onRequestCompleteListener) {
            hasPrivateListener = true;
            this.privateOnRequestCompleteListener = onRequestCompleteListener;
        }

        public HttpRequester(Context context, int requestCode, String URL, HashMap<String, String> hashMap, Boolean hasProgressDialog) {
            this.context = context;
            this.URL = URL;
            this.hashMap = hashMap;
            this.hasProgressDialog = hasProgressDialog;
            apiURLConnection = new ApiURLConnection();
            networkAvailable = ApiURLConnection.isNetworkAvailable(context);
            this.requstCode = requestCode;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if( hasProgressDialog ) {

                progressDialog = new MaterialDialog.Builder(context)
                        .content("please wait...")
                        .progress(true, 0)
                        .progressIndeterminateStyle(false).build();
                progressDialog.show();
            }
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            if( networkAvailable ) {

                if( requstCode == POST_REQUEST ) {
                    result = apiURLConnection.httpPost(URL, hashMap);

                } else if( requstCode == GET_REQUEST ) {
                    result = apiURLConnection.httpGet(URL, hashMap);
                }
                if( result == null ) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if( hasProgressDialog && progressDialog != null && progressDialog.isShowing() ) {
                progressDialog.cancel();
                hasProgressDialog = false;
            }
            if( hasPrivateListener ) {
                privateOnRequestCompleteListener.onComplete(aBoolean, requstCode, URL, result);
            }
            if( hasListener ) {
                onRequestCompleteListener.onComplete(aBoolean, requstCode, URL, result);
            }
            if( !networkAvailable ) {
                //CommonUtilities.message(context, CommonUtilities.NETWORKUNAVAILABLEERROR);
            }

        }
    }

    public interface OnRequestCompleteListener {
        void onComplete(Boolean success, int requestCode, String URL, String result);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
