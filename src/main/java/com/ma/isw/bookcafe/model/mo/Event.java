package com.ma.isw.bookcafe.model.mo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Event {
    private int eventId;
    private LocalDate dateEvent;
    private String nation;
    private String city;
    private String street;
    private LocalTime scheduleStart;
    private LocalTime scheduleEnd;
    private int numInterested;
    private String description;
    private int clubId;
    private boolean deleted;
    private String eventName;

    // Getters and Setters
    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public LocalDate getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(LocalDate dateEvent) {
        this.dateEvent = dateEvent;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public LocalTime getScheduleStart() {
        return scheduleStart;
    }

    public void setScheduleStart(LocalTime scheduleStart) {
        this.scheduleStart = scheduleStart;
    }

    public LocalTime getScheduleEnd() {
        return scheduleEnd;
    }

    public void setScheduleEnd(LocalTime scheduleEnd) {
        this.scheduleEnd = scheduleEnd;
    }

    public int getNumInterested() {
        return numInterested;
    }

    public void setNumInterested(int numInterested) {
        this.numInterested = numInterested;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getClubId() {
        return clubId;
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}

