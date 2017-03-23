package com.ysered.hatonhead.photo;

import android.graphics.Bitmap;
import android.util.SparseArray;

import com.google.android.gms.vision.face.Face;
import com.ysered.hatonhead.BasePresenter;
import com.ysered.hatonhead.BaseView;

/**
 * Contract between photo presenter and view.
 */
interface PhotoContract {

    interface Presenter extends BasePresenter<View> {

        void onShowFaceAnnotations(Bitmap bitmap);
    }

    interface View extends BaseView {

        void showLowStorageError();

        void showFaces(SparseArray<Face> faces);
    }
}
