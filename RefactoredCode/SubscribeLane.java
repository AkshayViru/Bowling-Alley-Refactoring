// This class handles lane subscribe
import java.io.Serializable;

public class SubscribeLane implements Serializable {


    public static void subscribe(Lane lane,LaneObserver adding ) {
        lane.subscribers.add( adding );
    }


    public static  void publish( Lane lane,LaneEvent event ) {
        if( lane.subscribers.size() > 0 ) {
            for (Object subscriber : lane.subscribers) {
                ((LaneObserver) subscriber).receiveLaneEvent(event);
            }
        }
    }
}
