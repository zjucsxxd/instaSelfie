package com.example.sameer.instaselfie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

// ParseInstagramJSON AsyncTask class made generic and hence MainActivity's UI changes are fired using implemented methods after json is parsed
public class MainActivity extends Activity implements ParseInstagramJSON.Caller {

    public List<Post> posts;
    private ListView imageListView;
    private ParseInstagramJSON parseInstagramJSON;
    private boolean isloading;
    private AdaptorImageListView adaptorImageListView;
    private String requestURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Instagram Request URL
        requestURL = getResources().getString(R.string.requestUrl) + getResources().getString(R.string.access_token);

        setContentView(R.layout.activity_main);
        posts = new ArrayList<Post>();
        adaptorImageListView = new AdaptorImageListView(this);

        imageListView = (ListView) findViewById(R.id.imageListView);
        imageListView.setAdapter(adaptorImageListView);

        //Fetch and parse the instagram posts via async task
        parseInstagramJSON = new ParseInstagramJSON(this, posts, requestURL);
        parseInstagramJSON.execute();

        //for lazy loading task handle end of the scroll view
        imageListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                int loadedItems = firstVisibleItem + visibleItemCount;

                //fire async task whenever user is about the finish the scroll
                if ((loadedItems == totalItemCount - 4) && !isloading) {
                    if (parseInstagramJSON != null && (parseInstagramJSON.getStatus() == AsyncTask.Status.FINISHED)) {
                        isloading = true;
                        parseInstagramJSON = new ParseInstagramJSON(MainActivity.this, posts, requestURL);
                        parseInstagramJSON.execute();
                    }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.gallery) {
            Intent galleryView = new Intent(this, GalleryPhotoActivity.class);
            startActivity(galleryView);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInstagramJsonParseComplete(List<Post> postCollection, String url) {
        this.posts = postCollection;
        this.requestURL = url;
        adaptorImageListView.notifyDataSetChanged();
        isloading = false;
    }

    public class AdaptorImageListView extends BaseAdapter {
        Context context;
        Uri uri;

        public AdaptorImageListView(Context cntxt) {
            this.context = cntxt;
        }

        @Override
        public int getCount() {
            return posts.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView = (View) inflater.inflate(R.layout.row_with_text, viewGroup, false);
                holder = new ViewHolder();
                holder.imgview = (ImageView) convertView.findViewById(R.id.imgView);
                holder.txtview = (TextView) convertView.findViewById(R.id.username);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            uri = Uri.parse(posts.get(i).getLowResolutionImageUrl());
            Picasso.with(context).load(uri).resize(640, 640).placeholder(R.drawable.default_insta).resize(640, 640).centerCrop().into(holder.imgview);
            holder.txtview.setText("@" + posts.get(i).getUserName());
            return convertView;
        }
    }

    private class ViewHolder {
        ImageView imgview;
        TextView txtview;
    }

}
