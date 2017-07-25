package edu.umb.cs.threads.project;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Local extends ThreadLocal{

	private static final AtomicInteger nextId = new AtomicInteger(1);
    public static int id;


    private static final ThreadLocal<ConcurrentHashMap<Integer, String>> myContextThreadLocal =
            new ThreadLocal<ConcurrentHashMap<Integer, String>>() {

                @Override
                protected ConcurrentHashMap<Integer,String> initialValue() {
                    id = nextId.getAndIncrement();
                    return new ConcurrentHashMap<Integer, String>();
                }
            };

   // private static final String USER_ID = "userId";

   /* public static String getUserId() {

        return myContextThreadLocal.get().get(USER_ID);
    }*/

    public static void setUserId(Integer id, String timeStamp) {

        //int id = nextId.getAndIncrement();

        myContextThreadLocal.get().put(id, timeStamp);
    }

    public int getUserId(){

        return id;
    }

    public  void getLocal() {
        Iterator it = myContextThreadLocal.get().entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
                System.out.println("Client ID:" + pair.getKey() + " \t " + "TimeStamp:" + pair.getValue());
                myContextThreadLocal.get().remove(pair.getKey());

        }
            // myContextThreadLocal.get().entrySet();
            //return myContextThreadLocal.get();

    }


  /*use this  private static final AtomicInteger nextId = new AtomicInteger(0);

    private static final ThreadLocal<Integer> threadId =
            new ThreadLocal<Integer>() {
                @Override protected Integer initialValue() {
                    return nextId.getAndIncrement();
                }
            };

    public  int getLocal() {
        return threadId.get();
    } use this */






 /*   public Local(){
        tLocal = new ThreadLocal();
    }

 /*   public void insert(String s, String c){
        mymap.put(s,c);

    }*/

  /*  @Override
	 protected ConcurrentHashMap<String,String> initialValue(){
    	//return new ConcurrentHashMap<>();
        return tLocal.getAndIncrement();
	}*/



}