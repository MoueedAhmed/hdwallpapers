package com.ingenious.hdwallpapers.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ingenious.hdwallpapers.R;
import com.like.LikeButton;
import com.like.OnLikeListener;
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
 */public class AdapterWallFav extends RecyclerView.Adapter {
    private DBHelper dbHelper;
    private ArrayList<ItemWallpaper> arrayList;
    private Context context;
    private Methods methods;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private RecyclerViewClickListener recyclerViewClickListener;
    private int columnWidth = 0, columnHeight = 0;

    private class MyViewHolder extends RecyclerView.ViewHolder {


        private RelativeLayout rootlayout, pay;
        private LikeButton likeButton;
        private TextView textView_cat;
        private View vieww;
        private SimpleDraweeView my_image_view;

        private MyViewHolder(View view) {
            super(view);
            rootlayout = view.findViewById(R.id.rootlayout);
            my_image_view = view.findViewById(R.id.my_image_view);
            likeButton = view.findViewById(R.id.button_wall_fav);
            textView_cat = view.findViewById(R.id.tv_wall_cat);
            vieww = view.findViewById(R.id.view_wall);
            pay = view.findViewById(R.id.pay);
        }
    }

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {
        private static ProgressBar progressBar;

        private ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
        }
    }

    public AdapterWallFav(Context context, ArrayList<ItemWallpaper> arrayList, RecyclerViewClickListener recyclerViewClickListener) {
        this.arrayList = arrayList;
        this.context = context;
        dbHelper = new DBHelper(context);
        methods = new Methods(context);
        this.recyclerViewClickListener = recyclerViewClickListener;
        columnWidth = methods.getColumnWidth(3, 3);
        columnHeight = (int) (columnWidth * 1.55);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_wall, parent, false);
            return new MyViewHolder(itemView);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_progressbar, parent, false);
            return new ProgressViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {

            final ItemWallpaper item = arrayList.get(position);

            ((MyViewHolder) holder).likeButton.setLiked(dbHelper.isFav(item.getId()));
            ((MyViewHolder) holder).textView_cat.setText(item.getCName());

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(columnWidth, (int) (columnHeight * 0.4));
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            ((MyViewHolder) holder).vieww.setLayoutParams(params);

            ((MyViewHolder) holder).my_image_view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ((MyViewHolder) holder).my_image_view.setLayoutParams(new RelativeLayout.LayoutParams(columnWidth, columnHeight));

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
                            .into(((MyViewHolder) holder).my_image_view);
                    break;
                case 2:
                    Picasso.get()
                            .load(imageurl)
                            .placeholder(R.color.news2)
                            .into(((MyViewHolder) holder).my_image_view);
                    break;
                case 3:
                    Picasso.get()
                            .load(imageurl)
                            .placeholder(R.color.news3)
                            .into(((MyViewHolder) holder).my_image_view);
                    break;
                case 4:
                    Picasso.get()
                            .load(imageurl)
                            .placeholder(R.color.news4)
                            .into(((MyViewHolder) holder).my_image_view);
                    break;
                case 5:
                    Picasso.get()
                            .load(imageurl)
                            .placeholder(R.color.news5)
                            .into(((MyViewHolder) holder).my_image_view);
                    break;
            }

            ((MyViewHolder) holder).likeButton.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    try {
                        dbHelper.addtoFavorite(arrayList.get(holder.getAdapterPosition()));
                        methods.showSnackBar(((MyViewHolder) holder).rootlayout, context.getString(R.string.added_to_fav));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    try {
                        dbHelper.removeFav(arrayList.get(holder.getAdapterPosition()).getId());
                        methods.showSnackBar(((MyViewHolder) holder).rootlayout, context.getString(R.string.removed_from_fav));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            if (Setting.in_app){
                if (Setting.getPurchases){
                    ((MyViewHolder) holder).pay.setVisibility(View.GONE);
                }else {
                    String pay_name = item.getPay();
                    if (pay_name.equals("premium")){
                        ((MyViewHolder) holder).pay.setVisibility(View.VISIBLE);
                    }else {
                        ((MyViewHolder) holder).pay.setVisibility(View.GONE);
                    }
                }
            }else {
                ((MyViewHolder) holder).pay.setVisibility(View.GONE);
            }

            ((MyViewHolder) holder).my_image_view.setOnClickListener(new View.OnClickListener() {
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

        } else {
            if (getItemCount() == 1) {
                ProgressViewHolder.progressBar.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public int getItemCount() {
        return arrayList.size() + 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    public void hideHeader() {
        try {
            ProgressViewHolder.progressBar.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean isHeader(int position) {
        return position == arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? VIEW_PROG : VIEW_ITEM;
    }
}