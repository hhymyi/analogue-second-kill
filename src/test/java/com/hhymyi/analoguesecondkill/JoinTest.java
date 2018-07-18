package com.hhymyi.analoguesecondkill;

public class JoinTest {
    public static void main(String[] args) throws InterruptedException {
        ThreadJoinTest t1=new ThreadJoinTest("小明");
        ThreadJoinTest t2=new ThreadJoinTest("小东");
        t1.start();
        t1.join(10);
        t2.start();
    }
}

class ThreadJoinTest extends Thread{
    public ThreadJoinTest(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 0; i <1000 ; i++) {
            System.out.println(this.getName()+":"+i);
        }
    }
}
