package com.simon.geek.ui.images;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;

import com.simon.agiledevelop.mvpframe.BaseFragment;
import com.simon.agiledevelop.mvpframe.Presenter;
import com.simon.agiledevelop.state.StateView;
import com.simon.agiledevelop.utils.ResHelper;
import com.simon.geek.R;
import com.simon.geek.widget.EnableScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * describe:
 *
 * @author Apeplan
 * @date 2017/2/13
 * @email hanzx1024@gmail.com
 */

public class ImagesContainerFragment extends BaseFragment {

    private TabLayout mTabLayout;
    private EnableScrollViewPager mViewPager;

    public static ImagesContainerFragment newInstance() {

        Bundle args = new Bundle();

        ImagesContainerFragment fragment = new ImagesContainerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_images_container;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void initView(LayoutInflater inflater, View view) {
        mTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        mViewPager = (EnableScrollViewPager) view.findViewById(R.id.vp_images);

        List<String> categoryList = getCategoryList();

        for (int i = 0, size = categoryList.size(); i < size; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(categoryList.get(i)));
        }

        ImageContainerPagerAdapter pagerAdapter = new ImageContainerPagerAdapter
                (getFragmentManager(), categoryList);

        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    protected StateView getLoadingView(View view) {
        return null;
    }

    @Override
    protected void initEventAndData() {
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String category = tab.getText().toString();
                int position = mTabLayout.getSelectedTabPosition();
                ImagesFragment fragment = (ImagesFragment) mViewPager.getAdapter()
                        .instantiateItem(mViewPager, position);
                fragment.onPagerSelect(category);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
