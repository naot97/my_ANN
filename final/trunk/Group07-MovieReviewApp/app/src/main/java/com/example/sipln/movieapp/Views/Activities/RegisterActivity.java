package com.example.sipln.movieapp.Views.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.sipln.movieapp.Models.Reviewer;
import com.example.sipln.movieapp.R;
import com.facebook.common.util.ExceptionWithNoStacktrace;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import java.util.Calendar;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, OnItemSelectedListener {

    public MobileServiceClient mClient;
    public MobileServiceTable<Reviewer> mReviewerTable;
    EditText first_name, surname, username,
            password, email_address,
            birthday;
    TextInputLayout signupInputLayoutFirstName,
            signupInputLayoutPassword, signupInputLayoutEmail,
            signupInputLayoutBirthday, signupInputLayoutUsername,
            signupInputLayoutSurname;
    DatePickerDialog datePickerDialog;
    RadioButton gender_male;

    public static void insert_FB_USER_DB(String first_name,String last_name, String email, MainActivity main){
        for (int i = 0; i < Reviewer.reviewerList.size(); i ++)
            if (Reviewer.reviewerList.get(i).getEmail().equals(email)){
                Reviewer.loggingReviewer = Reviewer.reviewerList.get(i);
                Intent intent_ToHome = new Intent(main, HomeActivity.class);
                intent_ToHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                main.startActivity(intent_ToHome);
                return;
            }
                try{
                Reviewer.loggingReviewer = new Reviewer(0,"","",email,true,first_name,last_name,"01/01/2018");
                Intent intent_ToHome = new Intent(main, HomeActivity.class);
                intent_ToHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                main.startActivity(intent_ToHome);
        }
                catch (Exception e){
                }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mClient = new MobileServiceClient(
                    "https://ttcnpm.azurewebsites.net",
                    this
            );
            mReviewerTable = mClient.getTable(Reviewer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_register);
        ProgressBar progressBar = findViewById(R.id.loadingPanel);
        progressBar.setVisibility(View.INVISIBLE);

        // mapped
        signupInputLayoutFirstName = findViewById(R.id.sign_up_input_layout_first_name);
        signupInputLayoutSurname = findViewById(R.id.sign_up_input_layout_surname);
        signupInputLayoutUsername = findViewById(R.id.sign_up_input_layout_user_name);
        signupInputLayoutPassword = findViewById(R.id.sign_up_input_layout_password);
        signupInputLayoutEmail = findViewById(R.id.sign_up_input_layout_email_address);
        signupInputLayoutBirthday = findViewById(R.id.sign_up_input_layout_birthday);

        // non-facebook register
        first_name = findViewById(R.id.editFirstName);
        surname = findViewById(R.id.editSurName);
        username = findViewById(R.id.editUsername);
        password = findViewById(R.id.editPassword);
        email_address = findViewById(R.id.editEmailAddress);
        birthday = findViewById(R.id.editBirthday);
        gender_male = findViewById(R.id.male);
        prepareDatePickerDialog();
    }

    private void prepareDatePickerDialog() {
        //Get current date
        Calendar calendar = Calendar.getInstance();

        //Create datePickerDialog with initial date which is current and decide what happens when a date is selected.

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //When a date is selected, it comes here.
                //Change birthdayEdittext's text and dismiss dialog.
                String dayOfMonthStr = Integer.toString(dayOfMonth);
                if (dayOfMonthStr.length() == 1) dayOfMonthStr = "0" + dayOfMonthStr;
                String monthOfYearStr = Integer.toString(monthOfYear);
                if (monthOfYearStr.length() == 1) monthOfYearStr = "0" + monthOfYearStr;
                birthday.setText(dayOfMonthStr + "/" + monthOfYearStr + "/" + year);
                datePickerDialog.dismiss();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignUp:

                String first_name_Input = first_name.getText().toString();
                String surname_Input = surname.getText().toString();
                final String username_Input = username.getText().toString();
                String password_Input = password.getText().toString();
                String email_Input = email_address.getText().toString();
                String birthday_Input = birthday.getText().toString();
                Boolean isMan = gender_male.isChecked();
                //why to do this???
                // because an error will occur when using short circuit
                if (!Input_is_OK(first_name_Input, surname_Input, username_Input, password_Input, email_Input)){
                    Toast.makeText(getBaseContext(),"Please fill full context",Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    set_disable_for_Processing();
                    Insert_Data_to_DB(first_name_Input,surname_Input,username_Input,password_Input,email_Input,birthday_Input,isMan);
                }
                break;
            case R.id.editBirthday:
                datePickerDialog.show();
                break;

        }

    }

    public boolean Input_is_OK(String first_name_Input,
                               String surname_Input,
                               String username_Input,
                               String password_Input,
                               String email_Input) {

        boolean step1 = valid_Input_name(first_name_Input, surname_Input);
        boolean step2 = valid_Username(username_Input);
        boolean step3 = valid_Password(password_Input);
        boolean step4 = valid_Email(email_Input);

        return step1 && step2 && step3 && step4;

    }

    /**
     * Check Input name of user
     * return true is valid
     */
    private boolean valid_Input_name(String first_name_Input, String surname_Input) {

        if (first_name_Input.isEmpty()) {
            signupInputLayoutFirstName.setError("FirstName can't be empty");
            signupInputLayoutSurname.setError("Try again");
            return false;
        } else if (surname_Input.isEmpty()) {
            signupInputLayoutFirstName.setError("SurName can't be empty");
            signupInputLayoutSurname.setError("Try again!");
            return false;
        } else if (first_name_Input.length() > 40 || surname_Input.length() > 40) {
            signupInputLayoutFirstName.setError("Too long");
            return false;
        } else {
            signupInputLayoutFirstName.setError(null);
            signupInputLayoutSurname.setError(null);
            return true;
        }
    }

    /**
     * Check Input username of user
     * return true is valid
     */
    private boolean valid_Username(String username_Input) {

        if (username_Input.isEmpty()) {
            signupInputLayoutUsername.setError("UserName can't be empty");
            return false;
        } else if (username_Input.length() > 40) {
            signupInputLayoutUsername.setError("Too long");
            return false;
        } else {
            signupInputLayoutUsername.setError(null);
            return true;
        }
    }

    /**
     * Check Input password of user
     * return true is valid
     */
    private boolean valid_Password(String password_Input) {

        if (password_Input.isEmpty()) {
            signupInputLayoutPassword.setError("Password can't be empty");
            return false;
        } else if (password_Input.length() < 6) {
            signupInputLayoutPassword.setError("Password must be at least 6 characters ");
            return false;
        } else {
            signupInputLayoutPassword.setError(null);
            return true;
        }
    }

    /**
     * Check Input email of user
     * return true is valid
     */
    private boolean valid_Email(String email_Input) {

        if (email_Input.isEmpty()) {         // empty email
            signupInputLayoutEmail.setError("Field Email can't be empty");
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email_Input).matches()) { // check syntax email
            signupInputLayoutEmail.setError("Email is invalid!!");
            return false;
        } else {        // email is valid
            signupInputLayoutEmail.setError(null);
            return true;
        }
    }


    /**
     * For Processing all button and edit_text must disable
     */
    public void set_disable_for_Processing() {
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        first_name.setEnabled(false);
        surname.setEnabled(false);
        username.setEnabled(false);
        password.setEnabled(false);
        email_address.setEnabled(false);
        birthday.setEnabled(false);
        findViewById(R.id.male).setEnabled(false);
        findViewById(R.id.female).setEnabled(false);
    }

    /**
     * For Processing fail  all button and edit_text must enable to re-edit
     */
    public void set_enable_for_ReEdit() {
        first_name.setEnabled(true);
        surname.setEnabled(true);
        username.setEnabled(true);
        password.setEnabled(true);
        email_address.setEnabled(true);
        birthday.setEnabled(true);
        findViewById(R.id.male).setEnabled(true);
        findViewById(R.id.female).setEnabled(true);
    }

    /**
     * insert data to database
     */
    public void Insert_DB(int userIndex,
                          String firstName,
                          String lastName,
                          String userName,
                          String passWord,
                          String emailInput,
                          String birthDay,
                          boolean isMan ) {
        try {
            Reviewer r = new Reviewer(userIndex, userName, passWord, emailInput, isMan, firstName, lastName, birthDay);
            mReviewerTable.insert(r);
            Toast.makeText(getBaseContext(), "Success, Let Login", Toast.LENGTH_LONG).show();
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        } catch (Exception exc) {
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            Toast.makeText(getBaseContext(), "Failed", Toast.LENGTH_SHORT).show();
            set_enable_for_ReEdit();
        }

    }

    /**
     * Init and check input data
     */
    public void Insert_Data_to_DB(String first_name_Input,
                                  String surname_Input,
                                  String username_Input,
                                  String password_Input,
                                  String email_Input,
                                  String birthday_Input,
                                  boolean isMan) {
        try {
            mReviewerTable.execute(new TableQueryCallback<Reviewer>() {
                @Override
                public void onCompleted(List<Reviewer> list, int i, Exception e, ServiceFilterResponse serviceFilterResponse) {

                    int userIndex = list.size() + 1;

                    // check username and email is exist in database
                    // if every thing is OK let upload to database
                    if (!Email_isExist(list, email_Input) && !UserName_isExist(list, username_Input))
                        Insert_DB(userIndex, first_name_Input, surname_Input, username_Input, password_Input, email_Input, birthday_Input, isMan);
                    else {
                        // set text input available again
                        set_enable_for_ReEdit();
                        findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);
                    }
                }
            });
            mReviewerTable = mClient.getTable(Reviewer.class); // what is here??
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This function check username is exist in database
     * return true if username exist
     */
    public boolean UserName_isExist(List<Reviewer> list, String userName) {
        // Check exist email or username
        for (int j = 0; j < list.size(); j++) {
            Reviewer temp = list.get(j);
            if (temp.getUserName().equals(userName)) {
                Toast.makeText(getBaseContext(), "Existed username", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    /**
     * This function check email is exist in database
     * return true if email exits
     */
    public boolean Email_isExist(List<Reviewer> list, String emailInput) {
        for (int j = 0; j < list.size(); j++) {
            Reviewer temp = list.get(j);
            if (temp.getEmail().equals(emailInput)) {
                Toast.makeText(getBaseContext(), "Existed email", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    //---Back to Main if press back
    @Override
    public void onBackPressed() {
        Intent back_to_Login = new Intent(this, MainActivity.class);
        startActivity(back_to_Login);
    }

}
