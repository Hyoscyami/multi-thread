package chapter1;

import org.junit.Test;

/**
* @Author xusf
* @Date 2020/1/3 11:07
* @Description : 线程常用方法，过时方法不在此列，如stop，suspend，resume等
*/
public class BaseMethodTest {
    /**
     * 获得当前线程
     */
    @Test
    public void testCurrentThread(){
        /**
         * 这里直接打印会打印出：Thread[main,5,main]，分别是当前线程名，当前线程优先级，当前线程组名
         * 具体可参考Thread的toString方法：
         * ThreadGroup group = getThreadGroup();
         *         if (group != null) {
         *             return "Thread[" + getName() + "," + getPriority() + "," +
         *                            group.getName() + "]";
         *         } else {
         *             return "Thread[" + getName() + "," + getPriority() + "," +
         *                             "" + "]";
         *         }
         */
        System.out.println(Thread.currentThread());
    }

    /**
     * 判断线程是否处于活动状态
     * 活动状态就是线程已经启动且尚未终止。线程处于正在运行或准备开始运行的状态。
     */
    @Test
    public void testIsAlive() throws InterruptedException {
        FirstThread firstThread = new FirstThread("firstThread");
        System.out.println("线程没有启动前的活动状态:" + firstThread.isAlive());
//        如果是用firstThread.run();代替firstThread.start();，会打印“启动线程后的活动状态：false”，因为只是运行了一个线程的方法
        firstThread.start();
//        不睡两秒的情况下，这时候firstThread还在运行状态或者准备运行状态，此时会打印出“启动线程后的活动状态：true”
//        睡了两秒后，线程已经执行完了，状态不是活动状态了，此时会打印出“启动线程后的活动状态：false”
//        Thread.sleep(2000);
        System.out.println("启动线程后的活动状态：" + firstThread.isAlive());
    }

    /**
     * 测试this.getName()为什么和Thread.currentThread().getName的不同
     */
    @Test
    public void testCurrentThreadAndThis() throws InterruptedException {
        TestCurrentThread myThread = new TestCurrentThread("新建的线程对象");
        myThread.start();
        Thread thread = new Thread(myThread);
        System.out.println("开始运行测试方法，线程名为B的线程，isAlive=" + thread.isAlive());
//        将新建的线程对象传入到Thread的构造方法后新建的线程对象，我们叫他A
        thread.setName("A");
        thread.start();
        Thread.sleep(1000);
        System.out.println("main end thread isAlive=" + thread.isAlive());
    }

    /**
     * 测试sleep方法，这里就简单的写了一下，睡的是main线程，所以时间会差两秒
     */
    @Test
    public void testSleep() throws InterruptedException {
        System.out.println("开始执行testSleep方法：time:" + System.currentTimeMillis());
        Thread.sleep(2000);
        System.out.println("结束执行testSleep方法: time:" + System.currentTimeMillis());
    }

    /**
     * 获取线程的唯一标识，这里就简单的打印了一下，这个id是Thread内部维护了一个静态变量，自增的方法保证了线程安全。在线程生命周期内这个id会一直跟着那个线程
     * 不在生命周期了以后这个id可能会对应另一个线程了。
     */
    @Test
    public void testGetId(){
        System.out.println(Thread.currentThread().getId());
        Thread thread = new Thread();
        Thread thread1 = new Thread();
        System.out.println(thread.getId());
        System.out.println(thread1.getId());
    }

    /**
     * 测试中断
     */
    @Test
    public void testInterrupt(){
        try {
            TestInterruptThread testInterrupt = new TestInterruptThread();
            testInterrupt.start();
            Thread.sleep(2000);
            testInterrupt.interrupt();
        } catch (InterruptedException e) {
            System.out.println("我被中断啦！！");
            e.printStackTrace();
        }
    }

    /**
     * 测试是否中断，即interrupted()方法普通用法
     */
    @Test
    public void testInterrupted(){
        try {
            TestInterruptThread testInterrupt = new TestInterruptThread();
            testInterrupt.start();
            Thread.sleep(2000);
            testInterrupt.interrupt();
//            两条语句打印结果是一样的，都是“当前线程:main是否被中断:false”，因为当前线程是main线程，没有收到中断信号
            System.out.println("当前线程:" + Thread.currentThread().getName() + "是否被中断:" + Thread.interrupted());
            System.out.println("当前线程:" + Thread.currentThread().getName() + "是否被中断:" + testInterrupt.interrupted());
        } catch (InterruptedException e) {
            System.out.println("我被中断啦！！");
            e.printStackTrace();
        }
    }

