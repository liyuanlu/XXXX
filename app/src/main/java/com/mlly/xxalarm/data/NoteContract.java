package com.mlly.xxalarm.data;

import android.provider.BaseColumns;

/**
 * Created by liyuanlu on 2019/1/6.
 * 便签数据库列项
 */
public class NoteContract {

    private NoteContract() {
    }

    public static final class NoteEntry implements BaseColumns {

        public static final String TABLE_NAME = "Notes";                //表名

        public static final String _ID = BaseColumns._ID;               //ID

        public static final String COLUMN_NOTE_CONTENT = "content";     //内容项

        public static final String COLUMN_NOTE_SET_TIME = "set_time";   //创建的时间

        public static final String COLUMN_NOTE_CODE = "code";           //表中的ID
    }
}
