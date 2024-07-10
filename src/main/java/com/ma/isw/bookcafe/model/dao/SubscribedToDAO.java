package com.ma.isw.bookcafe.model.dao;

import java.util.List;
import java.util.Date;

import com.ma.isw.bookcafe.model.mo.SubscribedTo;

public interface SubscribedToDAO {

    public void addSubscription(int clubId, int userId, Date clubSubscriptionDate);

    public void deleteSubscription(int clubId, int userId);

    public List<SubscribedTo> getAllSubscriptions();

    public List<SubscribedTo> getSubscriptionsByUserId(int userId);

    public List<SubscribedTo> getSubscriptionsByClubId(int clubId);
}
