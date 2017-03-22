package com.ysered.hatonhead;

/**
 * Represents a presenter in a model view presenter (MVP) pattern.
 */
public interface BasePresenter<T> {
    /**
     * Sets a view related to a presenter.
     * @param view
     */
    void setView(T view);

    /**
     * Should be called in the view's (Activity or Fragment) onResume() method.
     */
    void bind();

    /**
     * Should be called in the view's (Activity or Fragment) onDestroy() method.
     */
    void unbind();
}
