package com.codefusion.wasbackend.base.utils;

import com.codefusion.wasbackend.base.model.BaseEntity;
import com.codefusion.wasbackend.resourceFile.service.ResourceFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProcessUploadFileService {

    private final ResourceFileService resourceFileService;

    public void processUpload(MultipartFile file, BaseEntity baseEntity) throws IOException {
        if (file != null && !file.isEmpty()) {
            resourceFileService.saveFile(file, baseEntity);
        }
    }
}
