package com.simon.common.recycled;

import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by: Simon
 * Email: hanzx1024@gmail.com
 * Created on: 2017/4/11 8:54
 */

public abstract class MultiRecyledAdapter<D extends MultiItem> extends BasicRecycledAdapter<D,
        RecycledViewHolder> {
    private SparseIntArray mLayoutIds;

    public MultiRecyledAdapter(List<D> list) {
        super(list);
    }

    @Override
    protected RecycledViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        View itemView = getItemView(getItemLayoutId(viewType), parent);
        return new RecycledViewHolder(itemView);
    }

    @Override
    protected void onDefBindViewHolder(RecycledViewHolder holder, int position) {
        bindDataToView(holder, getItem(position));
    }

    @Override
    protected int getDefItemViewType(int position) {
        D item = mDatas.get(position);
        return item.getItemType();
    }

    /**
     * 添加多种类型item对应的布局
     *
     * @param type
     * @param layoutId
     */
    public void addItemType(int type, int layoutId) {
        if (null == mLayoutIds) {
            mLayoutIds = new SparseIntArray();
        }
        mLayoutIds.put(type, layoutId);
    }

    /**
     * 根据 Item ViewType，获取对应的 布局资源id
     *
     * @param viewType
     * @return
     */
    public int getItemLayoutId(int viewType) {

        if (mLayoutIds == null) {
            throw new NullPointerException("You must add a type");
        }
        return mLayoutIds.get(viewType);
    }

    protected abstract void bindDataToView(RecycledViewHolder holder, D item);
}
