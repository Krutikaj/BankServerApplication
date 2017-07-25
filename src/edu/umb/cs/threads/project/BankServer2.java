package edu.umb.cs.threads.project;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.concurrent.ConcurrentHashMap;

public class BankServer2 implements Runnable {

    private Socket s;


    public BankServer2(Socket socket) {
        this.s = socket;
    }




    public void run(){

       try{
          System.out.println("KJ:Current Thread id"+Thread.currentThread().getId());
          BankServer bs = new BankServer();
          bs.executeCommand(this.s);
          Local tLocal = new Local();
         // ConcurrentHashMap<String,String> localMap = bs.tLocal.get();
        /*   ConcurrentHashMap<String,String> localMap = (ConcurrentHashMap<String, String>) tLocal.get();
          localMap.entrySet().forEach(entry -> {
                       System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());});*/

            s.close();
       }catch (IOException e){}

    }

}
