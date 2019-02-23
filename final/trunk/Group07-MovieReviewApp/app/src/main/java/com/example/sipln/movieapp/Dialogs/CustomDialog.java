package com.example.sipln.movieapp.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sipln.movieapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomDialog extends Dialog {

    public Activity c;
    public Dialog d;
    @BindView(R.id.viewDialogLogin_tvExit)
    TextView mTvLogin;
    @BindView(R.id.viewDialogLogin_tvCamera)
    TextView mTvCamera;
    @BindView(R.id.viewDialogLogin_tvDirectory)
    TextView mTvDirectory;

    public CustomDialog(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_dialog_choose_avatar);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.viewDialogLogin_tvExit, R.id.viewDialogLogin_tvCamera, R.id.viewDialogLogin_tvDirectory})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.viewDialogLogin_tvCamera:
                openCamera();
                dismiss();
                break;
            case R.id.viewDialogLogin_tvDirectory:
                openDirectory();
                dismiss();
                 break;
            case R.id.viewDialogLogin_tvExit:
                dismiss();
                break;
            default:
                break;
        }

    }

    private void openDirectory(){
        Toast.makeText(c, "openDirectory", Toast.LENGTH_SHORT).show();
        Intent openDirectory = new Intent();
        openDirectory.setAction("Directory");
        LocalBroadcastManager.getInstance(c).sendBroadcast(openDirectory);
    }

    private void openCamera(){
        Toast.makeText(c, "openCamera", Toast.LENGTH_SHORT).show();
        Intent openCamera = new Intent();
        openCamera.setAction("Camera");
        LocalBroadcastManager.getInstance(c).sendBroadcast(openCamera);
    }

}