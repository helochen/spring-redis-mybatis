package com.chen.test;

import org.junit.Test;

/**
 * Created by Chen on 2017/6/6.
 */
public class testString {
    public static void main(String[] args){
        String hello = "hello";
        String hello1 = "he"+"llo";
        String hello2 = new String("hello");
        System.out.println(hello.equals(hello1));
        System.out.println(hello == hello1);
        System.out.println(hello == hello2);
        System.out.println(hello.equals(hello2));
    }

    @Test
    public void testError(){
        try{
            System.out.println("A");
            throw new Error();
        }catch (Exception ex){
            System.out.println("B");
            ex.printStackTrace();
        }finally {
            System.out.println("C");
        }
    }
}
