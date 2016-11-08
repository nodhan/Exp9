package com.nodhan.exp9;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        int selection = getIntent().getIntExtra("selection", -1);

        if (selection != -1) {

            SharedPreferences preferences = getSharedPreferences("students", MODE_PRIVATE);

            String name = preferences.getString("name" + selection, "");
            if (name.equals("")) {
                finish();
            }
            ((TextView) findViewById(R.id.id)).setText(preferences.getString("id" + selection, ""));
            ((TextView) findViewById(R.id.name)).setText(name);
            ((TextView) findViewById(R.id.department)).setText(preferences.getString("department" + selection, ""));
            ((TextView) findViewById(R.id.mentor)).setText(preferences.getString("mentor" + selection, ""));
            ((TextView) findViewById(R.id.contact)).setText(preferences.getString("contact" + selection, ""));

        } else {
            finish();
        }

    }
}
