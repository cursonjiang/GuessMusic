package com.curson.guessmusic.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.curson.guessmusic.R;

/**
 * 通关界面
 * Created by Curson on 15/3/18.
 */
public class AllPassView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_pass_view);

        //隐藏右上角金币
        FrameLayout view = (FrameLayout) findViewById(R.id.layout_bar_coin);
        view.setVisibility(View.GONE);
    }
}
