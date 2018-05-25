package com.kepler.projectsupportlib;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.ArrayDeque;
import java.util.Queue;

public abstract class MVPActivity<T extends MVP.BasePresenter> extends BaseActivity implements MVP.BaseView {

    /**
     * The Presenter attached to this View
     */
    protected T presenter;
    //in your Activity
    Queue<DeferredFragmentTransaction> deferredFragmentTransactions = new ArrayDeque<>();
    private ProgressDialog dialog;
    private SharedPreferences srdPrf;
    private boolean isRunning;

    /**
     * Must be overriden to define the Presenter used by this activity.
     *
     * @return The presenter that will be used by this View.
     */
    protected abstract T createPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.presenter = this.createPresenter();
        if (this.presenter != null) {
            this.presenter.attachView(this);
        }
        super.onCreate(savedInstanceState);
    }


}
