package com.ma.isw.bookcafe.model.mo;

import java.time.LocalDate;

public class SubscribedTo {
    private int clubId;
    private int userId;
    private LocalDate clubSubscriptionDate;
    private boolean deleted;

    // Getters and Setters
    public int getClubId() {
        return clubId;
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDate getClubSubscriptionDate() {
        return clubSubscriptionDate;
    }

    public void setClubSubscriptionDate(LocalDate clubSubscriptionDate) {
        this.clubSubscriptionDate = clubSubscriptionDate;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
