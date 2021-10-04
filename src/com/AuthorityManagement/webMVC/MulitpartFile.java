package com.AuthorityManagement.webMVC;

import java.io.InputStream;

public class MulitpartFile {
    private String fileName;
    private long size;
    private String contentType;
    private InputStream inputStream;

    public MulitpartFile() {
    }

    public MulitpartFile(String fileName, long size, String contentType, InputStream inputStream) {
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
        this.inputStream = inputStream;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
