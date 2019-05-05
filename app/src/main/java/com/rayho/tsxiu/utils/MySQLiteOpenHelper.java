package com.rayho.tsxiu.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.rayho.tsxiu.greendao.ChannelDao;
import com.rayho.tsxiu.greendao.DaoMaster;
import com.rayho.tsxiu.greendao.ImageDao;
import com.rayho.tsxiu.greendao.NewsCacheDao;
import com.rayho.tsxiu.greendao.NewsDao;
import com.rayho.tsxiu.greendao.SearchHistoryDao;
import com.rayho.tsxiu.greendao.UserDao;
import com.rayho.tsxiu.greendao.VideoAutoPlayDao;

import org.greenrobot.greendao.database.Database;

/**
 * Created by Rayho on 2019/3/14
 * Greendao数据库帮助类
 **/
public class MySQLiteOpenHelper extends DaoMaster.DevOpenHelper {

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }


    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {

                    @Override
                    public void onCreateAllTables(Database db, boolean ifNotExists) {
                        DaoMaster.createAllTables(db, ifNotExists);
                    }

                    @Override
                    public void onDropAllTables(Database db, boolean ifExists) {
                        DaoMaster.dropAllTables(db, ifExists);
                    }
                },
                //数据库升级时 将需要保存数据的DAO类传进来(一般将全部Dao加入)
                ChannelDao.class, NewsCacheDao.class,
                NewsDao.class, SearchHistoryDao.class,
                ImageDao.class, UserDao.class, VideoAutoPlayDao.class);
    }
}