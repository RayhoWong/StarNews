package com.rayho.tsxiu.module_news.viewmodel;

import androidx.databinding.ObservableField;

/**
 * Created by Rayho on 2019/3/16
 * ScannerResultActivity的ViewModel
 **/
public class ScannerResultViewModel{

    public ObservableField<String> title = new ObservableField<>();

    public ScannerResultViewModel(String title){
        this.title.set(title);
    }

}
