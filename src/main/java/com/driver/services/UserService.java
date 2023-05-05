package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){
        try {
            userRepository.save(user);
            return user.getId();
        } catch (Exception e) {
            // Handle duplicate email exception
            throw new RuntimeException();
        }
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId) {

        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository
        User user = userRepository.findById(userId).get();

        int ageLimit = user.getAge();
        SubscriptionType subscriptionType = user.getSubscription().getSubscriptionType();

        List<WebSeries> webSeriesList = webSeriesRepository.findAll();

        int count = 0;

        if(user.getSubscription().getSubscriptionType()==SubscriptionType.ELITE){
            count = webSeriesList.size();
        }
        else {
            for (WebSeries webSeries : webSeriesList) {
                if (webSeries.getAgeLimit() <= ageLimit) {
                    // If user's age is within the age limit of the web series
                    if (webSeries.getSubscriptionType() == SubscriptionType.BASIC ||
                            webSeries.getSubscriptionType() == subscriptionType) {
                        // If the web series is available for the user's subscription type
                        count++;
                    }
                }
            }
        }
        return count;
    }
}
