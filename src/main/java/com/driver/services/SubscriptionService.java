package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay

        User user = userRepository.findById(subscriptionEntryDto.getUserId()).get();

        Subscription subscription = new Subscription();

        subscription.setUser(user);
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());

        Integer amount = 0;

        if(subscription.getSubscriptionType() == SubscriptionType.BASIC) {
            amount = 500 + 200 * subscription.getNoOfScreensSubscribed();
        } else if(subscription.getSubscriptionType() == SubscriptionType.PRO) {
            amount = 800 + 250 * subscription.getNoOfScreensSubscribed();
        } else if(subscription.getSubscriptionType() == SubscriptionType.ELITE) {
            amount = 1000 + 350 * subscription.getNoOfScreensSubscribed();
        }

        subscription.setTotalAmountPaid(amount);
        subscriptionRepository.save(subscription);
        user.setSubscription(subscription);
        userRepository.save(user);

        return amount;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        User user = userRepository.findById(userId).get();
        Subscription subscription = user.getSubscription();

        SubscriptionType currentSubscription = subscription.getSubscriptionType();
        SubscriptionType newSubscription;
        int newPrice;
        if(currentSubscription==SubscriptionType.ELITE){
            throw new Exception("Already the best Subscription");
        } else if (currentSubscription==SubscriptionType.PRO) {
            newSubscription = SubscriptionType.ELITE;
            newPrice = 1000 + 350*subscription.getNoOfScreensSubscribed();
        } else{
            newSubscription = SubscriptionType.PRO;
            newPrice =  800 + 250*subscription.getNoOfScreensSubscribed();
        }
        int currentPrice = subscription.getTotalAmountPaid();
        subscription.setSubscriptionType(newSubscription);
        subscription.setTotalAmountPaid(newPrice);
        Subscription savedSub = subscriptionRepository.save(subscription);

        user.setSubscription(savedSub);

        return newPrice-currentPrice;

    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        Integer totalRevenue = 0;
        for(Subscription subscription : subscriptions){
            totalRevenue += subscription.getTotalAmountPaid();
        }
        return totalRevenue;
    }

}
