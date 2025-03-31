package com.dawn.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * Created by dawn on 2017/3/24.
 */
public abstract class LWifiBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        if(intent != null) {
            if(intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                //监听WiFi开启/关闭事件
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                if(wifiState == WifiManager.WIFI_STATE_ENABLED) {
                    //WiFi已开启
                    onWifiEnabled();
                } else if(wifiState == WifiManager.WIFI_STATE_DISABLED) {
                    //WiFi已关闭
                    onWifiDisabled();
                }
            } else if(intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                LWifiMgr LWifiMgr = new LWifiMgr(context);
                List<ScanResult> scanResults = LWifiMgr.getScanResults();
                if(LWifiMgr.isWifiEnabled() && scanResults != null && scanResults.size() > 0) {
                    //成功扫描
                    onScanResultsAvailable(scanResults);
                }
            } else if(intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                //网络状态改变的广播
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (info != null) {
                    if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
                        //WiFi已连接
                        LWifiMgr LWifiMgr = new LWifiMgr(context);
                        String connectedSSID = LWifiMgr.getConnectedSSID();
                        onWifiConnected(connectedSSID);
                    } else if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
                        //WiFi已断开连接
                        onWifiDisconnected();
                    }
                }
            }
        }
    }

    public abstract void onWifiEnabled();

    public abstract void onWifiDisabled();

    public abstract void onScanResultsAvailable(List<ScanResult> scanResults);

    public abstract void onWifiConnected(String connectedSSID);

    public abstract void onWifiDisconnected();
}
