package com.skymobi.video.interview.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.skymobi.video.framework.base.BaseFragment;
import com.skymobi.video.interview.R;

/**
 * Author:boshuai.li
 * Time:2020/3/26   15:31
 * Description:考试的fragment
 */
public class TestFragment extends BaseFragment {

    private TextView tv_test_f;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, null);
        tv_test_f = view.findViewById(R.id.tv_test_f);
        // TODO 初始化
        return view;
    }
}
