package com.rayho.tsxiu.module_news.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.LazyLoadFragment;
import com.rayho.tsxiu.base.listener.OnTabReselectedListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import timber.log.Timber;

/**
 * 新闻列表界面(根据分类id，显示不同的新闻)
 */
public class ContentFragment extends LazyLoadFragment implements OnTabReselectedListener {

    @BindView(R.id.tv_tag)
    TextView mTvTag;

    private static final String TYPE_ID = "tag";
    private String tag;
    private ContentViewModel mViewModel;

    public static ContentFragment newInstance(String tag) {
        Bundle args = new Bundle();
        args.putString(TYPE_ID,tag);
        ContentFragment fragment = new ContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tag = getArguments().getString(TYPE_ID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        Timber.tag("Contentfrg");
        Timber.d(tag+"创建了");
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public int getLayoutId() {
        return R.layout.content_fragment;
    }


    public void updateData(){
        if(mTvTag != null){
            mTvTag.setText("updateData!!!");
        }else {
            Timber.d("textview = null");
        }
    }

    @Override
    public void loadData() {
        mViewModel = ViewModelProviders.of(this).get(ContentViewModel.class);
        Timber.d(tag+"加载了数据");
        mTvTag.setText(tag);
    }

}
