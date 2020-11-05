package com.ingenious.hdwallpapers.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ingenious.hdwallpapers.R;
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
 */public class AdapterCategories extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<ItemCat> arrayList;
    private Methods methods;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private RecyclerViewClickListener recyclerViewClickListener;

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout relativeLayout;
        private RoundedImageView imageView;
        private TextView textView_cat, textView_item_no;

        private MyViewHolder(View view) {
            super(view);
            relativeLayout = view.findViewById(R.id.rl_cat);
            imageView = view.findViewById(R.id.iv_cat);
            textView_cat = view.findViewById(R.id.tv_cat_title);
            textView_item_no = view.findViewById(R.id.tv_cat_number);
        }
    }

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {
        private static ProgressBar progressBar;

        private ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar);
        }
    }

    public AdapterCategories(Context context, ArrayList<ItemCat> arrayList, RecyclerViewClickListener recyclerViewClickListener) {
        this.arrayList = arrayList;
        this.recyclerViewClickListener = recyclerViewClickListener;
        this.context = context;
        methods = new Methods(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_categories, parent, false);
            return new MyViewHolder(itemView);
        }else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_progressbar, parent, false);
            return new ProgressViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {

            ((MyViewHolder) holder).textView_cat.setTypeface(((MyViewHolder) holder).textView_cat.getTypeface(), Typeface.BOLD);
            ((MyViewHolder) holder).textView_cat.setText(arrayList.get(position).getName());
            ((MyViewHolder) holder).textView_item_no.setText("Items (" + arrayList.get(position).getTotalWallpaper() + ")");

            String imageurl = arrayList.get(position).getImageThumb();
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
                            .placeholder(R.color.md_blue_400)
                            .into(((MyViewHolder) holder).imageView);
                    break;
                case 2:
                    Picasso.get()
                            .load(imageurl)
                            .placeholder(R.color.md_green_400)
                            .into(((MyViewHolder) holder).imageView);
                    break;
                case 3:
                    Picasso.get()
                            .load(imageurl)
                            .placeholder(R.color.md_blue_grey_400)
                            .into(((MyViewHolder) holder).imageView);
                    break;
                case 4:
                    Picasso.get()
                            .load(imageurl)
                            .placeholder(R.color.md_deep_orange_400)
                            .into(((MyViewHolder) holder).imageView);
                    break;
                case 5:
                    Picasso.get()
                            .load(imageurl)
                            .placeholder(R.color.md_pink_400)
                            .into(((MyViewHolder) holder).imageView);
                    break;
            }

            ((MyViewHolder) holder).relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewClickListener.onClick(holder.getAdapterPosition());
                }
            });

        }else {
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