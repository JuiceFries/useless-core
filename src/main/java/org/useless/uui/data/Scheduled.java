package org.useless.uui.data;

import java.util.function.Consumer;

public class Scheduled implements Perform {
    private volatile boolean running;
    private final Thread thread;

    public Scheduled(int millis, Consumer<Perform> task) {
        if (millis <= 0) throw new RuntimeException("millisecond 不能小于等于0!");

        this.thread = new Thread(() -> {
            long intervalNanos = millis * 1_000_000L;
            long nextTick = System.nanoTime();

            while (running) {
                task.accept(this);
                nextTick += intervalNanos;

                while (System.nanoTime() < nextTick && running) {
                    Thread.onSpinWait();
                }
            }
        },"Scheduled-" + System.nanoTime());
    }

    public void start() {
        running = true;
        thread.start();
    }

    public void stop() {
        running = false;
        thread.interrupt();
    }

    public String getId() {
        return thread.getName();
    }
}