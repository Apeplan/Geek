package com.simon.geek.ui.images;

import com.simon.geek.data.Api;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * Created by: Simon
 * Created on: 2017/5/27 14:19
 * Email: hanzx1024@gmail.com
 */
public class ImagesPresenterTest {

    @Mock
    private ImagesContract.View mView;

    private ImagesPresenter mPresenter;

    @Before
    public void setUp() throws Exception {
        mPresenter = new ImagesPresenter(mView);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getImages() throws Exception {
        mPresenter.getImages("汽车", "全部", 1, 10, Api.ACTION_BEGIN);

        mPresenter.showDialog();
    }

}