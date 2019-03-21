package com.rayho.tsxiu.module_news.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.rxbus.RxBus;
import com.blankj.utilcode.util.ActivityUtils;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.Constant;
import com.rayho.tsxiu.greendao.NewsDao;
import com.rayho.tsxiu.module_news.activity.NewsDetailActivity;
import com.rayho.tsxiu.module_news.bean.NewsBean;
import com.rayho.tsxiu.module_news.dao.News;
import com.rayho.tsxiu.module_news.fragment.ContentFragment;
import com.rayho.tsxiu.module_news.viewmodel.NewsViewModel;
import com.rayho.tsxiu.utils.DaoManager;
import com.rayho.tsxiu.utils.ToastUtil;

import java.lang.reflect.Field;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Rayho on 2019/1/28
 * 新闻界面
 **/
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private List<NewsBean.DataBean> list;

    private ContentFragment mContext;

    public NewsAdapter(ContentFragment mContext) {
        this.mContext = mContext;
    }

    public void setNews(List<NewsBean.DataBean> list) {
        this.list = list;
    }

    /**
     * 判断每个item的类型（加载不同类型布局的方法）
     * 根据新闻的type属性 返回不同的viewType(直接返回布局ID)
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        switch (list.get(position).type) {
            case 0:
                return R.layout.item_news_no_photo;//无图
            case 1:
                return R.layout.item_news_single_photo;//单图
            case 2:
                return R.layout.item_news_three_photo;//3张图
            case 3:
                return R.layout.item_news_multiple_photo;//组图
            default:
                return super.getItemViewType(position);
        }
    }

    /**
     * 根据viewType创建不同的布局
     *
     * @param parent
     * @param viewType 布局ID
     * @return
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding;
        MyViewHolder holder;
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), viewType, parent, false);
        holder = new MyViewHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }

    /**
     * 根据返回holder的类型 绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        NewsViewModel model = new NewsViewModel(list.get(position));
        holder.getBinding().setVariable(BR.item, model);
        holder.getBinding().executePendingBindings();//防止画面闪烁
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getDefault().postSticky(list.get(position),"news");
                ActivityUtils.startActivity(NewsDetailActivity.class);
            }
        });


        holder.itemView.findViewById(R.id.iv_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(mContext.getActivity(), v, holder.getLayoutPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    /**
     * 弹出"更多"菜单
     * 使用PopupMenu(轻量级控件，替代自定义popupwindow弹出菜单)
     *
     * @param context
     * @param view     所依赖的view
     * @param position 当前item的位置
     */
    @SuppressLint("RestrictedApi")
    private void showMenu(final Context context, View view, final int position) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.news, popupMenu.getMenu());
        //利用反射 显示图标
        try {
            Field field = popupMenu.getClass().getDeclaredField("mPopup");
            field.setAccessible(true);
            MenuPopupHelper helper = (MenuPopupHelper) field.get(popupMenu);
            helper.setForceShowIcon(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.collect:
                        collectNews(position);
                        return true;
                    case R.id.hide:
                        //删除该新闻
                        if (position != -1) {
                            removeItem(position);
                        }
                        return true;
                }
                return true;
            }
        });
        popupMenu.show();
    }


    /**
     * 收藏新闻
     *
     * @param position 当前item的位置
     */
    private void collectNews(final int position) {
        //查询该新闻是否已经收藏过
        DaoManager.getInstance().getDaoSession().getNewsDao()
                .queryBuilder()
                .where(NewsDao.Properties.Nid.eq(list.get(position).id))
                .rx()
                .unique()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<News>() {
                    @Override
                    public void call(News news) {
                        if (news == null) {
                            //未收藏过
                            NewsBean.DataBean bean = list.get(position);
                            News data = new News(null, list.get(position).id,
                                    bean.viewCount, bean.publishDateStr, bean.catPathKey,
                                    bean.title, bean.publishDate, bean.dislikeCount,
                                    bean.commentCount, bean.likeCount, bean.posterId,
                                    bean.html, bean.url, bean.coverUrl,
                                    bean.content, bean.posterScreenName,
                                    bean.imageUrls, bean.tags, bean.type);
                            //插入数据
                            DaoManager.getInstance().getDaoSession().getNewsDao()
                                    .rx()
                                    .insertOrReplace(data)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Action1<News>() {
                                        @Override
                                        public void call(News news) {
                                            ToastUtil toast = new ToastUtil(mContext.getActivity(), mContext.getString(R.string.collect_news_success));
                                            toast.show();
                                        }
                                    });
                        } else {
                            ToastUtil toast = new ToastUtil(mContext.getActivity(), mContext.getString(R.string.collect_news_tip));
                            toast.show();
                        }
                    }
                });
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }

        public void setBinding(ViewDataBinding binding) {
            this.binding = binding;
        }
    }


    //////////////////////////////list的操作方法///////////////////////////////////////////////////////

    public List<NewsBean.DataBean> getList() {
        return list;
    }

    /**
     * 加载更多数据
     *
     * @param data
     */
    public void addItems(List<NewsBean.DataBean> data) {
        list.addAll(data);
        notifyDataSetChanged();
    }

    public void addItem(NewsBean.DataBean item) {
        list.add(item);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
