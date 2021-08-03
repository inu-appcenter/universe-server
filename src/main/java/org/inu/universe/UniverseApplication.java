package org.inu.universe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

//@EnableCaching
@SpringBootApplication
public class UniverseApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniverseApplication.class, args);
    }

}
