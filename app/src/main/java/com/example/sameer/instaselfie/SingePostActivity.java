package com.example.sameer.instaselfie;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class SingePostActivity extends Activity {

    private ImageView imgView;
    private TextView txtUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singe_post);
        imgView = (ImageView) findViewById(R.id.imageViewFull);
        txtUserName = (TextView)findViewById(R.id.txtUserName);
        Intent intent = getIntent();

        if (intent != null) {
            String url = intent.getStringExtra("Url");
            String username = intent.getStringExtra("Username");
            txtUserName.setText("@"+username);
            Uri uri = Uri.parse(url);
            Picasso.with(this).load(uri).resize(1000, 1000).placeholder(R.drawable.default_insta).resize(1000, 1000).centerCrop().into(imgView);

        }
    }
}
