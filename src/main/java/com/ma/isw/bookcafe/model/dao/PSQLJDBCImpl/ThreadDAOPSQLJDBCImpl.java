package com.ma.isw.bookcafe.model.dao.PSQLJDBCImpl;

import com.ma.isw.bookcafe.model.dao.ThreadDAO;
import com.ma.isw.bookcafe.model.mo.Thread;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ThreadDAOPSQLJDBCImpl implements ThreadDAO {

    Connection conn;
    public ThreadDAOPSQLJDBCImpl(Connection conn) {this.conn = conn;}

    @Override
    public void addThread(int threadId, Date creationTimestamp, Date timestampLastReply, String category, String content, int userId, int clubId) {

    }

    @Override
    public void updateThread(Thread thread) {

    }

    @Override
    public void deleteThread(int threadId) {

    }

    @Override
    public Thread getThreadById(int threadId) {
        Thread thread;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM thread WHERE thread_id=?";

            ps= conn.prepareStatement(sql);
            ps.setInt(1, threadId);

            rs = ps.executeQuery();

            thread = readThread(rs, threadId);
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
        return thread;
    }

    @Override
    public List<Thread> getAllThreads(int clubId) {
        List<Thread> threads = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM thread WHERE club_id=?";

            ps= conn.prepareStatement(sql);
            ps.setInt(1, clubId);

            rs = ps.executeQuery();

            while (rs.next()) {
                int threadId = rs.getInt("thread_id");
                Thread thread = readThread(rs, threadId);
                threads.add(thread);
            }
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
        return threads;
    }

    private Thread readThread(ResultSet rs, int threadId) throws SQLException {
        LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        LocalDateTime timestampLastReply = rs.getTimestamp("timestamp_last_reply").toLocalDateTime();
        String category = rs.getString("category");
        String contenuto = rs.getString("contenuto");
        int userId = rs.getInt("user_id");
        boolean deleted = rs.getBoolean("deleted");

        Thread thread = new Thread();
        thread.setThreadId(threadId);
        thread.setCreationTimestamp(creationTimestamp);
        thread.setTimestampLastReply(timestampLastReply);
        thread.setCategory(category);
        thread.setContenuto(contenuto);
        thread.setUserId(userId);
        thread.setDeleted(deleted);

        return thread;
    }
}
