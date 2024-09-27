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
    public void addMessage(Message message) {
        PreparedStatement ps = null;

        try {
            String sql = "INSERT INTO user_message "
                    + "(creation_timestamp, contenuto, thread_id, user_id, deleted) "
                    + "VALUES (?,?,?,?,?) "
                    + "RETURNING message_id";

            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            ps.setObject(i++, message.getCreationTimestamp());
            ps.setString(i++, message.getContent());
            ps.setInt(i++, message.getThreadId());
            ps.setInt(i++, message.getUserId());
            ps.setBoolean(i++, message.isDeleted());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int messageId = rs.getInt(1);
                        message.setMessageId(messageId);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'inserimento del messaggio nel database", e);
        }
    }


    @Override
    public void updateMessage(Message message) {

    }

    @Override
    public void deleteMessage(int messageId) {
        PreparedStatement ps = null;

        try {
            String sql = "DELETE FROM user_message WHERE message_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, messageId);

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new RuntimeException("Nessun messaggio trovato con l'ID: " + messageId);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'eliminazione del messaggio dal database", e);
        }
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
