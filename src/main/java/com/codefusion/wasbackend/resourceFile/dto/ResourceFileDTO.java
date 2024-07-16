package com.codefusion.wasbackend.resourceFile.dto;


public class ResourceFileDTO {
    private Long id;
    private byte[] data;
    private String fileName;
    private String contentType;  // Added to handle content type dynamically

    private Boolean isDeleted;

    public ResourceFileDTO(byte[] data, String fileName) {
        this.data = data;
        this.fileName = fileName;
    }


    public Long getId() {
        return id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
