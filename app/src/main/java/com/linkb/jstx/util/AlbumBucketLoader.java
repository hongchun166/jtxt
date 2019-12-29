
package com.linkb.jstx.util;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.Bucket;
import com.linkb.R;

import java.util.ArrayList;
import java.util.List;

public class AlbumBucketLoader extends CursorLoader {
    private static final String COLUMN_COUNT = "count";
    private static final String[] COLUMNS = {MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media._ID, COLUMN_COUNT};
    private static final String[] PROJECTION = {MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media._ID, "COUNT(*) AS " + COLUMN_COUNT};
    private static final String BUCKET_GROUP_BY = "1) GROUP BY 1,(2";
    private static final String BUCKET_ORDER_BY = "MAX(" + MediaStore.Images.Media.DATE_TAKEN + ") DESC";
    private static final String MEDIA_ID_DUMMY = String.valueOf(-1);

    public AlbumBucketLoader(Context context) {
        super(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PROJECTION, BUCKET_GROUP_BY, null,
                BUCKET_ORDER_BY);
    }

    @Override
    public Cursor loadInBackground() {
        Cursor albums = super.loadInBackground();
        MatrixCursor allAlbum = new MatrixCursor(COLUMNS);
        int totalCount = 0;
        while (albums.moveToNext()) {
            totalCount += albums.getInt(albums.getColumnIndex(COLUMN_COUNT));
        }
        String allAlbumId;
        if (albums.moveToFirst()) {
            allAlbumId = albums.getString(albums.getColumnIndex(MediaStore.Images.Media._ID));
        } else {
            allAlbumId = MEDIA_ID_DUMMY;
        }
        allAlbum.addRow(new String[]{null, LvxinApplication.getInstance().getString(R.string.label_ablbum_all), allAlbumId, String.valueOf(totalCount)});

        return new MergeCursor(new Cursor[]{allAlbum, albums});
    }

    public List<Bucket> syncLoadList() {
        Cursor cursor = loadInBackground();
        List<Bucket> list = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            Bucket bucket = new Bucket();
            bucket.id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
            bucket.name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
            bucket.size = cursor.getLong(cursor.getColumnIndex(COLUMN_COUNT));
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            bucket.cover = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            list.add(bucket);
        }
        AppTools.closeQuietly(cursor);
        return list;
    }

    @Override
    public void onContentChanged() {
    }
}