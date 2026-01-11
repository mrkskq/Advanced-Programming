package vtor_kolokvium.observer;


/*

Observer design pattern:

->  Subscriber / Observer / Listener (interface)
    - method: update(context) ex. update(String article)
    - Defines an interface with an update() method to ensure all observers (subscribers) receive updates consistently.

->  Publisher / Subject / Observable
    - methods: subscribe(), unsubscribe(), notifySubscribers(), mainBusinessLogic() -> ex. addArticle()
    - Maintains a list of observers (subscribers), provides methods to add/remove them, and notifies them of state changes.

->  Concrete Subscriber
    - Implements the observer (subscriber) interface and reacts to subject updates (e.g., a weather app showing weather updates).

->  Concrete Publisher
    - A specific subject (publisher) that holds actual data. On state change, it notifies registered observers (subscribers) (e.g., a weather station).

---------------------------------------------------------------------------------------------------------

*/


import java.util.ArrayList;
import java.util.List;


// =======================
// Subscriber / Observer
// =======================
interface NewsSubscriber{
    void update(String article);
}



// =======================
// Concrete Subscriber
// =======================
class User1 implements NewsSubscriber{

    String username;
    List<String> receivedNews;

    public User1(String username){
        this.username = username;
        receivedNews = new ArrayList<>();
    }

    @Override
    public void update(String article) {
        receivedNews.add(article);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", receivedNews=" + receivedNews +
                '}';
    }
}



// =======================
// Publisher / Subject
// =======================
class NewsOutlet{
    List<String> articles;
    List<NewsSubscriber> subscribers;

    public NewsOutlet(){
        this.articles = new ArrayList<>();
        this.subscribers = new ArrayList<>();
    }

    public void subscribe(NewsSubscriber subscriber){
        this.subscribers.add(subscriber);
    }

    public  void unsubscribe(NewsSubscriber subscriber){
        this.subscribers.remove(subscriber);
    }

    public void notifySubscribers(String article){
        for (NewsSubscriber s : subscribers) {
            s.update(article);
        }
    }

    public void addArticle(String article){
        this.articles.add(article);
    }

    @Override
    public String toString() {
        return "NewsOutlet{" +
                "articles=" + articles +
                ", subscribers=" + subscribers +
                '}';
    }
}



// =======================
// Main Class
// =======================
public class ObserverTest {
    public static void main(String[] args) {
        NewsSubscriber s1 = new User1("stefan");
        NewsSubscriber s2 = new User1("ana");

        NewsOutlet outlet = new NewsOutlet();
        outlet.subscribe(s1);
        outlet.subscribe(s2);

        for (int i=0;i<100;i++){
            String article = "article" + i;
            outlet.addArticle(article);

            if (i==33){
                outlet.unsubscribe(s1);
            }
            if (i==82){
                outlet.subscribe(s1);
            }
        }

        System.out.println(outlet);


        System.out.println(s1);

        System.out.println(s2);
    }
}
