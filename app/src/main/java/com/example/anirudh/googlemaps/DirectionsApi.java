package com.example.anirudh.googlemaps;

import com.example.anirudh.googlemaps.models.Directions;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by anirudh on 15/07/17.
 */

public interface DirectionsApi {

    @GET("maps/api/directions/json")
    Call<Directions> getDirections(
            @Query("origin") String coordinates ,
            @Query("destination") String coordinatesDestination ,
            @Header("key") String appKey
    ) ;

}
