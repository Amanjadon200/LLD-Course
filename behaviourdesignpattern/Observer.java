package behaviourdesignpattern;

import java.util.ArrayList;
import java.util.List;

public class Observer {
    public static void main(String args[]) {
        YoutubeChannel ytc = new YoutubeChannelImp("codewithaman");
        Subscriber youtubeSubscriber = new YoutubeSubscriber("Aman Jadon");
        Subscriber youtubeEmailSubscriber = new YoutubeEmailSubscriber("Aman Jadon", "aman.jadon@gmail.com");

        ytc.addSubscriber(youtubeSubscriber);
        ytc.addSubscriber(youtubeEmailSubscriber);
        ytc.uploadVideo("Observer desing pattern video");
    }
}

interface YoutubeChannel {
    void uploadVideo(String title);

    void addSubscriber(Subscriber sb);

    void removeSubscriber(Subscriber sb);
}

class YoutubeChannelImp implements YoutubeChannel {
    private List<Subscriber> subscribers = new ArrayList<>();
    private String channelName;

    public YoutubeChannelImp(String channelName) {
        this.channelName = channelName;
    }

    public void uploadVideo(String title) {
        // 1. notify Subscribers
        System.out.print("video uploaded on "+channelName+" successfully");
        for (Subscriber subscriber : subscribers) {
            subscriber.update(title);
        }
    }

    public void addSubscriber(Subscriber sb) {
        subscribers.add(sb);
    }

    public void removeSubscriber(Subscriber sb) {
        subscribers.remove(sb);
    }
}

interface Subscriber {
    void update(String title); // public method so inherited class should also make this public while
    // overriding
}

class YoutubeSubscriber implements Subscriber {
    String name;

    public YoutubeSubscriber(String name) {
        this.name = name;
    }

    public void update(String title) {
        // notifying on youtube
        System.out.println("notifying on youtube app to " + name + " this recent uploaded " + title);
    }
}

class YoutubeEmailSubscriber implements Subscriber {
    private String name;
    private String email;

    public YoutubeEmailSubscriber(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void update(String title) {
        // notifying on email
        System.out.println("notifying on email to " + name + " this recent uploaded " + title);

    }
}
