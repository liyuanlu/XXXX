package com.mlly.xxalarm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mlly.xxalarm.Note.NoteInfo;
import com.mlly.xxalarm.R;

public class NoteEditActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mNoteEdit;

    private int mType = NoteInfo.CREATE_NEW_NOTE_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        initView();
        getOldContent();
    }

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
        Button mCommit = (Button) findViewById(R.id.commit);
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
}
