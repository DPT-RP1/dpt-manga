package com.sony.dpt.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sony.dpt.override.UpdateMode;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.PAINT_FLAGS;
import static com.sony.dpt.image.Orientation.PORTRAIT;

public class ImageHelper {

    // DPT dimensions
    public static final float DEFAULT_WIDTH = 1650;
    public static final float DEFAULT_HEIGHT = 2200;
    public static final Paint DEFAULT_PAINT = new Paint(PAINT_FLAGS);

    public static Orientation orientation(float width, float height) {
        if (width < height) {
            return PORTRAIT;
        } else {
            return Orientation.LANDSCAPE;
        }
    }

    public static Transformation<Bitmap> scale(boolean bilinearFiltering) {
        if (bilinearFiltering) {
            return new BilinearFitHeightTransformation();
        } else {
            return new FitHeightTransformation();
        }
    }

    public static Transformation<Bitmap> centerWidth() {
        return new CenterWidthTransformation();
    }

    public static Transformation<Bitmap> frame() {
        return new FrameTransformation();
    }

    public static Bitmap.Config getNonNullConfig(@NonNull Bitmap bitmap) {
        return bitmap.getConfig() != null ? bitmap.getConfig() : Bitmap.Config.ARGB_8888;
    }

    public static void applyMatrix(@NonNull Bitmap inBitmap, @NonNull Bitmap targetBitmap, Matrix matrix) {

        Canvas canvas = new Canvas(targetBitmap);
        canvas.drawBitmap(inBitmap, matrix, DEFAULT_PAINT);
        canvas.setBitmap(null);
    }

    public interface BitmapCallback {
        void bitmapLoaded(Bitmap bitmap);

    }

    public static void loadBitmapInto(final ImageView target, final byte[] content,
                                      final int targetWdith, final int targetHeight,
                                      final BitmapCallback bitmapCallback) {
        Glide.with(target)
                .asBitmap()
                .load(content)
                .override(targetWdith, targetHeight)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        bitmapCallback.bitmapLoaded(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

}
