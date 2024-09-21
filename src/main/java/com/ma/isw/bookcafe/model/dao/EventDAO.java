package com.ma.isw.bookcafe.model.dao;

import com.ma.isw.bookcafe.model.dao.exception.NoEventFoundException;
import com.ma.isw.bookcafe.model.mo.Event;
import java.util.List;
import java.util.Date;

public interface EventDAO {

    public void addEvent(Event event);

    public void updateEvent(Event event);

    public void deleteEvent(int eventId);

    public Event getEventById(int eventId) throws NoEventFoundException;

    public List<Event> getAllEvents();
}
