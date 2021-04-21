package tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

//public class TiktokModel implements Serializable {
//
//    @SerializedName("status")
//    String status;
//    @SerializedName("message")
//    String message;
//    @SerializedName("responsecode")
//    String responsecode;
//    @SerializedName("description")
//    String description;
//    @SerializedName("data")
//    TiktokDataModel data;
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public String getResponsecode() {
//        return responsecode;
//    }
//
//    public void setResponsecode(String responsecode) {
//        this.responsecode = responsecode;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public TiktokDataModel getData() {
//        return data;
//    }
//
//    public void setData(TiktokDataModel data) {
//        this.data = data;
//    }
//}

//public class TiktokModel implements Serializable{
//
//        @SerializedName("downloadLink")
//        private String downloadLink;
//        @SerializedName("thumb")
//        private String thumb;
//        @SerializedName("username")
//        private String username;
//        @SerializedName("create_time")
//        private String createTime;
//        private final static long serialVersionUID = 7271002326837954385L;
//
//        public String getDownloadLink() {
//        return downloadLink;
//    }
//
//        public void setDownloadLink(String downloadLink) {
//        this.downloadLink = downloadLink;
//    }
//
//        public String getThumb() {
//        return thumb;
//    }
//
//        public void setThumb(String thumb) {
//        this.thumb = thumb;
//    }
//
//        public String getUsername() {
//        return username;
//    }
//
//        public void setUsername(String username) {
//        this.username = username;
//    }
//
//        public String getCreateTime() {
//        return createTime;
//    }
//
//        public void setCreateTime(String createTime) {
//        this.createTime = createTime;
//    }
//}

public class TiktokModel implements Serializable
{

    @SerializedName("marked")
    @Expose
    private String marked;
    @SerializedName("not_marked")
    @Expose
    private String notMarked;
    @SerializedName("mp3")
    @Expose
    private String mp3;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("create_time")
    @Expose
    private String createTime;
    private final static long serialVersionUID = 9006290617873705856L;

    public String getMarked() {
        return marked;
    }

    public void setMarked(String marked) {
        this.marked = marked;
    }

    public String getNotMarked() {
        return notMarked;
    }

    public void setNotMarked(String notMarked) {
        this.notMarked = notMarked;
    }

    public String getMp3() {
        return mp3;
    }

    public void setMp3(String mp3) {
        this.mp3 = mp3;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

}
