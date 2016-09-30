package com.example.n.lotte_jjim.Activity.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.n.lotte_jjim.Activity.Application.MyApplication;
import com.example.n.lotte_jjim.Activity.Service.RegistrationIntentService;
import com.example.n.lotte_jjim.Activity.Util.HttpUtil;
import com.example.n.lotte_jjim.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;


public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, View.OnClickListener {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String REGISTRATION_READY = "registrationReady";
    private static final String REGISTRATION_GENERATING = "registrationGenerating";
    private static final String REGISTRATION_COMPLETE = "registrationComplete";

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_READ_CONTACTS = 0;
    private static final String[] DUMMY_CREDENTIALS = new String[]{
    };

    JSONArray mLoginArray;
    String loginCheck;
    String tokenCheck;
    LinearLayout mLoginLayout;

    private UserLoginTask mAuthTask = null;

    //rasp: UI
    private AutoCompleteTextView mIdView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private String target_token;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private CheckBox mAutoLoginCheck;

    SharedPreferences setting;
    SharedPreferences.Editor   editor;
    /**
     * Instance ID를 이용하여 디바이스 토큰을 가져오는 RegistrationIntentService를 실행한다.
     */
    public void getInstanceIdToken() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    public void registBroadcastReceiver(){
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if(action.equals(REGISTRATION_READY)){
                    Log.d(TAG,"Token 대기 중");
                } else if(action.equals(REGISTRATION_GENERATING)){
                    Log.d(TAG, "Token 생성 중");
                } else if(action.equals(REGISTRATION_COMPLETE)){
                    Log.d(TAG, "Token 생성완료");
                    String token = intent.getStringExtra("token");
                    target_token = token;
                    Log.d(TAG, "Token : "+token);
                }
            }
        };
    }

    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(REGISTRATION_READY));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(REGISTRATION_GENERATING));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Init_Button();
        Init_View();
        registBroadcastReceiver();
        getInstanceIdToken();
        Init_getSharedPrefernences();
    }

    public void Init_Button() {

        Button mBtn_SingIn = (Button) findViewById(R.id.mBtn_Signin);
        Button mBtn_SignUp = (Button) findViewById(R.id.mBtn_Signup);
        mLoginLayout = (LinearLayout) findViewById(R.id.email_login_form);

        mBtn_SingIn.setOnClickListener(this);
        mBtn_SignUp.setOnClickListener(this);
        mLoginLayout.setOnClickListener(this);
    }

    public void Init_View() {

        mIdView = (AutoCompleteTextView) findViewById(R.id.mText_LoginID);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.mText_LoginPassword);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mAutoLoginCheck= (CheckBox) findViewById(R.id.AutoLoginChk);
    }

    public void  Init_getSharedPrefernences(){

        setting =  getSharedPreferences("MYID", MODE_PRIVATE);
        editor= setting.edit();

        if(setting.getBoolean("AutoLogin",false) == true) {
            Intent mIntent_SignIn1 = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mIntent_SignIn1);
            finish();
        }
        else {

        }
    }


    public void  mLoginJSON() {

        mLoginArray = new JSONArray();
        loginCheck=null;
        toJSON(mLoginArray, "userID", mIdView.getText().toString());
        toJSON(mLoginArray, "userPW", mPasswordView.getText().toString());

        Log.d(TAG, mIdView.getText().toString());
        Log.d(TAG, mPasswordView.getText().toString());

        try {

            HttpUtil httpUtil = new HttpUtil("Member", "LoginRequest", mLoginArray);
            httpUtil.start();
            httpUtil.join();

            JSONArray mLoginResult = httpUtil.getResponse();
            int size = mLoginResult.length();

            for (int i = 0; i < size; i++) {
                loginCheck = mLoginResult.getJSONObject(i).getString("check");
                Log.i(TAG, "Check :" + loginCheck);
            }

        } catch (Exception e) {
            e.toString();
        }
    }

    public void mTokenJSON()
    {
        JSONArray mTokenArray = new JSONArray();
        toJSON(mTokenArray, "tokenID", target_token);
        toJSON(mTokenArray, "userID", mIdView.getText().toString());

        try {

            HttpUtil httpUtil = new HttpUtil("Member", "TokenRequest", mTokenArray);

            httpUtil.start();

            httpUtil.join();

            JSONArray mTokenResult = httpUtil.getResponse();

            int size = mTokenResult.length();

            for (int i = 0; i < size; i++) {
                tokenCheck = mTokenResult.getJSONObject(i).getString("check");
                Log.i(TAG, "Check :" + tokenCheck);

            }

        } catch (Exception e) {
            e.toString();
        }
    }

    JSONArray toJSON(JSONArray array, String type, String item){

            JSONObject obj = new JSONObject();
            try {
                obj.put(type, item);
                array = array.put(obj);
                return array;
            }catch(Exception e){
                return null;
            }
        }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.mBtn_Signin:
                //rasp: Below Code is Origin
                //attemptLogin();
                 mLoginJSON();

                if(loginCheck.equals("true")){

                    editor.putString("ID", mIdView.getText().toString());
                    editor.putString("PW", mPasswordView.getText().toString());

                    //자동로그인 체크 여부 저장
                    if(mAutoLoginCheck.isChecked()){
                        editor.putBoolean("AutoLogin", true);}
                    else{
                        editor.putBoolean("AutoLogin", false);
                    }

                    editor.commit();

                    Log.d(TAG, setting.getString("ID", ""));

                    SetCurrentUserID();
                    mTokenJSON();

                    Intent mIntent_SignIn = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mIntent_SignIn);
                    finish();
                }
                else if(loginCheck.equals("false")){
                    Toast.makeText(getApplicationContext(), "아이디 혹은 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.mBtn_Signup:

                String mURL_Members = "https://member.lpoint.com/door/user/mobile/login_common.jsp?sid=MEMBERSMBL&opentype=P&returnurl=";
                Intent mIntent_Members = new Intent(Intent.ACTION_VIEW, Uri.parse(mURL_Members));
                startActivity(mIntent_Members);

                break;

            case R.id.email_login_form:

                Log.d(TAG,"click");

                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                break;

        }
    }


    public void SetCurrentUserID() {
        //rasp: ID 받는거 확인
        MyApplication mCurrentUser = (MyApplication)getApplication();
        mCurrentUser.SetUserID(mIdView.getText().toString());

    }


    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mIdView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        mIdView.setError(null);
        mPasswordView.setError(null);

        //rasp: 로그인을 시도하는 아이디와 비밀번호 저장
        String mAttempt_ID = mIdView.getText().toString();
        String mAttempt_Password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //rasp: 비밀번호 일치여부 확인
        if (!TextUtils.isEmpty(mAttempt_Password) && !isPasswordValid(mAttempt_Password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        //rasp: 가입된 아이디 확인
        if (TextUtils.isEmpty(mAttempt_ID)) {
            mIdView.setError(getString(R.string.error_field_required));
            focusView = mIdView;
            cancel = true;
        } else if (!isEmailValid(mAttempt_ID)) {
            mIdView.setError(getString(R.string.error_invalid_email));
            focusView = mIdView;
            cancel = true;
        }

        if (cancel) {
            //rasp: 에러있을 경우
            focusView.requestFocus();
        } else {
            //rasp: 스피너를 띄우고 로그인 태스크를 수행
            showProgress(true);
            mAuthTask = new UserLoginTask(mAttempt_ID, mAttempt_Password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //rasp: 아이디 조건
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //rasp: 비밀번호 조건
        return password.length() > 4;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    //rasp: 로그인 폼 숨기고 Progress
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };
        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
        mIdView.setAdapter(adapter);
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}




