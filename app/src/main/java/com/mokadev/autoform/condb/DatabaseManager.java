package com.mokadev.autoform.condb;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager
{
    private static final DatabaseManager INSTANCE = new DatabaseManager();

    class JsonPost extends AsyncTask<String, Void, JSONObject>
    {
        private List<NameValuePair> postData;
        private JsonResponse response;

        public JsonPost(JsonResponse response)
        {
            this.response = response;
        }

        public JsonPost(JsonResponse response, List<NameValuePair> postData)
        {
            this(response);
            this.postData = postData;
        }

        @Override
        protected JSONObject doInBackground(String... params)
        {
            // first check that the params has something.
            if (params.length == 0)
            {
                return null;
            }

            String url = params[0];
            InputStream inputStream = null;

            if (postData == null)
            {
                postData = new ArrayList<>();
            }

            try
            {
                // create the actual client that will make the consult.
                HttpClient client = new DefaultHttpClient();

                // create the post data to send.
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(postData));

                // get the response and create an entity out of that.
                HttpResponse response = client.execute(httpPost);
                HttpEntity entity = response.getEntity();

                // get the actual content of the response from the entity.
                inputStream = entity.getContent();
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            catch (ClientProtocolException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            JSONObject jsonObject = null;
            try
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    stringBuilder.append(line);
                }

                try
                {
                    jsonObject = new JSONObject(stringBuilder.toString());
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                inputStream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject)
        {
            response.onResponse(jsonObject);
        }
    }

    public void requestData(String url, List<NameValuePair> postData, JsonResponse response)
    {
        JsonPost jsonPost = new JsonPost(response, postData);
        jsonPost.execute(url);
    }

    public void requestData(String url, JsonResponse response)
    {
        JsonPost jsonPost = new JsonPost(response);
        jsonPost.execute(url);
    }

    public static DatabaseManager getInstance()
    {
        return INSTANCE;
    }
}
