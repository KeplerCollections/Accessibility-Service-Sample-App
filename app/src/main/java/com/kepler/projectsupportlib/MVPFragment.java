package com.kepler.projectsupportlib;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by special on 21/11/17.
 */

public abstract class MVPFragment<T extends MVP.BasePresenter> extends BaseFragment implements MVP.BaseView {

    private static final String TAG = MVPFragment.class.getSimpleName();
    /**
     * The Presenter attached to this View
     */
    protected T presenter;
    private View view;

    /**
     * Must be overriden to define the Presenter used by this fragment.
     *
     * @return The presenter that will be used by this View.
     */
    protected abstract T createPresenter();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.presenter = this.createPresenter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (this.presenter != null) {
            this.presenter.detachView();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (this.presenter != null) {
            this.presenter.attachView(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.presenter = null;
    }

}