    /**
     * 测试是否中断，interrupted()方法特性
     * 即，如果连续两次或两次以上调用该方法，则第二次调用将返回false（在第一次调用已清除了其中断状态之后，且第二次调用校验完中断状态前，当前线程再次中断的情况除外）
     * 特殊情况见该测试方法中的注释一
     */
    @Test
    public void testInterrupted1(){
        Thread.currentThread().interrupt();
//        这里是打印“当前线程:main是否被中断:true”
        System.out.println("当前线程:" + Thread.currentThread().getName() + "是否被中断:" + Thread.interrupted());
//        注释一：这里放开注释，会打印“打印“当前线程:main是否被中断:true”，因为中断状态被清除之后，又调用了中断方法，这时候中断状态又有了
//        Thread.currentThread().interrupt();
//        这里是打印“当前线程:main是否被中断:false”
        System.out.println("当前线程:" + Thread.currentThread().getName() + "是否被中断:" + Thread.interrupted());
    }

    /**
     * 测试isInterrupted()，这个方法和interrupted不同，不是静态方法，不会清除中断状态，这个方法是返回调用该方法的对象的中断状态
     */
    @Test
    public void testIsInterrupted(){
        try {
            TestInterruptThread testInterrupt = new TestInterruptThread();
            testInterrupt.start();
            Thread.sleep(2000);
            testInterrupt.interrupt();
//            会打印两次“线程:Thread-0是否被中断:true”
            System.out.println("线程:" + testInterrupt.getName() + "是否被中断:" + testInterrupt.isInterrupted());
            System.out.println("线程:" + testInterrupt.getName() + "是否被中断:" + testInterrupt.isInterrupted());
        } catch (InterruptedException e) {
            System.out.println("我被中断啦！！");
            e.printStackTrace();
        }
    }

    /**
     * 测试停止线程，通过中断去停止线程，推荐使用，如果run方法收到中断信号没有抛异常进入catch块的话，那线程收到中断信号不会立即停止，会在它认为合适的时候停止
     */
    @Test
    public void testStopThread(){
        TestStopThread testStopThread = new TestStopThread();
        testStopThread.start();
        try {
            Thread.sleep(2000);
            testStopThread.interrupt();
        } catch (InterruptedException e) {
            System.out.println("test方法catch掉了中断异常");
            e.printStackTrace();
        }
    }

    /**
     * 测试停止线程，通过中断去停止正在sleep的线程
     */
    @Test
    public void testStopThread2(){
        TestInterruptThread2 testInterruptThread2 = new TestInterruptThread2();
        testInterruptThread2.start();
        try {
            Thread.sleep(200);
            testInterruptThread2.interrupt();
        } catch (InterruptedException e) {
            System.out.println("test方法catch掉了中断异常");
            e.printStackTrace();
        }
    }

    /**
     * 测试yield方法
     */
    @Test
    public void testYield(){
        TestYieldThread testYieldThread = new TestYieldThread();
        testYieldThread.start();
        try {
//            这里不睡的话，打印不出来
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试线程优先级继承特性
     */
    @Test
    public void testPriority(){
        TestPriorityThread testPriorityThread = new TestPriorityThread();
        testPriorityThread.setPriority(6);
        testPriorityThread.start();
        try {
//            不睡的话test方法就结束了，会少打印东西
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试守护线程特性，当没有非守护线程在进程中运行后，守护线程会自动销毁
     */
    @Test
    public void testDaemon(){
        TestDaemonThread testDaemonThread = new TestDaemonThread();
//        这里注意，要在启动前设置是否为守护线程，不然会抛java.lang.IllegalThreadStateException异常，默认为否
        testDaemonThread.setDaemon(true);
        testDaemonThread.start();
        try {
            Thread.sleep(3000);
            System.out.println("我执行完了以后就没有非守护线程了，守护线程要开始销毁了！");
//            执行完这句后testDaemonThread守护线程可能还会打印出几次，因为还没那么快销毁
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
