package com.rayho.tsxiu.module_news.viewmodel;

import com.rayho.tsxiu.BR;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.BaseDataBindingApater;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.Observable;

/**
 * Created by Rayho on 2019/3/25
 * 历史记录的viewmodel
 **/
public class HistoryRecordViewModel extends BaseObservable implements BaseDataBindingApater.Items, Observable {

    private String record;

    public HistoryRecordViewModel(String record) {
        this.record = record;
    }

    @Bindable
    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
        notifyPropertyChanged(BR.record);
    }

    /**
     * 返回item的布局id
     *
     * @return
     */
    @Override
    public int getLayoutType() {
        return R.layout.item_history;
    }
}
