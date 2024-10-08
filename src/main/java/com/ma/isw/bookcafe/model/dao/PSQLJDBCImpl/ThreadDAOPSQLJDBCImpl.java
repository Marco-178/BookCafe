package com.ma.isw.bookcafe.model.dao.PSQLJDBCImpl;

import com.ma.isw.bookcafe.model.dao.ThreadDAO;
import com.ma.isw.bookcafe.model.dao.exception.NoThreadFoundException;
import com.ma.isw.bookcafe.model.mo.Message;
import com.ma.isw.bookcafe.model.mo.Thread;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ma.isw.bookcafe.controller.UserAccessManagement.formatLocalDateTime;


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
    public Thread getThreadById(int threadId) throws NoThreadFoundException {
        Thread thread;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM thread WHERE thread_id=?";

            ps= conn.prepareStatement(sql);
            ps.setInt(1, threadId);

            rs = ps.executeQuery();

            if(rs.next()) {
                thread = readThread(rs, threadId);
            }
            else throw new NoThreadFoundException("No_Thread_in_DB");
        }
        catch(Exception e){
            if(e.getMessage().contains("No_Thread_in_DB")){
                throw new NoThreadFoundException("Errore: discussione non trovata");
            }
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
    @Override
    public int countTotalMessages(int threadId) {
        int messageCount = 0;
        PreparedStatement ps;
        String sql = "SELECT COUNT(*) FROM user_message WHERE thread_id = ? AND deleted = false";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, threadId); // Imposta il threadId nella query

            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                messageCount = resultSet.getInt(1); // Ottiene il conteggio dei messaggi
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Gestisci eventuali eccezioni
        }

        return messageCount; // Restituisce il numero totale di messaggi
    }


    @Override
    public List<String> getFormattedCreationTimestamps(List<Message> messages) {
        List<String> formattedCreationTimestamps = new ArrayList<>();
        for(Message message : messages){
            formattedCreationTimestamps.add(formatLocalDateTime(message.getCreationTimestamp()));
        }
        return formattedCreationTimestamps;
    }

    public List<String> getThreadsFormattedCreationTimestamps(List<Thread> threads) {
        List<String> formattedCreationTimestamps = new ArrayList<>();
        for(Thread thread : threads){
            formattedCreationTimestamps.add(formatLocalDateTime(thread.getCreationTimestamp()));
        }
        return formattedCreationTimestamps;
    }

    private Thread readThread(ResultSet rs, int threadId) throws SQLException {
        LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        LocalDateTime timestampLastReply = rs.getTimestamp("timestamp_last_reply").toLocalDateTime();
        String category = rs.getString("category");
        String content = rs.getString("contenuto");
        int userId = rs.getInt("user_id");
        boolean deleted = rs.getBoolean("deleted");
        String title = rs.getString("title");

        Thread thread = new Thread();
        thread.setThreadId(threadId);
        thread.setTitle(title);
        thread.setCreationTimestamp(creationTimestamp);
        thread.setTimestampLastReply(timestampLastReply);
        thread.setCategory(category);
        thread.setContent(content);
        thread.setUserId(userId);
        thread.setDeleted(deleted);

        return thread;
    }
}
