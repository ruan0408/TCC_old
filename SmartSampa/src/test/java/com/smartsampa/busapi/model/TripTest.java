package com.smartsampa.busapi.model;

import com.smartsampa.busapi.impl.BusAPI;
import com.smartsampa.utils.Point;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static com.smartsampa.busapi.model.BusAPITestUtils.isAfter4amAndBeforeMidnight;
import static com.smartsampa.busapi.model.Heading.MAIN_TERMINAL;
import static com.smartsampa.busapi.model.Heading.SECONDARY_TERMINAL;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by ruan0408 on 13/04/2016.
 */
public class TripTest {

    @BeforeClass
    public static void setUp() {
        BusAPITestUtils.setUpDataSources();
    }
    //TODO test get cirular trip, non circular, hyphenated names


    //TODO maybe refactor this test
    @Test
    public void testGetTripsByOneTerm() throws Exception {
        Set<Trip> trips = BusAPI.getTripsByTerm("regina");
        assertTrue(trips.size() == 2);
        assertTrue(trips.stream().allMatch(t -> t.isCircular() && t.getNumberSign().equals("2732-10")));
        assertTrue(trips.stream().anyMatch(t -> containsIgnoreCase( t.getDestinationSign(),"vila regina") &&
                                                                    t.getHeading() == SECONDARY_TERMINAL));
        assertTrue(trips.stream().anyMatch(t -> containsIgnoreCase( t.getDestinationSign(),"artur alvim") &&
                                                                    t.getHeading() == MAIN_TERMINAL));
    }

    @Test
    public void testGetTripByTwoTerms() throws Exception {
        Set<Trip> trips = BusAPI.getTripsByTerm("vila regina");
        assertTrue(trips.size() == 2);
        assertTrue(trips.stream().allMatch(t -> t.isCircular() && t.getNumberSign().equals("2732-10")));
        assertTrue(trips.stream().anyMatch(t -> containsIgnoreCase( t.getDestinationSign(), "vila regina") &&
                                                                    t.getHeading() == SECONDARY_TERMINAL));
        assertTrue(trips.stream().anyMatch(t -> containsIgnoreCase( t.getDestinationSign(),"artur alvim") &&
                                                                    t.getHeading() == MAIN_TERMINAL));
    }

    @Test
    public void testGetTrip() throws Exception {
        Trip bonifacio = BusAPI.getTrip("273L-10", SECONDARY_TERMINAL);
        assertThat(bonifacio.getDestinationSign(), containsStringIgnoringCase("Metrô Artur Alvim"));
        assertThat(bonifacio.getNumberSign(), containsString("273L-10"));
        assertEquals(bonifacio.getHeading(), Heading.SECONDARY_TERMINAL);
    }

    @Test
    public void testGetStops() throws Exception {
        Trip butanta8022 = BusAPI.getTrip("8022-10", MAIN_TERMINAL);
        List<Stop> stops = butanta8022.getStops();
        assertFalse(stops.isEmpty());
    }

    @Test
    public void testGetWorkingDays() throws Exception {
        Trip usp8012 = BusAPI.getTrip("8012-10", SECONDARY_TERMINAL);
        assertEquals("USD", usp8012.getWorkingDays());
    }

    //TODO create iterator for shape to iterate over the two lists at the same time
    @Test
    public void testGetShape() throws Exception {
        Trip usp8012 = BusAPI.getTrip("8012-10", SECONDARY_TERMINAL);
        Point[] points = usp8012.getShape().getPoints();
        assumeNotNull(points);
        assertEquals(419, points.length);
        assertNotNull(usp8012.getShape().getTraveledDistances());
    }

    @Test
    public void testGetAllRunningBuses() throws ExecutionException {
        Trip alvimPerua = BusAPI.getTrip("2732-10", MAIN_TERMINAL);
        Set<Bus> buses = alvimPerua.getAllRunningBuses();
        assertFalse(buses.isEmpty());
        assertTrue(buses.size() > 0);
    }

    @Test
    public void testGetFarePrice() throws Exception {
        Trip penha = BusAPI.getTrip("263J-10", MAIN_TERMINAL);
        assertEquals(3.80, penha.getFarePrice(), 0.0001);
    }

    @Test
    public void testGetDepartureIntervalAtTime() throws Exception {
        Trip barroBranco = BusAPI.getTrip("407L-10", MAIN_TERMINAL);
        assertEquals(600, barroBranco.getDepartureIntervalInSecondsAtTime("14:30"));
    }

    //TODO test that the stops are complete(find a trip that has a stop which olhovivo knows its address)
    @Test
    public void testGetAllPredictions() throws Exception {
        assumeTrue(isAfter4amAndBeforeMidnight());
        Trip alvim = BusAPI.getTrip("273L-10", SECONDARY_TERMINAL);
        Map<Stop, List<PredictedBus>> predictions = alvim.getAllPredictions();
        assumeNotNull(predictions);
        assertTrue(predictions.size() > 0);
        assertTrue(predictions.values().stream().anyMatch(buses -> !buses.isEmpty()));
//        assertTrue(predictions.keySet().stream().anyMatch(stop -> stop.getAddress() != null));
    }

    @Test
    public void testGetPredictionsAtStop() throws Exception {
        assumeTrue(isAfter4amAndBeforeMidnight());

        Trip alvimPerua = BusAPI.getTrip("2732-10", MAIN_TERMINAL);
        AbstractStop campanella = mock(AbstractStop.class);
        when(campanella.getId()).thenReturn(360004869);
        List<PredictedBus> buses = alvimPerua.getPredictionsAtStop(campanella);

        assertFalse(buses.isEmpty());
    }
}