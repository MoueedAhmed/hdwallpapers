package com.ingenious.hdwallpapers.Adapter;

import android.app.Activity;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdsManager;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.AdChoicesView;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.ingenious.hdwallpapers.R;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
 */

public class AdapterWallpaper extends RecyclerView.Adapter {

    private ArrayList<ItemWallpaper> arrayList;
    private Context context;
    private RecyclerViewClickListener recyclerViewClickListener;
    private DBHelper dbHelper;
    private Methods methods;
    private String type;
    private int columnWidth = 0, columnHeight = 0;
    private final int VIEW_PROG = -1;
    private Boolean isAdLoaded = false;
    private NativeAdsManager mNativeAdsManager;
    private AdLoader adLoader = null;
    private List<UnifiedNativeAd> mNativeAdsAdmob = new ArrayList<>();
    private ArrayList<NativeAd> mNativeAdsFB = new ArrayList<>();

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
        }
    }

    private static class ADViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl_native_ad;

        private ADViewHolder(View view) {
            super(view);
            rl_native_ad = view.findViewById(R.id.rl_native_ad);
        }
    }

    public AdapterWallpaper(Context context, ArrayList<ItemWallpaper> arrayList, RecyclerViewClickListener recyclerViewClickListener) {
        this.arrayList = arrayList;
        this.context = context;
        this.type = type;
        dbHelper = new DBHelper(context);
        methods = new Methods(context);
        setColumnWidthHeight(type);
        this.recyclerViewClickListener = recyclerViewClickListener;
        loadNativeAds();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_PROG) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_progressbar, parent, false);
            return new ProgressViewHolder(v);
        } else if (viewType >= 1000) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ads, parent, false);
            return new ADViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_wall, parent, false);
            return new MyViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
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
        } else if (holder instanceof ADViewHolder) {
            if (isAdLoaded) {
                if (((ADViewHolder) holder).rl_native_ad.getChildCount() == 0) {
                    if (Setting.isAdmobNativeAd) {
                        if (mNativeAdsAdmob.size() >= 5) {

                            int i = new Random().nextInt(mNativeAdsAdmob.size() - 1);

//                            CardView cardView = (CardView) ((Activity) context).getLayoutInflater().inflate(R.layout.layout_native_ad_admob, null);

                            UnifiedNativeAdView adView = (UnifiedNativeAdView) ((Activity) context).getLayoutInflater().inflate(R.layout.layout_native_ad_admob, null);
                            populateUnifiedNativeAdView(mNativeAdsAdmob.get(i), adView);
                            ((ADViewHolder) holder).rl_native_ad.removeAllViews();
                            ((ADViewHolder) holder).rl_native_ad.addView(adView);

                            ((ADViewHolder) holder).rl_native_ad.setVisibility(View.VISIBLE);
                        }
                    } else {

                        LinearLayout ll_fb_native = (LinearLayout) ((Activity) context).getLayoutInflater().inflate(R.layout.layout_native_ad_fb, null);

                        com.facebook.ads.MediaView mvAdMedia, ivAdIcon;
                        TextView tvAdTitle;
                        TextView tvAdBody;
                        TextView tvAdSocialContext;
                        TextView tvAdSponsoredLabel;
                        Button btnAdCallToAction;
                        LinearLayout adChoicesContainer, ll_main;

                        mvAdMedia = ll_fb_native.findViewById(R.id.native_ad_media);
                        tvAdTitle = ll_fb_native.findViewById(R.id.native_ad_title);
                        tvAdBody = ll_fb_native.findViewById(R.id.native_ad_body);
                        tvAdSocialContext = ll_fb_native.findViewById(R.id.native_ad_social_context);
                        tvAdSponsoredLabel = ll_fb_native.findViewById(R.id.native_ad_sponsored_label);
                        btnAdCallToAction = ll_fb_native.findViewById(R.id.native_ad_call_to_action);
                        ivAdIcon = ll_fb_native.findViewById(R.id.native_ad_icon);
                        adChoicesContainer = ll_fb_native.findViewById(R.id.ad_choices_container);
                        ll_main = ll_fb_native.findViewById(R.id.ad_unit);


                        NativeAd ad;

                        if (mNativeAdsFB.size() >= 5) {
                            ad = mNativeAdsFB.get(new Random().nextInt(5));
                        } else {
                            ad = mNativeAdsManager.nextNativeAd();
                            mNativeAdsFB.add(ad);
                        }

                        ADViewHolder adHolder = (ADViewHolder) holder;

                        if (ad != null) {

                            tvAdTitle.setText(ad.getAdvertiserName());
                            tvAdBody.setText(ad.getAdBodyText());
                            tvAdSocialContext.setText(ad.getAdSocialContext());
                            tvAdSponsoredLabel.setText(ad.getSponsoredTranslation());
                            btnAdCallToAction.setText(ad.getAdCallToAction());
                            btnAdCallToAction.setVisibility(
                                    ad.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);

                            AdChoicesView adChoicesView = new AdChoicesView(context);
                            adChoicesContainer.addView(adChoicesView, 0);

                            ArrayList<View> clickableViews = new ArrayList<>();
                            clickableViews.add(ivAdIcon);
                            clickableViews.add(mvAdMedia);
                            clickableViews.add(btnAdCallToAction);
                            ad.registerViewForInteraction(
                                    adHolder.itemView,
                                    mvAdMedia,
                                    ivAdIcon,
                                    clickableViews);

                            ((ADViewHolder) holder).rl_native_ad.addView(ll_fb_native);
                        }
                    }
                }
            }
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
        if (isHeader(position)) {
            return VIEW_PROG;
        } else if (arrayList.get(position) == null) {
            return 1000 + position;
        } else {
            return position;
        }
    }

    public void setType(String type) {
        this.type = type;
        setColumnWidthHeight(type);
    }

    private void setColumnWidthHeight(String type) {
        columnWidth = methods.getColumnWidth(3, 3);
        columnHeight = (int) (columnWidth * 1.55);
    }

    public int getRealPos(int pos, ArrayList<ItemWallpaper> arrayListTemp) {
        return arrayListTemp.indexOf(arrayList.get(pos));
    }

    private void loadNativeAds() {
        if (Setting.isAdmobNativeAd) {
            AdLoader.Builder builder = new AdLoader.Builder(context, Setting.ad_native_id);
            adLoader = builder.forUnifiedNativeAd(
                    new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                        @Override
                        public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                            // A native ad loaded successfully, check if the ad loader has finished loading
                            // and if so, insert the ads into the list.
                            mNativeAdsAdmob.add(unifiedNativeAd);
                            isAdLoaded = true;
                        }
                    }).withAdListener(
                    new AdListener() {
                        @Override
                        public void onAdFailedToLoad(int errorCode) {

                        }
                    }).build();

            // Load the Native Express ad.
            adLoader.loadAds(new AdRequest.Builder().build(), 5);
        } else if (Setting.isFBNativeAd) {
            mNativeAdsManager = new NativeAdsManager(context, Setting.fb_ad_native_id, 5);
            mNativeAdsManager.setListener(new NativeAdsManager.Listener() {
                @Override
                public void onAdsLoaded() {
                    isAdLoaded = true;
                }

                @Override
                public void onAdError(AdError adError) {

                }
            });
            mNativeAdsManager.loadAds();
        }
    }

    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline is guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd);
    }

    public void destroyNativeAds() {
        try {
            for (int i = 0; i < mNativeAdsAdmob.size(); i++) {
                mNativeAdsAdmob.get(i).destroy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}