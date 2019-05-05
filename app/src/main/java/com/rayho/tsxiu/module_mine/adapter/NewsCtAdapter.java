package com.rayho.tsxiu.module_mine.adapter;

import android.view.View;

import com.blankj.rxbus.RxBus;
import com.blankj.utilcode.util.ActivityUtils;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.BaseDataBindingApater;
import com.rayho.tsxiu.greendao.NewsDao;
import com.rayho.tsxiu.module_mine.fragment.NewsCtFragment;
import com.rayho.tsxiu.module_mine.viewmodel.NewsCtViewModel;
import com.rayho.tsxiu.module_news.activity.NewsDetailActivity;
import com.rayho.tsxiu.module_news.bean.NewsBean;
import com.rayho.tsxiu.utils.DaoManager;
import com.rayho.tsxiu.utils.RxUtil;
import com.rayho.tsxiu.utils.ToastUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by Rayho on 2019/4/28
 * 新闻收藏
 **/
public class NewsCtAdapter extends BaseDataBindingApater {

    private NewsCtFragment mFragment;


    public NewsCtAdapter(NewsCtFragment mFragment) {
        this.mFragment = mFragment;
    }


    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);


        holder.itemView.setOnClickListener(v -> {
            NewsCtViewModel model = (NewsCtViewModel) items.get(position);
            NewsBean.DataBean news = new NewsBean.DataBean();
            news.url = model.getUrl();
            RxBus.getDefault().postSticky(news, "news");
            ActivityUtils.startActivity(NewsDetailActivity.class);
        });


        holder.itemView.findViewById(R.id.iv_close).setOnClickListener(v -> {
            NewsCtViewModel model = (NewsCtViewModel) items.get(position);
            //取消收藏(删除新闻)
            Observable.create((ObservableOnSubscribe<String>) emitter -> {
                DaoManager.getInstance().getDaoSession().getNewsDao()
                        .queryBuilder()
                        .where(NewsDao.Properties.Nid.eq(model.getNid()))
                        .buildDelete()
                        .executeDeleteWithoutDetachingEntities();
                emitter.onNext("success");
            })
                    .compose(RxUtil.rxSchedulerHelper())
                    .compose(mFragment.bindToLifecycle())
                    .subscribe(s -> {
                        if (s.equals("success")) {
                            removeItem(position);
                            ToastUtil toast = new ToastUtil(mFragment.getContext(), "已取消收藏该新闻");
                            toast.show();
                            if (items.size() == 0) {
                                //显示无数据布局
                                mFragment.mViewStub.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        });
    }

}
