package edu.umb.cs.threads.project;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.Timestamp;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.*;
import java.text.SimpleDateFormat;
import java.lang.*;
import java.util.concurrent.Executor;

import static java.util.concurrent.Executors.newWorkStealingPool;

//TRANSFER clientid   amount
//Each active client gets a client_id starting from 1  
//Example :  TRANSFER 2  50

public class BankServer extends ThreadLocal{

	private static final int BANKPORT = 8888;
	private BankAccount account;
	private ServerSocket serverSocket;
	private ExecutorService pool;
	public String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
	public int ids ;
	public Local l;
	public static boolean shutServer = false;
	public static final Map<Integer, Double> transferMap = new HashMap<Integer, Double>();
	


	public BankServer(){
		account = new BankAccount();
		pool = Executors.newWorkStealingPool();
		// l = new Local();
		//tLocal = new ThreadLocal<>();

	}

	/*public BankServer(Socket socket) {
		this.s = socket;
	}

	public void run(){
			executeCommand(this.s);
	}*/

	
	public void init(){
	//	try{
			try(ServerSocket serverSocket = new ServerSocket(BANKPORT)){
				System.out.println("Socket created.");

				// timeout after 60 seconds
				serverSocket.setSoTimeout(80000);
				Socket socket = null;
			
				while(!shutServer){
					System.out.println( "Listening for a connection on the local port " +
										serverSocket.getLocalPort() + "..." );
					try {
						socket = serverSocket.accept();
					}catch(SocketTimeoutException e){
						System.out.println( "Socket timeout ");
						continue;
					}

					System.out.println( "\nA connection established with the remote port " + 
										socket.getPort() + " at " +
										socket.getInetAddress().toString() );
					//executeCommand( socket );
				  //  new Thread(new BankServer2(socket)).start();

				    pool.execute(new MultiClientExecutor(socket));
				}
				System.out.println("Server has shutdown");

			//}finally{
			//	serverSocket.close();
			//}
		}
		catch(IOException exception){}
	}



	public void executeCommand( Socket socket ){
		try{
			try{
				Scanner in = new Scanner( socket.getInputStream() );
				PrintWriter out = new PrintWriter( socket.getOutputStream() );
				System.out.println( "I/O setup done" );

				l = new Local();
				ids = l.getUserId();

				while(true){

					if( in.hasNext() ){

						if(transferMap.containsKey(ids)) {
							//System.out.println( "Value in transferMap:"+transferMap.get(ids) );
							account.deposit(transferMap.get(ids));
							transferMap.remove(ids);
						}
						String command = in.next();
						if( command.equals("QUIT") ){
							System.out.println( "QUIT: Connection being closed." );
							out.println( "QUIT accepted. Connection being closed." );
							out.close();
							return;
						}

						accessAccount( command, in, out );
					}
				}
			}	finally{
				socket.close();
				System.out.println( "A connection is closed." );
			}
		}
		catch(Exception exception){
			exception.printStackTrace();
		}
	}

	private void accessAccount( String command, Scanner in, PrintWriter out ){
		double amount;
		int clientAccount;

		//String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());

		//l = new Local();
        l.setUserId(ids,timeStamp);
		l.getLocal();

		//ids = l.getUserId();

		if( command.equals("DEPOSIT") ){
			amount = in.nextDouble();
			account.deposit( amount );
			// ids = l.id;
		//	l.setUserId(timeStamp);
			System.out.println( "DEPOSIT: Current balance: " + account.getBalance() );
			out.println( "DEPOSIT Done. Current balance: " + account.getBalance() );
		}
		else if( command.equals("TRANSFER")){

			clientAccount = in.nextInt();
			amount = in.nextDouble();

			if(amount < account.getBalance())
			{
				transferMap.put(clientAccount, amount);
				account.withdraw(amount);
			}

			System.out.println( "TRANSFER: Current balance: " + account.getBalance() );
			out.println( "TRANSFER Done. Current balance: " + account.getBalance() );

		}
		else if( command.equals("WITHDRAW") ){
			amount = in.nextDouble();
			account.withdraw( amount );
			//ids = l.id;
		//	l.setUserId(timeStamp);
			System.out.println( "WITHDRAW: Current balance: " + account.getBalance() );
			out.println( "WITHDRAW Done. Current balance: " + account.getBalance() );
		}
		else if( command.equals("BALANCE") )
		{
			// ids = l.id;java.util.concurrent.Executors
			//System.out.println("Print ids="+ids);
		//	l.setUserId(timeStamp);
			System.out.println( "BALANCE: Current balance: " + account.getBalance() );
			out.println( "BALANCE accepted. Current balance: " + account.getBalance() );
		}
		else if ( command.equals("TERMINATE")){

			this.pool.shutdownNow();
			shutServer = true;

		}
		else{
			//ids = l.id;
		//	l.setUserId(timeStamp);
			System.out.println( "Invalid Command" );
			out.println( "Invalid Command. Try another command." );
		}
		out.flush();
		l.getLocal();
	}
	
	public static void main(String[] args){
		BankServer server = new BankServer();
		server.init();
	}
	

}
	