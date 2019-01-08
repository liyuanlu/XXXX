package com.mlly.xxalarm.note.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mlly.xxalarm.R;
import com.mlly.xxalarm.note.NoteInfo;

import java.util.ArrayList;
import java.util.Objects;

/**
 * 便签RecyclerView适配器类
 * Created by liyuanlu on 2019/1/5.
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> implements
                                                View.OnClickListener,View.OnLongClickListener {

    private ArrayList<NoteInfo> mNoteInfos;

    private Context mContext;

    private NoteViewHolder mViewHolder;

    private RecyclerViewOnItemClickListner clickListner;                //点击事件监听器

    private RecyclerViewOnItemLongClickListener longClickListener;      //长按事件监听器

    private static final int BACKGROUND_COUNT = 4;

    public NoteAdapter(ArrayList<NoteInfo> mNoteInfos, Context mContext) {
        this.mNoteInfos = mNoteInfos;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mViewHolder = new NoteViewHolder(LayoutInflater.from(mContext).inflate(
                R.layout.item_note,viewGroup,false
        ));
        mViewHolder.root.setOnClickListener(this);
        mViewHolder.root.setOnLongClickListener(this);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i) {
        mViewHolder.setData(mNoteInfos.get(i));
        mViewHolder.root.setTag(i);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mNoteInfos.size();
    }

    /**
     * 在列表中移除item
     * @param position 需要移除的item在列表中的位置
     */
    public void remove(int position){
        mNoteInfos.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,mNoteInfos.size() - position);
    }

    /**
     * 在列表中添加item
     * @param info 需要添加的item对象
     */
    public void add(NoteInfo info){
        mNoteInfos.add(mNoteInfos.size(),info);
        notifyItemInserted(mNoteInfos.size());
        notifyItemRangeChanged(mNoteInfos.size(),mNoteInfos.size());
    }

    public String getItemCode(RecyclerView recyclerView,int position){
        int now = mNoteInfos.size() - 1;
        NoteViewHolder holder = (NoteViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        return Objects.requireNonNull(holder).code;
    }

    @Override
    public void onClick(View view) {
        if (clickListner != null){
            clickListner.OnItemClickListener(view,(Integer)view.getTag());
        }
    }

    @Override
    public boolean onLongClick(View view) {
        return longClickListener != null && longClickListener.OnItemLongClickListener(view,(Integer)view.getTag());
    }

    /**
     * 设置点击事件监听器
     * @param listner 点击事件监听器
     */
    public void setClickListner(RecyclerViewOnItemClickListner listner){
        this.clickListner = listner;
    }

    /**
     * 设置长按事件监听器
     * @param listener 长按事件监听器
     */
    public void setLongClickListener(RecyclerViewOnItemLongClickListener listener){
        this.longClickListener = listener;
    }

    /**
     * 点击事件监听接口
     */
    public interface RecyclerViewOnItemClickListner{
        void OnItemClickListener(View view,int position);
    }

    /**
     * 长按事件监听接口
     */
    public interface RecyclerViewOnItemLongClickListener{
        boolean OnItemLongClickListener(View view,int position);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView mNoteContent;

        TextView mNoteSetTime;

        String code;

        View root;                  //该Item

        private NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            mNoteContent = (TextView)itemView.findViewById(R.id.note_content);
            mNoteSetTime = (TextView)itemView.findViewById(R.id.note_set_time);
            root = itemView;
        }

        public void setData(NoteInfo noteInfo){
            mNoteContent.setText(noteInfo.getContent());
            mNoteSetTime.setText(noteInfo.getSetTime());
            code = noteInfo.getCode();
        }
    }
}
