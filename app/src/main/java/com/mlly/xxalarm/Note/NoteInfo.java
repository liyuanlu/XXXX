package com.mlly.xxalarm.Note;

/**
 * Created by liyuanlu on 2019/1/5.
 */
public class NoteInfo {

    public static final int CREATE_NEW_NOTE_CODE = 0;          //创建新的便签的请求值

    public static final int EDIT_NOTE_CODE = 1;                //编辑便签的请求值

    private String content;

    private String setTime;

    private String code;

    public NoteInfo(String content,String setTime) {
        this.content = content;
        this.setTime = setTime;
    }

    public String getContent() {
        return content;
    }

    public String getSetTime() {
        return setTime;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
