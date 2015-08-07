package com.csu.partytime.util;

import com.amap.api.services.poisearch.PoiItemDetail;
import com.csu.partytime.model.Dining;
import com.csu.partytime.model.LatLonPoint;
import com.csu.partytime.model.PartyActivity;
import com.csu.partytime.model.Photo;

/**
 * Created by ${Chen_Xingchao} on 2015/4/24.
 */
public class ModelUtils {
    public static PartyActivity poiDetailItem2PartyActivity(PoiItemDetail detail) {
        PartyActivity activity = new PartyActivity();
        activity._id = detail.getPoiId();
        activity.title = detail.getTitle();
        activity.tel = detail.getTel();
        activity.distance = detail.getDistance();
        activity.snippet = detail.getSnippet();
        activity.cityName = detail.getCityName();
        activity.provinceName = detail.getProvinceName();
        activity.type = detail.getTypeDes();
        activity.isDiscounts = detail.isDiscountInfo();
        activity.dining = detail.getDining() == null ? null : parseDining(detail.getDining());
        activity.email = detail.getEmail();
        activity.isGroupBuy = detail.isGroupbuyInfo();
        activity.latLonPoint = parseLatLonPoint(detail.getLatLonPoint());
        return activity;
    }

    public static Dining parseDining(com.amap.api.services.poisearch.Dining dining) {
        Dining din = new Dining();
        din.addition = dining.getAddition();
        din.atmosphere = dining.getAtmosphere();
        din.cost = dining.getCost();
        din.rating = dining.getRating();
        din.cuisines = dining.getCuisines();
        din.cpRating = dining.getCpRating();
        din.deepSrc = dining.getDeepsrc();
        din.environmentRating = dining.getEnvironmentRating();
        din.intro = dining.getIntro();
        din.openTime = dining.getOpentime();
        din.openTimeGDF = dining.getOpentimeGDF();
        din.orderingAppUrl = dining.getOrderinAppUrl();
        din.orderingWapUrl = dining.getOrderingWapUrl();
        din.orderingWebUrl = dining.getOrderingWebUrl();
        for (com.amap.api.services.poisearch.Photo temp : dining.getPhotos()) {
            din.photos.add(parsePhoto(temp));
        }
        din.recommend = dining.getRecommend();
        din.serviceRating = dining.getServiceRating();
        din.tasteRating = dining.getTasteRating();
        din.tag = dining.getTag();
        din.isMealOrdering = dining.isMealOrdering();
        return din;
    }

    public static Photo parsePhoto(com.amap.api.services.poisearch.Photo p) {
        Photo photo = new Photo();
        photo.title = p.getTitle();
        photo.url = p.getUrl();
        return photo;
    }

    public static LatLonPoint parseLatLonPoint(com.amap.api.services.core.LatLonPoint latLonPoint) {
        LatLonPoint lonPoint = new LatLonPoint();
        lonPoint.Latitude = latLonPoint.getLatitude();
        lonPoint.longitude = latLonPoint.getLongitude();
        return lonPoint;
    }
}