package com.company.server.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Observer {
    private static List<BlockingQueue<String>> customers = new ArrayList<>();

    public static void subscribe(BlockingQueue<String> customer) {
        customers.add(customer);
    }

    public static void inform(String mess) throws InterruptedException {
        for(BlockingQueue<String> customer: customers) {
            customer.put(mess);
        }
    }

    public static void unsubscribe(BlockingQueue<String> customer) {
        customers.remove(customer);
    }
}
