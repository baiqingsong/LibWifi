package com.dawn.libdownload;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class WifiConnectDialog extends LBaseDialog {
    private String ssid;
    /**
     * 构造方法
     *
     * @param context
     * @param listener
     * @param objs
     */
    public WifiConnectDialog(Context context, OnLBaseDialogListener listener, Object... objs) {
        super(context, listener, objs);
    }

    @Override
    protected int getDialogLayoutRes() {
        return R.layout.dialog_wifi_connect;
    }

    @Override
    protected void setDialogListener() {
        setDialogCenter();
        if(objs != null && objs.length > 0){
            ssid = (String) objs[0];
        }
        TextView tvSSID = view.findViewById(R.id.tv_ssid);
        tvSSID.setText(TextUtils.isEmpty(ssid) ? "未知SSID": ssid);
        final EditText etPassword = view.findViewById(R.id.et_password);
        TextView tvCertain = view.findViewById(R.id.tv_certain);
        tvCertain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwordStr = etPassword.getText().toString();
                if(TextUtils.isEmpty(passwordStr)){
                    Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mListener != null){
                    ((OnWifiConnectListener)mListener).getPassword(passwordStr);
                    dissmissDialog();
                }
            }
        });

    }
    public interface OnWifiConnectListener extends OnLBaseDialogListener{
        void getPassword(String password);
    }
}
