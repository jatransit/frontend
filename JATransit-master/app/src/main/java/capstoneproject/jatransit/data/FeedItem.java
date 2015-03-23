package capstoneproject.jatransit.data;

/**
 * Created by Caliph Cole on 03/10/2015.
 */
public class FeedItem {

    private String route;
    private String origin;
    private String via;
    private String destination;



    private String timeStamp;
    public FeedItem(String route, String origin, String via,String destination,String timeStamp){

        super();
        this.route = route;
        this.origin = origin;
        this.via = via;
        this.destination = destination;
        this.timeStamp = timeStamp;

    }
    public FeedItem(){

    }
    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }



    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }



    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }



    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }






}
