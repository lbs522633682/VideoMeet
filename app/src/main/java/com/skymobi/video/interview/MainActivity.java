package com.skymobi.video.interview;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.skymobi.video.framework.base.BaseUIActivity;
import com.skymobi.video.interview.fragment.MineFragment;
import com.skymobi.video.interview.fragment.TestFragment;

/**
 * FileName: BaseActivity
 * Founder: boshuai.li
 * Profile: 主页
 */
public class MainActivity extends BaseUIActivity implements View.OnClickListener {

    // 填充fragment
    private FrameLayout mMainLayout;

    // 考试
    private ImageView iv_test;
    private TextView tv_test;
    private LinearLayout ll_test;
    private TestFragment mTestFragment;
    private FragmentTransaction mTestTransaction;

    // 我的
    private ImageView iv_mine;
    private TextView tv_mine;
    private LinearLayout ll_mine;
    private MineFragment mMineFragment;
    private FragmentTransaction mMineTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    private void initView() {
        mMainLayout = (FrameLayout) findViewById(R.id.mMainLayout);
        iv_test = (ImageView) findViewById(R.id.iv_test);
        tv_test = (TextView) findViewById(R.id.tv_test);
        ll_test = (LinearLayout) findViewById(R.id.ll_test);
        iv_mine = (ImageView) findViewById(R.id.iv_mine);
        tv_mine = (TextView) findViewById(R.id.tv_mine);
        ll_mine = (LinearLayout) findViewById(R.id.ll_mine);

        ll_mine.setOnClickListener(this);
        ll_test.setOnClickListener(this);

        //设置文本
        tv_test.setText(getString(R.string.text_test));
        tv_mine.setText(getString(R.string.text_mine));

        initFragment();

        //切换默认的选项卡
        checkMainTab(0);
    }

    private void checkMainTab(int tabIndex) {
        switch (tabIndex) {
            case 0:
                showFragment(mTestFragment);

                iv_test.setImageResource(R.drawable.img_star_p);

                iv_mine.setImageResource(R.drawable.img_me);

                tv_test.setTextColor(getResources().getColor(R.color.colorAccent));

                tv_mine.setTextColor(Color.BLACK);

                break;
            case 1:
                showFragment(mMineFragment);

                iv_test.setImageResource(R.drawable.img_star);

                iv_mine.setImageResource(R.drawable.img_me_p);

                tv_test.setTextColor(Color.BLACK);

                tv_mine.setTextColor(getResources().getColor(R.color.colorAccent));

                break;

        }
    }

    /**
     * 显示Fragment
     *
     * @param fragment
     */
    private void showFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            hideAllFragment(transaction);
            transaction.show(fragment);
            transaction.commitAllowingStateLoss();
        }
    }

    /**
     * 隐藏所有的Fragment
     *
     * @param transaction
     */
    private void hideAllFragment(FragmentTransaction transaction) {
        if (mMineFragment != null) {
            transaction.hide(mMineFragment);
        }
        if (mTestFragment != null) {
            transaction.hide(mTestFragment);
        }
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        if (mMineFragment == null) {
            mMineFragment = new MineFragment();
            mMineTransaction = getSupportFragmentManager().beginTransaction();
            mMineTransaction.add(R.id.mMainLayout, mMineFragment);
            mMineTransaction.commit();
        }

        if (mTestFragment == null) {
            mTestFragment = new TestFragment();
            mTestTransaction = getSupportFragmentManager().beginTransaction();
            mTestTransaction.add(R.id.mMainLayout, mTestFragment);
            mTestTransaction.commit();
        }
    }

    /**
     * 处理按钮点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_test:
                checkMainTab(0);
                break;
            case R.id.ll_mine:
                checkMainTab(1);
                break;
        }
    }
}
