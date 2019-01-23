package in.ureport.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.Date;

/**
 * Created by johncordeiro on 7/21/15.
 */
public class ChatMessage implements Parcelable {

    @Expose
    private String key;

    @Expose
    private String message;

    @Expose
    private long date;

    @Expose
    private User user;

    @Expose
    private Media media;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatMessage that = (ChatMessage) o;

        return key.equals(that.key);

    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "key='" + key + '\'' +
                ", message='" + message + '\'' +
                ", date=" + date +
                ", user=" + user +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.message);
        dest.writeLong(date);
        dest.writeParcelable(this.user, 0);
        dest.writeParcelable(this.media, 0);
    }

    public ChatMessage() {
    }

    protected ChatMessage(Parcel in) {
        this.key = in.readString();
        this.message = in.readString();
        this.date = in.readLong();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.media = in.readParcelable(Media.class.getClassLoader());
    }

    public static final Creator<ChatMessage> CREATOR = new Creator<ChatMessage>() {
        public ChatMessage createFromParcel(Parcel source) {
            return new ChatMessage(source);
        }

        public ChatMessage[] newArray(int size) {
            return new ChatMessage[size];
        }
    };
}
