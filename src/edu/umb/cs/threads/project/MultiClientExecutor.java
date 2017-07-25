package edu.umb.cs.threads.project;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by vikram on 5/2/17.
 */
public class MultiClientExecutor implements Runnable {

    private Socket s;

    public MultiClientExecutor(Socket socket) {
        this.s = socket;
    }

    public void run(){

        try{
          //  System.out.println("KJ:Current Thread id"+Thread.currentThread().getId());
            BankServer bs = new BankServer();
            bs.executeCommand(this.s);
            s.close();
        }catch (IOException e){}


    }
}
