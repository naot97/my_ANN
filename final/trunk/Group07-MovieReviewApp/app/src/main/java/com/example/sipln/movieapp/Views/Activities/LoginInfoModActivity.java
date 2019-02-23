package com.example.sipln.movieapp.Views.Activities;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sipln.movieapp.Models.MobileService;
import com.example.sipln.movieapp.Models.Reviewer;
import com.example.sipln.movieapp.R;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginInfoModActivity extends AppCompatActivity {


    MobileServiceClient mClient;
    MobileServiceTable<Reviewer> mReviewerTable;

    @BindView(R.id.activityAccountMode_etNewPassword)
    TextInputEditText mEtNewPassword;
    @BindView(R.id.activityAccountMode_etConfirmPassword)
    TextInputEditText mEtConfirmPassword;
    @BindView(R.id.activityAccountMode_tvAccept)
    TextView mTvAccept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_info_mod);
        ButterKnife.bind(this);

        mClient = new MobileService(this).client;
        mReviewerTable = mClient.getTable(Reviewer.class);
        mTvAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p1 = mEtNewPassword.getText().toString();
                String p2 = mEtConfirmPassword.getText().toString();
                if (p1.equals(p2) && !p1.contains(" ") && !p1.equals("")) {
                    Reviewer.loggingReviewer.setPassWord(p1);
                    mReviewerTable.update(Reviewer.loggingReviewer);
                    Toast.makeText(getBaseContext(), "Success ", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getBaseContext(), "Wrong ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
