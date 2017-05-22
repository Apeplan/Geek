package com.simon.geek.ui.images;

import android.text.TextUtils;

import com.simon.common.recycled.RecycledAdapter;
import com.simon.common.recycled.RecycledViewHolder;
import com.simon.common.utils.ImgLoadHelper;
import com.simon.geek.R;
import com.simon.geek.data.model.BDImageEntity;
import com.simon.geek.widget.PLAImageView;

/**
 * describe:
 *
 * @author Apeplan
 * @date 2017/2/10
 * @email hanzx1024@gmail.com
 */

public class ImagesAdapter extends RecycledAdapter<BDImageEntity> {
    public ImagesAdapter() {
        super(R.layout.item_images, null);
    }


    @Override
    protected void bindDataToView(RecycledViewHolder holder, BDImageEntity item) {
        if (null != item) {
            PLAImageView plaImageView = holder.getView(R.id.pimv_image);
            String thumbnail_url = item.getThumbnail_url();
            int width = item.getThumbnail_width();
            int height = item.getThumbnail_height();

            if (!TextUtils.isEmpty(thumbnail_url)) {
                ImgLoadHelper.imageSimple(thumbnail_url, plaImageView);
            }

            plaImageView.setImageWidth(width);
            plaImageView.setImageHeight(height);

        }
    }
}
