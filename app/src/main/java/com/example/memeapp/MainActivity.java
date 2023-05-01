package com.example.memeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;

import org.json.JSONException;

@GlideModule
public class MainActivity extends AppCompatActivity {

    ImageView meme_image;
    Button share,next;
    String currentImageUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        meme_image = findViewById(R.id.meme_image);
        share = findViewById(R.id.btn_share);
        next = findViewById(R.id.btn_next);

        load_meme();

    }

    private void load_meme() {

// Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://meme-api.com/gimme";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    try {
                        //select what you want from the response after hitting the api, here only meme url was needed
                        currentImageUrl = response.getString("url");
                        Glide.with(MainActivity.this).load(currentImageUrl)
                                .fitCenter()
                                .into(meme_image);
                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, "Error! check your internet connection", Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(e);
                    }
                }, error -> {
                    // Handle error
                });

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
    public void share_meme(View view) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, "Hey! checkout this cool meme I got from Meme App " + currentImageUrl);
        Intent chooser = Intent.createChooser(i, "Share this meme using...");
        startActivity(chooser);
    }

    public void next_meme(View view) {
        load_meme();
    }
}