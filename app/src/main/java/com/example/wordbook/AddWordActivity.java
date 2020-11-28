package com.example.wordbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AddWordActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addword);

        Button list, option, btnAdd;
        final EditText word, mean;

        list = (Button) findViewById(R.id.list);
        option = (Button) findViewById(R.id.option);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        word = (EditText) findViewById(R.id.word);
        mean = (EditText) findViewById(R.id.mean);

        myHelper = new myDBHelper(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            String textword, textmean;
            @Override
            public void onClick(View v) {
                textword = word.getText().toString();
                textmean = mean.getText().toString();

                if(textword.equals("") || textmean.equals("")) {
                    Toast.makeText(getApplicationContext(), "단어와 의미를 모두 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else{
                    sqlDB = myHelper.getWritableDatabase();
                    sqlDB.execSQL("INSERT OR REPLACE INTO wordTBL VALUES('" + textword + "', '" + textmean + "');");
                    sqlDB.close();
                    word.setText("");
                    mean.setText("");
                }
            }
        });

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OptionActivity.class);
                startActivity(intent);
            }
        });
    }
}
