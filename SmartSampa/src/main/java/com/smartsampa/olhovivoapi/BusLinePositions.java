package com.smartsampa.olhovivoapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartsampa.busapi2.model.Bus;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by ruan0408 on 12/02/2016.
 */
public class BusLinePositions {

    @JsonProperty("hr") public String currentTime;
    @JsonProperty("vs") public OlhovivoBus[] vehicles;

    public String getCurrentTime() {
        return currentTime;
    }

    public Bus[] getVehicles() {return vehicles;}

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("currentTime", currentTime)
                .append("vehicles", vehicles)
                .toString();
    }
}