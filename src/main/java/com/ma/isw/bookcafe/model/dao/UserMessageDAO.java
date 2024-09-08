package com.ma.isw.bookcafe.model.dao;

import com.ma.isw.bookcafe.model.mo.User;
import com.ma.isw.bookcafe.model.mo.UserMessage;
import java.util.List;
import java.util.Date;

public interface UserMessageDAO {
    public void addMessage(int messageId, Date creationTimestamp, String content, int userId, int threadId);

    public void updateMessage(UserMessage message);

    public void deleteMessage(int messageId);

    public UserMessage getMessageById(int messageId);

    public List<UserMessage> getAllMessages(int threadId);
}
