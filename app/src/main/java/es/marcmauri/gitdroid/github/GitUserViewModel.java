package es.marcmauri.gitdroid.github;

import android.os.Parcel;
import android.os.Parcelable;

public class GitUserViewModel implements Parcelable {
    private long id;
    private String username;
    private String avatarUrl;

    public GitUserViewModel(long id, String username, String avatarUrl) {
        this.id = id;
        this.username = username;
        this.avatarUrl = avatarUrl;
    }

    protected GitUserViewModel(Parcel in) {
        id = in.readLong();
        username = in.readString();
        avatarUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(username);
        parcel.writeString(avatarUrl);
    }

    public static final Creator<GitUserViewModel> CREATOR = new Creator<GitUserViewModel>() {
        @Override
        public GitUserViewModel createFromParcel(Parcel in) {
            return new GitUserViewModel(in);
        }

        @Override
        public GitUserViewModel[] newArray(int size) {
            return new GitUserViewModel[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
