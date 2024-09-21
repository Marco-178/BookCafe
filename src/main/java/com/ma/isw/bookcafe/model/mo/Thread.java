package com.ma.isw.bookcafe.model.mo;

import java.time.LocalDateTime;

public class Thread {
    private int threadId;
    private LocalDateTime creationTimestamp;
    private LocalDateTime timestampLastReply;
    private String category;
    private String content;
    private int userId;
    private boolean deleted;
    private int clubId;

    // Getters and Setters
    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public LocalDateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(LocalDateTime creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public LocalDateTime getTimestampLastReply() {
        return timestampLastReply;
    }

    public void setTimestampLastReply(LocalDateTime timestampLastReply) {
        this.timestampLastReply = timestampLastReply;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getClubId() {
        return userId;
    }

    public void setClubId(int userId) {
        this.userId = userId;
    }
}

