package ru.ibsqa.chameleon.utils.file;

import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileUtils {

    public List<File> getFilesInDirectory(String path){
        List<File> files = new ArrayList<>();
        fileList(path, files);
        return files;
    }

    private void fileList(String path, List<File> files){
        File directory = new File(path);

        File[] fList = directory.listFiles();
        if (null != fList) {
            for (File file : fList) {
                if (file.isFile()) {
                    files.add(file);
                } else if (file.isDirectory()) {
                    fileList(file.getAbsolutePath(), files);
                }
            }
        }
    }

}
