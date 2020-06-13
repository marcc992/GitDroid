package es.marcmauri.gitdroid.github.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

public class GitUserModel implements Parcelable {
    private long id;
    private String username;
    private String avatarUrl;

    public GitUserModel(long id, String username, String avatarUrl) {
        this.id = id;
        this.username = username;
        this.avatarUrl = avatarUrl;
    }

    protected GitUserModel(Parcel in) {
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

    public static final Creator<GitUserModel> CREATOR = new Creator<GitUserModel>() {
        @Override
        public GitUserModel createFromParcel(Parcel in) {
            return new GitUserModel(in);
        }

        @Override
        public GitUserModel[] newArray(int size) {
            return new GitUserModel[size];
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
