package com.yonyou.zml.greendao_demo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

import db.Note;
import db.NoteDao;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText;
    private Button btn_add;
    private Button btn_query;
    private ListView listView;
    private NoteDao noteDao;
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        noteDao = ((MyApplication) getApplication()).getDaoSession().getNoteDao();
        db = ((MyApplication) getApplication()).getDb();
        //通过xxxDao.Properties.属性名.columnName 获取列名
        String textColumn = NoteDao.Properties.Text.columnName;
        String orderBy = textColumn + " COLLATE LOCALIZED ASC";
        cursor = db.query(noteDao.getTablename(), noteDao.getAllColumns(), null, null, null, null, orderBy);
        String[] form = {textColumn, NoteDao.Properties.Comment.columnName};
        int[] to = {android.R.id.text1, android.R.id.text2};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, form, to);
        listView.setAdapter(adapter);
    }

    private void initView() {
        editText = (EditText) findViewById(R.id.editText);
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_query = (Button) findViewById(R.id.btn_query);
        listView = (ListView) findViewById(R.id.listView);

        btn_add.setOnClickListener(this);
        btn_query.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                addNote();
                break;
            case R.id.btn_query:

                break;
        }
    }

    /**
     * 添加
     */
    private void addNote() {
        // validate
        String noteText = editText.getText().toString().trim();
        editText.setText("");
        if (TextUtils.isEmpty(noteText)) {
            Toast.makeText(this, "editText不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        // TODO validate success, do something
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String comment = "Added on " + df.format(new Date());
        // 插入操作，简单到只要你创建一个 Java 对象
        Note note = new Note(null,noteText,comment,new Date());
        noteDao.insert(note);
        // 遍历所有数据
        cursor.requery();

    }

}
