/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keyo.utils;


/**
 *
 * @author mgalvao3
 */
public class Random {
    public static int randId() {
        int min = 10000;
        int max = Integer.MAX_VALUE - 1;

        java.util.Random rand =new java.util.Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
   
}
