package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        /*Scanner in = new Scanner(System.in);
        String path = in.nextLine();
        String regex = in.nextLine();
        FileSystemWalker fileSystemWalker = new FileSystemWalker(path);*/
        Test1();
        Test2();
    }
    public static void Test1(){
        FileSystemWalker fileSystemWalker = new FileSystemWalker("C:\\Users\\user\\Desktop\\A", "[A-Z]1");
    }
    public static void Test2(){
        FileSystemWalker fileSystemWalker = new FileSystemWalker("C:\\Users\\user\\Desktop\\A");
    }
}
