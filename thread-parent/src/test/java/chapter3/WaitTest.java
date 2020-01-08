package chapter3;

import org.junit.Test;

/**
* @Author xusf
* @Date 2020/1/8 14:21
* @Description : 测试wait和notify
*/
public class WaitTest {
    /**
     * 测试单个wait和单个notify，notify以后需要等调用notify的方法执行完毕才会释放锁，所以wait后面的内容需要等notify执行完成才能执行
     * wait方法会使调用该方法的线程释放锁，然后从运行状态退出，进入等待队列，直到被再次唤醒
     */
    @Test
    public void testWait(){
        TestWait testWait = new TestWait();
        TestWait2 testWait2 = new TestWait2(testWait);
//        如果直接这么调用的话，main线程就一直要等待唤醒，下面的代码就走不下去了
//        testWait.method();
        TestWait3 testWait3 = new TestWait3(testWait);
        testWait3.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        testWait2.start();
    }

    /**
     * 这是先notify再wait，由于这时候没有被阻塞的线程，所以发出的notify会被忽略，最后wait的线程会一直等待
     */
    @Test
    public void testWait2(){
        TestWait testWait = new TestWait();
        TestWait2 testWait2 = new TestWait2(testWait);
        TestWait3 testWait3 = new TestWait3(testWait);
        testWait2.start();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        testWait3.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * wait必须要在先获得对象的锁以后才能调用，不然会抛出IllegalMonitorStateException异常
     */
    @Test
    public void testWait3(){
        Object object = new Object();
        try {
            object.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试当线程在wait的时候收到中断，结果是会抛出InterruptedException异常
     */
    @Test
    public void testWait4(){
        TestWait testWait = new TestWait();
        TestWait3 testWait3 = new TestWait3(testWait);
        testWait3.start();
        testWait3.interrupt();
    }

    /**
     * 当多个线程wait的时候，执行一次notify只会随机唤醒一个线程
     */
    @Test
    public void testWait5(){
        TestWait testWait = new TestWait();
        TestWait3 testWait3 = new TestWait3(testWait);
        TestWait3 testWait31 = new TestWait3(testWait);
        testWait3.start();
        testWait31.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TestWait2 testWait2 = new TestWait2(testWait);
        testWait2.start();
    }

    /**
     * 当多个线程wait的时候，执行一次notifyAll会唤醒所有线程
     */
    @Test
    public void testWait6(){
        TestWait testWait = new TestWait();
        TestWait3 testWait3 = new TestWait3(testWait);
        TestWait3 testWait31 = new TestWait3(testWait);
        testWait3.start();
        testWait31.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TestWait4 testWait4 = new TestWait4(testWait);
        testWait4.start();
    }

    /**
     * 限制wait时间，测试wait(long)方法
     */
    @Test
    public void testWait7(){
        TestWait testWait = new TestWait();
        testWait.method2();
    }
}
