package tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Stats implements Serializable
{

    @SerializedName("comments")
    @Expose
    private Integer comments;
    @SerializedName("likes")
    @Expose
    private Integer likes;
    @SerializedName("shares")
    @Expose
    private Integer shares;
    @SerializedName("play")
    @Expose
    private Integer play;
    private final static long serialVersionUID = -648960770069287428L;

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getShares() {
        return shares;
    }

    public void setShares(Integer shares) {
        this.shares = shares;
    }

    public Integer getPlay() {
        return play;
    }

    public void setPlay(Integer play) {
        this.play = play;
    }

}
