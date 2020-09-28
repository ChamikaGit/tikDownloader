package tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TiktokModelNew implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("video_id")
    @Expose
    private String videoId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("caption")
    @Expose
    private String caption;
    @SerializedName("cover")
    @Expose
    private String cover;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("url_nwm")
    @Expose
    private String urlNwm;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("music")
    @Expose
    private Music music;
    @SerializedName("stats")
    @Expose
    private Stats stats;
    @SerializedName("uploaded_at")
    @Expose
    private Object uploadedAt;
    @SerializedName("dl_count")
    @Expose
    private Integer dlCount;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("video_url")
    @Expose
    private String videoUrl;
    @SerializedName("share_url")
    @Expose
    private String shareUrl;
    private final static long serialVersionUID = 4709039205695411326L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlNwm() {
        return urlNwm;
    }

    public void setUrlNwm(String urlNwm) {
        this.urlNwm = urlNwm;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public Object getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Object uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public Integer getDlCount() {
        return dlCount;
    }

    public void setDlCount(Integer dlCount) {
        this.dlCount = dlCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

}
