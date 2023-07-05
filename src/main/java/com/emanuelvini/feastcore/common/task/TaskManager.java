package com.emanuelvini.feastcore.common.task;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private static List<Thread> threads = new ArrayList<>();

    private static List<Runnable> tasksWaiting = new ArrayList<>();

    private static List<Runnable> tasksRunning = new ArrayList<>();

    public static void async(Runnable taskToRun) {
        if (tasksRunning.size() < threads.size()) {
            tasksRunning.add(taskToRun);
        } else {
            Thread theading = new Thread(() -> {
                while (true) {
                    if (tasksWaiting.size() != 0) {
                        Runnable task = tasksWaiting.get(0);
                        tasksWaiting.remove(task);
                        tasksRunning.add(task);
                        task.run();
                        tasksRunning.remove(task);
                    }
                }
            });
            theading.start();
            threads.add(theading);
        }
    }

}
