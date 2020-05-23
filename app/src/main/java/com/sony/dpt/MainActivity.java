package com.sony.dpt;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.view.ViewGroup;

import com.sony.dpt.controller.DptGestureDetector;
import com.sony.dpt.controller.DptGestureListener;
import com.sony.dpt.drawing.DrawableView;
import com.sony.dpt.media.CbzImagePack;
import com.sony.dpt.media.ImagePack;
import com.sony.dpt.media.ImagePackImageView;
import com.sony.dpt.network.FtpMount;
import com.sony.dpt.override.UpdateMode;
import com.sony.dpt.override.ViewOverride;

import org.apache.ftpserver.ftplet.FtpException;

import java.io.IOException;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DrawableView drawableView = findViewById(R.id.pen);
        drawableView.setZOrderOnTop(true);

        FtpMount ftpMount = new FtpMount("/data/manga/", 5050);
        try {
            ftpMount.mount();
        } catch (FtpException e) {
            e.printStackTrace();
        }

        final ImagePackImageView imagePackImageView = findViewById(R.id.background);
        try {
            ImagePack cbz = CbzImagePack.open("/data/manga/01.cbz");
            imagePackImageView.setImagePack(cbz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setHierarchyUpdateMode(getWindow().getDecorView());

        GestureDetector gestureDetector = new DptGestureDetector(getApplicationContext(), new DptGestureListener() {
            @Override
            public void onFlingRight() {
                try {
                    imagePackImageView.flipNext();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFlingLeft()  {
                try {
                    imagePackImageView.flipPrevious();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        drawableView.setGestureDetector(gestureDetector);

    }

    public void setHierarchyUpdateMode(View parent) {
        ViewOverride.getInstance().setDefaultUpdateMode(parent, UpdateMode.UPDATE_MODE_NOWAIT_NOCONVERT_DU_SP1_IGNORE);

        if (parent instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) parent;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                setHierarchyUpdateMode(viewGroup.getChildAt(i));
            }
        }
    }


}