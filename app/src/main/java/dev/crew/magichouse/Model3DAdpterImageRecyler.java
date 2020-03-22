package dev.crew.magichouse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class Model3DAdpterImageRecyler extends RecyclerView.Adapter<Model3DAdpterImageRecyler.ViewHolder> {
    Context context ;
    List<ItemImage3DModel> item ;

    public Model3DAdpterImageRecyler(Context context, List<ItemImage3DModel> item) {
        this.context = context;
        this.item = item;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.model3dimagegraidview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemImage3DModel itemImage3DModel = item.get(position);
        Glide.with(context).load(itemImage3DModel.getImage()).placeholder(R.drawable.room)
                .error(R.drawable.error_image).into(holder.imageView);
        holder.textView.setText(itemImage3DModel.getImageTitle());
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView  = itemView.findViewById(R.id.img3DModel);
            textView = itemView.findViewById(R.id.imgNameModel);
        }
    }
}
