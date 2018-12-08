package com.rayho.tsxiu.module_news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rayho.tsxiu.R;
import com.rayho.tsxiu.utils.ToastUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
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
    private Unbinder unbinder;
    /* @BindView(R.id.toolbar_news)
     Toolbar mToolbar;*/
    private NewsTabViewModel mViewModel;

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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @OnClick({R.id.ll_search, R.id.ll_scan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                ToastUtil util = new ToastUtil(getActivity(),"search");
                util.show();
                break;
            case R.id.ll_scan:
                ToastUtil util2 = new ToastUtil(getActivity(),"scan");
                util2.show();
//                util2.show(Toast.LENGTH_SHORT);
                break;
        }
    }

   /* private void initToolbar() {
        mToolbar.setTitle("头条");
        mToolbar.setLogo(R.mipmap.ic_launcher);
        mToolbar.inflateMenu(R.menu.setting_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.search:
                        Intent intent = new Intent(getActivity(), TestActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.favor:
                        SnackbarUtil.showSnackbarColorAction(getActivity(), container,
                                "ni hao a", "press", R.color.colorAccent, 2000,
                                R.color.colorPrimary, R.color.colorPrimary,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ToastUtil toastUtil = new ToastUtil(getActivity(), "testtest");
                                        toastUtil.show();
//                                       ActivityUtils.startActivity(TestActivity.class);
                                    }
                                });
                        break;
                    case R.id.like:
                        break;
                }
                return false;
            }
        });

    }*/

}
