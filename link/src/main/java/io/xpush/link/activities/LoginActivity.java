package io.xpush.link.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.xpush.chat.core.CallbackEvent;
import io.xpush.chat.core.XPushCore;
import io.xpush.chat.network.LoginRequest;
import io.xpush.link.R;

public class LoginActivity extends AppCompatActivity  {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int REQUEST_SIGNUP = 100;

    @Bind(R.id.input_id)
    EditText mIdText;

    @Bind(R.id.input_password)
    EditText mPasswordText;

    @OnClick(R.id.link_signup)
    public void signUp() {
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivityForResult(intent, REQUEST_SIGNUP);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
    }

    @Bind(R.id.btn_login)
    Button mLoginButton;

    @OnClick(R.id.btn_login)
    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        mLoginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String id = mIdText.getText().toString();
        String password = mPasswordText.getText().toString();

        XPushCore.getInstance().login(id, password, new CallbackEvent() {
            @Override
            public void call(Object... args) {
                if (args == null || args.length == 0) {
                    progressDialog.dismiss();
                    onLoginSuccess();
                } else {
                    progressDialog.dismiss();
                    onLoginFailed();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getBaseContext(), "Signup success", Toast.LENGTH_LONG).show();
                //this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        mLoginButton.setEnabled(true);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        Intent intent = null;
        if( pref.getBoolean("SITE_READY", false) ) {
            intent = new Intent(LoginActivity.this, MainActivity.class);
        } else {
            intent = new Intent(LoginActivity.this, UnreadyActivity.class);
        }
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        mLoginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String id = mIdText.getText().toString();
        String password = mPasswordText.getText().toString();

        if (id.isEmpty() || id.length() < 4 || id.length() > 10) {
            mIdText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            mIdText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mPasswordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            mPasswordText.setError(null);
        }

        return valid;
    }
}