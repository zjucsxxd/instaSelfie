package com.example.sameer.instaselfie;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by sameer on 12/4/14.
 */
public class ParseInstagramJSON extends AsyncTask<Void, Void, Void> {

    private Post instaPost;
    private List<Post> posts;
    private String requestUrl;
    private Caller caller;

    public ParseInstagramJSON(Context activity, List<Post> posts, String requestUrl) {
        this.caller = (Caller) activity;
        this.posts = posts;
        this.requestUrl = requestUrl;
    }

    public interface Caller {
        public void onInstagramJsonParseComplete(List<Post> postCollection, String url);
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(requestUrl);
            HttpResponse response = client.execute(get);
            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                Log.d("INSTA", "Connected");
                HttpEntity entity = response.getEntity();
                String data = EntityUtils.toString(entity);
                JSONObject jObj = new JSONObject(data);

                //get next url to load
                JSONObject jsonPagination = jObj.getJSONObject("pagination");
                requestUrl = jsonPagination.getString("next_url");


                JSONArray jsonArray = jObj.getJSONArray("data");

                String thumbURL, lowResolutionImage, highResolutionImage, username;

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject post = jsonArray.getJSONObject(i);
                    JSONObject image = post.getJSONObject("images");

                    JSONObject photo = image.getJSONObject("standard_resolution");
                    highResolutionImage = photo.getString("url");

                    photo = image.getJSONObject("low_resolution");
                    lowResolutionImage = photo.getString("url");

                    photo = image.getJSONObject("thumbnail");
                    thumbURL = photo.getString("url");

                    JSONObject user = post.getJSONObject("user");
                    username = user.getString("username");

                    instaPost = new Post(username, thumbURL, lowResolutionImage, highResolutionImage);
                    posts.add(instaPost);
                }
            } else {
                Log.d("INSTA", "Error in Connection");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        caller.onInstagramJsonParseComplete(posts, requestUrl);
    }
}
