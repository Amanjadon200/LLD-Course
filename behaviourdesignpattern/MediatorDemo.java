package behaviourdesignpattern;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MediatorDemo {
    public static void main(String[] args) {
        ChatRoom chatRoom = new ChatRoom("coding channel");
        User user1 = new User("aman");
        User user2 = new User("naman");
        User user3 = new User("raman");
        chatRoom.addUser(user1);
        chatRoom.addUser(user2);
        chatRoom.addUser(user3);
        user1.sendMessageToChat("aman is a greatest coder of all time", chatRoom);
    }

}

class User {
    String name;
    ChatRoom chatRoom;

    public User(String name) {
        this.name = name;
    }

    public void sendMessageToChat(String message, ChatRoom chatRoom) {
        chatRoom.broadcast(message, this);
    }

    public void receiveMessage(String message, User sender) {
        System.out.println(this.name+" got the message " + message + " from " + sender.name);
    }

}

class ChatRoom {
    List<User> users;
    String chatName;

    public ChatRoom(String chatName) {
        users = new ArrayList<>();
        this.chatName = chatName;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void broadcast(String message, User sender) {
        Iterator<User> iterator = users.iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            if (user != sender)
                user.receiveMessage(message, sender);
        }
    }
}
