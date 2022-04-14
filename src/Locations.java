
import java.util.ArrayList;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author admin
 */
public class Locations {
    private Map<Integer,RouteDetails> locations;

    public Map<Integer, RouteDetails> getLocations() {
        return locations;
    }

    public void setLocations(Map<Integer, RouteDetails> locations) {
        this.locations = locations;
    }

    @Override
    public String toString() {
        return "Locations{" + "locations=" + locations + '}';
    }
    
    
}
