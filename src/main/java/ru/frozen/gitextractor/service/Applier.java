package ru.frozen.gitextractor.service;

import org.eclipse.egit.github.core.RepositoryContents;

import java.io.IOException;

public interface Applier {

	void startDownloading();

	void apply(RepositoryContents e) throws IOException;

}
