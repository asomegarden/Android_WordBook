package com.garden.wordbook;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AddWordActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addword);

        Button btnAdd, btnSearch;
        LinearLayout list, option, learn;
        final EditText word, mean;

        list = (LinearLayout) findViewById(R.id.list);
        option = (LinearLayout) findViewById(R.id.option);
        learn = (LinearLayout) findViewById(R.id.learn);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        word = (EditText) findViewById(R.id.word);
        mean = (EditText) findViewById(R.id.mean);
        btnSearch = (Button) findViewById(R.id.btnSearch);

        myHelper = new myDBHelper(this);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(word.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "먼저 내용을 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                    intent.putExtra(SearchManager.QUERY, word.getText().toString() + " 뜻");
                    startActivity(intent);
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            String textword, textmean;
            @Override
            public void onClick(View v) {
                textword = word.getText().toString();
                textmean = mean.getText().toString();
                String level = "lack";

                if(textword.equals("") || textmean.equals("")) {
                    Toast.makeText(getApplicationContext(), "단어와 의미를 모두 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else{
                    sqlDB = myHelper.getWritableDatabase();
                    sqlDB.execSQL("INSERT OR REPLACE INTO wordTBL VALUES('" + textword + "', '" + textmean + "', '" + level + "');");
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
        learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LearnWordActivity.class);
                startActivity(intent);
            }
        });
    }
}
