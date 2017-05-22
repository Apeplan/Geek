package com.simon.geek.ui.images;

import android.os.Bundle;
import android.support.design.widget.TabLayout;

import com.simon.common.utils.ResHelper;
import com.simon.geek.R;
import com.simon.geek.widget.EnableScrollViewPager;
import com.simon.mvp_frame.BaseFragmentWithUIContract;

import java.util.ArrayList;
import java.util.List;

/**
 * describe:
 *
 * @author Apeplan
 * @date 2017/2/13
 * @email hanzx1024@gmail.com
 */

public class ImagesContainerFragment extends BaseFragmentWithUIContract {

    private TabLayout mTabLayout;
    private EnableScrollViewPager mViewPager;

    public static ImagesContainerFragment newInstance() {
        Bundle args = new Bundle();
        ImagesContainerFragment fragment = new ImagesContainerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_images_container;
    }

    @Override
    protected void findViews() {
        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.vp_images);
        mViewPager.setOffscreenPageLimit(7);
    }

    @Override
    protected void initObjects() {
        List<String> categoryList = getCategoryList();

        for (int i = 0, size = categoryList.size(); i < size; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(categoryList.get(i)));
        }

        ImageContainerPagerAdapter pagerAdapter = new ImageContainerPagerAdapter
                (getFragmentManager(), categoryList);

        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                String category = tab.getText().toString();
//                int position = mTabLayout.getSelectedTabPosition();
//                ImagesFragment fragment = (ImagesFragment) mViewPager.getAdapter()
//                        .instantiateItem(mViewPager, position);
//                fragment.onPagerSelect(category);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    protected void initData() {

    }


    public List<String> getCategoryList() {
        String[] stringArray = ResHelper.getStringArray(R.array.image_category_list);
        int length = stringArray.length;
        List<String> categoryList = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            categoryList.add(stringArray[i]);
        }
        return categoryList;
    }

}
