package com.example.demo.Collection.Hashsets;



public class i2 {
    public static void main(String[] args) {
        MyHashSet set = new MyHashSet();
        set.add(new User(1, "Amit"));
        set.add(new User(1, "Amit"));
        System.out.println(set);
    }
}

class MyHashSet {
    Node[] bucket = new Node[16];

    void add(User key) {
        int index = Math.abs(key.hashCode() % bucket.length);
        Node head = bucket[index];
        while (head != null) {
            if (head.key.equals(key)) {
                return; // duplicate
            }
            head = head.next;
        }

        Node newNode = new Node(key);
        newNode.next = bucket[index];
        bucket[index] = newNode;

    }
}

class Node {
    User key;
    Node next;

    public Node(User key) {
        this.key = key;
    }

}

class User {
    int id;
    String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User u = (User) obj;
        return this.id == u.id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }
}
