package com.example.forhackerton;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class ForHackerTonApplication {

    public static void main(String[] args) {

        SpringApplication.run(ForHackerTonApplication.class, args);
        long heapSize = Runtime.getRuntime().totalMemory();
        System.out.println("HEAP Size(M) : " + heapSize / (1024 * 1024) + " MB");
        System.out.println("Available processors (cores): " + Runtime.getRuntime().availableProcessors());
        System.out.println("Operating system: " + System.getProperty("os.name"));
        System.out.println("Java version: " + System.getProperty("java.version"));
        System.out.println("Available memory (MB): " + (Runtime.getRuntime().freeMemory() / (1024 * 1024)) + " MB");
        System.out.println("Total memory available to JVM (MB): " + (Runtime.getRuntime().totalMemory() / (1024 * 1024)) + " MB");
        System.out.println("Current time: " + new Date());
    }

}
