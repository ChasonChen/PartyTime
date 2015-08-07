package com.csu.partytime.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.csu.partytime.R;
import com.csu.partytime.util.DensityUtil;

import org.w3c.dom.Text;

/**
 * Created by Chason on 2015/5/30.
 */
public class NormalDialog {
    Context context;
    LayoutInflater mInflater;
    Dialog dialog;
    TextView tvTitle;
    TextView tvContent;
    Button btCancel;
    Button btOk;
    boolean isShow;

    public NormalDialog(Context context){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        initViews();
    }

    public void initViews(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = mInflater.inflate(R.layout.dialog_normal,null);
        this.tvTitle = (TextView) view.findViewById(R.id.tv_dialog_normal_title);
        this.tvContent = (TextView) view.findViewById(R.id.tv_dialog_normal_content);
        this.btCancel = (Button) view.findViewById(R.id.bt_dialog_normal_cancel);
        this.btOk = (Button) view.findViewById(R.id.bt_dialog_normal_ok);
        this.btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        builder.setView(view);
        dialog=builder.create();
    }

    public void show(){
        if (!dialog.isShowing()){
            dialog.show();
        }
    }

    public void setTitle(String title){
        tvTitle.setText(title);
    }

    public void setContent(String content){
        tvContent.setText(content);
    }

    public Button getBtCancel(){
        return this.btCancel;
    }

    public Button getBtOk(){
        return this.btOk;
    }

    public void dismiss(){
        if (dialog.isShowing()){
            dialog.dismiss();
        }
    }

}
