package com.rayho.tsxiu.module_news.adapter;

import android.view.View;

import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.BaseDataBindingApater;
import com.rayho.tsxiu.databinding.ActivitySearchBinding;
import com.rayho.tsxiu.greendao.SearchHistoryDao;
import com.rayho.tsxiu.module_news.activity.SearchActivity;
import com.rayho.tsxiu.module_news.viewmodel.HistoryRecordViewModel;
import com.rayho.tsxiu.utils.DaoManager;
import com.rayho.tsxiu.utils.RxUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * Created by Rayho on 2019/3/25
 * 历史记录的adapter
 **/
public class HistoryRecordAdapter extends BaseDataBindingApater {

    private SearchActivity mContext;

    private ActivitySearchBinding mBinding;


    public HistoryRecordAdapter(SearchActivity context, ActivitySearchBinding binding) {
        mContext = context;
        mBinding = binding;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);

        final HistoryRecordViewModel bean = (HistoryRecordViewModel) items.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.etContent.setText(bean.getRecord());
            }
        });

        holder.itemView.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除当前搜索记录
                removeItem(position);
                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        DaoManager.getInstance().getDaoSession().getSearchHistoryDao()
                                .queryBuilder()
                                .where(SearchHistoryDao.Properties.Record.eq(bean.getRecord()))
                                .buildDelete()
                                .executeDeleteWithoutDetachingEntities();
                        emitter.onNext(mContext.getString(R.string.success));
                    }
                })
                        .compose(mContext.<String>bindToLifecycle())
                        .compose(RxUtil.<String>rxSchedulerHelper())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                if (s.equals(mContext.getString(R.string.success))) {
                                    //当前记录条数=0 隐藏搜索历史的布局
                                    if (items.size() == 0) {
                                        mBinding.rlHistory.setVisibility(View.GONE);
                                    }
                                }
                            }
                        });
            }
        });
    }
}
