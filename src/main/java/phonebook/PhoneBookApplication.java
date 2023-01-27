/*
 * Copyright (c) 2023 Dorustree private limited. All rights reserved.
 */

package phonebook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Phonebook crud application
 */
@SpringBootApplication
public class PhoneBookApplication extends SpringBootServletInitializer
{

    //
    // static
    //

    public static void main(String[] args) {
        SpringApplication.run(PhoneBookApplication.class, args);
    }
}