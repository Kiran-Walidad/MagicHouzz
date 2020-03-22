package dev.crew.magichouse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class Model3DImageAdpter extends BaseAdapter {

    private Context contexts ;
    private List<ItemImage3DModel> itemImage3DModel;
    private LayoutInflater inflater;

    public Model3DImageAdpter(Context contexts, List<ItemImage3DModel> itemImage3DModel) {
        this.contexts = contexts;
        inflater = LayoutInflater.from(contexts);
        this.itemImage3DModel = itemImage3DModel;
    }

    @Override
    public int getCount() {
        return itemImage3DModel.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewinflater = inflater.inflate(R.layout.model3dimagegraidview,null);
        ImageView imageView = viewinflater.findViewById(R.id.img3DModel);
        TextView textView = viewinflater.findViewById(R.id.imgNameModel);
        ItemImage3DModel item = itemImage3DModel.get(i);
        Glide.with(contexts).load(item.getImage()).placeholder(R.drawable.room)
                .error(R.drawable.error_image).into(imageView);
        textView.setText(item.getImageTitle());
        return viewinflater;
    }

/*
    public Model3DImageAdpter( Context context, List<ItemImage3DModel> itemImage3DModels ) {
        super(context, R.layout.model3dimagegraidview, itemImage3DModels);
        contexts = context;
        itemImage3DModel = itemImage3DModels;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ItemImage3DModel itemImage3DModel = getItem(position);
        ItemImage3DModelViewHolder viewHolder;

        if (convertView == null) {
            convertView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
            convertView = vi.inflate(R.layout.model3dimagegraidview, parent, false);

            //
            viewHolder = new ItemImage3DModelViewHolder();
            viewHolder.img = (ImageView)convertView.findViewById(R.id.image);
            viewHolder.title = (TextView)convertView.findViewById(R.id.title);

            //
            convertView.setTag(holder);
        }else{
            viewHolder = (ItemImage3DModelViewHolder) convertView.getTag();
        }


        //
        viewHolder.populate(product, ((MyActivityGrid)mContext).isLvBusy());

        //
        return convertView;

    }


    private class ItemImage3DModelViewHolder {
        public ImageView img;
        public TextView title;

        void populate(ItemImage3DModel p) {
            title.setText(p.imageTitle);

            //
            ImageDownloader imageDownloader = new ImageDownloader();
            imageDownloader.download(p.img_url, img);
        }
    }*/
}
