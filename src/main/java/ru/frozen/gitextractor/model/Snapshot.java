package ru.frozen.gitextractor.model;

public class Snapshot {
    private String repoName;
    private String prevSnapshotName;
    private String nextSnapshotName;
    private String prevSnapshotPath;
    private String nextSnapshotPath;
    private String pathToUploadingArchive;
    private Boolean hasBackup = false;
    private String cryptoKey;
    private String backupFrequency;
    private String forDeleteSnapshotName;
    private String forDeleteSnapshotPath;
    private Integer snapshotTimesCount;
    private String googleDriveFolderID;

    public String getBackupFrequency() {
        return backupFrequency;
    }

    public void setBackupFrequency(String backupFrequency) {
        this.backupFrequency = backupFrequency;
    }

    public String getCryptoKey() {
        return cryptoKey;
    }

    public void setCryptoKey(String cryptoKey) {
        this.cryptoKey = cryptoKey;
    }

    public String getPathToUploadingArchive() {
        return pathToUploadingArchive;
    }

    public void setPathToUploadingArchive(String pathToUploadingArchive) {
        this.pathToUploadingArchive = pathToUploadingArchive;
    }

    public String getPrevSnapshotPath() {
        return prevSnapshotPath;
    }

    public void setPrevSnapshotPath(String prevSnapshotPath) {
        this.prevSnapshotPath = prevSnapshotPath;
    }

    public String getNextSnapshotPath() {
        return nextSnapshotPath;
    }

    public void setNextSnapshotPath(String newxSnapshotPath) {
        this.nextSnapshotPath = newxSnapshotPath;
    }



    public void setHasBackup(Boolean hasBackup) {
        this.hasBackup = hasBackup;
    }

    public Boolean getHasBackup() {
        return hasBackup;
    }

    public String getRepoName() {

        return repoName;

    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getPrevSnapshotName() {
        return prevSnapshotName;
    }

    public void setPrevSnapshotName(String prevSnapshotName) {
        this.prevSnapshotName = prevSnapshotName;
    }

    public String getNextSnapshotName() {
        return nextSnapshotName;
    }

    public void setNextSnapshotName(String nextSnapshotName) {
        this.nextSnapshotName = nextSnapshotName;
    }

    public String getForDeleteSnapshotName() {
        return forDeleteSnapshotName;
    }

    public void setForDeleteSnapshotName(String forDeleteSnapshotName) {
        this.forDeleteSnapshotName = forDeleteSnapshotName;
    }

    public String getForDeleteSnapshotPath() {
        return forDeleteSnapshotPath;
    }

    public void setForDeleteSnapshotPath(String forDeleteSnapshotPath) {
        this.forDeleteSnapshotPath = forDeleteSnapshotPath;
    }

    public Integer getSnapshotTimesCount() {
        return snapshotTimesCount;
    }

    public void setSnapshotTimesCount(Integer snapshotTimesCount) {
        this.snapshotTimesCount = snapshotTimesCount;
    }

    public String getGoogleDriveFolderID() {
        return googleDriveFolderID;
    }

    public void setGoogleDriveFolderID(String googleDriveFolderID) {
        this.googleDriveFolderID = googleDriveFolderID;
    }
}
