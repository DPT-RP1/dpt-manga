package com.sony.dpt.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.sony.dpt.image.ImageHelper;
import com.sony.dpt.media.ImagePack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ThumbnailView extends GridView {

    private BaseAdapter baseAdapter;
    private Canvas canvas;
    private Paint framePaint;

    private static final int THUMBNAIL_WIDTH = (int) ImageHelper.DEFAULT_WIDTH / 3 - 33;
    private static final int THUMBNAIL_HEIGHT = 800;

    public ThumbnailView(Context context) {
        super(context);
        init(context);
    }

    public ThumbnailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ThumbnailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ThumbnailView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private List<byte[]> thumbnails;

    private void init(final Context context) {
        canvas = new Canvas();
        framePaint = new Paint();
        framePaint.setColor(Color.BLACK);
        framePaint.setStyle(Paint.Style.STROKE);
        framePaint.setStrokeWidth(10);

        thumbnails = new ArrayList<>();
        this.setNumColumns(3);
        baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return thumbnails.size();
            }

            @Override
            public Object getItem(int position) {
                return thumbnails.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final ImageView picturesView;

                picturesView = new ImageView(context);

                byte[] thumbnail = (byte[]) getItem(position);

                ImageHelper.loadBitmapInto(picturesView, thumbnail, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, new ImageHelper.BitmapCallback() {
                    @Override
                    public void bitmapLoaded(Bitmap bitmap) {
                        canvas.setBitmap(bitmap);
                        canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), framePaint);
                        picturesView.setImageBitmap(bitmap);
                        picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        picturesView.setPadding(8, 8, 8, 8);
                        picturesView.setLayoutParams(new GridView.LayoutParams(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT));
                    }
                });

                return picturesView;
            }
        };
        setAdapter(baseAdapter);
    }

    public void addThumbnail(final byte[] thumbnail) {
        thumbnails.add(thumbnail);
        baseAdapter.notifyDataSetChanged();
    }
}
