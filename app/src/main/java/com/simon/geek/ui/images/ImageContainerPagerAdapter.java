package com.simon.geek.ui.images;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * describe:
 *
 * @author Apeplan
 * @date 2017/2/13
 * @email hanzx1024@gmail.com
 */

public class ImageContainerPagerAdapter extends FragmentStatePagerAdapter {
    private List<String> mCategoryList = null;

    public ImageContainerPagerAdapter(FragmentManager fm, List<String> categoryList) {
        super(fm);
        mCategoryList = categoryList;
    }

    @Override
    public Fragment getItem(int position) {
        ImagesFragment fragment = new ImagesFragment();
        Bundle args = new Bundle();
        String s = mCategoryList.get(position);
        args.putString("type",s);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return null == mCategoryList ? 0 : mCategoryList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null == mCategoryList ? null : mCategoryList.get(position);
    }
}
