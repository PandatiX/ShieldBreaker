package com.shieldbreaker;

import com.shieldbreaker.kernel.ShieldBreaker;

public class Main {

    //TODO add bot progress/stopping process
    //TODO add Swing Terminal for bot progress display
    //TODO review what have to be exported as a library (think to an automation of this system when building main artifact)
    //TODO create tests
    //TODO review MVC framework app
    //TODO build logger

    public static void main(String[] args) {

        ShieldBreaker.createInstance(args).startApp();

    }

}