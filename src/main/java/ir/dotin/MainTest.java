package ir.dotin;

import ir.dotin.terminal.business.MultiThreadTerminal;

public class MainTest {
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MultiThreadTerminal.main(new String [] {"Terminal.xml"});
                MultiThreadTerminal.main(new String[] {"terminal2.xml"});
            }
        }).start();
    }
}



//import ir.dotin.terminal.business.MultiThreadTerminal;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class MainTest  {
//    public static void main(String[] args) throws InterruptedException {
//        List<Thread> threads = new ArrayList<>();
////        for(int i=0 ; i< 3; i++){
////            threads.add(new Thread((Runnable) new MultiThreadTerminal(i)));
////        }
//
//        for(Thread thread : threads){
//            thread.start();
//        }
//
//
//        for(Thread thread : threads){
//            thread.join();
//        }
//        int maxNumberOfThreads=3;
//
//        for(int i=0 ; i< maxNumberOfThreads; i++){
//            new Thread((Runnable) new MultiThreadTerminal(i)).start();
//        }
//
//
//    }
//}
