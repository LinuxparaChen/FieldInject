package com.chenzhanyang.filedinject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.chenzhanyang.FieldBind;
import com.chenzhanyang.FieldInject;

public class MainActivity extends AppCompatActivity {

    @FieldBind
    public Presenter mPresenter;
    @FieldBind
    public Presenter mPresenter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FieldInject.inject(this);
        mPresenter.invoke();
    }
}
