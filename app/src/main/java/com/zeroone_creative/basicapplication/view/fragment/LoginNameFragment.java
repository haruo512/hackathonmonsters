package com.zeroone_creative.basicapplication.view.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.zeroone_creative.basicapplication.R;
import com.zeroone_creative.basicapplication.model.enumerate.LoginPage;
import com.zeroone_creative.basicapplication.view.LoginFragmentCallbackListener;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_login_name)
public class LoginNameFragment extends Fragment {

    private LoginFragmentCallbackListener mListener;

    @ViewById(R.id.login_edittext_name)
    EditText mNameEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_name, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof LoginFragmentCallbackListener) {
            mListener = (LoginFragmentCallbackListener) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Click(R.id.login_button_next)
    public void clickNext() {
        if (mListener != null) {
            mListener.onNextStepListener(mNameEditText.getText().toString(), LoginPage.NickName);
        }
    }

}
