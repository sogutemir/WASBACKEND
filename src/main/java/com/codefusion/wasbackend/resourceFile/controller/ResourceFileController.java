package com.codefusion.wasbackend.resourceFile.controller;


import com.codefusion.wasbackend.resourceFile.dto.ResourceFileDTO;
import com.codefusion.wasbackend.resourceFile.service.ResourceFileService;
import com.codefusion.wasbackend.resourceFile.utility.DetermineResourceFileType;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resourceFile")
public class ResourceFileController {
    private final ResourceFileService resourceFileService;

    /**
     * Downloads a file from the file repository based on the specified file ID.
     *
     * @param fileId the ID of the file to be downloaded
     * @return a ResponseEntity containing the downloaded file data and filename
     * @throws FileNotFoundException if the file with the specified ID is not found in the repository
     */
    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long fileId) throws FileNotFoundException {
        ResponseEntity.BodyBuilder responseBuilder = resourceFileService.retrieveResourceFile(fileId);
        ResourceFileDTO fileDto = resourceFileService.downloadFile(fileId);
        return responseBuilder
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getFileName() + "\"")
                .body(fileDto.getData());
    }

    /**
     * Retrieve and serve an image file from the resource file repository.
     *
     * @param fileId the ID of the image file to be served
     * @return a ResponseEntity with the image file as a Resource and appropriate headers
     * @throws FileNotFoundException if the image file with the specified ID is not found in the repository
     */
    @GetMapping("/image/{fileId}")
    public ResponseEntity<Resource> serveImage(@PathVariable Long fileId) throws FileNotFoundException {
        ResponseEntity.BodyBuilder responseBuilder = resourceFileService.retrieveResourceFile(fileId);
        ResourceFileDTO fileDto = resourceFileService.downloadFile(fileId);
        return responseBuilder
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileDto.getFileName() + "\"")
                .body(new ByteArrayResource(fileDto.getData()));
    }

    /**
     * Retrieves the URL of an image given its file ID.
     *
     * @param fileId the ID of the image file
     * @return the URL of the image file
     * @throws FileNotFoundException if the file with the provided ID does not exist
     */
    @GetMapping("/imageUrl/{fileId}")
    public ResponseEntity<String> getImageUrl(@PathVariable Long fileId) throws FileNotFoundException {
        String fileName = resourceFileService.getFileName(fileId);
        String fileUrl = "/images/" + fileName;
        return ResponseEntity.ok().body(fileUrl);
    }

}

