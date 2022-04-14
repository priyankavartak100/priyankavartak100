
import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import org.javatuples.Triplet;

public class TollCalculatorLogic {

    static List<Triplet<Integer, RouteDetails, Route>> routes = new ArrayList<Triplet<Integer, RouteDetails, Route>>();

    public static void main(String[] args) {
        System.out.println("Hi welcome to tollcalculator!!!");
        try {

            FileReader reader = new FileReader("interchanges.json");
            Gson gson = new Gson();
            Locations locations = gson.fromJson(reader, Locations.class);
            locations.getLocations().forEach((k, v) -> v.setId(k));
            Integer starting_point_id = 0;
            Integer end_point_id = 0;
            List<Route> connectingRouteList = new ArrayList<>();
            //System.out.println(locations);
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter starting location for distance and price calculation.");
            String start_point = sc.nextLine();
            System.out.println("Enter ending location for distance and price calculation.");
            String end_point = sc.nextLine();


            /*Test 2*/
            //String start_point = "QEW";
            //String end_point = "Highway 400";
            /*Test 3*/ //Failed
            //String start_point = "Salem Road";
            //String end_point = "QEW";
            /*Test 4*/
            //String start_point = "QEW";
            //String end_point = "Salem Road";
            for (RouteDetails routeDetails : locations.getLocations().values()) {
                if (routeDetails.getName().equalsIgnoreCase(start_point)) {
                    starting_point_id = routeDetails.getId();
                };

                if (routeDetails.getName().equalsIgnoreCase(end_point)) {
                    end_point_id = routeDetails.getId();
                };

                if (starting_point_id > 0 && end_point_id > 0) {
                    break;
                }

            };

            for (RouteDetails routeDetails : locations.getLocations().values()) {
                if (Objects.equals(starting_point_id, routeDetails.getId())) {
                    routeDetails.getRoutes().forEach((routeobj) -> {
                        connectingRouteList.add(routeobj);
                    });
                    break;
                }
            }

            RouteExt route = nextRoute(connectingRouteList, end_point_id, locations.getLocations());
            if (route.getId() == end_point_id) {

            }

            //calcualte toll charges
            double RatePerKM = 0.25;
            double distance = 0;
            for (Triplet<Integer, RouteDetails, Route> r : routes) {
                distance += r.getValue2().getDistance();
            }

            double totalD = distance * RatePerKM;  //routes.Select(x => x.Item3).Sum(s => s.distance);

            double toll = totalD * RatePerKM;

            //System.out.println("connectingRouteList: " + connectingRouteList);
            System.out.println("Distance: " + distance);
            System.out.println("Cost: " + totalD);

            // Integer start_id = locations.getLocations().values().
            // int end_id = data.locations.Values.Where(x =  > x.name == end).FirstOrDefault().id;
        } catch (Exception e) {
            System.out.println("Error..." + e);
        }

    }

    static RouteExt nextRoute(List<Route> connectingRouteList, Integer end_point_id, Map<Integer, RouteDetails> locations) {
        try {
            RouteExt routeExt = new RouteExt();
            for (Route route : connectingRouteList) {

                Integer key = null;
                RouteDetails routeDetails = new RouteDetails();
                for (Map.Entry<Integer, RouteDetails> loc : locations.entrySet()) {
                    if (loc.getKey() == route.getToId()) {
                        key = loc.getKey();
                        routeDetails = loc.getValue();
                        break;
                    }
                }
                if (key == null) {
                    System.out.println("No route found");
                    return routeExt;
                }

                if (key == end_point_id) {
                    Triplet<Integer, RouteDetails, Route> tuple
                            = new Triplet(key, routeDetails, route);
                    routes.add(tuple);
                    routeExt.setRoute(route);
                    routeExt.setRouteDetails(routeDetails);
                    routeExt.setId(end_point_id);
                    return routeExt;
                } else {
                    routeExt = nextRoute(routeDetails.getRoutes(), end_point_id, locations);
                    if (routeExt != null) {
                        Triplet<Integer, RouteDetails, Route> tuple
                                = new Triplet(routeExt.getId(), routeExt, route);
                        routes.add(tuple);
                        return routeExt;
                    }
                }
            }
            System.out.println(routeExt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
