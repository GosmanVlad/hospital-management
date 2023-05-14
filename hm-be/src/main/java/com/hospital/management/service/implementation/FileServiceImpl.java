package com.hospital.management.service.implementation;

import com.hospital.management.service.util.FileServiceUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileServiceImpl implements FileServiceUtil {

    @Value("${repository-path}")
    private String repositoryPath;

    @Override
    public String store(MultipartFile file, String savePath, String fileName) {
        try {
            Path path = Paths.get(savePath);

            Files.copy(file.getInputStream(), path.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            return savePath + "/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MediaType getMediaTypeForFileName(ServletContext servletContext, String fileName) {
        String mineType = servletContext.getMimeType(fileName);
        try {
            MediaType mediaType = MediaType.parseMediaType(mineType);
            return mediaType;
        } catch (Exception e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    @Override
    public String createDirectoriesAndSaveFile(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String pathToFolder = this.repositoryPath + "upload/";
            createPathIfNotExist(pathToFolder);
            return store(file, pathToFolder, fileName);
        } catch (Exception exception) {
            System.out.println(exception);
        }
        return null;
    }

    public void createPathIfNotExist(String path) {
        File file = new File(path);
        if (!Files.exists(Paths.get(path)))
            file.mkdir();
    }
}
