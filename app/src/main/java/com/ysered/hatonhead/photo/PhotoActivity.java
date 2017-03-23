package com.ysered.hatonhead.photo;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.ysered.hatonhead.R;
import com.ysered.hatonhead.view.FaceView;

public class PhotoActivity extends AppCompatActivity implements PhotoContract.View, CheckBox.OnCheckedChangeListener {
    private static final String TAG = PhotoActivity.class.getSimpleName();

    private FaceView faceView;
    private PhotoContract.Presenter presenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        faceView = (FaceView) findViewById(R.id.faceView);
        ((CheckBox) findViewById(R.id.showAnnotationsCheckBox)).setOnCheckedChangeListener(this);
        ((CheckBox) findViewById(R.id.showHatButton)).setOnCheckedChangeListener(this);

        final FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext())
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.ACCURATE_MODE)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        presenter = new PhotoPresenter(faceDetector, new Frame.Builder());
        presenter.setView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.bind();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unbind();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.showAnnotationsCheckBox:
                if (isChecked) {
                    presenter.onShowFaceAnnotations(faceView.getBitmap());
                } else {
                    presenter.onHideFaceAnnotations();
                }
                break;
            case R.id.showHatButton:
                if (isChecked) {
                    presenter.onShowHat(faceView.getBitmap());
                } else {
                    presenter.onHideHat();
                }
                break;
        }
    }

    @Override
    public void showProgress() {
        // TODO: show loading
    }

    @Override
    public void hideProgress() {
        // TODO: hide loading
    }

    @Override
    public void showLowStorageError() {
        Log.w(TAG, "Face faceDetector dependencies are not yet available.");
        final IntentFilter lowStorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
        final boolean hasLowStorage = registerReceiver(null, lowStorageFilter) != null;
        if (hasLowStorage) {
            Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
            Log.w(TAG, getString(R.string.low_storage_error));
        }
    }

    @Override
    public void showFaceAnnotations(SparseArray<Face> faces) {
        faceView.showFaceAnnotations(faces);
    }

    @Override
    public void hideFaceAnnotations() {
        faceView.hideFaceAnnotations();
    }

    @Override
    public void showHats(SparseArray<Face> faces) {
        faceView.showHats(faces);
    }

    @Override
    public void hideHats() {
        faceView.hideHats();
    }
}
