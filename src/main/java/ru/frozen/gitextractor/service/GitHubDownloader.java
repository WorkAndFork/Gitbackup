package ru.frozen.gitextractor.service;

import org.apache.logging.log4j.Logger;
import ru.frozen.gitextractor.model.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class GitHubDownloader {



    public void downloadRepo(String reponame, String targetfolder, Logger log, User user){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String url = "api.github.com";
            new GitHubExtractor(url, user.getLogin(), user.getPassword()).extract(reponame, new FileSystemApplier(targetfolder));
        } catch (IOException e) {
            log.error("Failed to extract from github.", e);
        }
    }
}
