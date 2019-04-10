package com.rayho.tsxiu.module_photo.viewmodel;

import androidx.databinding.ObservableField;

/**
 * Created by Rayho on 2019/4/3
 * 图片详情viewmodel
 **/
public class PhotoDetailViewModel {

    public ObservableField<String> number = new ObservableField<>();

    public PhotoDetailViewModel(String number) {
        this.number.set(number);
    }


}
