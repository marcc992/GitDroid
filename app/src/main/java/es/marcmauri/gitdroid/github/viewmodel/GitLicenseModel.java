package es.marcmauri.gitdroid.github.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

public class GitLicenseModel implements Parcelable {
    private String key;
    private String name;
    private String url;

    public GitLicenseModel(String key, String name, String url) {
        this.key = key;
        this.name = name;
        this.url = url;
    }

    protected GitLicenseModel(Parcel in) {
        key = in.readString();
        name = in.readString();
        url = in.readString();
    }

    public static final Creator<GitLicenseModel> CREATOR = new Creator<GitLicenseModel>() {
        @Override
        public GitLicenseModel createFromParcel(Parcel in) {
            return new GitLicenseModel(in);
        }

        @Override
        public GitLicenseModel[] newArray(int size) {
            return new GitLicenseModel[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(key);
        parcel.writeString(name);
        parcel.writeString(url);
    }
}
