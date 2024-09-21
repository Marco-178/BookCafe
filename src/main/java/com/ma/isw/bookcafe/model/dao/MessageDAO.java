package com.ma.isw.bookcafe.model.dao;

import com.ma.isw.bookcafe.model.mo.Message;
import java.util.List;
import java.util.Date;

public interface MessageDAO {
    public void addMessage(int messageId, Date creationTimestamp, String content, int userId, int threadId);

    public void updateMessage(Message message);

    public void deleteMessage(int messageId);

    public Message getMessageById(int messageId);

    public List<Message> getThreadMessages(int threadId);
}