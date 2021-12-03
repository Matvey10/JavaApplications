package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try{
            Test3(args[0], args[1]);
            Test4(args[0]);
        }
        catch (Exception e){
            System.out.print("wrong input");
        }
    }
    public static void Test1(){
        FileSystemWalker fileSystemWalker = new FileSystemWalker("C:\\Users\\user\\Desktop\\A", "[A-Z]1");
    }
    public static void Test2(){
        FileSystemWalker fileSystemWalker = new FileSystemWalker("C:\\Users\\user\\Desktop\\A");
    }
    public static void Test3(String dir, String reg) {
        FileSystemWalker fileSystemWalker = new FileSystemWalker(dir, reg);
    }
    public static void Test4(String dir){
        FileSystemWalker fileSystemWalker = new FileSystemWalker(dir);
    }
}
