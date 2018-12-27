package com.example.mgh.diary;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener {
    public static ArrayList<info> info = new ArrayList<>();
    private ListView mlistview;
    public static SQLiteDatabase db;
    public Button buttonHide;
    public Button btn_search;
    public ImageView iv;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        info = new ArrayList<>();
        editText = (EditText) findViewById(R.id.editText);
        btn_search = (Button) findViewById(R.id.btn_search);
        buttonHide = (Button) findViewById(R.id.buttonHide);
        iv = (ImageView) findViewById(R.id.imageView);
        buttonHide.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        iv.setOnClickListener(this);


        mlistview = (ListView) findViewById(R.id.mlistview);
        db = new MyHelper(this).getWritableDatabase();
        Cursor cursor = db.query("diary", null, null,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
// 遍历Cursor对象，取出数据
                info.add(new info(cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("content")),
                                cursor.getInt(cursor.getColumnIndex("_id"))));
            } while (cursor.moveToNext());
        }

        db.close();
        updateAdapter();


    }

    public void updateAdapter() {
        MyAdapter myAdapter = new MyAdapter();
        mlistview.setAdapter(myAdapter);
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, Show_diary.class);
                intent.putExtra("id", i);
                db = new MyHelper(MainActivity.this).getWritableDatabase();
                startActivity(intent);

            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView:
                Intent intent = new Intent(this, Add_diary.class);
                intent.putExtra("id", info.size());
                startActivity(intent);
            case R.id.btn_search:
                info = new ArrayList<>();

                buttonHide.setVisibility(View.VISIBLE);
                db = new MyHelper(this).getWritableDatabase();
                Cursor cursor = db.query("diary", new String[]{"_id,name,content"},
                        "name like ?", new String[]{"%"+editText.getText().toString()+"%"}, null, null,
                        null, null);
                if (cursor.moveToFirst()) {
                    do {
// 遍历Cursor对象，取出数据
                        info.add(new info(cursor.getString(cursor.getColumnIndex("name")),
                                cursor.getString(cursor.getColumnIndex("content")),
                                cursor.getInt(cursor.getColumnIndex("_id"))));
                    } while (cursor.moveToNext());
                }

                db.close();
                updateAdapter();
                break;
            case R.id.buttonHide:
                buttonHide.setVisibility(View.GONE);
                init();
                break;
        }
    }

    class MyAdapter extends BaseAdapter {

        public MyAdapter() {
            super();
        }

        public int getCount() {
            return info.size();
        }

        @Override
        public Object getItem(int i) {
            return info.get(i).name;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewholder;
            if (view == null) {
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.for_list, viewGroup, false);
                viewholder = new ViewHolder();
                viewholder.tv = (TextView) view.findViewById(R.id.name);
            } else {
                viewholder = (ViewHolder) view.getTag();
            }

            viewholder.tv.setText(String.valueOf(i + 1) + ": " + info.get(i).name);
            viewholder.tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

            return view;
        }

        class ViewHolder {
            TextView tv;
        }


    }

    class info {
        String name;
        String content;
        int id;

        public info(String name, String content, int id) {
            this.name = name;
            this.content = content;
            this.id = id;
        }
    }
}
