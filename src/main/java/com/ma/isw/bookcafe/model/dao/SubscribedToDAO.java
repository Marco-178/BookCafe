package com.ma.isw.bookcafe.model.dao;

import java.util.List;
import java.util.Date;

import com.ma.isw.bookcafe.model.mo.SubscribedTo;

public interface SubscribedToDAO {

    void addSubscription(int clubId, int userId, Date clubSubscriptionDate);

    void deleteSubscription(int clubId, int userId);

    List<SubscribedTo> getAllSubscriptions();

    int countTotalSubscriptions();

    List<SubscribedTo> getSubscriptionsByUserId(int userId);

    List<SubscribedTo> getSubscriptionsByClubId(int clubId);
}
