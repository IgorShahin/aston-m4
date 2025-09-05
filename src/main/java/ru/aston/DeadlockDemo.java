package ru.aston;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadlockDemo {
    private static final Logger log = LoggerFactory.getLogger(DeadlockDemo.class);

    public static void main(String[] args) {
        Lock lockA = new ReentrantLock();
        Lock lockB = new ReentrantLock();

        Thread t1 = new Thread(createTask(lockA, lockB, "T1"));
        Thread t2 = new Thread(createTask(lockB, lockA, "T2"));

        t1.start();
        t2.start();
    }

    private static Runnable createTask(Lock first, Lock second, String name) {
        return () -> {
            first.lock();
            log.info("{}: взял {}", name, first);
            sleep();
            log.info("{}: пытаюсь взять {}", name, second);
            second.lock();
            try {
                log.info("{}: взял {}", name, second);
            } finally {
                second.unlock();
                first.unlock();
            }
        };
    }

    private static void sleep() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            log.warn("Sleep interrupted");
            Thread.currentThread().interrupt();
        }
    }
}