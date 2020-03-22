package dev.crew.magichouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Model3DImage extends AppCompatActivity {

    ImageButton close;
    TextView table,bad;
    GridView gridView;

    List<ItemImage3DModel> itemImage3DModels;
    Model3DImageAdpter model3DImageAdpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model3_dimage);

        table = findViewById(R.id.modelCatgryTable);
        bad = findViewById(R.id.modelCatgryBad);
        close = findViewById(R.id.imageBtnClose);
        gridView = findViewById(R.id.gridImage);
        itemImage3DModels = new ArrayList<>();

      /*  final ItemImage3DModel itemImage3DModel = new ItemImage3DModel("ali","cofa",R.drawable.room);
        ItemImage3DModel itemImage3DModel2 = new ItemImage3DModel("ali2","cofa2",R.drawable.room);
        ItemImage3DModel itemImage3DModel22 = new ItemImage3DModel("ali2","cofa2",R.drawable.room);
        ItemImage3DModel itemImage3DModel23 = new ItemImage3DModel("ali2","cofa2",R.drawable.room);
        ItemImage3DModel itemImage3DModel3 = new ItemImage3DModel("ali3","cofa3",R.drawable.error_image);
        itemImage3DModels.add(itemImage3DModel);
        itemImage3DModels.add(itemImage3DModel2);
        itemImage3DModels.add(itemImage3DModel22);
        itemImage3DModels.add(itemImage3DModel23);
        itemImage3DModels.add(itemImage3DModel3);*/


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(Model3DImage.this, "image of "+i, Toast.LENGTH_SHORT).show();

                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        "http://192.168.10.14/MagicHouse/select3DImage.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("no found")){
                            Toast.makeText(Model3DImage.this, "model not found", Toast.LENGTH_SHORT).show();
                        }else {
                            Intent intent = new Intent(Model3DImage.this,ImageDesignActivity.class);
                            intent.putExtra("image",response);
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Model3DImage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(Model3DImage.this);
                requestQueue.add(stringRequest);

            }
        });


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://192.168.10.14/MagicHouse/selectImage.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("no found")){
                    Toast.makeText(Model3DImage.this, "model not found", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String imageTitle = jsonObject.getString("title");
                            String image = jsonObject.getString("image");
                            ItemImage3DModel itemImage = new ItemImage3DModel(id,imageTitle,image);
                            itemImage3DModels.add(itemImage);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                model3DImageAdpter = new Model3DImageAdpter(Model3DImage.this,itemImage3DModels);
                gridView.setAdapter(model3DImageAdpter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Model3DImage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


        


        bad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Model3DImage.this, "sdsdfs", Toast.LENGTH_SHORT).show();
            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Model3DImage.this, "close", Toast.LENGTH_SHORT).show();
            }
        });






    }
}
