package com.dam.recyclerjsonvolley;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class AdapterItem extends RecyclerView.Adapter <AdapterItem.ViewHolder> {
    Context context;
    ArrayList<ModelItem> itemArrayList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImage;
        private TextView tvCreator, tvLikes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvCreator = itemView.findViewById(R.id.tvCreator);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // getAdapterPosition from RecyclerView.ViewHolder
                    if (onItemClickListener != null){
                        int pos = getBindingAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            onItemClickListener.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }

    public AdapterItem(Context context, ArrayList<ModelItem> itemArrayList) {
        this.context = context;
        this.itemArrayList = itemArrayList;
    }

    // bonne pratique
    public AdapterItem() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler, parent, false);
        return new ViewHolder(view);
//        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recycler, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelItem currentItem = itemArrayList.get(position);

        String imageUrl = currentItem.getImageUrl();
        String creator = currentItem.getCreator();
        int likes = currentItem.getLikes();

        holder.tvCreator.setText(creator);
        holder.tvLikes.setText("likes : " + likes);

        // Glide
        RequestOptions errorManagement = new RequestOptions()
                .centerCrop() // center / crop pour les images de remplacement
                .error(R.drawable.ic_baseline_warning_amber_24) // cas erreur
                .placeholder(R.drawable.ic_baseline_wallpaper_24); // cas pas d'image
        Context context = holder.ivImage.getContext();
        Glide.with(context)
                .load(imageUrl)
                //  .apply(errorManagement)
                .fitCenter()
//                .override(150, 150)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivImage);

/*
        holder.tvCreator.setText(itemArrayList.get(position).getCreator());
        holder.tvLikes.setText(itemArrayList.get(position).getLikes());
        String imgUrl = itemArrayList.get(position).getImageUrl();
        // Glide
        RequestOptions options = new RequestOptions()
                .centerCrop() // center / crop pour les images de remplacement
                .error(R.drawable.ic_baseline_warning_amber_24) // cas erreur
                .placeholder(R.drawable.ic_baseline_warning_amber_24); // cas pas d'image
        Context context = holder.ivImage.getContext();
        Glide.with(context)
                .load(imgUrl)
              //  .apply(options)
                .fitCenter()
                .override(150, 150)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
               .into(holder.ivImage);
*/
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    // an interface object
    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

}
