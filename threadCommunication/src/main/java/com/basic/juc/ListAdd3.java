package com.basic.juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * locate com.basic.juc
 * Created by mastertj on 2018/4/4.
 * wait notify 方法 wait释放锁 notify不释放锁
 *
 * result不是理想结果 原因是 notify不释放锁
 *
 * CountDownLatch 没有锁
 * CountDownLatch 可以实现
 */
public class ListAdd3 {
    //volatile
    private volatile static List list=new ArrayList();

    public static void main(String[] args) throws InterruptedException {

        ListAdd3 listAdd1=new ListAdd3();

        /**
         * 实例化一个Objcet lock锁
         * 当使用wait 和 notify的时候，一定要配合使用synchronized 关键字去使用
         */
        final CountDownLatch countDownLatch=new CountDownLatch(1);

        Thread t1=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 10; i++) {
                        listAdd1.addList();
                        System.out.println(Thread.currentThread().getName() + " 添加了一个元素");
                        Thread.sleep(500);
                        if(list.size()==5){
                            System.out.println("发送通知.....");
                            countDownLatch.countDown();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (list.size() != 5) {
                        System.out.println("t2进入");
                        Thread.sleep(3000);
                        //countDownLatch睡眠
                        countDownLatch.await();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("当前线程收到通知" + Thread.currentThread().getName() + " list size=5 线程停止");
                throw new RuntimeException();
            }
        });

        t2.start();
        Thread.sleep(100);
        t1.start();

    }

    public void addList(){
        list.add("tanjie");
    }

    public int size(){
        return list.size();
    }
}
