package com.dawn.wifi;

import java.util.List;

public interface OnWifiListener {
    void refreshWifiList(List<String> wifiList);// 刷新WiFi列表
    void wifiConnectSuccess(String ssid);// 连接成功的回调
    void wifiDisconnect();
}
