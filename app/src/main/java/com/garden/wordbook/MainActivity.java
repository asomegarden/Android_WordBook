package com.garden.wordbook;

import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    Boolean toggle = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button search;
        LinearLayout add, option, learn;
        final AutoCompleteTextView auto = (AutoCompleteTextView) findViewById(R.id.auto);

        add = (LinearLayout) findViewById(R.id.add);
        option = (LinearLayout) findViewById(R.id.option);
        learn = (LinearLayout) findViewById(R.id.learn);
        search = (Button) findViewById(R.id.search);

        final ArrayList<String> items = new ArrayList<String>();
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        final ListView listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);

        final ArrayList<String> searchItems = new ArrayList<String>();
        final ArrayAdapter adapter_search = new ArrayAdapter(this, R.layout.custom_dropdown_item, searchItems);
        auto.setAdapter(adapter_search);

        LoadList(items, adapter, auto);
        SetAuto(searchItems, adapter_search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadList(items, adapter, auto);
            }
        });

        AdapterView.OnItemClickListener OICL = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(toggle) toggle = false;
                else toggle = true;
                LoadList(items, adapter, auto);
                if(items.get(position).equals("단어를 추가해보세요"))
                {
                    Intent intent = new Intent(getApplicationContext(), AddWordActivity.class);
                    startActivity(intent);
                }
            }
        };

        AdapterView.OnItemLongClickListener OILCL = new AdapterView.OnItemLongClickListener() {
            String str;
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);

                ad.setTitle("확인");       // 제목 설정
                ad.setMessage("단어가 삭제됩니다");   // 내용 설정

                ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        str = items.get(position);
                        sqlDB = myHelper.getWritableDatabase();
                        String deleteQuery = "DELETE FROM wordTBL WHERE gWord='" + str + "'";
                        sqlDB.execSQL(deleteQuery);
                        deleteQuery = "DELETE FROM wordTBL WHERE gMean='" + str + "'";
                        sqlDB.execSQL(deleteQuery);
                        items.remove(position);
                        adapter.notifyDataSetChanged();
                        sqlDB.close();
                        SetAuto(searchItems, adapter_search);
                    }
                });

                // 취소 버튼 설정
                ad.setNegativeButton("No", null);
                ad.show();
                return false;
            }
        };

        listview.setOnItemLongClickListener(OILCL);
        listview.setOnItemClickListener(OICL);

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
        learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LearnWordActivity.class);
                startActivity(intent);
            }
        });
    }
    public void LoadList(final ArrayList<String> items, final ArrayAdapter adapter, final AutoCompleteTextView auto){
        String str = auto.getText().toString();
        String sqlSelect;
        adapter.clear();
        Boolean check = false;
        if(str.equals("")){
            sqlSelect = "SELECT * FROM wordTBL";
        }
        else{
            sqlSelect = "SELECT * FROM wordTBL WHERE gWord=" + "'" + auto.getText().toString() + "'";
            check = true;
        }
        myHelper = new myDBHelper(this);
        if(toggle) {
            int index = 0;
            sqlDB = myHelper.getReadableDatabase();
            Cursor cursor;
            cursor = sqlDB.rawQuery(sqlSelect, null);
            while (cursor.moveToNext()) {
                items.add(cursor.getString(0));
                index++;
            }
            if (index == 0) {
                if(check){
                    sqlSelect = "SELECT * FROM wordTBL WHERE gMean=" + "'" + auto.getText().toString() + "'";
                    cursor = sqlDB.rawQuery(sqlSelect, null);
                    while (cursor.moveToNext()) {
                        items.add(cursor.getString(0));
                        index++;
                    }
                    if(index == 0) items.add("단어를 추가해보세요");
                }
                else items.add("단어를 추가해보세요");
            }
        }
        else{
            int index = 0;
            sqlDB = myHelper.getReadableDatabase();
            Cursor cursor;
            cursor = sqlDB.rawQuery(sqlSelect, null);
            while (cursor.moveToNext()) {
                items.add(cursor.getString(1));
                index++;
            }
            if (index == 0) {
                if(check){
                    sqlSelect = "SELECT * FROM wordTBL WHERE gMean=" + "'" + auto.getText().toString() + "'";
                    cursor = sqlDB.rawQuery(sqlSelect, null);
                    while (cursor.moveToNext()) {
                        items.add(cursor.getString(1));
                        index++;
                    }
                    if(index == 0) items.add("단어를 추가해보세요");
                }
                else items.add("단어를 추가해보세요");
            }
        }
        adapter.notifyDataSetChanged();
        sqlDB.close();
    }
    public void SetAuto(final ArrayList<String> searchItems, final ArrayAdapter adapter_search) {
        searchItems.clear();
        myHelper = new myDBHelper(this);
        sqlDB = myHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM wordTBL", null);
        while (cursor.moveToNext()) {
            searchItems.add(cursor.getString(0));
            searchItems.add(cursor.getString(1));
        }
        sqlDB.close();
        adapter_search.notifyDataSetChanged();
    }
}