package com.zeroone_creative.basicapplication.view;

import com.zeroone_creative.basicapplication.model.enumerate.LoginPage;

/**
 * Created by shunhosaka on 15/05/16.
 */
public interface LoginFragmentCallbackListener {
    public void onNextStepListener(String data, LoginPage page);
    public void onBackStepListener(String data, LoginPage page);
}
