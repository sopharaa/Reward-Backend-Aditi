package com.phara.pontrix_backend.service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudStorageService {

    /**
     * Upload a file to cloud storage
     * @param file The file to upload
     * @param folder The folder path (e.g., "rewards", "companies")
     * @return The public URL of the uploaded file
     */
    String uploadFile(MultipartFile file, String folder);

    /**
     * Delete a file from cloud storage
     * @param fileUrl The public URL of the file to delete
     * @return true if deleted successfully, false otherwise
     */
    boolean deleteFile(String fileUrl);

    /**
     * Get file name from URL
     * @param fileUrl The public URL
     * @return The file name
     */
    String getFileNameFromUrl(String fileUrl);
}

