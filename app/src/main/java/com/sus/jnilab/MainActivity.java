package com.sus.jnilab;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.sus.jnilab.utils.JniManager;

public class MainActivity extends AppCompatActivity {

    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        tvResult = getView(R.id.tv_result);
        tvResult.setText(JniManager.getInfo());
    }


    private <T extends View> T getView(@IdRes int id) {
        return (T) findViewById(id);
    }

}
