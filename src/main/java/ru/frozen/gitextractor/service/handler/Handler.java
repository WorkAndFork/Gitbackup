package ru.frozen.gitextractor.service.handler;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import ru.frozen.gitextractor.model.Snapshot;
import ru.frozen.gitextractor.model.User;
import ru.frozen.gitextractor.service.GitHubDownloader;
import ru.frozen.gitextractor.util.Crypto;
import ru.frozen.gitextractor.util.Delta;
import ru.frozen.gitextractor.util.Util;
import ru.frozen.gitextractor.util.ZipApp;
import ru.frozen.gitextractor.web.GoogleDrive;

import javax.crypto.Cipher;
import java.io.*;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Handler {

    String pathToRepos = "";
    String pathToReposZip = "";
    Util util = new Util();
    ZipApp zipApp = new ZipApp();
    GoogleDrive googleDrive = new GoogleDrive();
    Crypto crypto = new Crypto();
    Boolean downloaded = false;


    public Snapshot commandHandle(Logger log, Snapshot snapshot, Timer timer, ScheduleHandler scheduleHandler, User user) {
        GitHubDownloader gitHubDownloader = new GitHubDownloader();

        try {
            downloadCommandHandler(gitHubDownloader, log, snapshot, user);
            zipCommandHandler(snapshot, snapshot.getCryptoKey());
            googleDrive.uploadToGDrive(snapshot);
            Properties properties = new Properties();
            InputStream inputStream = new FileInputStream("config.properties");
            properties.load(inputStream);
            if (!snapshot.getBackupFrequency().equals(properties.getProperty(snapshot.getRepoName()))){
                String newDelay = snapshot.getBackupFrequency();
                snapshot.setBackupFrequency(properties.getProperty(snapshot.getRepoName()));
                timer.cancel();
                scheduleHandler.scheduleTask(Handler.this, log, snapshot, newDelay, user);
            }
            return snapshot;


        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException gse) {
            gse.printStackTrace();
        }
        return snapshot;
    }

    void downloadCommandHandler(GitHubDownloader gitHubDownloader, Logger log, Snapshot snapshot, User user) {
        String reponame = snapshot.getRepoName();
        if (!snapshot.getHasBackup()) snapshot.setPrevSnapshotPath(pathToRepos + reponame + "/backup");
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
        String nextSnapshotPath = snapshot.getHasBackup() ? pathToRepos + reponame + "/" + timeStamp : pathToRepos + reponame + "/backup";
        snapshot.setNextSnapshotPath(nextSnapshotPath);
        gitHubDownloader.downloadRepo(reponame, snapshot.getNextSnapshotPath(), log, user);
        if (snapshot.getHasBackup()) {
            snapshot.setNextSnapshotName(timeStamp);
        } else {
            snapshot.setHasBackup(true);
        }
    }

    void zipCommandHandler(Snapshot snapshot, String cryptoKey) throws IOException {
        String metaDeltaPath = pathToRepos + snapshot.getRepoName() + "/" + snapshot.getNextSnapshotName() + "/meta.properties";
        Boolean hasSnapshot = snapshot.getNextSnapshotName() != null;
        List<String> fileListOld = snapshot.getPrevSnapshotName() != null
                ? util.getFileList(snapshot.getPrevSnapshotPath())
                : util.getFileList(pathToRepos + snapshot.getRepoName() + "/backup");
        List<String> fileListNew = util.getFileList(snapshot.getNextSnapshotPath());


        if (hasSnapshot) {
            Delta checkDelta = new Delta();

            Map<String, String> delta = checkDelta.getDelta(fileListOld, fileListNew, pathToRepos, snapshot);
            Map<String, String> deltaMeta = new HashMap<>();
            deltaMeta.putAll(delta);
            if (!delta.isEmpty()) {
                for (Iterator<Map.Entry<String, String>> it = delta.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<String, String> entry = it.next();
                    if (entry.getValue().equals("Deleted")) {
                        it.remove();
                    }
                }

                FileWriter fileWriter = new FileWriter(metaDeltaPath);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                delta.put("meta.properties", "Meta");
                deltaMeta.put("meta.properties", "Meta");
                if(delta.size() != 1) {
                    for (String key : deltaMeta.keySet()) {
                        bufferedWriter.write(key + "=" + deltaMeta.get(key));
                        bufferedWriter.newLine();
                    }
                    if (bufferedWriter != null)
                        bufferedWriter.close();
                    if (fileWriter != null)
                        fileWriter.close();
                    snapshot.setPathToUploadingArchive(pathToReposZip + snapshot.getRepoName() + "/" + snapshot.getNextSnapshotName());
                    for (String key : delta.keySet()) {
                        String pathFileForCrypto = snapshot.getNextSnapshotPath() + "/" + key;
                        crypto.fileProcessor(Cipher.ENCRYPT_MODE, cryptoKey, new File(pathFileForCrypto), new File(pathFileForCrypto));
                    }
                    zipApp.zipIt(snapshot.getNextSnapshotPath(),
                            snapshot.getPathToUploadingArchive(), new ArrayList(delta.keySet()));
                    for (String key : delta.keySet()) {
                        String pathFileForCrypto = snapshot.getNextSnapshotPath() + "/" + key;
                        crypto.fileProcessor(Cipher.DECRYPT_MODE, cryptoKey, new File(pathFileForCrypto), new File(pathFileForCrypto));
                    }
                    FileUtils.deleteDirectory(new File(snapshot.getPrevSnapshotPath()));
                    snapshot.setPrevSnapshotName(snapshot.getNextSnapshotName());
                    snapshot.setPrevSnapshotPath(snapshot.getPrevSnapshotPath());
                }
            }

                FileUtils.deleteDirectory(new File(snapshot.getPrevSnapshotPath()));
                snapshot.setPrevSnapshotName(snapshot.getNextSnapshotName());
                snapshot.setPrevSnapshotPath(snapshot.getNextSnapshotPath());
            } else {
                new File(pathToReposZip + snapshot.getRepoName()).mkdir();
                for (String key : fileListOld) {
                    String pathFileForCrypto = pathToRepos + snapshot.getRepoName() + "/backup/" + key;
                    crypto.fileProcessor(Cipher.ENCRYPT_MODE, cryptoKey, new File(pathFileForCrypto), new File(pathFileForCrypto));
                }
                snapshot.setPathToUploadingArchive(pathToReposZip + snapshot.getRepoName() + "/backup");
                zipApp.zipIt(pathToRepos + snapshot.getRepoName() + "/backup",
                        snapshot.getPathToUploadingArchive(), fileListOld);
                for (String key : fileListOld) {
                    String pathFileForCrypto = pathToRepos + snapshot.getRepoName() + "/backup/" + key;
                    crypto.fileProcessor(Cipher.DECRYPT_MODE, cryptoKey, new File(pathFileForCrypto), new File(pathFileForCrypto));
                }
                if (snapshot.getPrevSnapshotName() != null)
                    FileUtils.deleteDirectory(new File(snapshot.getPrevSnapshotPath()));
                snapshot.setPrevSnapshotName(snapshot.getNextSnapshotName());
                snapshot.setPrevSnapshotPath(snapshot.getNextSnapshotPath());

            }
        }
    }
