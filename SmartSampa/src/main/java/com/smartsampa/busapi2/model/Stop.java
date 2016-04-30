package com.smartsampa.busapi2.model;

import com.smartsampa.utils.Point;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ruan0408 on 28/04/2016.
 */
public interface Stop {

    Map<Trip, List<PredictedBus>> getAllPredictions();

    List<PredictedBus> getPredictedBusesOfTrip(Trip trip);

    Set<Trip> getTrips();

    Integer getId();

    String getName();

    String getReference();

    String getAddress();

    Point getLocation();
}