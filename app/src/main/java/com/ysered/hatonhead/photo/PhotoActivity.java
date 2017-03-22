package com.ysered.hatonhead.photo;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.ysered.hatonhead.R;
import com.ysered.hatonhead.view.FaceView;

public class PhotoActivity extends AppCompatActivity implements PhotoContract.View {
    private static final String TAG = PhotoActivity.class.getSimpleName();

    private FaceDetector faceDetector;
    private PhotoContract.Presenter presenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        FaceView faceView = (FaceView) findViewById(R.id.faceView);

        faceDetector = new FaceDetector.Builder(getApplicationContext())
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.ACCURATE_MODE)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        presenter = new PhotoPresenter(null);
        presenter.setView(this);

        final Bitmap faceDemoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.face_demo2);
        final Frame frame = new Frame.Builder().setBitmap(faceDemoBitmap).build();
        final SparseArray<Face> faces = faceDetector.detect(frame);

        if (!faceDetector.isOperational()) {
            Log.w(TAG, "Face faceDetector dependencies are not yet available.");

            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            final IntentFilter lowStorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowStorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                Log.w(TAG, getString(R.string.low_storage_error));
            }
        }
        faceView.setContent(faceDemoBitmap, faces, true);
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
        faceDetector.release();
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
