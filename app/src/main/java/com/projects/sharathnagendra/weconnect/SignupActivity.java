package com.projects.sharathnagendra.weconnect;

import android.app.Activity;

/**
 * Created by Sharath Nagendra on 10/22/2016.
 */

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ai.api.http.HttpClient;
import butterknife.ButterKnife;
import butterknife.InjectView;

import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private static String url = "";


    @InjectView(R.id.input_name)
    EditText _nameText;
    @InjectView(R.id.input_email)
    EditText _emailText;
    // @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_signup)
    Button _signupButton;
    @InjectView(R.id.link_login)
    TextView _loginLink;
    @InjectView(R.id.input_phone)
    EditText _phoneText;
    @InjectView(R.id.input_time)
    EditText _timeText;
    @InjectView(R.id.input_money)
    EditText _moneyText;
    @InjectView(R.id.input_days)
    EditText _daysText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);





        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
//        String password = _passwordText.getText().toString();
        String phone = _phoneText.getText().toString();
        String time = _timeText.getText().toString();
        String money = _moneyText.getText().toString();
        String days = _daysText.getText().toString();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
       new HttpAsyncTask().execute("https://weconnect-imnikhil.c9users.io/api/supporter");

        url = "https://weconnect-imnikhil.c9users.io/api/supporter";
    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override

        protected String doInBackground(String... urls) {

            SupporterGetterSetter supporterGetterSetter = new SupporterGetterSetter();

            supporterGetterSetter.getName();
            System.out.println(supporterGetterSetter.getName());
//                        supporterGetterSetter.setEmail(tv2.getText().toString());
//            supporterGetterSetter.setPhone(tv3.getText().toString());
//            supporterGetterSetter.setTime(tv4.getText().toString());
//            supporterGetterSetter.setMoney(tv5.getText().toString());
//            supporterGetterSetter.setDays(tv6.getText().toString());

            return POST(urls[0], supporterGetterSetter);

        }

        protected void onPostExecute(JSONObject jsonObject) {
            //System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%########"+json);

            try {
                Toast.makeText(SignupActivity.this, "successfully posted", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    public String POST(String url, SupporterGetterSetter supporterGetterSetter) {
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            org.apache.http.client.HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            JSONObject jsonObject = new JSONObject();
            //JSONObject place = new JSONObject();
            JSONObject supporter = new JSONObject();

            supporter.accumulate("name", supporterGetterSetter.getName());
            supporter.accumulate("email", supporterGetterSetter.getEmail());
            supporter.accumulate("phone", supporterGetterSetter.getPhone());
            supporter.accumulate("time", supporterGetterSetter.getTime());
            supporter.accumulate("money", supporterGetterSetter.getMoney());
            supporter.accumulate("days", supporterGetterSetter.getDays());

            json = jsonObject.toString();

            System.out.println("############################## $#######################");

            System.out.println(jsonObject);

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);
            httpPost.setHeader("Content-type", "application/json");
            //   httpPost.setHeader("token", email);

            HttpResponse httpResponse = httpclient.execute(httpPost);

            inputStream = httpResponse.getEntity().getContent();

            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

        private  String convertInputStreamToString(InputStream inputStream) throws IOException {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();
            return result;


        }
}
//finish();


    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
       // String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

//        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
//            _passwordText.setError("between 4 and 10 alphanumeric characters");
//            valid = false;
//        }
//        else {
//            _passwordText.setError(null);
//        }

        return valid;
    }
}