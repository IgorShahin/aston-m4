package ru.aston.alternatingthread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Semaphore;

class PrintThread extends Thread {
    private static final Logger log = LoggerFactory.getLogger(PrintThread.class);

    private final String text;
    private final Semaphore acquire;
    private final Semaphore release;


    PrintThread(String text, Semaphore acquire, Semaphore release) {
        super("Print-" + text);
        this.text = text;
        this.acquire = acquire;
        this.release = release;
    }

    @Override
    public void run() {
        for (; ; ) {
            try {
                acquire.acquire();
                log.info("{} → {}", getName(), text);
            } catch (InterruptedException ignored) {
                log.warn("{} interrupted", getName());
                return;
            } finally {
                release.release();
            }
        }
    }
}
