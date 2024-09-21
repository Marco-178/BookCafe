package com.ma.isw.bookcafe.model.dao.PSQLJDBCImpl;

import java.sql.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ma.isw.bookcafe.model.mo.Message;
import com.ma.isw.bookcafe.model.dao.MessageDAO;

public class MessageDAOPSQLJDBCImpl implements MessageDAO {
    Connection conn;

    public MessageDAOPSQLJDBCImpl(Connection conn) { this.conn = conn; }

    @Override
    public void addMessage(int messageId, Date creationTimestamp, String content, int userId, int threadId) {

    }

    @Override
    public void updateMessage(Message message) {

    }

    @Override
    public void deleteMessage(int messageId) {

    }

    @Override
    public Message getMessageById(int messageId) {
        return null;
    }

    @Override
    public List<Message> getThreadMessages(int threadId) {
        List<Message> messages = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            String sql = "SELECT * FROM user_message where thread_id=?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, threadId);

            rs = ps.executeQuery();

            while(rs.next()){
                Message message = MessageDAOPSQLJDBCImpl.read(rs);
                messages.add(message);
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
        return messages;
    }

    public static Message read(ResultSet rs){
        Message message = new Message();

        try{
            message.setMessageId(rs.getInt("message_id"));
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }

        try{
            message.setCreationTimestamp(rs.getObject("creation_timestamp", LocalDateTime.class));
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }

        try{
            message.setContent(rs.getString("contenuto"));
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }

        try{
            message.setThreadId(rs.getInt("thread_id"));
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }

        try{
            message.setUserId(rs.getInt("user_id"));
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }

        try{
            message.setDeleted(rs.getBoolean("deleted"));
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }

        return message;
    }
}
