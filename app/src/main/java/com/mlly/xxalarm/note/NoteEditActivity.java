package com.mlly.xxalarm.note;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.mlly.xxalarm.R;

import java.util.Objects;

public class NoteEditActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mNoteEdit;
    private int mType = NoteInfo.CREATE_NEW_NOTE_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        setFlag();
        setToolBar();
        initView();
        getOldContent();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setFlag() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }
    /**
     * 设置Toolbar
     */
    private void setToolBar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.note_edit_toolbar);
        setSupportActionBar(toolbar);                             //使用ToolBar替换AppBar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getSupportActionBar().setDisplayShowTitleEnabled(false);    //禁止显示标题
    }

    /**
     * 从上一个活动获取原始内容
     */
    private void getOldContent(){
        String content;
        Intent intent = getIntent();
        if (intent != null){
            content = intent.getStringExtra("old_content");
            mNoteEdit.setText(content);
            mType = NoteInfo.EDIT_NOTE_CODE;
        }
    }

    private void initView() {
        mNoteEdit = (EditText) findViewById(R.id.note_edit);
        ImageView mCommit = (ImageView) findViewById(R.id.commit);
        mCommit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commit:
                commit(mType);
                break;
                default:break;
        }
    }

    /**
     * 向上一个活动返回输入的内容
     */
    private void commit(int type){
        String content = mNoteEdit.getText().toString();
        Intent intent = new Intent();
        intent.putExtra("content",content);
        setResult(type,intent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
