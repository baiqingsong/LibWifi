package com.dawn.libdownload;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * WIFI连接的适配器
 */
public class WifiConnectAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public WifiConnectAdapter(@Nullable List<String> data) {
        super(R.layout.item_wifi_connect, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_ssid, TextUtils.isEmpty(item) ? "" : item);
        helper.addOnClickListener(R.id.rel_main);
    }

    public void updateWifiList(List<String> wifiList) {
        if (wifiList != null && !wifiList.isEmpty()) {
            getData().clear(); // 清空原有数据
            getData().addAll(wifiList); // 添加新数据
            notifyDataSetChanged(); // 通知适配器数据已更改
        }
    }
}
