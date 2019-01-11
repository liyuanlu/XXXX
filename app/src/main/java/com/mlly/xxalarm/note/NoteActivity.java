package com.mlly.xxalarm.note;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import com.mlly.xxalarm.R;
import com.mlly.xxalarm.note.data.NoteAdapter;
import com.mlly.xxalarm.note.data.NoteContract;
import com.mlly.xxalarm.note.data.NoteDBHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;


public class NoteActivity extends AppCompatActivity {

    private static final int COLUMN_COUNT = 2;                  //RecyclerView列数

    private ArrayList<NoteInfo> mNoteInfos = new ArrayList<>();

    private FloatingActionButton mFloatingButton;

    private NoteAdapter mAdapter;

    private NoteDBHelper mDBHelper;

    private RecyclerView mNoteView;

    private StaggeredGridLayoutManager manager;

    private String mUpdateCode;                                 //需要更新项的识别码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSPTheme();
        setContentView(R.layout.activity_note);
        setToolBar();
        initView();
    }

    private void getSPTheme() {
        SharedPreferences sp = getSharedPreferences("save_theme",Context.MODE_PRIVATE);
        int theme = sp.getInt("color",0);
        Log.d("theme", "" + theme);
        switch (theme){
            case 0:setTheme(R.style.AppTheme);
                break;
            case 1:setTheme(R.style.BlueTheme);
                break;
            case 2:setTheme(R.style.PinkTheme);
                break;
            case 3:setTheme(R.style.GreenTheme);
                break;
            case 4:setTheme(R.style.PurpleTheme);
                break;
            default:setTheme(R.style.AppTheme);
                break;
        }
    }

    private void initView() {
        mNoteView = (RecyclerView) findViewById(R.id.note_view);
        mFloatingButton = (FloatingActionButton) findViewById(R.id.add_note);
        manager = new StaggeredGridLayoutManager(COLUMN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        mNoteView.setLayoutManager(manager);
        mAdapter = new NoteAdapter(mNoteInfos,this);
        mDBHelper = new NoteDBHelper(this,null);
        setListener();
        queryAllFromDB();
        mNoteView.setAdapter(mAdapter);
    }

    private void setToolBar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
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
     * 显示PopupMenu菜单
     * @param view 菜单显示的位置
     * @param position 需要删除的item的位置
     */
    private void showPopupMenu(View view,int position){
        PopupMenu menu = new PopupMenu(this,view);
        menu.getMenuInflater().inflate(R.menu.menu,menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.delete_item:
                        deleteFromDB(mNoteInfos.get(position).getCode());
                        mAdapter.remove(position);                  //移除该项item
                        break;
                    default:break;
                }
                return true;
            }
        });
        menu.show();                                                //显示菜单
    }

    /**
     * 为控件设置监听器
     */
    private void setListener(){
        mAdapter.setClickListner(new NoteAdapter.RecyclerViewOnItemClickListner() {
            @Override
            public void OnItemClickListener(View view, int position) {
                //跳转到便签编辑活动，携带原有内容
                Intent intent = new Intent(NoteActivity.this,NoteEditActivity.class);
                mUpdateCode = mNoteInfos.get(position).getCode();
                intent.putExtra("old_content",mNoteInfos.get(position).getContent());
                startActivityForResult(intent,NoteInfo.EDIT_NOTE_CODE);
            }
        });
        mAdapter.setLongClickListener(new NoteAdapter.RecyclerViewOnItemLongClickListener() {
            @Override
            public boolean OnItemLongClickListener(View view, int position) {
                showPopupMenu(view,position);
                return true;
            }
        });
        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到便签创建活动
                startActivityForResult(new Intent(NoteActivity.this,NoteEditActivity.class),NoteInfo.CREATE_NEW_NOTE_CODE);
            }
        });
    }

    /**
     * 用从便签编辑活动返回的内容创建NoteInfo对象
     * @param requestCode 不同活动返回的请求值
     * @param resultCode 执行结果返回值
     * @param data 返回带数据的Intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == NoteInfo.CREATE_NEW_NOTE_CODE && data != null){
            String content = data.getStringExtra("content");
            saveToDB(content);
        }else if (requestCode == NoteInfo.EDIT_NOTE_CODE && data != null){
            String content = data.getStringExtra("content");
            upDateToDB(content);
        }
    }

    /**
     * 在数据库中更新数据
     */
    private void upDateToDB(String content) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NoteContract.NoteEntry.COLUMN_NOTE_CONTENT,content);
        db.update(NoteContract.NoteEntry.TABLE_NAME,values,NoteContract.NoteEntry.COLUMN_NOTE_CODE + "=?",
                new String[]{mUpdateCode});
        mNoteInfos.clear();
        queryAllFromDB();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 将内容储存到数据库
     * @param content 需要储存的字符串
     */
    private void saveToDB(String content){
        //创建对象并添加到列表
        String setTime = getCurrentTime();
        String code = getCode();
        NoteInfo info = new NoteInfo(content,setTime);
        info.setCode(code);
        //储存到数据库
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NoteContract.NoteEntry.COLUMN_NOTE_CONTENT,content);
        values.put(NoteContract.NoteEntry.COLUMN_NOTE_SET_TIME,setTime);
        values.put(NoteContract.NoteEntry.COLUMN_NOTE_CODE,code);
        long newRowId = db.insert(NoteContract.NoteEntry.TABLE_NAME,null,values);
        mAdapter.add(info);
    }

    /**
     * 在数据库中查找所有储存的内容
     */
    private void queryAllFromDB(){
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        //在数据库中查找的条件（Column）
        String[] projection = {
                NoteContract.NoteEntry._ID,
                NoteContract.NoteEntry.COLUMN_NOTE_CODE,
                NoteContract.NoteEntry.COLUMN_NOTE_SET_TIME,
                NoteContract.NoteEntry.COLUMN_NOTE_CONTENT
        };
        //查询结果
        Cursor cursor = db.query(
                NoteContract.NoteEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        if (cursor != null){
            while (cursor.moveToNext()){
                //从表中得到该行所储存的字符串（content）
                String content = cursor.getString(cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE_CONTENT));
                String setTime = cursor.getString(cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE_SET_TIME));
                String code = cursor.getString(cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE_CODE));
                NoteInfo info = new NoteInfo(content,setTime);
                info.setCode(code);
                mNoteInfos.add(info);
            }
            cursor.close();
        }
    }
    /**
     * 在数据库中删除内容
     */
    private void deleteFromDB(String content){
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        db.delete(NoteContract.NoteEntry.TABLE_NAME,NoteContract.NoteEntry.COLUMN_NOTE_CODE + "=?",
                new String[]{content});
    }

    /**
     * 获取系统当前时间
     * @return 系统当前时间字符串
     */
    private String getCurrentTime(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" +
                calendar.get(Calendar.DAY_OF_MONTH) + "/  " + calendar.get(Calendar.HOUR_OF_DAY) +
                ":" + calendar.get(Calendar.MINUTE);
    }

    /**
     * 利用当前系统时间作为唯一识别码
     * @return 识别码
     */
    private String getCode(){
        Calendar calendar = Calendar.getInstance();
        long code = calendar.get(Calendar.MILLISECOND);
        return code + calendar.getTimeInMillis() +"";
    }
}
