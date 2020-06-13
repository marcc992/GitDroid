package es.marcmauri.gitdroid.github.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class GitRepositoryBasicModel implements Parcelable {
    private long id;
    private String name;
    private String fullName;
    private String description;
    private Date createdAt;
    private Date updatedAt;

    public GitRepositoryBasicModel(long id, String name, String fullName, String description,
                                   Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.fullName = fullName;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    protected GitRepositoryBasicModel(Parcel in) {
        id = in.readLong();
        name = in.readString();
        fullName = in.readString();
        description = in.readString();
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(fullName);
        parcel.writeString(description);
    }
}