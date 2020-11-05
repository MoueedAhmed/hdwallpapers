package com.ingenious.hdwallpapers.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.util.ArrayList;


import com.ingenious.Methods.Methods;
import com.ingenious.items.ItemGIF;
import com.ingenious.items.ItemWallpaper;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */public class DBHelper extends SQLiteOpenHelper {

    private Methods methods;
    private static String DB_NAME = "wall.db";
    private SQLiteDatabase db;
    private final Context context;

    private static String TAG_ID = "id";

    public static final String TABLE_WALL_BY_FAV = "fav";
    public static final String TABLE_WALL_BY_RECENT = "recent";
    public static final String TABLE_GIF = "gif";

    private static final String TAG_PID = "pid";
    private static final String TAG_GID = "gid";
    private static final String TAG_IMAGE = "image";
    private static final String TAG_VIEWS = "views";
    private static final String TAG_PAY = "pay";
    private static final String TAG_TOTAL_RATE = "total_rate";
    private static final String TAG_AVG_RATE = "avg_rate";
    private static final String TAG_TOTAL_DWONLOAD = "total_download";;
    private static final String TAG_RES = "res";
    private static final String TAG_SIZE = "size";
    private static final String TAG_IMAGE_BIG = "img";
    private static final String TAG_IMAGE_SMALL = "img_thumb";
    private static final String TAG_CAT_ID = "cid";
    private static final String TAG_CAT_NAME = "cname";

    private String[] columns_wall = new String[]{TAG_ID, TAG_PID, TAG_CAT_ID, TAG_CAT_NAME, TAG_IMAGE_SMALL, TAG_IMAGE_BIG, TAG_VIEWS,
            TAG_TOTAL_RATE, TAG_AVG_RATE, TAG_PAY, TAG_TOTAL_DWONLOAD, TAG_RES, TAG_SIZE};

    private String[] columns_gif = new String[]{TAG_ID, TAG_GID, TAG_IMAGE, TAG_VIEWS, TAG_TOTAL_RATE, TAG_AVG_RATE, TAG_PAY, TAG_TOTAL_DWONLOAD,
             TAG_RES, TAG_SIZE};

    // Creating table query
    private static final String CREATE_TABLE_WALL_BY_FAV = "create table " + TABLE_WALL_BY_FAV + "(" +
            TAG_ID + " integer PRIMARY KEY AUTOINCREMENT," +
            TAG_PID + " TEXT UNIQUE," +
            TAG_CAT_ID + " TEXT," +
            TAG_CAT_NAME + " TEXT," +
            TAG_IMAGE_SMALL + " TEXT," +
            TAG_IMAGE_BIG + " TEXT," +
            TAG_VIEWS + " NUMERIC," +
            TAG_TOTAL_RATE + " TEXT," +
            TAG_AVG_RATE + " TEXT," +
            TAG_PAY + " TEXT," +
            TAG_TOTAL_DWONLOAD + " TEXT," +
            TAG_RES + " TEXT," +
            TAG_SIZE + " TEXT);";

    // Creating table query
    private static final String CREATE_TABLE_GIF = "create table " + TABLE_GIF + "(" +
            TAG_ID + " integer PRIMARY KEY AUTOINCREMENT," +
            TAG_GID + " TEXT UNIQUE," +
            TAG_IMAGE + " TEXT," +
            TAG_VIEWS + " NUMERIC," +
            TAG_TOTAL_RATE + " TEXT," +
            TAG_AVG_RATE + " TEXT," +
            TAG_PAY+ " TEXT," +
            TAG_TOTAL_DWONLOAD + " TEXT," +
            TAG_RES + " TEXT," +
            TAG_SIZE + " TEXT);";

    // Creating table query
    private static final String CREATE_TABLE_WALL_BY_RECENT = "create table " + TABLE_WALL_BY_RECENT + "(" +
            TAG_ID + " integer PRIMARY KEY AUTOINCREMENT," +
            TAG_PID + " TEXT UNIQUE," +
            TAG_CAT_ID + " TEXT," +
            TAG_CAT_NAME + " TEXT," +
            TAG_IMAGE_SMALL + " TEXT," +
            TAG_IMAGE_BIG + " TEXT," +
            TAG_VIEWS + " NUMERIC," +
            TAG_TOTAL_RATE + " TEXT," +
            TAG_AVG_RATE + " TEXT," +
            TAG_PAY+ " TEXT," +
            TAG_TOTAL_DWONLOAD + " TEXT," +
            TAG_RES + " TEXT," +
            TAG_SIZE + " TEXT);";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 5);
        this.context = context;
        methods = new Methods(context);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_WALL_BY_FAV);
            db.execSQL(CREATE_TABLE_GIF);
            db.execSQL(CREATE_TABLE_WALL_BY_RECENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ItemWallpaper> getWallpapers(String table, String limit) {
        ArrayList<ItemWallpaper> arrayList = new ArrayList<>();

        String query = TAG_ID + " DESC";

        Cursor cursor = db.query(table, columns_wall, null, null, null, null, query, limit);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String pid = cursor.getString(cursor.getColumnIndex(TAG_PID));
                String cid = cursor.getString(cursor.getColumnIndex(TAG_CAT_ID));
                String cname = cursor.getString(cursor.getColumnIndex(TAG_CAT_NAME));
                String img = methods.decrypt(cursor.getString(cursor.getColumnIndex(TAG_IMAGE_BIG)));
                String img_thumb = methods.decrypt(cursor.getString(cursor.getColumnIndex(TAG_IMAGE_SMALL)));
                String views = String.valueOf(cursor.getInt(cursor.getColumnIndex(TAG_VIEWS)));
                String totalrate = cursor.getString(cursor.getColumnIndex(TAG_TOTAL_RATE));
                String averagerate = cursor.getString(cursor.getColumnIndex(TAG_AVG_RATE));
                String pay = cursor.getString(cursor.getColumnIndex(TAG_PAY));
                String download = cursor.getString(cursor.getColumnIndex(TAG_TOTAL_DWONLOAD));
                String res = cursor.getString(cursor.getColumnIndex(TAG_RES));
                String size = cursor.getString(cursor.getColumnIndex(TAG_SIZE));

                ItemWallpaper itemWallpaper = new ItemWallpaper(pid, cid, cname, img, img_thumb, views, totalrate, averagerate, pay, download);
                itemWallpaper.setResolution(res);
                itemWallpaper.setSize(size);
                arrayList.add(itemWallpaper);

                cursor.moveToNext();
            }
            cursor.close();
        }
        return arrayList;
    }

    private Boolean checkRecent(String id) {
        Cursor cursor = db.query(TABLE_WALL_BY_RECENT, columns_wall, TAG_PID + "='" + id + "'", null, null, null, null);
        Boolean isRecent = cursor != null && cursor.getCount() > 0;
        if (cursor != null) {
            cursor.close();
        }
        return isRecent;
    }

    public void addToRecent(ItemWallpaper itemWallpaper) {
        Cursor cursor_delete = db.query(TABLE_WALL_BY_RECENT, columns_wall, null, null, null, null, null);
        if (cursor_delete != null && cursor_delete.getCount() > 20) {
            cursor_delete.moveToFirst();
            db.delete(TABLE_WALL_BY_RECENT, TAG_PID + "=" + cursor_delete.getString(cursor_delete.getColumnIndex(TAG_PID)), null);
        }
        cursor_delete.close();

        if (checkRecent(itemWallpaper.getId())) {
            db.delete(TABLE_WALL_BY_RECENT, TAG_PID + "='" + itemWallpaper.getId() + "'", null);
        }

        String imageBig = methods.encrypt(itemWallpaper.getImage().replace(" ", "%20"));
        String imageSmall = methods.encrypt(itemWallpaper.getImageThumb().replace(" ", "%20"));

        ContentValues contentValues = new ContentValues();
        contentValues.put(TAG_PID, itemWallpaper.getId());
        contentValues.put(TAG_CAT_ID, itemWallpaper.getCId());
        contentValues.put(TAG_CAT_NAME, itemWallpaper.getCName());
        contentValues.put(TAG_IMAGE_BIG, imageBig);
        contentValues.put(TAG_IMAGE_SMALL, imageSmall);
        contentValues.put(TAG_VIEWS, Integer.parseInt(itemWallpaper.getTotalViews()));
        contentValues.put(TAG_TOTAL_RATE, itemWallpaper.getTotalRate());
        contentValues.put(TAG_AVG_RATE, itemWallpaper.getAverageRate());
        contentValues.put(TAG_PAY, itemWallpaper.getPay());
        contentValues.put(TAG_TOTAL_DWONLOAD, itemWallpaper.getTotalDownloads());
        contentValues.put(TAG_RES, itemWallpaper.getResolution());
        contentValues.put(TAG_SIZE, itemWallpaper.getSize());

        db.insert(TABLE_WALL_BY_RECENT, null, contentValues);
    }

    public void updateView(String id, String totview, String download, String reso, String size) {

        ContentValues values = new ContentValues();
        values.put(TAG_VIEWS, totview);
        values.put(TAG_TOTAL_DWONLOAD, download);
        values.put(TAG_RES, reso);
        values.put(TAG_SIZE, size);

        String where = TAG_PID + "=?";
        String[] args = {id};

        db.update(TABLE_WALL_BY_FAV, values, where, args);
        db.update(TABLE_WALL_BY_RECENT, values, where, args);
    }

    public void updateViewGIF(String id, String totview, String download, String reso, String size) {
        int views = Integer.parseInt(totview) + 1;

        ContentValues values = new ContentValues();
        values.put(TAG_VIEWS, views);
        values.put(TAG_TOTAL_DWONLOAD, download);
        values.put(TAG_RES, reso);
        values.put(TAG_SIZE, size);

        String where = TAG_GID + "=?";
        String[] args = {id};

        db.update(TABLE_GIF, values, where, args);
    }

    public Boolean isFav(String id) {
        String where = TAG_PID + "=?";
        String[] args = {id};

        Cursor cursor = db.query(TABLE_WALL_BY_FAV, columns_wall, where, args, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            try {
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public Boolean isFavGIF(String id) {
        String where = TAG_GID + "=?";
        String[] args = {id};

        Cursor cursor = db.query(TABLE_GIF, columns_gif, where, args, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            try {
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public ArrayList<ItemGIF> getGIF(String table) {
        ArrayList<ItemGIF> arrayList = new ArrayList<>();

        Cursor cursor = db.query(table, columns_gif, TAG_ID, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {

                String gid = cursor.getString(cursor.getColumnIndex(TAG_GID));
                String img = methods.decrypt(cursor.getString(cursor.getColumnIndex(TAG_IMAGE)));
                String views = String.valueOf(cursor.getInt(cursor.getColumnIndex(TAG_VIEWS)));
                String totalrate = cursor.getString(cursor.getColumnIndex(TAG_TOTAL_RATE));
                String averagerate = cursor.getString(cursor.getColumnIndex(TAG_AVG_RATE));
                String pay = cursor.getString(cursor.getColumnIndex(TAG_PAY));
                String download = cursor.getString(cursor.getColumnIndex(TAG_TOTAL_DWONLOAD));
                String res = cursor.getString(cursor.getColumnIndex(TAG_RES));
                String size = cursor.getString(cursor.getColumnIndex(TAG_SIZE));

                ItemGIF itemGIF = new ItemGIF(gid,img,views,totalrate,averagerate,pay,download);
                itemGIF.setResolution(res);
                itemGIF.setSize(size);
                arrayList.add(itemGIF);

                cursor.moveToNext();
            }
            cursor.close();
        }
        return arrayList;
    }

    public void addtoFavorite(ItemWallpaper itemWallpaper) {
        String imageBig = methods.encrypt(itemWallpaper.getImage().replace(" ", "%20"));
        String imageSmall = methods.encrypt(itemWallpaper.getImageThumb().replace(" ", "%20"));

        ContentValues contentValues = new ContentValues();
        contentValues.put(TAG_PID, itemWallpaper.getId());
        contentValues.put(TAG_CAT_ID, itemWallpaper.getCId());
        contentValues.put(TAG_CAT_NAME, itemWallpaper.getCName());
        contentValues.put(TAG_IMAGE_BIG, imageBig);
        contentValues.put(TAG_IMAGE_SMALL, imageSmall);
        contentValues.put(TAG_VIEWS, Integer.parseInt(itemWallpaper.getTotalViews()));
        contentValues.put(TAG_TOTAL_RATE, itemWallpaper.getTotalRate());
        contentValues.put(TAG_AVG_RATE, itemWallpaper.getAverageRate());
        contentValues.put(TAG_PAY, itemWallpaper.getPay());
        contentValues.put(TAG_TOTAL_DWONLOAD, itemWallpaper.getTotalDownloads());
        contentValues.put(TAG_RES, itemWallpaper.getResolution());
        contentValues.put(TAG_SIZE, itemWallpaper.getSize());

        db.insert(TABLE_WALL_BY_FAV, null, contentValues);
    }

    public void removeFav(String id) {
        db.delete(TABLE_WALL_BY_FAV, TAG_PID + "=" + id, null);
    }

    public void addtoFavoriteGIF(ItemGIF itemGIF) {
        String image = methods.encrypt(itemGIF.getImage().replace(" ", "%20"));

        ContentValues contentValues = new ContentValues();
        contentValues.put(TAG_GID, itemGIF.getId());
        contentValues.put(TAG_IMAGE, image);
        contentValues.put(TAG_VIEWS, itemGIF.getTotalViews());
        contentValues.put(TAG_TOTAL_RATE, itemGIF.getTotalRate());
        contentValues.put(TAG_AVG_RATE, itemGIF.getAveargeRate());
        contentValues.put(TAG_PAY, itemGIF.getPay());
        contentValues.put(TAG_TOTAL_DWONLOAD, itemGIF.getTotalDownload());
        contentValues.put(TAG_RES, itemGIF.getResolution());
        contentValues.put(TAG_SIZE, itemGIF.getSize());

        db.insert(TABLE_GIF, null, contentValues);
    }

    public void removeFavGIF(String id) {
        try {
            db.delete(TABLE_GIF, TAG_GID + "=" + id, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("aaa", "upgrade");
        Log.e("aaa -oldVersion", "" + oldVersion);
        Log.e("aaa -newVersion", "" + newVersion);
    }
}