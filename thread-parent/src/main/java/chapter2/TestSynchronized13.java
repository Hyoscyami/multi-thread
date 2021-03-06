package chapter2;
/**
* @Author xusf
* @Date 2020/1/7 9:42
* @Description : 测试同步不具有继承性，即父类方法加了synchronized，子类重写该方法时不加synchronized，多线程执行子类方法时不会同步
*/
public class TestSynchronized13 extends TestSynchronized12{
    /**
     * 这里不加关键字synchronized的话，会出现异步打印的情况，即上一个线程还没走完，另一个线程就进来了
     */
    @Override
    public void service() {
        System.out.println("这是子类TestSynchronized13的service方法，当前线程名:" + Thread.currentThread().getName() + "，时间戳：" + System.currentTimeMillis());
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.service();
        System.out.println("这是子类TestSynchronized13的service方法末尾，当前线程名:" + Thread.currentThread().getName() + "，时间戳：" + System.currentTimeMillis());
    }
}
