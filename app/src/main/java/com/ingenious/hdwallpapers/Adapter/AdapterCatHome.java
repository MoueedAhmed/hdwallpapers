package com.ingenious.hdwallpapers.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ingenious.hdwallpapers.R;
import com.like.LikeButton;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.ingenious.Listener.RecyclerViewClickListener;
import com.ingenious.Methods.Methods;
import com.ingenious.items.ItemCat;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */public class AdapterCatHome extends RecyclerView.Adapter<AdapterCatHome.MyViewHolder> {

    private Context context;
    private ArrayList<ItemCat> arrayList;
    private Methods methods;
    private RecyclerViewClickListener recyclerViewClickListener;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rootlayout, pay;
        private RoundedImageView imageView;
        private LikeButton likeButton;
        private TextView textView_cat;

        MyViewHolder(View view) {
            super(view);
            rootlayout = view.findViewById(R.id.rootlayout);
            imageView = view.findViewById(R.id.iv_home_latest);
            likeButton = view.findViewById(R.id.button_home_fav);
            textView_cat = view.findViewById(R.id.tv_home_cat);
            pay = view.findViewById(R.id.pay);
        }
    }

    public AdapterCatHome(Context context, ArrayList<ItemCat> arrayList,  RecyclerViewClickListener recyclerViewClickListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.recyclerViewClickListener = recyclerViewClickListener;
        methods = new Methods(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_home, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final ItemCat item = arrayList.get(position);

        holder.textView_cat.setText(item.getName());
        holder.likeButton.setVisibility(View.GONE);
        holder.pay.setVisibility(View.GONE);

        String imageurl = item.getImageThumb();
        if(imageurl.equals("")) {
            imageurl = "null";
        }

        int step = 1;
        int final_step = 1;
        for (int i = 1; i < position + 1; i++) {
            if (i == position + 1) {
                final_step = step;
            }
            step++;
            if (step > 5) {
                step = 1;
            }
        }

        switch (step) {
            case 1:
                Picasso.get()
                        .load(imageurl)
                        .placeholder(R.color.news1)
                        .into(holder.imageView);
                break;
            case 2:
                Picasso.get()
                        .load(imageurl)
                        .placeholder(R.color.news2)
                        .into(holder.imageView);
                break;
            case 3:
                Picasso.get()
                        .load(imageurl)
                        .placeholder(R.color.news3)
                        .into(holder.imageView);
                break;
            case 4:
                Picasso.get()
                        .load(imageurl)
                        .placeholder(R.color.news4)
                        .into(holder.imageView);
                break;
            case 5:
                Picasso.get()
                        .load(imageurl)
                        .placeholder(R.color.news5)
                        .into(holder.imageView);
                break;
        }

        holder.rootlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewClickListener.onClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public String getID(int pos) {
        return arrayList.get(pos).getId();
    }
}