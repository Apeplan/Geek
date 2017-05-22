package com.simon.common.state.network;

import com.simon.mvp_frame.NetWorkHelper;

/**
 * describe: Listener for NetWork
 *
 * @author Simon Han
 * @date 2015.08.27
 * @email hanzx1024@gmail.com
 */

public interface NetWorkListener {
    void onNetWorkConnected(NetWorkHelper.NetType type);

    void onNetWorkDisConnect();
}
