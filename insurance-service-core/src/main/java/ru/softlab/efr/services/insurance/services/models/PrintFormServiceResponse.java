package ru.softlab.efr.services.insurance.services.models;

import org.springframework.core.io.ByteArrayResource;

public class PrintFormServiceResponse {

    private String filename;
    private ByteArrayResource byteArrayResource;

    public String getFilename() {
        return this.filename;
    }
    public ByteArrayResource getByteArrayResource() {
        return this.byteArrayResource;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
    public void setByteArrayResource(ByteArrayResource byteArrayResource) {
        this.byteArrayResource = byteArrayResource;
    }
}
