package com.kepler.projectsupportlib;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.socialrehab.R;

import butterknife.ButterKnife;

/**
 * Created by special on 21/11/17.
 */

public abstract class BaseDialogFragment extends DialogFragment {

    private static final String TAG = BaseDialogFragment.class.getSimpleName();
    private View view;
    private FragmentCommunicator fragmentCommunicator;

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (this.view != null) {
            this.view = null;
        }
        if (this.fragmentCommunicator != null) {
            this.fragmentCommunicator = null;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentCommunicator = (FragmentCommunicator) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        setFragmentTitle(getFragmentTitle());
    }

    protected abstract int getFragmentTitle();

    protected void setFragmentTitle(String title) {
        fragmentCommunicator.setFragmentTitle(title);
    }

    private void setFragmentTitle(int title) {
        fragmentCommunicator.setFragmentTitle(title);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getViewResource(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    protected abstract int getViewResource();


    protected void showToast(int message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

//    protected void showSnack(int message) {
//        showSnack(getResources().getString(message));
//    }
//
//    protected void showSnack(String message) {
//        Snackbar.make(view, Html.fromHtml("<font color=\"#ffffff\">" + message + "</font>"), Snackbar.LENGTH_SHORT).show();
//    }
//
//    protected void showSnack(int message, int action, View.OnClickListener onClickListener) {
//        showSnack(getResources().getString(message), action, onClickListener);
//    }
//
//    protected void showSnack(String message, int action, View.OnClickListener onClickListener) {
//        Snackbar.make(view, Html.fromHtml("<font color=\"#ffffff\">" + message + "</font>"), Snackbar.LENGTH_LONG).setAction(action, onClickListener).show();
//    }

    protected void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    protected void hideInputKeyboard(View view) {
        if (view.hasFocus()) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void showInputKeyboard(View view) {
        if (view.hasFocus()) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, 0);
        }
    }

    protected int getInt(String value) {
        try {
            return (value.length() == 0) ? 0 : Integer.parseInt(value);
        } catch (Exception e) {
        }
        return 0;
    }

    protected void replaceFragment(Fragment fragment) {
        replaceFragment(fragment, null, true);
    }

    protected void replaceFragment(Fragment fragment, Bundle bundle) {
        replaceFragment(fragment, bundle, true);
    }

    private void replaceFragment(Fragment fragment, Bundle bundle, boolean addTo) {
        fragmentCommunicator.replaceFragment(fragment, bundle, addTo);
    }

    protected void addFragment(Fragment fragment) {
        addFragment(fragment, null, false);

    }

    protected void addFragment(Fragment fragment, boolean addToStack) {
        addFragment(fragment, null, addToStack);
    }

    private void addFragment(Fragment fragment, Bundle bundle, boolean addToStack) {
        fragmentCommunicator.addFragment(fragment, bundle, addToStack);
    }

//    protected View getVerticalSeperator() {
//        View view = new View(getActivity());
//        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
//        view.setBackgroundResource(R.color.colorSeprator);
//        return view;
//    }


    protected void showAlert(String message) {
        new AlertDialog.Builder(getActivity()).setCancelable(true).setMessage(message).setPositiveButton(R.string.okay, null).create().show();
    }

    protected void startActivity(@NonNull Class<? extends BaseActivity> aClass) {
        Intent intent = new Intent(getActivity(), aClass);
        startActivity(intent);
    }

    protected void startActivity(@NonNull Class<? extends BaseActivity> aClass, int flags) {
        Intent intent = new Intent(getActivity(), aClass);
        intent.setFlags(flags);
        startActivity(intent);
    }


    protected void startActivity(@NonNull Class<? extends BaseActivity> aClass, Bundle bundle) {
        Intent intent = new Intent(getActivity(), aClass);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivity(intent);
    }


}
