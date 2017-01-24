package com.forsrc.utils;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

/**
 * The type Git utils.
 */
public class GitUtils {

    /**
     * Gets repository.
     *
     * @param path the path
     * @return the repository
     * @throws IOException the io exception
     */
    public static Repository getRepository(File path) throws IOException {
        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        repositoryBuilder.setMustExist(true);
        repositoryBuilder.setGitDir(path);
        return repositoryBuilder.build();
    }

    /**
     * Gets git.
     *
     * @param path the path
     * @return the git
     * @throws IOException the io exception
     */
    public static Git getGit(File path) throws IOException {
        return Git.open(path);
    }
}
