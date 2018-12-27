package com.example.mgh.diary;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Show_diary extends Activity {
    private EditText name;
    private EditText ed;
    int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_diary);

        name = (EditText) findViewById(R.id.name);
        ed = (EditText) findViewById(R.id.content);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);

        name.setText(MainActivity.info.get(id).name);
        ed.setText(MainActivity.info.get(id).content);
    }

    public void backandchange(View v) {

        ContentValues values=new ContentValues();
        values.put("name",name.getText().toString());
        values.put("content",ed.getText().toString());

        MainActivity.db.update("diary", values,"_id=?", new String[]{String.valueOf(MainActivity.info.get(id).id)});
        MainActivity.db.close();


        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public void del(View v) {
        MainActivity.db.delete("diary", "_id=?", new String[]{String.valueOf(MainActivity.info.get(id).id)});
        MainActivity.db.close();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
