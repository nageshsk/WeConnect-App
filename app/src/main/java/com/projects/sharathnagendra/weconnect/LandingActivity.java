package com.projects.sharathnagendra.weconnect;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

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

        mLinearLayout.removeView(mEditTextChat);
        mLinearLayout.removeView(mButtonChat);

        TextView textView = new TextView(this);
        textView.setText(sentText);
        textView.setGravity(Gravity.RIGHT);

        mLinearLayout.addView(textView);

        textView = new TextView(this);
        textView.setText(response);

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

    private void displayResults(AIResponse aiResponse) {
        Toast.makeText(this, "Will make a new intent later. Time for a new BEER!!!!", Toast.LENGTH_LONG).show();
    }
}
