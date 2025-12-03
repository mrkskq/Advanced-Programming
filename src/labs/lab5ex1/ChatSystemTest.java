package labs.lab5ex1;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.TreeSet;
import java.util.stream.Collectors;

class NoSuchRoomException extends Exception {
    public NoSuchRoomException(String roomName) {
        super(roomName + " does not exist.");
    }
}

class NoSuchUserException extends Exception {
    public NoSuchUserException(String username) {
        super(username + " does not exist.");
    }
}

class User implements Comparable<User> {
    private String username;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(username);
        return sb.toString();
    }

    @Override
    public int compareTo(User o) {
        return username.compareTo(o.username);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }
}

class ChatRoom implements Comparable<ChatRoom> {
    private String name;
    private TreeSet<User> users;

    public ChatRoom() {
        this.name = "ChatRoom";
        this.users = new TreeSet<User>();
    }

    public ChatRoom(String name) {
        this.name = name;
        this.users = new TreeSet<User>();
    }

    public void addUser(String username) {
        users.add(new User(username));
    }

    public void removeUser(String username) {
        users.removeIf(u -> u.getUsername().equals(username));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append('\n');
        if (users.isEmpty()) {
            sb.append("EMPTY").append('\n');
        } else {
            users.forEach(u -> sb.append(u).append("\n"));
        }
        return sb.toString();
    }


    public boolean hasUser(String username) {
        return users.stream().anyMatch(u -> u.getUsername().equals(username));
    }

    public int numUsers() {
        return users.size();
    }

    public String getName() {
        return name;
    }

    public Set<User> getUsers() {
        return users;
    }

    @Override
    public int compareTo(ChatRoom o) {
        return Integer.compare(users.size(), o.users.size());
    }

    public int sort(ChatRoom o) {
        return name.compareTo(o.name);
    }

}

class ChatSystem {
    private Map<String, ChatRoom> rooms;
    private Set<User> registeredUsers;

    public ChatSystem() {
        this.rooms = new TreeMap<String, ChatRoom>();
        this.registeredUsers = new HashSet<User>();
    }

    public void addRoom(String roomName) {
        this.rooms.put(roomName, new ChatRoom(roomName));
    }

    public void removeRoom(String roomName) {
        this.rooms.remove(roomName);
    }

    public ChatRoom getRoom(String roomName) throws NoSuchRoomException {
        ChatRoom room = this.rooms.get(roomName);
        if (room == null) {
            throw new NoSuchRoomException(roomName);
        }
        return room;
    }

    public void register(String userName) {
        registeredUsers.add(new User(userName));
        rooms.values().stream()
                .min(Comparator.comparing(ChatRoom::numUsers)
                        .thenComparing(ChatRoom::getName))
                .ifPresent(r -> r.addUser(userName));
    }

    public void registerAndJoin(String userName, String roomName) {
        registeredUsers.add(new User(userName));

        try {
            ChatRoom room = getRoom(roomName);
            room.addUser(userName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void joinRoom(String userName, String roomName) throws NoSuchRoomException, NoSuchUserException {
        if (!registeredUsers.contains(new User(userName))) {
            throw new NoSuchUserException(userName);
        }

        ChatRoom room = this.rooms.get(roomName);
        if (room == null) {
            throw new NoSuchRoomException(roomName);
        }
        room.addUser(userName);
    }

    public void leaveRoom(String username, String roomName) throws NoSuchRoomException {
        ChatRoom room = this.rooms.get(roomName);
        if (room == null) {
            throw new NoSuchRoomException(roomName);
        }
        room.removeUser(username);
    }

    public void followFriend(String username, String friend_username) throws NoSuchUserException {

        if (!registeredUsers.contains(new User(username))) {
            throw new NoSuchUserException(username);
        }

        if (!registeredUsers.contains(new User(friend_username))) {
            throw new NoSuchUserException(friend_username);
        }

        for (ChatRoom room : rooms.values()) {
            if (room.hasUser(friend_username)) {
                room.addUser(username);
            }
        }
    }
}

public class ChatSystemTest {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for (int i = 0; i < n; ++i) {
                k = jin.nextInt();
                if (k == 0) cr.addUser(jin.next());
                if (k == 1) cr.removeUser(jin.next());
                if (k == 2) System.out.println(cr.hasUser(jin.next()));
            }
            //System.out.println("");
            System.out.println(cr.toString());
            n = jin.nextInt();
            if (n == 0) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for (int i = 0; i < n; ++i) {
                k = jin.nextInt();
                if (k == 0) cr2.addUser(jin.next());
                if (k == 1) cr2.removeUser(jin.next());
                if (k == 2) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if (k == 1) {
            ChatSystem cs = new ChatSystem();
            Method mts[] = cs.getClass().getMethods();
            while (true) {
                String cmd = jin.next();
                if (cmd.equals("stop")) break;
                if (cmd.equals("print")) {
                    System.out.println(cs.getRoom(jin.next()) + "\n");
                    continue;
                }

                for (Method m : mts) {
                    if (m.getName().equals(cmd)) {
                        String params[] = new String[m.getParameterTypes().length];
                        for (int i = 0; i < params.length; ++i) params[i] = jin.next();
                        m.invoke(cs, (Object[]) params);
                    }
                }
            }
        }
    }
}
