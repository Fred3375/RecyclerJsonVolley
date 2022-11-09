package com.dam.recyclerjsonvolley;

import static com.dam.recyclerjsonvolley.Nodes.*;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class DetailActivity extends AppCompatActivity {

    ImageView ivImage;
    TextView tvCreator,tvLikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ivImage = findViewById(R.id.ivImage);
        TextView tvCreator = findViewById(R.id.tvCreator);
        TextView tvLikes = findViewById(R.id.tvLikes);
        Bundle bundle = getIntent().getExtras();

        tvCreator.setText(bundle.getString(EXTRA_CREATOR)!= null ? bundle.getString("creator") : "inconnu" );

        tvLikes.setText("Likes : " + bundle.getInt(EXTRA_LIKES, 0));
        String imageUrl = bundle.getString(EXTRA_URL);

        RequestOptions errorManagement = new RequestOptions()
                .centerCrop() // center / crop pour les images de remplacement
                .error(R.drawable.ic_baseline_warning_amber_24) // cas erreur
                .placeholder(R.drawable.ic_baseline_wallpaper_24); // cas pas d'image
        Context context = ivImage.getContext();
        Glide.with(context)
                .load(imageUrl)
                .apply(errorManagement)
                .fitCenter()
//                .override(150, 150)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivImage);
    }
}