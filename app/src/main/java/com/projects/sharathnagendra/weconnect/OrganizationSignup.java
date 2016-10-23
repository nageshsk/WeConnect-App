package com.projects.sharathnagendra.weconnect;

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

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.ButterKnife;

/**
 * Created by Sharath Nagendra on 10/23/2016.
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

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import ai.api.http.HttpClient;
import butterknife.ButterKnife;
import butterknife.InjectView;

import org.apache.http.HttpResponse;
//mport org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import android.app.Activity;
public class OrganizationSignup extends Activity {

            private static final String TAG = "OrganizationSignup";
        private static String url = "";
        SupporterGetterSetter supporterGetterSetter = new SupporterGetterSetter();


        EditText _nameText;

        EditText _emailText;


        Button _signupButton;

        TextView _loginLink;

        EditText _phoneText;



        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.organization_signup);
            ButterKnife.inject(this);

            _nameText = (EditText) findViewById(R.id.input_name);
            _emailText = (EditText) findViewById(R.id.input_email);
            _phoneText = (EditText) findViewById(R.id.input_phone);

            _signupButton = (Button) findViewById(R.id.btn_signup);


            _signupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signup();
                }
            });
        }
//            _loginLink.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // Finish the registration screen and return to the Login activity
//                    finish();
//                }
//            });
//        }

        public void signup() {
            Log.d(TAG, "Signup");

            _signupButton.setEnabled(false);

            final ProgressDialog progressDialog = new ProgressDialog(com.projects.sharathnagendra.weconnect.OrganizationSignup.this,
                    R.style.AppTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();

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

        public String POST(String url, SupporterGetterSetter supporterGetterSetter) {
            InputStream inputStream = null;
            String result = "";
            try {
                // 1. create HttpClient
                org.apache.http.client.HttpClient httpclient = new DefaultHttpClient();

                // 2. make POST request to the given URL
                HttpPost httpPost = new HttpPost(url);

                String json = "";

                // JSONObject jsonObject = new JSONObject();
                //JSONObject place = new JSONObject();
                JSONObject supporter = new JSONObject();

                supporter.accumulate("name", supporterGetterSetter.getName());
                supporter.accumulate("email", supporterGetterSetter.getEmail());
                supporter.accumulate("phone", supporterGetterSetter.getPhone());

                json = supporter.toString();


                System.out.println("############################## $#######################");

                System.out.println(json);

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

            return result;
        }

        public void onSignupSuccess() {
            supporterGetterSetter.setName(_nameText.getText().toString());
            supporterGetterSetter.setEmail(_emailText.getText().toString());
            supporterGetterSetter.setPhone(_phoneText.getText().toString());
            new HttpAsyncTask().execute("https://weconnect-imnikhil.c9users.io/api/organisation");
        }

        private class HttpAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... urls) {

                return POST(urls[0], supporterGetterSetter);
            }
        }

        protected void onPostExecute(JSONObject jsonObject) {
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%########" + jsonObject);

            try {
                Toast.makeText(com.projects.sharathnagendra.weconnect.OrganizationSignup.this, "successfully registered", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String convertInputStreamToString(InputStream inputStream) throws IOException {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null)
                result += line;
            inputStream.close();
            return result;
        }

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

            return valid;
        }
    }

