package com.dawn.libdownload;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;


/**
 * dialog的基类
 */
public abstract class LBaseDialog {
    private final String TAG = LBaseDialog.class.getSimpleName();
    protected Context mContext;
    protected Dialog dialog;
    protected View view;
    protected OnLBaseDialogListener mListener;
    protected Object[] objs;
    protected boolean isOutSide = true;
    protected int dialogStyle = R.style.base_dialog;

    /**
     * 构造方法
     * @param context
     * @param listener
     */
    public LBaseDialog(Context context, OnLBaseDialogListener listener, Object... objs) {
        this.mContext = context;
        this.mListener = listener;
        this.objs = objs;
    }

    /**
     * 设置回调
     * @param listener
     */
    public void setListener(OnLBaseDialogListener listener){
        this.mListener = listener;
    }

    public void setOutSide(boolean isOutSide){
        this.isOutSide = isOutSide;
    }
    public void setDialogStyle(int dialogStyle){
        this.dialogStyle = dialogStyle;
    }

    /**
     * 显示dialog
     */
    public void showDialog(){
        setDialog(isOutSide);
        if(dialog != null)
            dialog.show();
    }

    /**
     * 取消dialog
     */
    public void dissmissDialog(){
        if(dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * 获取dialog实例
     * @return
     */
    public Dialog getDialog(){
        return dialog;
    }
    private void setDialog(boolean isOutSide){
        if(dialog == null){
            dialog = new Dialog(mContext, this.dialogStyle);
            view  = LayoutInflater.from(mContext).inflate(getDialogLayoutRes(), null, false);
            if(view != null){
                dialog.setContentView(view);
                dialog.setCanceledOnTouchOutside(isOutSide);
                setDialogListener();
            }else{
                dialog = null;
            }

        }
    }

    /**
     * 设置弹窗居中
     */
    protected void setDialogCenter(){
        if(dialog == null)
            return;
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams attr = window.getAttributes();
            if (attr != null) {
                attr.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                attr.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                attr.gravity = Gravity.CENTER;//设置dialog 在布局中的位置
            }
        }
    }
    protected abstract @LayoutRes
    int getDialogLayoutRes();
    protected abstract void setDialogListener();
    public interface OnLBaseDialogListener{

    }
}
