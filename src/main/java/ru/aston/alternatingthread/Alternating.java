package ru.aston.alternatingthread;

import java.util.concurrent.Semaphore;

public class Alternating {
    public static void main(String[] args) throws InterruptedException {
        Semaphore s1 = new Semaphore(1);
        Semaphore s2 = new Semaphore(0);

        Thread t1 = new PrintThread("1", s1, s2);
        Thread t2 = new PrintThread("2", s2, s1);

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}