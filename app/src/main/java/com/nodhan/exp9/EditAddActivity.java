package com.nodhan.exp9;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditAddActivity extends AppCompatActivity {

    EditText editText[];
    SharedPreferences sharedPreferences;
    boolean flag = true;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_add);

        editText = new EditText[5];
        editText[0] = (EditText) findViewById(R.id.idAdd);
        editText[1] = (EditText) findViewById(R.id.nameAdd);
        editText[2] = (EditText) findViewById(R.id.departmentAdd);
        editText[3] = (EditText) findViewById(R.id.mentorAdd);
        editText[4] = (EditText) findViewById(R.id.contactAdd);

        boolean editFlag = getIntent().getBooleanExtra("flag", true);
        sharedPreferences = getSharedPreferences("students", MODE_PRIVATE);

        if (!editFlag) {
            int selection = getIntent().getIntExtra("selection", -1);
            editText[0].setText(sharedPreferences.getString("id" + selection, ""));
            editText[1].setText(sharedPreferences.getString("name" + selection, ""));
            editText[2].setText(sharedPreferences.getString("department" + selection, ""));
            editText[3].setText(sharedPreferences.getString("mentor" + selection, ""));
            editText[4].setText(sharedPreferences.getString("contact" + selection, ""));
        }


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkForNull(editText);

                if (flag) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    int count = sharedPreferences.getInt("count", 0);

                    editor.putString("id" + count, editText[0].getText().toString());
                    editor.putString("name" + count, editText[1].getText().toString());
                    editor.putString("department" + count, editText[2].getText().toString());
                    editor.putString("mentor" + count, editText[3].getText().toString());
                    editor.putString("contact" + count, editText[4].getText().toString());
                    editor.putInt("count", ++count);

                    editor.apply();

                    Toast.makeText(getApplicationContext(), "Data has been saved!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

    private void checkForNull(EditText[] editTexts) {
        for (EditText editText : editTexts)
            if (editText.getText().toString().trim().length() < 0) {
                flag = false;
                editText.setError("Cannot be empty!!");
            }
    }
}