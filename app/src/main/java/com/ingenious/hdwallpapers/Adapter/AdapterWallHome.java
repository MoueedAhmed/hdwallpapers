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
import com.like.OnLikeListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.ingenious.hdwallpapers.DBHelper.DBHelper;
import com.ingenious.Listener.RecyclerViewClickListener;
import com.ingenious.Methods.Methods;
import com.ingenious.SharedPref.Setting;
import com.ingenious.items.ItemWallpaper;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */public class AdapterWallHome extends RecyclerView.Adapter<AdapterWallHome.MyViewHolder> {

    private Context context;
    private ArrayList<ItemWallpaper> arrayList;
    private Methods methods;
    private DBHelper dbHelper;
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

    public AdapterWallHome(Context context, ArrayList<ItemWallpaper> arrayList, RecyclerViewClickListener recyclerViewClickListener) {
        this.context = context;
        this.arrayList = arrayList;
        dbHelper = new DBHelper(context);
        methods = new Methods(context);
        this.recyclerViewClickListener = recyclerViewClickListener;
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
        final ItemWallpaper item = arrayList.get(position);

        holder.likeButton.setLiked(dbHelper.isFav(item.getId()));
        holder.textView_cat.setText(item.getCName());

        if (Setting.in_app){
            if (Setting.getPurchases){
            }else {
                String pay_name = item.getPay();
                if (pay_name.equals("premium")){
                    holder.pay.setVisibility(View.VISIBLE);
                }else {
                    holder.pay.setVisibility(View.GONE);
                }
            }
        }else {
            holder.pay.setVisibility(View.GONE);
        }

        holder.likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                dbHelper.addtoFavorite(arrayList.get(holder.getAdapterPosition()));
                methods.showSnackBar(holder.rootlayout, context.getString(R.string.added_to_fav));
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                dbHelper.removeFav(arrayList.get(holder.getAdapterPosition()).getId());
                methods.showSnackBar(holder.rootlayout, context.getString(R.string.removed_from_fav));
            }
        });

        String imageurl = methods.getImageThumbSize(item.getImageThumb());
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

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Setting.in_app){
                    if (Setting.getPurchases){
                        recyclerViewClickListener.onClick(holder.getAdapterPosition());
                    }else {
                        String pay_name = item.getPay();
                        if (pay_name.equals("premium")){
                            methods.ShowDialog_pay();
                        }else {
                            recyclerViewClickListener.onClick(holder.getAdapterPosition());
                        }
                    }
                }else {
                    recyclerViewClickListener.onClick(holder.getAdapterPosition());
                }
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