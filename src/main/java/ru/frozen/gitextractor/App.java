package ru.frozen.gitextractor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.frozen.gitextractor.service.handler.InputHandler;
import ru.frozen.gitextractor.web.GoogleDrive;

import java.io.File;

/**
 * This is the test application for extracting a content from GitHub.
 *
 */
public class App {

	private static final Logger log = LogManager.getLogger(App.class);

	public static void main(String[] args) {
		GoogleDrive googleDrive = new GoogleDrive();
		File googleTokens = new File("tokens/StoredCredential");
		if (!googleTokens.exists()) googleDrive.applicationRegistration();
		InputHandler inputHandler = new InputHandler();
		inputHandler.handle(log);
	}
}
