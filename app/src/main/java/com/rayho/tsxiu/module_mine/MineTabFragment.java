package com.rayho.tsxiu.module_mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.Constant;
import com.rayho.tsxiu.greendao.UserDao;
import com.rayho.tsxiu.module_mine.activity.EditInfoActivity;
import com.rayho.tsxiu.module_mine.activity.FaceDetailActivity;
import com.rayho.tsxiu.module_mine.activity.MyCollectionActivity;
import com.rayho.tsxiu.module_mine.activity.SettingsActivity;
import com.rayho.tsxiu.module_mine.dao.User;
import com.rayho.tsxiu.module_mine.viewmodel.MineTabViewModel;
import com.rayho.tsxiu.utils.DaoManager;
import com.rayho.tsxiu.utils.GlideUtils;
import com.rayho.tsxiu.utils.ToastUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MineTabFragment extends Fragment {

    @BindView(R.id.iv_cover)
    ImageView mIvCover;

    @BindView(R.id.iv_face)
    ImageView mIvFace;

    @BindView(R.id.tv_name)
    TextView mTvName;

    @BindView(R.id.rl_edit)
    RelativeLayout mRlEdit;

    @BindView(R.id.rl_collect)
    RelativeLayout mRlCollect;

    @BindView(R.id.rl_settings)
    RelativeLayout mRlSettings;


    private Unbinder mUnbinder;

    private MineTabViewModel mViewModel;

    private String mUrl;

    private String mPhotoPath;//数据库返回的图片路径


    public static MineTabFragment newInstance() {
        return new MineTabFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }


    /*
      返回MainActivity时更新用户信息
     */
    @Override
    public void onResume() {
        super.onResume();
        initUser();
    }


    /**
     * 初始化默认用户
     */
    private void initUser() {
        DaoManager.getInstance().getDaoSession().getUserDao()
                .queryBuilder()
                .where(UserDao.Properties.Uid.eq(Constant.DEFAULT_USER_ID))
                .rx()
                .unique()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    //未设置默认用户
                    if (user == null) {
                        //插入默认用户
                        DaoManager.getInstance().getDaoSession().getUserDao()
                                .rx()
                                .insertOrReplace(new User(null, Constant.DEFAULT_USER_ID,
                                        Constant.DEFAULT_USER_NAME, Constant.DEFAULT_USER_FACE))
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(user1 -> {
                                    if (user1 == null) {
                                        //插入失败
                                        ToastUtil toast = new ToastUtil(getActivity(), "设置默认用户失败");
                                        toast.show();
                                    } else {
                                        mTvName.setText(Constant.DEFAULT_USER_NAME);
                                        GlideUtils.loadCircleImage(getActivity(), Constant.DEFAULT_USER_FACE, mIvFace);
                                        GlideUtils.loadBlurImage(getActivity(), Constant.DEFAULT_USER_FACE, mIvCover, 50);
                                        mPhotoPath = Constant.DEFAULT_USER_FACE;
                                    }
                                });
                    } else {
                        //已设置默认用户
                        mTvName.setText(user.getName());
                        GlideUtils.loadCircleImage(getActivity(), user.getImgName(), mIvFace);
                        GlideUtils.loadBlurImage(getActivity(), user.getImgName(), mIvCover, 50);
                        mPhotoPath = user.getImgName();
                    }
                });
    }


    @OnClick({R.id.iv_face, R.id.rl_edit, R.id.rl_collect, R.id.rl_settings})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_face:
                Intent intent = new Intent(getActivity(), FaceDetailActivity.class);
                intent.putExtra("path",mPhotoPath);
                startActivity(intent);
                break;
            case R.id.rl_edit:
                ActivityUtils.startActivity(EditInfoActivity.class);
                break;
            case R.id.rl_collect:
                ActivityUtils.startActivity(MyCollectionActivity.class);
                break;
            case R.id.rl_settings:
                ActivityUtils.startActivity(SettingsActivity.class);
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }
}
