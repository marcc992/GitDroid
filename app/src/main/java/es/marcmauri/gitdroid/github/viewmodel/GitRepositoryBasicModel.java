package es.marcmauri.gitdroid.github.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

public class GitRepositoryBasicModel implements Parcelable {
    private long id;
    private String name;
    private String fullName;
    private String description;
    private String ownerName;

    public GitRepositoryBasicModel(long id, String name, String fullName, String description, String ownerName) {
        this.id = id;
        this.name = name;
        this.fullName = fullName;
        this.description = description;
        this.ownerName = ownerName;
    }

    protected GitRepositoryBasicModel(Parcel in) {
        id = in.readLong();
        name = in.readString();
        fullName = in.readString();
        description = in.readString();
        ownerName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(fullName);
        dest.writeString(description);
        dest.writeString(ownerName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GitRepositoryBasicModel> CREATOR = new Creator<GitRepositoryBasicModel>() {
        @Override
        public GitRepositoryBasicModel createFromParcel(Parcel in) {
            return new GitRepositoryBasicModel(in);
        }

        @Override
        public GitRepositoryBasicModel[] newArray(int size) {
            return new GitRepositoryBasicModel[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}