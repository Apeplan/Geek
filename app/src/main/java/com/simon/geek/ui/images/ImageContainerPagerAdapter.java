package com.simon.geek.ui.images;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * describe:
 *
 * @author Apeplan
 * @date 2017/2/13
 * @email hanzx1024@gmail.com
 */

public class ImageContainerPagerAdapter extends FragmentPagerAdapter {
    private List<String> mCategoryList = null;

    public ImageContainerPagerAdapter(FragmentManager fm, List<String> categoryList) {
        super(fm);
        mCategoryList = categoryList;
    }

    @Override
    public Fragment getItem(int position) {
        return ImagesFragment.newInstance();
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
