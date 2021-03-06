package com.sony.dpt.image;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

import java.security.MessageDigest;

import static com.sony.dpt.image.ImageHelper.DEFAULT_HEIGHT;
import static com.sony.dpt.image.ImageHelper.getNonNullConfig;

public class BilinearFitHeightTransformation implements Transformation<Bitmap> {

    private static final String ID = "com.sony.dpt.fit_height_bilinear";

    @NonNull
    @Override
    public Resource<Bitmap> transform(@NonNull Context context, @NonNull Resource<Bitmap> resource, int outWidth, int outHeight) {

        BitmapPool bitmapPool = Glide.get(context).getBitmapPool();
        Bitmap toTransform = resource.get();

        Bitmap.Config config = getNonNullConfig(toTransform);

        float width = toTransform.getWidth();
        float height = toTransform.getHeight();

        // We will fit height
        float ratio = width / height;
        final float targetWidth = DEFAULT_HEIGHT * ratio;

        Bitmap toReuse = Bitmap.createScaledBitmap(toTransform, (int) targetWidth, (int) DEFAULT_HEIGHT, true);
        return BitmapResource.obtain(toReuse, bitmapPool);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof BilinearFitHeightTransformation;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID.getBytes(CHARSET));
    }
}
