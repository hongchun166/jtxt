
package com.linkb.jstx.util;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

import java.util.ArrayList;
import java.util.List;

public class AlbumPhotoLoader extends CursorLoader {
    private static final String[] PROJECTION = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
    private static final String ORDER_BY = MediaStore.Images.Media._ID + " DESC";

    private AlbumPhotoLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs,
                             String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }

    public static AlbumPhotoLoader newInstance(Context context, String bucket) {
        if (bucket == null) {
            return new AlbumPhotoLoader(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PROJECTION, MediaStore.Images.Media.SIZE + " >= " +30L  *50,
                    null,
                    ORDER_BY);
        } else {
            return new AlbumPhotoLoader(context,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    PROJECTION,
                    MediaStore.Images.Media.BUCKET_ID + " = ?",
                    new String[]{bucket},
                    ORDER_BY
            );
        }

    }

    @Override
    public Cursor loadInBackground() {
        Cursor result = super.loadInBackground();
        MatrixCursor dummy = new MatrixCursor(PROJECTION);
        return new MergeCursor(new Cursor[]{dummy, result});
    }

    public List<Uri> syncLoadList() {
        Cursor cursor = loadInBackground();
        List<Uri> list = new ArrayList<>(cursor.getCount());
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))));
            }
        }
        AppTools.closeQuietly(cursor);
        return list;
    }


    @Override
    public void onContentChanged() {
        // FIXME a dirty way to fix loading multiple times
    }
}
