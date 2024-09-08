package com.ma.isw.bookcafe.model.dao;

import com.ma.isw.bookcafe.model.mo.Thread;

import java.util.List;
import java.util.Date;

public interface ThreadDAO {

    public void addThread(int threadId, Date creationTimestamp, Date timestampLastReply, String category, String content, int userId, int clubId);

    public void updateThread(Thread thread);

    public void deleteThread(int threadId);

    public Thread getThreadById(int threadId);

    public List<Thread> getAllThreads(int clubId);
}
