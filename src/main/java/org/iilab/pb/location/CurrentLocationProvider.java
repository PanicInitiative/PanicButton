package org.iilab.pb.location;

import org.iilab.pb.common.ApplicationSettings;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.Criteria;
import android.util.Log;

import static android.location.Criteria.ACCURACY_FINE;
import static android.location.LocationManager.NETWORK_PROVIDER;
import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.PASSIVE_PROVIDER;

public class CurrentLocationProvider extends LocationListenerAdapter {
    private Location currentLocation;

    public CurrentLocationProvider(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(ACCURACY_FINE);
        String provider = manager.getBestProvider(criteria, true);
        Location bestLocation;
        if (provider != null)
          bestLocation = manager.getLastKnownLocation(provider);
        else
          bestLocation = null;
        Location latestLocation = getLatest(bestLocation,
            manager.getLastKnownLocation(GPS_PROVIDER));
        Log.e("CurrentLocationProvider", "latestlocation GPS_PROVIDER = " + latestLocation);
        latestLocation = getLatest(latestLocation,
            manager.getLastKnownLocation(NETWORK_PROVIDER));
        Log.e("CurrentLocationProvider", "latestlocation NETWORK_PROVIDER = " + latestLocation);
        latestLocation = getLatest(latestLocation,
            manager.getLastKnownLocation(PASSIVE_PROVIDER));
        Log.e("CurrentLocationProvider", "latestlocation PASSIVE_PROVIDER = " + latestLocation);
        currentLocation = latestLocation;
        if (currentLocation != null) 
        	ApplicationSettings.setCurrentBestLocation(context, currentLocation);
        return;
    }

    private static Location getLatest(final Location location1,
	      final Location location2) {
	    if (location1 == null)
	      return location2;
	
	    if (location2 == null)
	      return location1;
	
	    if (location2.getTime() > location1.getTime())
	      return location2;
	    else
	      return location1;
	  }
    
    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
    }

    public Location getLocation() {
        return currentLocation;
    }
}
