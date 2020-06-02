package com.sony.dpt;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.view.MenuCompat;
import androidx.fragment.app.FragmentActivity;

import com.sony.dpt.drawing.DrawableView;
import com.sony.dpt.network.FtpMount;
import com.sony.dpt.override.UpdateMode;
import com.sony.dpt.override.ViewOverride;

import org.apache.ftpserver.ftplet.FtpException;

public class MainActivity extends FragmentActivity {

    public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/dp_app_data/manga";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        DrawableView drawableView = findViewById(R.id.pen);
        drawableView.setZOrderOnTop(true);

        FtpMount ftpMount = new FtpMount(ROOT_PATH, 5050);
        try { ftpMount.mount(); } catch (FtpException ignored) { }

        setHierarchyUpdateMode(getWindow().getDecorView());
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