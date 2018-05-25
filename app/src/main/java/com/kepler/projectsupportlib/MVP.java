package com.kepler.projectsupportlib;

/**
 * Created by kepler on 20/3/18.
 */

public interface MVP {

    /**
     * Represents a View in MVP.
     */
    interface BaseView {

    }

    /**
     * Represents a Presenter in MVP.
     * <p>
     * By default, it can attach and detach its View.
     *
     * @param <T> The type of the View that the Presenter will handle
     */
    interface BasePresenter<T extends BaseView> {

        void attachView(T view);

        void detachView();
    }
}
