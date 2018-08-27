package ru.frozen.gitextractor.service.handler;

import ru.frozen.gitextractor.model.Snapshot;

import java.io.*;
import java.util.Properties;

import org.apache.logging.log4j.Logger;
import ru.frozen.gitextractor.model.User;
import ru.frozen.gitextractor.service.GitHubDownloader;
import ru.frozen.gitextractor.service.GitHubExtractor;

public class InputHandler {
    public void handle(Logger log) {
        try {
            String GITHUB_URL = "api.github.com";
            Boolean connected = false;
            String login = "";
            String pswrd = "";
            Properties properties = new Properties();
            Snapshot snapshot = new Snapshot();
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            File file = new File("config.properties");
            String absolutePath = file.getAbsolutePath();
            OutputStream outputStream = new FileOutputStream(absolutePath);
            while (!connected) {
                System.out.println("Enter login: ");
                login = (br.readLine());
                System.out.println("Enter password: ");
                pswrd = (br.readLine());
                System.out.println("Enter repoName: ");
                snapshot.setRepoName(br.readLine());
                GitHubExtractor gitHubExtractor = new GitHubExtractor(GITHUB_URL, login, pswrd);
                connected = gitHubExtractor.checkConnection(snapshot.getRepoName());
                if (!connected) System.out.println("Wrong username or password \"");
            }
            User user = new User(login, pswrd);
            System.out.println("Enter crypto key: ");
            snapshot.setCryptoKey(br.readLine());
            System.out.println("Enter backup frequency, in minutes. \n" +
                    "You can change frequency anytime. Change value near your repository name in 'config.properties. ");
            String typedFrequency = br.readLine();
            snapshot.setBackupFrequency(typedFrequency);
            properties.setProperty(snapshot.getRepoName(), typedFrequency);
            properties.store(outputStream, null);
            outputStream.close();
            Handler handler = new Handler();
            ScheduleHandler scheduleHandler = new ScheduleHandler();
            scheduleHandler.scheduleTask(handler, log, snapshot, "0", user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
