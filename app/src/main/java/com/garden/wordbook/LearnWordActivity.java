package com.garden.wordbook;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class LearnWordActivity extends BaseActivity {
    String level;
    int length;
    EditText number;
    Boolean toggle = false;
    int index = 0, cntO, cntX;
    TextView resultO, resultX;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        LinearLayout list, add, option;
        final RadioButton selAll, selPer, selLack;
        final RelativeLayout layout_select, layout_learn, layout_result;
        LinearLayout stop;
        final TextView viewRemain, viewWord;
        Button start, btnO, btnX;

        list = (LinearLayout) findViewById(R.id.list);
        add = (LinearLayout) findViewById(R.id.add);
        option = (LinearLayout) findViewById(R.id.option);
        selAll = (RadioButton) findViewById(R.id.selAll);
        selPer = (RadioButton) findViewById(R.id.selPer);
        selLack = (RadioButton) findViewById(R.id.selLack);
        start = (Button) findViewById(R.id.start);
        layout_select = (RelativeLayout) findViewById(R.id.layout_select);
        layout_learn = (RelativeLayout) findViewById(R.id.layout_learn);
        layout_result = (RelativeLayout) findViewById(R.id.layout_result);
        stop = (LinearLayout) findViewById(R.id.stop);
        viewRemain = (TextView) findViewById(R.id.viewRemain);
        viewWord = (TextView) findViewById(R.id.viewWord);
        resultO = (TextView) findViewById(R.id.resultO);
        resultX = (TextView) findViewById(R.id.resultX);
        btnO = (Button) findViewById(R.id.btnO);
        btnX = (Button) findViewById(R.id.btnX);
        number = (EditText) findViewById(R.id.number);

        final ArrayList<String> word = new ArrayList<String>();
        final ArrayList<String> mean = new ArrayList<String>();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selAll.isChecked()) level = "all";
                else if(selPer.isChecked()) level = "perfect";
                else if(selLack.isChecked()) level = "lack";

                LoadList(word, mean);

                if(length>0) {
                    layout_select.setVisibility(View.GONE);
                    layout_learn.setVisibility(View.VISIBLE);
                    viewWord.setText(word.get(0));
                    viewRemain.setText(1 + "/" + length);
                }
                else{
                    Toast.makeText(getApplicationContext(), "학습할 단어가 없습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        layout_learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggle)
                {
                    viewWord.setText(word.get(index));
                    toggle = false;
                }
                else
                {
                    viewWord.setText(mean.get(index));
                    toggle = true;
                }
            }
        });

        btnO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("INSERT OR REPLACE INTO wordTBL VALUES('" + word.get(index) + "', '" + mean.get(index) + "', '" + "perfect" + "');");
                sqlDB.close();
                index++;
                cntO++;

                if(index == length)
                {
                    layout_learn.setVisibility(View.GONE);
                    layout_result.setVisibility(View.VISIBLE);

                    resultO.setText("맞은 단어 : " + cntO);
                    resultX.setText("틀린 단어 : " + cntX);

                    index = 0;
                    cntX = 0;
                    cntO = 0;

                    word.clear();
                    mean.clear();
                }
                else {
                    viewWord.setText(word.get(index));
                    toggle = false;
                    viewRemain.setText(index + 1 + "/" + length);
                }
            }
        });

        btnX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("INSERT OR REPLACE INTO wordTBL VALUES('" + word.get(index) + "', '" + mean.get(index) + "', '" + "lack" + "');");
                sqlDB.close();
                index++;
                cntX++;

                if(index == length)
                {
                    layout_learn.setVisibility(View.GONE);
                    layout_result.setVisibility(View.VISIBLE);

                    resultO.setText("맞은 단어 : " + cntO);
                    resultX.setText("틀린 단어 : " + cntX);

                    index = 0;
                    cntX = 0;
                    cntO = 0;

                    word.clear();
                    mean.clear();
                }
                else {
                    viewWord.setText(word.get(index));
                    toggle = false;
                    viewRemain.setText(index + 1 + "/" + length);
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_learn.setVisibility(View.GONE);
                layout_result.setVisibility(View.VISIBLE);

                resultO.setText("맞은 단어 : " + cntO);
                resultX.setText("틀린 단어 : " + cntX);

                index = 0;
                cntX = 0;
                cntO = 0;

                word.clear();
                mean.clear();
            }
        });

        layout_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_result.setVisibility(View.GONE);
                layout_select.setVisibility(View.VISIBLE);
            }
        });

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddWordActivity.class);
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
    public void LoadList(final ArrayList<String> word, final ArrayList<String> mean){
        String sqlSelect = null;

        if(level.equals("all")) sqlSelect = "SELECT * FROM wordTBL";
        else sqlSelect = "SELECT * FROM wordTBL WHERE gLevel=" + "'" + level + "'";

        myHelper = new myDBHelper(this);
        int cnt = 0;
        sqlDB = myHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery(sqlSelect, null);
        while (cursor.moveToNext()) {
            word.add(cursor.getString(0));
            mean.add(cursor.getString(1));
            cnt++;
        }
        sqlDB.close();
        if(number.getText().toString().equals("")) length = cnt;
        else{
            if(Integer.parseInt(number.getText().toString())<cnt) length = Integer.parseInt(number.getText().toString());
            else length = cnt;
        }
    }
}
