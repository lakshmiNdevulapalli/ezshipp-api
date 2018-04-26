package com.ezshipp.api.pubnub;

import com.ezshipp.api.document.Driver;
import com.ezshipp.api.model.BikerDistance;
import com.ezshipp.api.model.MatrixDistance;
import com.ezshipp.api.model.pubnub.DriverChannelMessage;
import com.ezshipp.api.service.BikerService;
import com.ezshipp.api.service.GoogleMapService;
import com.google.maps.model.LatLng;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Scope("prototype")
public class ProcessMessages implements Runnable {
    @Inject
    private GoogleMapService googleMapService;

    @Inject
    private BikerService bikerService;

    private DriverChannelMessage driverChannelMessage;

    public void setOrder(DriverChannelMessage driverChannelMessage)   {
        this.driverChannelMessage = driverChannelMessage;
    }

    public static Map<String, BikerDistance> driverDistances = new HashMap<>();

    @Override
    public void run() {
        try {
            String driverId = driverChannelMessage.getDriverid();
            String driverName = driverChannelMessage.getFname();
            LatLng newLocation = new LatLng(driverChannelMessage.getLt(), driverChannelMessage.getLg());
            if (driverDistances.containsKey(driverId)) {
                BikerDistance bikerDistance = driverDistances.get(driverId);
                List<MatrixDistance> matrixDistanceList = googleMapService.calculateDistance(new LatLng[]{bikerDistance.getLastLatLng()}, new LatLng[]{newLocation});
                if (!CollectionUtils.isEmpty(matrixDistanceList)) {
                    Double totalDistance = bikerDistance.getTotalDistance() + matrixDistanceList.get(0).getDistance();
                    if (totalDistance > 1) {
                        log.info(driverName + " -> " + String.valueOf(totalDistance));
                    }
                    bikerDistance.setTotalDistance(totalDistance);
                    bikerDistance.setLastLatLng(newLocation);
                    bikerDistance.setCurrentDistance(matrixDistanceList.get(0).getDistance());
                    driverDistances.put(driverId, bikerDistance);
                }
            } else  {
                Driver driver = bikerService.findByDriverId(driverId);
                BikerDistance newBikerDistance = new BikerDistance();
                newBikerDistance.setLastLatLng(newLocation);
                newBikerDistance.setCurrentLatLng(newLocation);
                newBikerDistance.setDriverId(driverId);
                newBikerDistance.setName(driver.getName() + " " + driver.getLname());
                newBikerDistance.setCurrentDistance(0.00);
                newBikerDistance.setTotalDistance(0.00);
                driverDistances.put(driverId, newBikerDistance);
            }
        } catch(Exception e)    {
            e.printStackTrace();
        }
    }
}
