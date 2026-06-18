package behaviourdesignpattern;

import java.util.ArrayList;
import java.util.List;
/*
when should we use iterator design pattern?
1. when you want to provide a way to access the elements of an aggregate object sequentially without exposing its underlying representation.
2. when you want to provide a uniform interface for traversing different aggregate structures.
Real-world examples
Use it when:
You need to traverse collections.
You don't want users to know the internal data structure.
Different collections should be traversed uniformly.
*/
public class IteratorDemo {
    public static void main(String[] args) {
        // ArrayList<Integer> arr = new ArrayList<>();
        // arr.add(0);
        // arr.add(1);
        // Iterator<Integer> iterator=arr.iterator();
        // while(iterator.hasNext()){
        // System.out.println(iterator.next());
        // }
        // Playlist<String> playlist = new Playlist<String>();
        // playlist.addSong("tere liye aya m");
        // playlist.addSong("sayira");
        // while (playlist.hasNext()) {
        // System.out.println(playlist.next());
        // }
        Playlist playlist = new Playlist();
        playlist.addSong("Na Na Na Na Na");
        playlist.addSong("chahat ki kasam");
        try{
            PlayListIterator pListIterator = playlist.iterator("simple");
            while (pListIterator.hasNext()) {
                System.out.println(pListIterator.next());
            }

        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

}

// class Playlist<T> {
// List<T> songs;
// int index = 0;

// public Playlist() {
// songs = new ArrayList<>();
// }

// public void addSong(T song) {
// songs.add(song);
// }

// public boolean hasNext() {
// if (index<songs.size() && songs.get(index) != null) {
// return true;
// }
// return false;
// }

// public T next() {
// return songs.get(index++);
// }
// }
class Playlist {
    List<String> songs = new ArrayList<>();

    public void addSong(String song) {
        songs.add(song);
    }

    public PlayListIterator iterator(String iterator) {
        switch (iterator) {
            case "simple":
                PlayListIterator itr = new simplePlayListIterator(songs);
                return itr;
            case "random":
                return new randomPlayListIterator(songs);
            default:
                throw new IllegalArgumentException("Unknown iterator type");
            // break;
        }
    }
}

interface PlayListIterator {
    boolean hasNext();

    String next();
}

class simplePlayListIterator implements PlayListIterator {

    List<String> songs;
    int index = 0;

    public simplePlayListIterator(List<String> songs) {
        this.songs = songs;
    }

    public boolean hasNext() {
        return index < songs.size() ;
    }

    public String next() {
        return songs.get(index++);
    }
}
class randomPlayListIterator implements PlayListIterator {
    List<String> songs;
    List<Integer> indices;
    int index = 0;

    public randomPlayListIterator(List<String> songs) {
        this.songs = songs;
        indices = new ArrayList<>();
        for (int i = 0; i < songs.size(); i++) {
            indices.add(i);
        }
        java.util.Collections.shuffle(indices);
    }

    public boolean hasNext() {
        return index < songs.size();
    }

    public String next() {
        return songs.get(indices.get(index++));
    }
}