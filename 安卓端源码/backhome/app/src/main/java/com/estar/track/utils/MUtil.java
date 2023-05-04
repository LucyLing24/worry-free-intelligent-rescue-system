package com.estar.track.utils;

import java.util.Random;

public class MUtil {

    public static double getRandom(){
        Random random = new Random();

        int a = random.nextInt(10);

        return (Math.random()*Math.pow((-1),a));

    }



}
