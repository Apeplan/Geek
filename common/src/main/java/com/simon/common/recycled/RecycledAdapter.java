package com.simon.common.recycled;

import android.view.ViewGroup;

import java.util.List;

/**
 * Created by: Simon
 * Email: hanzx1024@gmail.com
 * Created on: 2017/4/10 17:45
 */

public abstract class RecycledAdapter<D> extends BasicRecycledAdapter<D, RecycledViewHolder> {

    private int mLayoutId;

    public RecycledAdapter(int layoutId, List<D> data) {
        super(data);
        mLayoutId = layoutId;
    }

    @Override
    protected int getDefItemViewType(int position) {
        return 0;
    }

    @Override
    protected RecycledViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return new RecycledViewHolder(getItemView(mLayoutId, parent));
    }

    @Override
    protected void onDefBindViewHolder(RecycledViewHolder holder, int position) {
        bindDataToView(holder, getItem(position));
    }

    protected abstract void bindDataToView(RecycledViewHolder holder, D item);

}
