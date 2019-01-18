package com.rayho.tsxiu.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rayho.tsxiu.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rayho on 2019/1/14
 **/
public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {
    private List<String> mList;

    public TestAdapter(List<String> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test, parent, false);
        TestViewHolder holder = new TestViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        holder.mTv.setText(mList.get(position));
    }


    @Override
    public int getItemCount() {
        return mList.size() == 0 ? 0 : mList.size();
    }

    public static class TestViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv)
        TextView mTv;

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
