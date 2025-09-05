package ru.aston;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.ReentrantLock;

public class LivelockDemo {
    private static final Logger log = LoggerFactory.getLogger(LivelockDemo.class);
    private static final ReentrantLock LEFT = new ReentrantLock(true);
    private static final ReentrantLock RIGHT = new ReentrantLock(true);

    public static void main(String[] args) {
        Runnable polite = () -> {
            String name = Thread.currentThread().getName();
            boolean done = false;

            while (!done) {
                boolean gotLeft = LEFT.tryLock();
                if (gotLeft) {
                    try {
                        if (RIGHT.tryLock()) {
                            try {
                                log.info("{}: получил оба замка → сделал работу", name);
                                done = true;
                            } finally {
                                RIGHT.unlock();
                            }
                        } else {
                            log.info("{}: не взял RIGHT → отпускаю LEFT и пробую снова", name);
                        }
                    } finally {
                        if (LEFT.isHeldByCurrentThread()) LEFT.unlock();
                    }
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    log.warn("{}: interrupted", name);
                    Thread.currentThread().interrupt(); // восстановление флага
                    return;
                }
            }
        };

        Thread t1 = new Thread(polite, "T1");
        Thread t2 = new Thread(polite, "T2");
        t1.start();
        t2.start();
    }
}