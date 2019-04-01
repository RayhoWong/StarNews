package com.rayho.tsxiu.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.trello.rxlifecycle3.components.support.RxFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Rayho on 2018/12/4
 * Fragment懒加载的封装
 **/
public abstract class LazyLoadFragment extends RxFragment {
    //当前页面是否可见
    private boolean isUiVisible;
    //标志view控件是否已经初始化完成，因为setUserVisibleHint是在onCreateView之前调用的，
    // 在视图未初始化的时候，在lazyLoad当中就使用的话，就会有空指针的异常
    private boolean isViewCreated;
    //Butterknife绑定
    private Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        if(getLayoutId() != 0){
            view = inflater.inflate(getLayoutId(),container,false);
            //返回一个Unbinder值（进行解绑），注意这里的this不能使用getActivity()
            unbinder = ButterKnife.bind(this,view);
        }
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewCreated = true;
        lazyLoad();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(unbinder != null){
            unbinder.unbind();
        }
    }

    /**
     * 返回子类的布局id
     * @return
     */
    public abstract int getLayoutId();

    /**
     *
     * 通过判断setUserVisibleHint的isVisibleToUser，判断界面是否可见
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            isUiVisible = true;
            lazyLoad();
        }else {
            isUiVisible = false;
        }
    }

    private void lazyLoad(){
        //只有页面可见并且view初始化完成 才可以加载数据
        if(isUiVisible && isViewCreated){
            loadData();
            isViewCreated = false;
            isUiVisible = false;
        }else {
            return;
        }
    }

    public abstract void loadData();

}
