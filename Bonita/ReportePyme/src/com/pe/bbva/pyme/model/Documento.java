package com.pe.bbva.pyme.model;

import java.io.Serializable;

public class Documento implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long tenantId;
    private Long id;
    private Long author;
    private Long creationDate;
    private boolean hasContent;
    private String filename;
    private String mimetype;
    private String url;
    private byte[] content;
    private Long processInstanceId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuthor() {
        return author;
    }

    public void setAuthor(Long author) {
        this.author = author;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isHasContent() {
        return hasContent;
    }

    public void setHasContent(boolean hasContent) {
        this.hasContent = hasContent;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public Long getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(Long processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Documento [");
        if (tenantId != null) {
            builder.append("tenantId=");
            builder.append(tenantId);
            builder.append(", ");
        }
        if (id != null) {
            builder.append("id=");
            builder.append(id);
            builder.append(", ");
        }
        if (author != null) {
            builder.append("author=");
            builder.append(author);
            builder.append(", ");
        }
        if (creationDate != null) {
            builder.append("creationDate=");
            builder.append(creationDate);
            builder.append(", ");
        }
        builder.append("hasContent=");
        builder.append(hasContent);
        builder.append(", ");
        if (filename != null) {
            builder.append("filename=");
            builder.append(filename);
            builder.append(", ");
        }
        if (mimetype != null) {
            builder.append("mimetype=");
            builder.append(mimetype);
            builder.append(", ");
        }
        if (url != null) {
            builder.append("url=");
            builder.append(url);
        }
        if (processInstanceId != null) {
            builder.append("processInstanceId=");
            builder.append(processInstanceId);
        }
        builder.append("]");
        return builder.toString();
    }

}
