package com.dawn.wifi;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class WifiFactory {
    //单例
    private static WifiFactory instance;
    private Context mContext;

    public static WifiFactory getInstance(Context context) {
        if (instance == null) {
            synchronized (WifiFactory.class) {
                if (instance == null) {
                    instance = new WifiFactory(context);
                }
            }
        }
        return instance;
    }

    // 私有化构造函数，防止外部实例化
    private WifiFactory(Context context) {
        // 私有化构造函数，防止外部实例化
        // 可以在这里进行一些初始化操作
        this.mContext = context.getApplicationContext(); // 使用应用程序上下文，防止内存泄漏
        mWifiMgr = new LWifiMgr(mContext);
    }

    private LWifiMgr mWifiMgr;//wifi管理类
    private LWifiBroadcastReceiver mReceiver; // 广播接收器，用于监听WiFi状态变化
    private List<ScanResult> mScanResults = new ArrayList<>();//扫描到的可用的WiFi列表
    private OnWifiListener mListener;

    /**
     * 开启WiFi
     */
    public void openWifi(){
        registerWifiReceiver();
        if(mWifiMgr.isWifiEnabled()) {
            mWifiMgr.startScan();
        } else {
            mWifiMgr.openWifi();
        }
    }

    /**
     * 关闭WiFi接收
     */
    public void closeWifiReceiver(){
        if(mReceiver != null)
            mContext.unregisterReceiver(mReceiver);
    }

    /**
     * 获取当前连接WIFI的名称
     */
    public String getConnectWifiSsid(){
        return mWifiMgr.getConnectedSSID();
    }

    /**
     * 清除WiFi配置
     */
    public void clearWifiConfig(){
        mWifiMgr.clearWifiConfig();
    }

    public void connectWifi(String ssid, String password) {
        // 连接到指定的WiFi
        try{
            if (mWifiMgr != null){
                boolean connectStatus = mWifiMgr.connectWifi(ssid, password, mScanResults);
                if(!connectStatus)
                    mWifiMgr.connectWifi(ssid, password, mScanResults); // 尝试再次连接
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 设置回调函数
     * @param listener 回调
     */
    public void setListener(OnWifiListener listener) {
        this.mListener = listener;
    }

    /**
     * 获取ssid得列表
     */
    private List<String> getSsidNameList(){
        List<String> resultNameList = new ArrayList<>();
        String connectSSID = getConnectWifiSsid();
        for(int i = 0; i < mScanResults.size(); i ++){
            if(("\"" + mScanResults.get(i).SSID + "\"").equals(connectSSID))
                continue;
            resultNameList.add(mScanResults.get(i).SSID);
        }
        return resultNameList;
    }

    /**
     * 注册监听WiFi操作的系统广播
     */
    private void registerWifiReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        mReceiver = new LWifiBroadcastReceiver() {
            @Override
            public void onWifiEnabled() {
                //WiFi已开启，开始扫描可用WiFi
                if(mWifiMgr != null)
                    mWifiMgr.startScan();
            }

            @Override
            public void onWifiDisabled() {
                //WiFi已关闭，清除可用WiFi列表
                if(mWifiMgr != null)
                    mScanResults.clear();
            }

            @Override
            public void onScanResultsAvailable(List<ScanResult> scanResults) {
                //扫描周围可用WiFi成功，设置可用WiFi列表
                if(mScanResults != null){
                    mScanResults.clear();
                    mScanResults.addAll(scanResults);
                    List<String> wifiList = getSsidNameList();
                    if(mListener != null)
                        mListener.refreshWifiList(wifiList);
                }
            }

            @Override
            public void onWifiConnected(String connectedSSID) {
                //判断指定WiFi是否连接成功
                if(mListener != null)
                    mListener.wifiConnectSuccess(connectedSSID);
            }

            @Override
            public void onWifiDisconnected() {
                if(mListener != null)
                    mListener.wifiDisconnect(); // 连接断开，传空字符串表示未连接
            }
        };
        mContext.registerReceiver(mReceiver, filter);
    }

}
