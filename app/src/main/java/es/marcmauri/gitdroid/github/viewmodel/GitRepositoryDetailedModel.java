package es.marcmauri.gitdroid.github.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class GitRepositoryDetailedModel implements Parcelable {
    private long id;
    private String nodeId;
    private String name;
    private String fullName;
    private String description;
    private String contentLanguage;
    private String defaultBranch;
    private Date createdAt;
    private Date updatedAt;
    private long size;
    private long openIssues;
    private long watchers;
    private String htmlUrl;
    private String gitUrl;
    private String sshUrl;
    private String cloneUrl;
    private String homepage;
    private GitLicenseModel license;

    public GitRepositoryDetailedModel(long id, String nodeId, String name, String fullName, String description,
                                      String contentLanguage, String defaultBranch, Date createdAt, Date updatedAt,
                                      long size, long openIssues, long watchers, String htmlUrl, String gitUrl,
                                      String sshUrl, String cloneUrl, String homepage, GitLicenseModel license) {
        this.id = id;
        this.nodeId = nodeId;
        this.name = name;
        this.fullName = fullName;
        this.description = description;
        this.contentLanguage = contentLanguage;
        this.defaultBranch = defaultBranch;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.size = size;
        this.openIssues = openIssues;
        this.watchers = watchers;
        this.htmlUrl = htmlUrl;
        this.gitUrl = gitUrl;
        this.sshUrl = sshUrl;
        this.cloneUrl = cloneUrl;
        this.homepage = homepage;
        this.license = license;
    }

    protected GitRepositoryDetailedModel(Parcel in) {
        id = in.readLong();
        nodeId = in.readString();
        name = in.readString();
        fullName = in.readString();
        description = in.readString();
        contentLanguage = in.readString();
        defaultBranch = in.readString();
        size = in.readLong();
        openIssues = in.readLong();
        watchers = in.readLong();
        htmlUrl = in.readString();
        gitUrl = in.readString();
        sshUrl = in.readString();
        cloneUrl = in.readString();
        homepage = in.readString();
        license = in.readParcelable(GitLicenseModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(nodeId);
        dest.writeString(name);
        dest.writeString(fullName);
        dest.writeString(description);
        dest.writeString(contentLanguage);
        dest.writeString(defaultBranch);
        dest.writeLong(size);
        dest.writeLong(openIssues);
        dest.writeLong(watchers);
        dest.writeString(htmlUrl);
        dest.writeString(gitUrl);
        dest.writeString(sshUrl);
        dest.writeString(cloneUrl);
        dest.writeString(homepage);
        dest.writeParcelable(license, flags);
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

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
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

    public String getContentLanguage() {
        return contentLanguage;
    }

    public void setContentLanguage(String contentLanguage) {
        this.contentLanguage = contentLanguage;
    }

    public String getDefaultBranch() {
        return defaultBranch;
    }

    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
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

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getOpenIssues() {
        return openIssues;
    }

    public void setOpenIssues(long openIssues) {
        this.openIssues = openIssues;
    }

    public long getWatchers() {
        return watchers;
    }

    public void setWatchers(long watchers) {
        this.watchers = watchers;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getGitUrl() {
        return gitUrl;
    }

    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }

    public String getSshUrl() {
        return sshUrl;
    }

    public void setSshUrl(String sshUrl) {
        this.sshUrl = sshUrl;
    }

    public String getCloneUrl() {
        return cloneUrl;
    }

    public void setCloneUrl(String cloneUrl) {
        this.cloneUrl = cloneUrl;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public GitLicenseModel getLicense() {
        return license;
    }

    public void setLicense(GitLicenseModel license) {
        this.license = license;
    }
}