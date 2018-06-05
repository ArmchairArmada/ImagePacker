package net.natewm.imagepacker;

import java.util.concurrent.ExecutorService;

public class Services {
    private static ExecutorService executorService;

    public static void setExecutorService(ExecutorService service) {
        executorService = service;
    }

    public static ExecutorService getExecutorService() {
        return executorService;
    }
}
