package ru.frozen.gitextractor.service.handler;

import org.apache.logging.log4j.Logger;
import ru.frozen.gitextractor.model.Snapshot;
import ru.frozen.gitextractor.model.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class ScheduleHandler {

    void scheduleTask(Handler handler, Logger log, Snapshot snapshot, String delayParam, User user) throws FileNotFoundException, IOException {
        Properties properties = new Properties();
        InputStream inputStream = new FileInputStream("config.properties");
        properties.load(inputStream);

        Timer timer = new Timer("Timer");

        TimerTask task = new TimerTask() {
            public void run() {
                handler.commandHandle(log, snapshot, timer, ScheduleHandler.this, user);
            }
        };


        long delay = Long.valueOf(delayParam) * 60000;
        long period = Long.valueOf(properties.getProperty(snapshot.getRepoName())) * 60000 ;
        timer.schedule(task, delay, period);
    }

}
