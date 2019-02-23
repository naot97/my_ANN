package com.example.sipln.movieapp.Views.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sipln.movieapp.Models.Actor;
import com.example.sipln.movieapp.Models.Comment;
import com.example.sipln.movieapp.Models.Movie;
import com.example.sipln.movieapp.Models.Reviewer;
import com.example.sipln.movieapp.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;


public class MainActivity extends AppCompatActivity {

    private static final int TIME_LIMIT = 1500;
    private static long backPressed;
    public MobileServiceTable<Reviewer> mReviewerTable;
    public Button btnLogin;
    public Button btnRegister;

    public EditText txtId;
    public EditText txtPass;

    //------FACEBOOK--------------------
    CallbackManager callbackManager;
    LoginButton loginButton;
    //----------------------------------

    private MobileServiceClient mClient;
    private MobileServiceTable<Actor> actorList;

    @Override
    public void onStart() {

        super.onStart();
        // comment

    }

    protected void onCreate(Bundle savedInstanceState) {


        //1. Set layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //2. Binding UI element
        txtId = findViewById(R.id.txtId);
        txtPass = findViewById(R.id.txtPass);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        loginButton = findViewById(R.id.login_button_facebook);


        //3. Đăng ký sự kiện cho nút đăng ký tài khoản
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        //4. Tạo đăng nhập bằng FB với nút loginButton
        Intent intent_ToHome = new Intent(MainActivity.this, HomeActivity.class);
        intent_ToHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Context context = this.getBaseContext();

        //5. Kết nối remote data server lấy dữ liệu
        try {
            mClient = new MobileServiceClient("https://ttcnpm.azurewebsites.net", this);
            Actor.getActorTable(mClient);
            Reviewer.getTableReviewer(mClient);
            Movie.getMovieTable(mClient);
            Comment.getCommentTable(mClient);

        } catch (Exception e) {
            Log.e("ERRORS", e.getMessage().toString());
        }
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();

        // cap quyen truy cap du lieu
        loginButton.setReadPermissions("public_profile", "email");

        // set btn chờ sự kiện đăng nhập bằng facebook
        FacebookService.set_LoginFacebookButton(loginButton, callbackManager, intent_ToHome, context,this);

        setBtnLogin();

    }

    //-------------------------------------------------------------------

    private void setBtnLogin() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 1. Khong co Internet ---> Return
                if (!networkConnectionIsAvailable()) {
                    Toast.makeText(getBaseContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 2. Username hoặc Password để trống --> Return
                String id = txtId.getText().toString();
                String pass = txtPass.getText().toString();
                if (id.equals("") || pass.equals("")) {
                    Toast.makeText(getBaseContext(), "Please Enter your username and password", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 3. Validating Username and Password
                if (Reviewer.reviewerList != null) {
                    try {

                        boolean check = false;

                        for (int i = 0; i < Reviewer.reviewerList.size(); i++) {

                            if (Reviewer.reviewerList.get(i).getUserName().equals(id) && Reviewer.reviewerList.get(i).getPassWord().equals(pass)) {
                                check = true;
                                Reviewer.loggingReviewer = Reviewer.reviewerList.get(i);
                                Intent intent_ToHome = new Intent(MainActivity.this, HomeActivity.class);
                                intent_ToHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent_ToHome);
                            }
                        }
                        if (check == false) {
                            Toast.makeText(getBaseContext(), "Username and Password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("ERRORS", e.getMessage().toString());
                    }
                } else {
                    Toast.makeText(getBaseContext(), "An error occurred. Please try again later!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //---------------------------------------------------------------------------------------------------------------
//set back twice to exits for main activity -- vnd
    @Override
    public void onBackPressed() {
        if (TIME_LIMIT + backPressed > System.currentTimeMillis()) {
            LoginManager.getInstance().logOut();
            super.onBackPressed();
        } else {
            Toast.makeText(getApplicationContext(), "Press back twice to exit", Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();

    }

    //-----------------------------------------------------------------------------------------------------------------
//---------------------------------------------------UTILS---------------------------------------------------------
    protected boolean networkConnectionIsAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) return false;
        NetworkInfo.State network = info.getState();
        return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginManager.getInstance().logOut();

    }
//-----------------------------------------------------------------------------------------------------------------

}

