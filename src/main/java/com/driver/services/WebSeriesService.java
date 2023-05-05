package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto){

        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo
        try {
            String seriesName = webSeriesEntryDto.getSeriesName();
            Integer productionHouseId = webSeriesEntryDto.getProductionHouseId();

            if (webSeriesRepository.findBySeriesName(seriesName) != null) {
                throw new Exception("Series is already present");
            }

            ProductionHouse productionHouse = productionHouseRepository.findById(productionHouseId).get();

            WebSeries webSeries = new WebSeries();

            webSeries.setSeriesName(seriesName);
            webSeries.setAgeLimit(webSeriesEntryDto.getAgeLimit());
            webSeries.setRating(webSeriesEntryDto.getRating());
            webSeries.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());
            webSeries.setProductionHouse(productionHouse);

            WebSeries savedWebSeries=webSeriesRepository.save(webSeries);

            productionHouse.getWebSeriesList().add(savedWebSeries);

            productionHouse.setRatings(updateRatings(productionHouse));
            productionHouseRepository.save(productionHouse);

            return webSeries.getId();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private double updateRatings(ProductionHouse productionHouse) {
        List<WebSeries> webSeriesList = webSeriesRepository.findByProductionHouse(productionHouse);
        if (webSeriesList.isEmpty()) {
            return -1; // no web series to compute the average rating
        }
        double totalRating = 0.0;
        for (WebSeries webSeries : webSeriesList) {
            totalRating += webSeries.getRating();
        }
        double avgRating = totalRating / webSeriesList.size();

        return avgRating;
    }

}
