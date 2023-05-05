package com.driver.EntryDto;

import com.driver.model.SubscriptionType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;


public class SubscriptionEntryDto {

    private int userId;
    @Enumerated(EnumType.STRING)
    private SubscriptionType subscriptionType;
    private int noOfScreensRequired;

    public SubscriptionEntryDto() {
    }

    public SubscriptionEntryDto(int userId, SubscriptionType subscriptionType, int noOfScreensRequired) {
        this.userId = userId;
        this.subscriptionType = subscriptionType;
        this.noOfScreensRequired = noOfScreensRequired;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public int getNoOfScreensRequired() {
        return noOfScreensRequired;
    }

    public void setNoOfScreensRequired(int noOfScreensRequired) {
        this.noOfScreensRequired = noOfScreensRequired;
    }
}
