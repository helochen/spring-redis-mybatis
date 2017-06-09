package com.chen.test;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Chen on 2017/6/7.
 */
public class testCollection {

    @Test
    public void testSet(){
        Set<String> set = new HashSet<String>();
        String str1 = new String("1Hello");
        String str2 = new String("0World");
        String str3 = new String("2Hello");

        System.out.println(set.add(str1));
        System.out.println(set.add(str2));
        System.out.println(set.size());
        System.out.println(set.add(str3));
        System.out.println(set.size());
        for(String s : set){
            System.out.println(s);
        }
    }
}
