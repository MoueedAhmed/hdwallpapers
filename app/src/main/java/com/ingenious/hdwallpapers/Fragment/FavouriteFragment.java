package com.ingenious.hdwallpapers.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.ingenious.hdwallpapers.Activity.WallpaperDetailsActivity;
import com.ingenious.hdwallpapers.Adapter.AdapterWallFav;
import com.ingenious.hdwallpapers.DBHelper.DBHelper;
import com.ingenious.Listener.InterAdListener;
import com.ingenious.Listener.RecyclerViewClickListener;
import com.ingenious.Methods.Methods;
import com.ingenious.SharedPref.Setting;
import com.ingenious.hdwallpapers.R;
import com.ingenious.items.ItemWallpaper;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */public class FavouriteFragment extends Fragment {

    public View rootView;
    private RecyclerView recyclerView;
    private AdapterWallFav adapter;
    private ArrayList<ItemWallpaper> arrayList;
    private Methods methods;
    private GridLayoutManager grid;
    private FrameLayout progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_gif_fav, container, false);

        DBHelper dbHelper = new DBHelper(getActivity());
        methods = new Methods(getActivity());
        arrayList = new ArrayList<>();

        methods = new Methods(getActivity(), new InterAdListener() {
            @Override
            public void onClick(int position, String type) {
                Intent intent_view = new Intent(getActivity(), WallpaperDetailsActivity.class);
                Setting.arrayList.clear();
                Setting.arrayList.addAll(arrayList);
                intent_view.putExtra("pos", position);
                intent_view.putExtra("page", 0);
                startActivity(intent_view);
            }
        });

        arrayList.clear();
        arrayList.addAll(dbHelper.getWallpapers(DBHelper.TABLE_WALL_BY_FAV,null));

        progressBar = rootView.findViewById(R.id.load_video);

        recyclerView = rootView.findViewById(R.id.tv);
        recyclerView.setHasFixedSize(true);

        grid = new GridLayoutManager(getActivity(), 3);

        grid.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.isHeader(position) ? grid.getSpanCount() : 1;
            }
        });
        recyclerView.setLayoutManager(grid);

        adapter = new AdapterWallFav(getActivity(),  arrayList, new RecyclerViewClickListener() {
            @Override
            public void onClick(int position) {
                methods.showInter(position, "");
            }
        });
        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.INVISIBLE);

        setHasOptionsMenu(true);
        return rootView;
    }
}