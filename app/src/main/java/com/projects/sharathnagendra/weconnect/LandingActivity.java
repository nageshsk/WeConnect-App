package com.projects.sharathnagendra.weconnect;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.AIService;
import ai.api.AIServiceException;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;

/**
 * Created by Sharath Nagendra on 10/22/2016.
 */

public class LandingActivity extends Activity {

    private AIService aiService;
    private AIDataService mAIDataService;
    private AIRequest mAIRequest;
    private Button mButtonChat;
    private EditText mEditTextChat;
    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_activity);

        final AIConfiguration config = new AIConfiguration("0dbb519dd0b64841b71a6048a10f8d1e",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        mAIDataService = new AIDataService(this, config);

        mAIRequest = new AIRequest();

        mButtonChat = (Button) findViewById(R.id.button_chat);

        mEditTextChat = (EditText) findViewById(R.id.editTextChat1);

        mLinearLayout = (LinearLayout) findViewById(R.id.liner_chat_layout);

        mButtonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiCall();
            }
        });

    }

    private void createElementsForSearch(AIResponse aiResponse){
        String sentText = mEditTextChat.getText().toString();
        String response = aiResponse.getResult().getFulfillment().getSpeech();

        int padding_in_dp = 30;  // 6 dps
        final float scale = getResources().getDisplayMetrics().density;
        int padding_in_px = (int) (padding_in_dp * scale + 0.5f);

        mLinearLayout.removeView(mEditTextChat);
        mLinearLayout.removeView(mButtonChat);

        TextView textView = new TextView(this);
        textView.setText(sentText);
        textView.setGravity(Gravity.RIGHT);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        textView.setPadding(0,0,padding_in_px,padding_in_px/2);
        textView.setBackgroundResource(R.mipmap.in_message_bg);

        mLinearLayout.addView(textView);

        textView = new TextView(this);
        textView.setText(response);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        textView.setPadding(padding_in_px,0,0,padding_in_px/2);
        textView.setBackgroundResource(R.mipmap.out_message_bg);

        mLinearLayout.addView(textView);

        mEditTextChat = new EditText(this);

        mLinearLayout.addView(mEditTextChat);

        mButtonChat = new Button(this);
        mButtonChat.setText("Search");

        mButtonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiCall();
            }
        });

        mLinearLayout.addView(mButtonChat);

    }

    private void apiCall(){

        String query = mEditTextChat.getText().toString();

        mAIRequest.setQuery(query);

        final Activity context = this;

        new AsyncTask<AIRequest, Void, AIResponse>() {
            @Override
            protected AIResponse doInBackground(AIRequest... requests) {
                final AIRequest request = requests[0];
                try {
                    final AIResponse response = mAIDataService.request(mAIRequest);
                    return response;
                } catch (AIServiceException e) {
                }
                return null;
            }
            @Override
            protected void onPostExecute(AIResponse aiResponse) {
                if (aiResponse != null) {
                    // process aiResponse here
                    System.out.print(aiResponse.toString());

                    if(aiResponse.getResult().getFulfillment().getSpeech().equals("donewithresults")){
                        displayResults(aiResponse);
                    } else {
                        createElementsForSearch(aiResponse);
                    }

                }
            }
        }.execute(mAIRequest);
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;


    }

    private void displayResults(AIResponse aiResponse) {

        System.out.println(aiResponse);

        if(aiResponse.getResult().getAction().equals("find.volunteers")) {

            String message = aiResponse.getResult().getParameters().get("cause").getAsString();

            String urls = "https://weconnect-imnikhil.c9users.io/api/volunteer/"+message;
            //String urls = "http://requestb.in/1na9stl1";

            new AsyncTask<String, Void, String>() {
                @Override
                protected String doInBackground(String... requests) {
                    final String url = requests[0];
                    InputStream inputStream = null;
                    String result = "";
                    try {
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpGet httpget = new HttpGet(url);
                        httpget.setHeader("Content-type", "application/json");
                        HttpResponse httpResponse = httpclient.execute(httpget);
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
                @Override
                protected void onPostExecute(String response) {
                    if (response != null) {
                        // process aiResponse here
                        System.out.print(response.toString());
                    }
                }
            }.execute(urls);

        } else if (aiResponse.getResult().getAction().equals("find.donors")) {

            String message = aiResponse.getResult().getParameters().get("cause").getAsString();

            String urls = "https://weconnect-imnikhil.c9users.io/api/donor/"+message;
            //String urls = "http://requestb.in/1na9stl1";

            new AsyncTask<String, Void, String>() {
                @Override
                protected String doInBackground(String... requests) {
                    final String url = requests[0];
                    InputStream inputStream = null;
                    String result = "";
                    try {
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpGet httpget = new HttpGet(url);
                        httpget.setHeader("Content-type", "application/json");
                        HttpResponse httpResponse = httpclient.execute(httpget);
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
                @Override
                protected void onPostExecute(String response) {
                    if (response != null) {
                        // process aiResponse here
                        System.out.println("this is a string to check");
                        System.out.println(response.toString());
                    }
                }

            }.execute(urls);

        }
    }
}
