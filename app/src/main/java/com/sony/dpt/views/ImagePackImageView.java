package com.sony.dpt.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sony.dpt.image.ImageView;
import com.sony.dpt.media.ImagePack;
import com.sony.dpt.override.UpdateMode;

import java.io.IOException;

import static com.sony.dpt.image.ImageHelper.centerWidth;
import static com.sony.dpt.image.ImageHelper.frame;
import static com.sony.dpt.image.ImageHelper.scale;

public class ImagePackImageView extends ImageView {

    private ImagePack imagePack;

    public ImagePackImageView(Context context) {
        super(context);
    }

    public ImagePackImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImagePackImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ImagePackImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private int lastPageDisplayed = 0;

    public void display() throws IOException {
        display(lastPageDisplayed);
    }

    public void display(int page) throws IOException {
        lastPageDisplayed = page;
        Glide.with(this)
                .asBitmap()
                .load(imagePack.page(page))
                .transform(scale(true), frame(), centerWidth())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        imageRenderer.flipPage(resource, UpdateMode.UI_DEFAULT_MODE);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    public void setImagePack(ImagePack imagePack) throws IOException {
        this.imagePack = imagePack;
    }

    public int flipNext() throws IOException {
        if (imagePack.currentPage() < imagePack.pageCount() - 1) {
            display(imagePack.currentPage() + 1);
        }
        return imagePack.currentPage();
    }

    public int flipPrevious() throws IOException {
        if (imagePack.currentPage() > 0) {
            display(imagePack.currentPage() - 1);
        }
        return imagePack.currentPage();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        super.surfaceCreated(holder);
        try {
            display();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
