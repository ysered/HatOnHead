package com.ysered.hatonhead;

/**
 * Represents a basic view. All views should implement these common methods.
 */
public interface BaseView {
    /**
     * This is a general method used for showing some kind of progress during a background task. For example, this
     * method should show a progress bar and/or disable buttons before some background work starts.
     */
    void showProgress();

    /**
     * This is a general method used for hiding progress information after a background task finishes.
     */
    void hideProgress();
}
