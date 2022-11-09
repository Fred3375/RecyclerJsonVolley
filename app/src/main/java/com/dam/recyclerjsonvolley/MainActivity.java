package com.dam.recyclerjsonvolley;

import static com.dam.recyclerjsonvolley.Nodes.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterItem.OnItemClickListener{

    private RecyclerView recyclerView;
    private EditText etSearch;
    private Button btnSearch;

    private ArrayList<ModelItem> itemArrayList;
    String search;
    private AdapterItem adapterItem;

    private RequestQueue requestQueue;

    private void initUI(){
       recyclerView = findViewById(R.id.recyclerView);
       recyclerView.setHasFixedSize(true);
       recyclerView.setLayoutManager(new LinearLayoutManager(this));
       etSearch = findViewById(R.id.etSearch);
       btnSearch = findViewById(R.id.btnSearch);
       itemArrayList = new ArrayList<>();
       requestQueue = Volley.newRequestQueue(this);
    }

    public void newSearch(View view){
        itemArrayList.clear();
        search = etSearch.getText().toString().trim().replace(" ", "+");
        parseJSON(search);
    }

   private void parseJSON(String search){

       // 31201159-6bce891c462e0e55bbdb3f90f
       // https://pixabay.com/api/?key=31201159-6bce891c462e0e55bbdb3f90f&q=yellow+flowers&image_type=photo&pretty=true
       // https://pixabay.com/api/
       // ?key=31201159-6bce891c462e0e55bbdb3f90f&q=yellow+flowers
       // &image_type=photo
       // &pretty=true

        String pixabayKey = "31201159-6bce891c462e0e55bbdb3f90f";
        String pixabayUrl = "https://pixabay.com/api/";
        String pixabayOptions1 = "&image_type=photo&pretty=true";
        String pixabayOptions2 = "&orientation=horizontal&per_page=100&order=latest";
        String urlJSONFile = pixabayUrl + "?key=" + pixabayKey + "&q=" + search + pixabayOptions1 + pixabayOptions2;

        Log.i("DEBUG", "parseJSON: " + urlJSONFile);
        JsonObjectRequest request = new JsonObjectRequest(
            Request.Method.GET,
            urlJSONFile,
            null, new
            Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        JSONArray jsonArray = response.getJSONArray("hits");
                        for(int i=0; i<jsonArray.length();i++){ // TODO foreach
                            JSONObject hit = jsonArray.getJSONObject(i);
                            String creaator = hit.getString("user");
                            int likes = hit.getInt("likes");
                            String imgUrl = hit.getString("webformatURL");
                            itemArrayList.add(new ModelItem(imgUrl, creaator, likes));
                        }
                        adapterItem = new AdapterItem(getApplicationContext(), itemArrayList);
                        recyclerView.setAdapter(adapterItem);
                        adapterItem.setOnItemClickListener(MainActivity.this::onItemClick);
/*
                        adapterItem.setOnItemClickListener(new AdapterItem.OnItemClickListener() {
                            @Override
                            public void onItemClick(int pos, View v) {
                                // playSong(pos);
                                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                                intent.putExtra("imageUrl", itemArrayList.get(pos).getImageUrl());
                                intent.putExtra("creator", itemArrayList.get(pos).getCreator());
                                intent.putExtra("likes", itemArrayList.get(pos).getLikes());
                                startActivity(intent);
                            }
                        });
*/

                    } catch (JSONException e){ // en erreur si pas de JSON Array dans le try.... !
                        e.printStackTrace();
                    }
                }
            },
            // error -> {}
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }
        );
        requestQueue.add(request);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        parseJSON("");
    }

    @Override
    public void onItemClick(int pos) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(EXTRA_URL, itemArrayList.get(pos).getImageUrl());
        intent.putExtra(EXTRA_CREATOR, itemArrayList.get(pos).getCreator());
        intent.putExtra(EXTRA_LIKES, itemArrayList.get(pos).getLikes());
        startActivity(intent);
    }
}