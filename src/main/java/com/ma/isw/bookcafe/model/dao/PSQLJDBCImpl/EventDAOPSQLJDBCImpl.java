package com.ma.isw.bookcafe.model.dao.PSQLJDBCImpl;

import com.ma.isw.bookcafe.model.dao.*;
import com.ma.isw.bookcafe.model.dao.exception.NoEventFoundException;
import com.ma.isw.bookcafe.model.dao.exception.NoThreadFoundException;
import com.ma.isw.bookcafe.model.mo.Event;
import com.ma.isw.bookcafe.model.mo.Message;
import com.ma.isw.bookcafe.model.mo.Thread;
import com.ma.isw.bookcafe.services.config.Configuration;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.time.LocalTime;

public class EventDAOPSQLJDBCImpl implements EventDAO{

    Connection conn;
    public EventDAOPSQLJDBCImpl(Connection conn) {this.conn = conn;}

    @Override
    public void addEvent(Event event) {

    }

    @Override
    public void updateEvent(Event event) {

    }

    @Override
    public void deleteEvent(int eventId) {

    }

    @Override
    public Event getEventById(int eventId) throws NoEventFoundException{
        Event event;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM club_event WHERE event_id=?";

            ps= conn.prepareStatement(sql);
            ps.setInt(1, eventId);

            rs = ps.executeQuery();

            if(rs.next()) {
                event = readEvent(rs);
            }
            else throw new NoEventFoundException("No_Event_in_DB");
        }
        catch(Exception e){
            if(e.getMessage().contains("No_Thread_in_DB")){
                throw new NoEventFoundException("Errore: evento non trovato");
            }
            throw new RuntimeException(e);
        }
        return event;
    }

    @Override
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            String sql = "SELECT * FROM club_event";

            ps = conn.prepareStatement(sql);

            rs = ps.executeQuery();

            while(rs.next()){
                Event event = EventDAOPSQLJDBCImpl.readEvent(rs);
                events.add(event);
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
        return events;
    }

    public static Event readEvent(ResultSet rs) {
        Event event = new Event();

        try {
            event.setEventId(rs.getInt("event_id"));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        try {
            event.setDateEvent(rs.getObject("date_event", LocalDate.class));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        try {
            event.setNation(rs.getString("nation"));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        try {
            event.setCity(rs.getString("city"));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        try {
            event.setStreet(rs.getString("street"));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        try {
            event.setScheduleStart(rs.getObject("schedule_start", LocalTime.class));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        try {
            event.setScheduleEnd(rs.getObject("schedule_end", LocalTime.class));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        try {
            event.setNumInterested(rs.getInt("num_interested"));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        try {
            event.setDescription(rs.getString("description"));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        try {
            event.setClubId(rs.getInt("club_id"));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        try {
            event.setDeleted(rs.getBoolean("deleted"));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        try {
            event.setEventName(rs.getString("eventname"));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return event;
    }

}
