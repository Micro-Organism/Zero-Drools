package com.zero.drools.common.util;

import java.time.LocalDate;

/**
 * Class for testing global variable in Drools engine.
 */
public class OutputDisplay {

    public void showText(String text) {
        System.out.println("==================================================");
        System.out.println("Text: " + text + ",  date: " + LocalDate.now());
        System.out.println("==================================================");
    }

}
