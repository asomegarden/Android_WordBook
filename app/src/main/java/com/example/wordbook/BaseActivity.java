package com.example.wordbook;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class BaseActivity extends AppCompatActivity {
    myDBHelper myHelper;
    SQLiteDatabase sqlDB;
    private long PressTime = 0;
    private Toast toast;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > PressTime + 2000) {
            PressTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= PressTime + 2000) {
            ActivityCompat.finishAffinity(this);
            toast.cancel();
        }
    }
    public static class myDBHelper extends SQLiteOpenHelper { //투두 테이블, 패스워드 테이블 생성
        public myDBHelper(Context context) {
            super(context, "wordDB", null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE wordTBL (gWord String PRIMARY KEY, gMean String);");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS wordTBL");
            onCreate(db);
        }
    }
}
