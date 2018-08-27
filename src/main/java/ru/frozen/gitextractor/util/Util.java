package ru.frozen.gitextractor.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Util {

    public List<String> getFileList(String sourceFolder){
        List<String> fileList = new ArrayList<>();
        File node1 = new File(sourceFolder);
        return generateFileList(sourceFolder, fileList, node1);
    }

    private List<String> generateFileList(String sourceFolder, List<String> fileList, File node) {

        if (node.isFile()) {
            fileList.add(generateZipEntry(sourceFolder, node.getAbsoluteFile().toString()));
        }

        if (node.isDirectory()) {
            String[] subNote = node.list();
            for (String filename : subNote) {
                generateFileList(sourceFolder, fileList, new File(node, filename));
            }
        }
        return fileList;
    }

    private String generateZipEntry(String sourceFolder, String file){
        return file.substring(sourceFolder.length()+1, file.length());
    }

    public Boolean compareFiles(String fileNameOld, String fileNameNew) throws IOException {
        Boolean isSame = true;
        BufferedInputStream fis1 = new BufferedInputStream(new FileInputStream(fileNameOld));
        BufferedInputStream fis2 = new BufferedInputStream(new FileInputStream(fileNameNew));
        int b1 = 0, b2 = 0, pos = 1;
        while (b1 != -1 && b2 != -1) {
            if (b1 != b2) {
                isSame = false;
            }
            pos++;
            b1 = fis1.read();
            b2 = fis2.read();
        }
        if (b1 != b2) {
            isSame = false;
        }
        fis1.close();
        fis2.close();
        return isSame;
    }
}
