package com.ruoyi.business.process;

import com.ruoyi.business.domain.BaseHotel;
import com.ruoyi.business.service.impl.BaseHotelServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@EnableScheduling
public class HotelPositionAutoTask {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private BaseHotelServiceImpl baseHotelService;
    private final String key = "hotels";

    @Scheduled(initialDelay = 0,fixedDelay = (1000 * 60 * 60 * 2))
    public void pushPositions() {
        final List<BaseHotel> baseHotels = baseHotelService.selectBaseHotelList(new BaseHotel());
        final GeoOperations geo = redisTemplate.opsForGeo();
        for (BaseHotel baseHotel : baseHotels) {
            final List<Double> position = parsePosition(baseHotel.getLatlng());
            geo.add(key,new Point(position.get(1),position.get(0)),baseHotel.getHotelId());
            redisTemplate.expire(key,3, TimeUnit.HOURS);
        }
    }

    /**
     * 解析位置获得经纬度
     *
     * @param latlng latlng
     * @return {@link List}<{@link Double}>
     */
    private List<Double> parsePosition(String latlng) {
        final String[] split = latlng.split(",");
        final Double lat = Double.valueOf(split[0]);
        final Double lng = Double.valueOf(split[1]);
        final ArrayList<Double> doubles = new ArrayList<>();
        doubles.add(lat);
        doubles.add(lng);
        return doubles;
    }
}
