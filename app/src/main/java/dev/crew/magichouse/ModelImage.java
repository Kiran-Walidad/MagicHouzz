package dev.crew.magichouse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;

import org.checkerframework.checker.units.qual.A;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ModelImage extends AppCompatActivity {

    private ArFragment arFragment;
    List<ItemImage3DModel> itemImage3DModels;
    Model3DAdpterImageRecyler model3DAdpterImageRecyler;
    RecyclerView recyclerView;

    String MODELURL = "http:\\/\\/192.168.10.14\\/MagicHouse\\/furniture\\/bed\\/bed 4\\/ModifiedBed02.gltf";

    View arrayView[];
    ViewRenderable nameAnimal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_image);
        itemImage3DModels = new ArrayList<>();

        /*recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ModelImage.this));

        recyclerView = findViewById(R.id.modelRecylerView);*/
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        arFragment.setOnTapArPlaneListener(((hitResult, plane, motionEvent) -> {
            placeModel(hitResult.createAnchor());
        }));

    /*    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://192.168.10.14/MagicHouse/selectImage.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("no found")){
                    Toast.makeText(ModelImage.this, "model not found", Toast.LENGTH_SHORT).show();
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
                model3DAdpterImageRecyler = new Model3DAdpterImageRecyler(ModelImage.this,itemImage3DModels);
                recyclerView.setAdapter(model3DAdpterImageRecyler);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ModelImage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

*/
    }

    private void placeModel(Anchor anchor) {
        ModelRenderable
                .builder()
                .setSource(
                        this, RenderableSource
                        .builder()
                        .setSource(this, Uri.parse(MODELURL),RenderableSource.SourceType.GLTF2)
                        .setScale(0.98f)
                        .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                        .build()
                ).setRegistryId(MODELURL)
                .build()
                .thenAccept(modelRenderable -> addNodetoScenc(modelRenderable,anchor))
                .exceptionally(throwable -> {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setMessage(throwable.getMessage()).show();
                    return null;
                });
    }

    private void addNodetoScenc(ModelRenderable modelRenderable, Anchor anchor) {
        AnchorNode anchorNode  = new AnchorNode(anchor);
        anchorNode.setRenderable(modelRenderable);
        arFragment.getArSceneView().getScene().addChild(anchorNode);
    }


    public void obtonMenActonBtn(View view) {
        String options [] = {"BAD" ,  "Table" };

        AlertDialog.Builder builder = new AlertDialog.Builder(ModelImage.this);
        builder.setTitle("Choose Action");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dataRetrive(options[i]);
            }
        });
        builder.create().show();
    }

    private void dataRetrive(String option) {

        itemImage3DModels.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://192.168.10.14/MagicHouse/selectImage.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("no found")){
                    Toast.makeText(ModelImage.this, "model not found", Toast.LENGTH_SHORT).show();
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
                model3DAdpterImageRecyler = new Model3DAdpterImageRecyler(ModelImage.this,itemImage3DModels);
                recyclerView.setAdapter(model3DAdpterImageRecyler);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ModelImage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



    }
}
