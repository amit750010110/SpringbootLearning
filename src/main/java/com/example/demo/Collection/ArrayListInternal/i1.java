package com.example.demo.Collection.ArrayListInternal;

public class i1 {
    public static void main(String[] args) {
        MyArrayList list = new MyArrayList();
//        int[] arr = new int[2];
        list.add(1);
        list.add(2);

        System.out.println(list.contain(2));

    }


}

class user {
    int id;
    String name;

    public user(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        user u = (user) obj;
        return this.id == u.id;

    }

    @Override
    public int hashCode() {
        return this.id;
    }
}

class MyArrayList {
    Object[] data = new Object[2];
    int size = 0;

    public void add(Object obj) {
        if (data.length == size) {
            grow();
        }
        data[size] = obj;
        size++;
    }

    private void grow() {
        Object[] newData = new Object[data.length * 2];
//        System.arraycopy(data, 0, newData, 0, data.length);
        for (int i = 0; i < data.length; i++) {
            newData[i] = data[i];
        }
        data = newData;

    }

    boolean contain(Object o) {
        for (int i = 0; i < size; i++) {
            if (data[i].equals(o)) {
                return true;
            }
        }
        return false;
    }

}