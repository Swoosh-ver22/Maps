package com.example.anirudh.googlemaps.models

/**
 * Created by anirudh on 15/07/17.
 */
data class Directions(var status : String , var routes : Array<Route>){
    data class Route(var legs : Array<Leg>){
        data class Leg(var distance : Distance , var duration : Duration , var steps : Array<Step>){
            data class Distance(var text  :String )
            data class Duration(var text : String)
            data class Step(var end_location : LatLong , var start_location : LatLong){
                data class LatLong(var lat : Double , var lng : Double)
            }
        }
    }
}