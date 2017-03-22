package com.ysered.hatonhead.photo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ysered.hatonhead.R;

public class PhotoActivity extends AppCompatActivity implements PhotoContract.View {

    private PhotoContract.Presenter presenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        presenter = new PhotoPresenter();
        presenter.setView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @Override
    public void showProgress() {
        // TODO: show loading
    }

    @Override
    public void hideProgress() {
        // TODO: hide loading
    }
}
