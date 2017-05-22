package com.simon.common.recycled;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: Simon
 * Email: hanzx1024@gmail.com
 * Created on: 2017/4/10 17:03
 */

public abstract class BasicRecycledAdapter<D, VH extends RecycledViewHolder> extends RecyclerView
        .Adapter<VH> {

    private int oldItemPosition = -1;

    /**
     * 绑定到列表的数据集合
     */
    protected final List<D> mDatas;

    public BasicRecycledAdapter() {
        this(null);
    }

    public BasicRecycledAdapter(List<D> list) {
        mDatas = new ArrayList<>();
        if (null != list && !list.isEmpty()) {
            mDatas.addAll(list);
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        VH viewHolder;
        viewHolder = onCreateDefViewHolder(parent, viewType);
        initItemClickListener(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        onDefBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return null == mDatas ? 0 : mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return getDefItemViewType(position);
    }

    @Override
    public void onViewAttachedToWindow(VH holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(VH holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    protected View getItemView(int layoutResId, ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
    }

    protected void setFullSpan(RecycledViewHolder holder) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams slp = (StaggeredGridLayoutManager
                    .LayoutParams) layoutParams;
            slp.setFullSpan(true);
        }
    }

    public D getItem(int position) {
        return mDatas.get(position);
    }

    /**
     * 获取所有的列表数据<BR>
     *
     * @return 返回所有的集合数据
     */
    public List<D> getAllList() {
        return mDatas;
    }

    /**
     * 刷新数据
     *
     * @param isClear 是否清除原 mDatas 数据
     */
    public void addAllAndNotifyChanged(List<? extends D> list, boolean isClear) {

        if (list == null || list.isEmpty()) {
            return;
        }

        if (isClear) {
            this.mDatas.clear();
        }

        this.mDatas.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 刷新单个itemm
     *
     * @param currentSelect 当前选中position
     */
    public void refereshItem(int currentSelect) {
        if (oldItemPosition != -1) {
            notifyItemChanged(oldItemPosition);
        }
        oldItemPosition = currentSelect;
        notifyItemChanged(currentSelect);
    }

    /**
     * 列表条目对应的 viewType
     * 可以通过重写该方法来获取对应的 viewType
     */
    protected abstract int getDefItemViewType(int position);


    protected abstract VH onCreateDefViewHolder(ViewGroup parent, int viewType);

    protected abstract void onDefBindViewHolder(VH holder, int position);


    //---------------------------------- 点击事件 -----------------------------

    private OnItemClickListener mOnItemClickListener;
    private OnRViewItemLongClickListener onRViewItemLongClickListener;

    private void initItemClickListener(final VH viewHolder) {
        if (mOnItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, viewHolder.getLayoutPosition());
                }
            });
        }

        if (onRViewItemLongClickListener != null) {
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return onRViewItemLongClickListener.onItemLongClick(v, viewHolder
                            .getLayoutPosition());
                }
            });
        }
    }

    /**
     * 设置 RecyclerView Item 的点击事件
     *
     * @param itemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    /**
     * 设置 RecyclerView Item 的长点击事件
     *
     * @param itemLongClickListener
     */
    public void setOnRViewItemLongClickListener(OnRViewItemLongClickListener
                                                        itemLongClickListener) {
        this.onRViewItemLongClickListener = itemLongClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnRViewItemLongClickListener {
        /**
         * callback method to be invoked when an item in this view has been
         * click and held
         *
         * @param view     The view whihin the AbsListView that was clicked
         * @param position The position of the view int the adapter
         * @return true if the callback consumed the long click ,false otherwise
         */
        boolean onItemLongClick(View view, int position);
    }
}
