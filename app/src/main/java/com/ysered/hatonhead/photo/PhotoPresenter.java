package com.ysered.hatonhead.photo;

import android.graphics.Bitmap;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.FaceDetector;

class PhotoPresenter implements PhotoContract.Presenter {
    private FaceDetector faceDetector;
    private Frame.Builder frameBuilder;
    private PhotoContract.View view;

    PhotoPresenter(FaceDetector faceDetector, Frame.Builder frameBuilder) {
        this.faceDetector = faceDetector;
        this.frameBuilder = frameBuilder;
    }

    @Override
    public void setView(PhotoContract.View view) {
        this.view = view;
    }

    @Override
    public void bind() {

    }

    @Override
    public void unbind() {
        view = null;
        faceDetector.release();
        faceDetector = null;
        frameBuilder = null;
    }

    @Override
    public void onShowFaceAnnotations(Bitmap bitmap) {
        if (faceDetector.isOperational()) {
            final Frame frame = frameBuilder.setBitmap(bitmap).build();
            view.showFaces(faceDetector.detect(frame));
        } else {
            view.showLowStorageError();
        }
    }
}
