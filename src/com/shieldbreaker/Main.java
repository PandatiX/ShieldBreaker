package com.shieldbreaker;

import com.shieldbreaker.kernel.ShieldBreaker;

public class Main {

    //TODO review what have to be exported as a library (think to an automation of this system when building main artifact)
    //TODO review MVC framework app
    //TODO build logger

    public static void main(String[] args) {

        ShieldBreaker.createInstance(args).startApp();

    }

}