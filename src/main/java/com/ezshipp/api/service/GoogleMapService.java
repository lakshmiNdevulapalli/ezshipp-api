package com.ezshipp.api.service;

import com.ezshipp.api.config.ApplicationPropertyConfig;
import com.ezshipp.api.exception.ServiceException;
import com.ezshipp.api.exception.ServiceExceptionCode;
import com.ezshipp.api.model.MatrixDistance;
import com.google.maps.DirectionsApi;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoogleMapService {

    private static final String STATUS_OK = "OK";
    private static final String EN_LANGUAGE = "en-EN";
    private static final int DECIMAL_POINTS = 2;
    private static final int THOUSAND = 1000;
    private static final int SIXTY = 60;

    @Inject
    private ApplicationPropertyConfig applicationPropertyConfig;

    public List<MatrixDistance> calculateDistance(LatLng[] origins, LatLng[] destinations) throws ServiceException {
        GeoApiContext context = new GeoApiContext().setApiKey(applicationPropertyConfig.getGoogleApiKey()).setQueryRateLimit(10);
        List<MatrixDistance> matrixDistanceList = new ArrayList<>();

        try {
            DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(context);
            DistanceMatrix matrix = req.origins(origins)
                    .destinations(destinations)
                    .mode(TravelMode.DRIVING)
                    .avoid(DirectionsApi.RouteRestriction.HIGHWAYS)
                    .language(EN_LANGUAGE)
                    .await();

//            System.out.println(origins[0]);
//            System.out.println(destinations[0]);

            for (DistanceMatrixRow row : matrix.rows) {
                if (row.elements.length > 0 && row.elements[0].status.equalsIgnoreCase(STATUS_OK)) {
                    BigDecimal distApart = new BigDecimal(row.elements[0].distance.inMeters);
                    BigDecimal kms = distApart.divide(new BigDecimal(THOUSAND), DECIMAL_POINTS, RoundingMode.HALF_EVEN);
                    BigDecimal durationInMins = new BigDecimal(row.elements[0].duration.inSeconds)
                            .divide(new BigDecimal(SIXTY), DECIMAL_POINTS, RoundingMode.HALF_EVEN);

                    matrixDistanceList.add(new MatrixDistance(kms.doubleValue(), durationInMins.doubleValue()));
                }
            }

        } catch(ApiException e){
            e.printStackTrace();
            throw new ServiceException(ServiceExceptionCode.INVALID_LONGITUDE_LATITUDE);
        } catch(Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new ServiceException(ServiceExceptionCode.INVALID_LONGITUDE_LATITUDE);
        }

        return matrixDistanceList;
    }

}
