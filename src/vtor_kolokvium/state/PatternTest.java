package vtor_kolokvium.state;

/*

State design pattern:

->  Context (MP3Player, chuvat od interfejsot -> PlayerState state)
    - The primary object that maintains a reference to the current State object.
    - It exposes the interface for clients and delegates state-specific requests to the current state implementation.
    - The Context often has a method to update its current state.

->  State interface (PlayerState)
    - metodi za sekoja strelka sho e premin od edna vo druga sostojba (PLAY, STOP, FWD, REW)
    - Defines common methods for all states, allowing Context to work with them without knowing concrete types.

->  Abstract State (MP3PlayerState, chuvat od Contextot -> MP3Player player)

->  Concrete States (PlayingState, PausedState, StoppedState)
    - Each concrete class implements the State interface and provides the specific behavior for a particular state.
    - These classes may also manage the transitions to the next state by telling the Context to update its state reference.

---------------------------------------------------------------------------------------------------------

sostojbi: PlayingState, PausedState, StoppedState
akcii koi gi menuvaat tie sostojbi: PLAY, STOP, FWD, REW

*/


import java.util.ArrayList;
import java.util.List;

class Song {
    private String title;
    private String artist;

    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    @Override
    public String toString() {
        return String.format("Song{title=%s, artist=%s}", title, artist);
    }
}



// =======================
// Context (chuvat od interfejsot -> PlayerState state)
// =======================
class MP3Player{
    PlayerState state;
    private List<Song> songs;
    int currentSong;
    boolean alreadyStopped = false;

    public MP3Player(List<Song> songs) {
        this.songs = songs;
        this.currentSong = 0;
        this.state = new StoppedState(this);
    }

    public void pressPlay(){
        state.pressPlay();
    }

    public void pressStop(){
        state.pressStop();
    }

    public void pressFWD(){
        state.pressFWD();
    }

    public void pressREW(){
        state.pressREW();
    }

    public void next(){
        currentSong = (currentSong + 1) % songs.size();
    }

    public void prev(){
        currentSong = (currentSong - 1 + songs.size()) % songs.size();
    }

    public void reset(){
        currentSong = 0;
    }

    public void printCurrentSong() {
        System.out.println(songs.get(currentSong));
    }

    @Override
    public String toString() {
        return String.format("MP3Player{currentSong = %d, songList = %s}", currentSong, songs);
    }
}



// =======================
// State interface (za sekoja akcija (strelka) po eden metod)
// =======================
interface PlayerState{
    void pressPlay();
    void pressStop();
    void pressFWD();
    void pressREW();
}



// =======================
// Abstract State (chuvat od Contextot -> MP3Player player)
// =======================
abstract class MP3PlayerState implements PlayerState{
    MP3Player player;

    public MP3PlayerState(MP3Player player){
        this.player = player;
    }

    public MP3PlayerState(MP3PlayerState old){
        this.player = old.player;
    }
}



// =======================
// Concrete States (PlayingState, PausedState, StoppedState)
// =======================
class PlayingState extends MP3PlayerState{

    public PlayingState(MP3Player player) {
        super(player);
    }

    public PlayingState(MP3PlayerState old) {
        super(old);
    }

    @Override
    public void pressPlay() {
        System.out.println("Song is already playing");
    }

    @Override
    public void pressStop() {
        System.out.println("Song " + player.currentSong + " is paused");
        player.state = new PausedState(this);
    }

    @Override
    public void pressFWD() {
        System.out.println("Forward...");
        player.next();
        player.state = new PausedState(this);
    }

    @Override
    public void pressREW() {
        System.out.println("Reward...");
        player.prev();
        player.state = new PausedState(this);
    }
}


class PausedState extends MP3PlayerState{

    public PausedState(MP3Player player) {
        super(player);
    }

    public PausedState(MP3PlayerState old) {
        super(old);
    }

    @Override
    public void pressPlay() {
        System.out.println("Song " + player.currentSong + " is playing");
        player.state = new PlayingState(this);
    }

    @Override
    public void pressStop() {
        System.out.println("Songs are stopped");
        player.reset();
        player.alreadyStopped = true;
        player.state = new StoppedState(this);
    }

    @Override
    public void pressFWD() {
        System.out.println("Forward...");
        player.next();
    }

    @Override
    public void pressREW() {
        System.out.println("Reward...");
        player.prev();
    }
}


class StoppedState extends MP3PlayerState{

    public StoppedState(MP3Player player) {
        super(player);
    }

    public StoppedState(MP3PlayerState old) {
        super(old);
    }

    @Override
    public void pressPlay() {
        System.out.println("Song " + player.currentSong + " is playing");
        player.state = new PlayingState(this);
    }

    @Override
    public void pressStop() {
        if (player.alreadyStopped){
            System.out.println("Songs are already stopped");
        } else {
            System.out.println("Songs are stopped");
            player.alreadyStopped = true;
        }
    }

    @Override
    public void pressFWD() {
        System.out.println("Forward...");
        player.next();
    }

    @Override
    public void pressREW() {
        System.out.println("Reward...");
        player.prev();
    }
}



// =======================
// Main class
// =======================
public class PatternTest {
    public static void main(String args[]) {
        List<Song> listSongs = new ArrayList<Song>();
        listSongs.add(new Song("first-title", "first-artist"));
        listSongs.add(new Song("second-title", "second-artist"));
        listSongs.add(new Song("third-title", "third-artist"));
        listSongs.add(new Song("fourth-title", "fourth-artist"));
        listSongs.add(new Song("fifth-title", "fifth-artist"));
        MP3Player player = new MP3Player(listSongs);


        System.out.println(player.toString());
        System.out.println("First test");


        player.pressPlay();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Second test");


        player.pressStop();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Third test");


        player.pressFWD();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
    }
}

//Vasiot kod ovde
