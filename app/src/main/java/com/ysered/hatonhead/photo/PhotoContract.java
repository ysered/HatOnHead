package com.ysered.hatonhead.photo;

import com.ysered.hatonhead.BasePresenter;
import com.ysered.hatonhead.BaseView;

/**
 * Contract between photo presenter and view.
 */
interface PhotoContract {

    interface Presenter extends BasePresenter<View> {

    }

    interface View extends BaseView {

    }
}
