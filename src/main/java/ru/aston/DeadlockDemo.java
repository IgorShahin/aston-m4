package ru.aston;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadlockDemo {
    private static final Logger log = LoggerFactory.getLogger(DeadlockDemo.class);
    private static final Lock LOCK_A = new ReentrantLock();
    private static final Lock LOCK_B = new ReentrantLock();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            LOCK_A.lock();
            log.info("T1: взял LOCK_A");
            sleep();
            log.info("T1: пытаюсь взять LOCK_B");
            LOCK_B.lock();
            try {
                log.info("T1: взял LOCK_B");
            } finally {
                LOCK_B.unlock();
                LOCK_A.unlock();
            }
        });

        Thread t2 = new Thread(() -> {
            LOCK_B.lock();
            log.info("T2: взял LOCK_B");
            sleep();
            log.info("T2: пытаюсь взять LOCK_A");
            LOCK_A.lock();
            try {
                log.info("T2: взял LOCK_A");
            } finally {
                LOCK_A.unlock();
                LOCK_B.unlock();
            }
        });

        t1.start();
        t2.start();
    }

    private static void sleep() {
        try {
            Thread.sleep((long) 200);
        } catch (InterruptedException ignored) {
        }
    }
}