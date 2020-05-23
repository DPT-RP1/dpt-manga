package com.sony.dpt;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentActivity;

import com.sony.dpt.controller.DptGestureDetector;
import com.sony.dpt.controller.DptGestureListener;
import com.sony.dpt.drawing.DrawableView;
import com.sony.dpt.media.CbzImagePack;
import com.sony.dpt.media.ImagePack;
import com.sony.dpt.views.ImagePackImageView;
import com.sony.dpt.network.FtpMount;
import com.sony.dpt.override.UpdateMode;
import com.sony.dpt.override.ViewOverride;
import com.sony.dpt.views.ThumbnailView;

import org.apache.ftpserver.ftplet.FtpException;

import java.io.IOException;

public class MainActivity extends FragmentActivity {

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

        setHierarchyUpdateMode(getWindow().getDecorView());

        FrameLayout mainLayout = (FrameLayout) findViewById(R.id.main_layout);
        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println();
            }
        });
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