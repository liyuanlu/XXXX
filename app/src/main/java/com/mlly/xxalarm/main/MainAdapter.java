package com.mlly.xxalarm.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mlly.xxalarm.R;

import java.util.ArrayList;

/**
 * Created by liyuanlu on 2019/1/8.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainHolder> implements View.OnClickListener{

    private ArrayList<Info> mInfos;

    private MainHolder mHoloder;

    private Context context;

    private RecyclerViewOnItemClickListener listener;

    public MainAdapter(ArrayList<Info> mInfos, Context context) {
        this.mInfos = mInfos;
        this.context = context;
    }

    @NonNull
    @Override
    public MainHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_main,viewGroup,false);
        mHoloder = new MainHolder(view);
        mHoloder.root = view;
        mHoloder.root.setOnClickListener(this);
        return mHoloder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainHolder mainHolder, int i) {
        mHoloder.setData(mInfos.get(i));
        mainHolder.root.setTag(i);

    }

    @Override
    public int getItemCount() {
        return mInfos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onClick(View view) {
        if (listener != null){
            listener.OnItemClickListener(view,(Integer)view.getTag());
        }
    }

    public void setListener(RecyclerViewOnItemClickListener listener) {
        this.listener = listener;
    }

    public interface RecyclerViewOnItemClickListener{
        void OnItemClickListener(View view,int position);
    }

    class MainHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        TextView textView;

        View root;

        public MainHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.item_main_icon);
            textView = (TextView)itemView.findViewById(R.id.item_main_text);
        }

        public void setData(Info info){
            imageView.setImageResource(info.getIcon());
            textView.setText(info.getText());
        }
    }
}

