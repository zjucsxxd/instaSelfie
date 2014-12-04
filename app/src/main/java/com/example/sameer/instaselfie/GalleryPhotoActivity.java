package com.example.sameer.instaselfie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
import java.util.ArrayList;
import java.util.List;


public class GalleryPhotoActivity extends Activity implements AdapterView.OnItemClickListener, ParseInstagramJSON.Caller {

    GridView galleryImages;
    private String requestURL;
    public List<Post> posts;
    private boolean isloading;
    private ParseInstagramJSON parseInstagramJSON;
    private GalleryPhotoAdapter galleryPhotoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        posts = new ArrayList<Post>();
        requestURL = getResources().getString(R.string.requestUrl) + getResources().getString(R.string.access_token);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_gallery_photo);

        galleryImages = (GridView) findViewById(R.id.gridView);
        galleryPhotoAdapter = new GalleryPhotoAdapter(this);
        galleryImages.setAdapter(galleryPhotoAdapter);

        parseInstagramJSON = new ParseInstagramJSON(this, posts, requestURL);
        parseInstagramJSON.execute();

        galleryImages.setOnItemClickListener(this);
        galleryImages.setOnScrollListener(new GridView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int loadedItems = firstVisibleItem + visibleItemCount;

                if ((loadedItems == totalItemCount || loadedItems == totalItemCount - 9) && !isloading) {
                    if (parseInstagramJSON != null && (parseInstagramJSON.getStatus() == AsyncTask.Status.FINISHED)) {
                        parseInstagramJSON = new ParseInstagramJSON(GalleryPhotoActivity.this, posts, requestURL);
                        parseInstagramJSON.execute();
                    }
                }
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, SingePostActivity.class);
        String imageUrl = posts.get(i).getHighResolutionImageUrl();
        String username = posts.get(i).getUserName();
        intent.putExtra("Url", imageUrl);
        intent.putExtra("Username", username);
        startActivity(intent);
    }

    @Override
    public void onInstagramJsonParseComplete(List<Post> postCollection, String url) {
        this.posts = postCollection;
        this.requestURL = url;
        galleryPhotoAdapter.notifyDataSetChanged();
        isloading = false;
    }

    private class ViewHolder {
        ImageView imgview;

        ViewHolder(View v) {
            imgview = (ImageView) v.findViewById(R.id.imageView);
        }
    }


    class GalleryPhotoAdapter extends BaseAdapter {

        private Context context;
        Uri uri;

        public GalleryPhotoAdapter(Context _context) {
            this.context = _context;
        }

        @Override
        public int getCount() {
            return posts.size();
        }

        @Override
        public Object getItem(int i) {
            return posts.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {

            View row = convertView;
            ViewHolder holder = null;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.single_item_gallery, viewGroup, false);
                holder = new ViewHolder(row);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }
            uri = Uri.parse(posts.get(i).getThumbUrl());
            Picasso.with(context).load(uri).resize(220, 220).placeholder(R.drawable.ic_launcher).into(holder.imgview);
            return row;
        }
    }

}
