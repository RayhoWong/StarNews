package com.rayho.tsxiu.module_news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.module_news.fragment.ContentFragment;
import com.rayho.tsxiu.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class NewsTabFragment extends Fragment {

    @BindView(R.id.container)
    CoordinatorLayout container;
    @BindView(R.id.ll_search)
    LinearLayout mllSearch;
    @BindView(R.id.ll_scan)
    LinearLayout mllScan;
    @BindView(R.id.tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    private Unbinder unbinder;
    private NewsTabViewModel mViewModel;
    private List<Fragment> fragments;
    private String[] titles;


    public static NewsTabFragment newInstance() {
        return new NewsTabFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.news_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
//        initToolbar();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(NewsTabViewModel.class);
        // TODO: Use the ViewModel
        initView();
    }

    private void initView(){
        titles = new String[]{"社会","科技","娱乐","体育","文化","视频","金融"};
        fragments = new ArrayList<>();
        for(int i=0;i<titles.length;i++){
            fragments.add(ContentFragment.newInstance(titles[i]));
        }
        mViewPager.setAdapter(new ContentAdapter(getChildFragmentManager()));
        //除当前页面的预加载页面数(保证切换页面时 不会重新创建)
        mViewPager.setOffscreenPageLimit(titles.length - 1);
        mTabLayout.setupWithViewPager(mViewPager);
    }


    class ContentAdapter extends FragmentPagerAdapter {
        public ContentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }


    @OnClick({R.id.ll_search, R.id.ll_scan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                ToastUtil util = new ToastUtil(getActivity(), "search");
                util.show();
                break;
            case R.id.ll_scan:
                ToastUtil util2 = new ToastUtil(getActivity(), "scan");
                util2.show();
//                util2.show(Toast.LENGTH_SHORT);
                break;
        }
    }

    /**
     * Butterknife解绑
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
