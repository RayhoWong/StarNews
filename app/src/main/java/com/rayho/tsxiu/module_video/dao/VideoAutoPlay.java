package com.rayho.tsxiu.module_video.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Rayho on 2019/5/4
 **/
@Entity
public class VideoAutoPlay {
    @Id(autoincrement = true)
    private Long id;

    @NotNull
    @Unique
    private String vid;//标记id

    @NotNull
    @Unique
    private boolean autoplay;//视频是否自动播放

    @Generated(hash = 1676814363)
    public VideoAutoPlay(Long id, @NotNull String vid, boolean autoplay) {
        this.id = id;
        this.vid = vid;
        this.autoplay = autoplay;
    }

    @Generated(hash = 1559515066)
    public VideoAutoPlay() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVid() {
        return this.vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public boolean getAutoplay() {
        return this.autoplay;
    }

    public void setAutoplay(boolean autoplay) {
        this.autoplay = autoplay;
    }
}
