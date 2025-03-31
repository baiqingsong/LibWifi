package com.dawn.libdownload;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dawn.wifi.OnWifiListener;
import com.dawn.wifi.WifiFactory;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private WifiFactory wifiFactory;
    private RecyclerView recyclerView;
    private WifiConnectAdapter wifiConnectAdapter;
    private List<String> wifiList;
    private TextView tvStartScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wifiFactory = WifiFactory.getInstance(this);
        recyclerView = findViewById(R.id.recycler_view);

        wifiFactory.setListener(new OnWifiListener() {
            @Override
            public void refreshWifiList(List<String> wifiList) {
                Log.i("MainActivity", "refreshWifiList: " + wifiList.toString());
                // 更新WiFi列表
                MainActivity.this.wifiList = wifiList;
                if (wifiConnectAdapter == null) {
                    wifiConnectAdapter = new WifiConnectAdapter(wifiList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this)); // 设置布局管理器
                    recyclerView.setAdapter(wifiConnectAdapter);
                    wifiConnectAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                        // 点击连接WiFi
                        String ssid = wifiList.get(position);
                        // 弹出连接对话框
                        new WifiConnectDialog(MainActivity.this, new WifiConnectDialog.OnWifiConnectListener() {
                            @Override
                            public void getPassword(String password) {
                                wifiFactory.connectWifi(ssid, password);
                            }
                        }, ssid).showDialog();
                    });
                } else {
                    wifiConnectAdapter.updateWifiList(wifiList);
                }
            }

            @Override
            public void wifiConnectSuccess(String ssid) {
                Log.i("MainActivity", "wifiConnectSuccess: " + ssid);
            }

            @Override
            public void wifiDisconnect() {
                Log.i("MainActivity", "wifiDisconnect: ");
            }
        });
        tvStartScan = findViewById(R.id.tv_start_scan);
        tvStartScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wifiFactory.openWifi();
            }
        });

    }

}