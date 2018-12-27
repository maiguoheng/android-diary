package com.example.mgh.diary;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Add_diary extends Activity {
    public int id;
    private EditText name;
    private EditText diary;
    private Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_diary);
        name = (EditText) findViewById(R.id.diary_name);
        diary = (EditText) findViewById(R.id.diary);
        btn = (Button) findViewById(R.id.submit);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);

    }

    public void sub(View v) {
        ContentValues values = new ContentValues();
        values.put("name", name.getText().toString());
        values.put("content", diary.getText().toString());
        MainActivity.db = new MyHelper(this).getWritableDatabase();

        MainActivity.db.insert("diary", null, values);
        MainActivity.db.close();

        Toast.makeText(this, "日志保存成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
