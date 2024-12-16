package com.lion.demo.service;

import com.lion.demo.entity.Restaurant;
import com.lion.demo.entity.RestaurantDto;
import com.lion.demo.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.lion.demo.util.NlpUtil.buildMultiMatchQuery;

@Service
public class RestaurantService {
    public static final int PAGE_SIZE = 10;
    @Autowired private RestaurantRepository restaurantRepository;
    @Autowired private ElasticsearchTemplate elasticsearchTemplate;

    public Restaurant findById(String id) {
        return restaurantRepository.findById(id).orElse(null);
    }

    public void insertRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }

    public Page<RestaurantDto> getPagedRestaurants(int page, String field, String keyword) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        Query query = NativeQuery.builder()
                .withQuery(buildMultiMatchQuery(field, keyword))
                .withPageable(PageRequest.of(page - 1, PAGE_SIZE))
                .build();
        SearchHits<Restaurant> searchHits = elasticsearchTemplate.search(query, Restaurant.class);
        List<RestaurantDto> restaurantDtoList = searchHits
                .getSearchHits()
                .stream()
                .map(hit -> {
                    Restaurant restaurant = hit.getContent();
                    double sunOfReviewScore = 0.0;
                    for (Map<String, Object> map: restaurant.getReviews()) {
                        sunOfReviewScore += Integer.parseInt((String) map.get("score"));
                    }
                    return RestaurantDto.builder()
                            .restaurant(restaurant)
                            .infoCount(restaurant.getInfo().size())
                            .reviewCount(restaurant.getReviews().size())
                            .reviewScore(restaurant.getReviews().size() == 0 ? 0.0 : sunOfReviewScore/restaurant.getReviews().size())
                            .matchScore(hit.getScore())
                            .build();
                })
                .toList();

        long totalHits = searchHits.getTotalHits();
        return new PageImpl<>(restaurantDtoList, pageable, totalHits);
    }
}