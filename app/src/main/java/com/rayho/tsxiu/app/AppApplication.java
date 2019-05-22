package com.rayho.tsxiu.app;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Created by Rayho on 2018/11/8 0008
 * 这个类集成TinkerApplication类，这里面不做任何操作，
 * 所有Application的代码都会放到ApplicationLike继承类当中
 */
public class AppApplication extends TinkerApplication {

    public AppApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.rayho.tsxiu.app.AppApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }

    private static class AppApplicationHolder {
        public static AppApplication application = new AppApplication();
    }

    public static AppApplication getAppApplication() {
        return AppApplicationHolder.application;
    }


}
