package com.projects.sharathnagendra.weconnect;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Sharath Nagendra on 10/23/2016.
 */

public class SupporterLoginSuccess extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supporter_login_success);
        TextView textView=(TextView) findViewById(R.id.tv);
        textView.setText("You have successfully enrolled");
    }
}
