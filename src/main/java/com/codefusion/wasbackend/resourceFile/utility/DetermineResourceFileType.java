package com.codefusion.wasbackend.resourceFile.utility;

public class DetermineResourceFileType {

    public static String determineFileType(String fileName) {
        String Type;
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

        Type = switch (extension) {
            case "png" -> "image/png";
            case "jpg", "jpeg" -> "image/jpeg";
            case "pdf" -> "application/pdf";
            case "csv" -> "text/csv";
            default -> "application/octet-stream";
        };

        return Type;
    }
}
