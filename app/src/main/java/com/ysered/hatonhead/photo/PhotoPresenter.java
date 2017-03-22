package com.ysered.hatonhead.photo;

public class PhotoPresenter implements PhotoContract.Presenter {

    private PhotoContract.View view;

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
    }
}
