package com.hmdp;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
class RedissonTest {

    @Resource
    private RedissonClient redissonClient;

    private RLock lock;

    @BeforeEach
    void setUp() {
        lock = redissonClient.getLock("order");
    }

    @Test
    void lockTest0() throws InterruptedException {
        lock.lock();
        System.out.println("lock.lock() 获取到了锁");

        Thread.sleep(100*1000);

        System.out.println("结束");


    }

    @Test
    void lockTest1() throws InterruptedException {
        lock.lock(90L, TimeUnit.SECONDS);
        System.out.println("lock.lock(90l, TimeUnit.SECONDS) 获取到了锁");

        Thread.sleep(200*1000);

        System.out.println("结束");

    }


    @Test
    void lockTest2() throws InterruptedException {
        lock.lock();
        System.out.println("lock.lock() 获取到了锁");
        Thread.sleep(100*1000);
        lock.lock(90L, TimeUnit.SECONDS);
        System.out.println("lock.lock(90L, TimeUnit.SECONDS) 获取到了锁");
        Thread.sleep(300*1000);
    }


    @Test
    void tryLock0() throws InterruptedException {
        boolean b1 = lock.tryLock();
        System.out.println("获取到了锁lock.tryLock()"+b1);
        Thread.sleep(100*1000);

        System.out.println("结束");

    }


    @Test
    void tryLock1() throws InterruptedException {


        System.out.println("开始获取锁："+LocalDateTime.now());
        boolean b2 = lock.tryLock(15L, TimeUnit.SECONDS);
        System.out.println("tryLock执行完："+LocalDateTime.now());


        System.out.println("获取到了锁lock.tryLock()"+b2);
        Thread.sleep(100*1000);

        System.out.println("结束");

    }

    @Test
    void tryLockTest() throws InterruptedException {

        System.out.println("开始获取锁："+LocalDateTime.now());
        boolean b2 = lock.tryLock(10,90L, TimeUnit.SECONDS);
        System.out.println("tryLock执行完："+LocalDateTime.now());


        System.out.println("获取到了锁lock.tryLock()"+b2);
        Thread.sleep(400*1000);

        System.out.println("结束");

    }


    @Test
    void method1() throws InterruptedException {
        // 尝试获取锁
        boolean isLock = lock.tryLock(111L, TimeUnit.SECONDS);
        if (!isLock) {
            log.error("获取锁失败 .... 1");
            return;
        }
        try {
            log.info("获取锁成功 .... 1");
            method2();
            log.info("开始执行业务 ... 1");
        } finally {
            log.warn("准备释放锁 .... 1");
            lock.unlock();
        }
    }
    void method2() {
        // 尝试获取锁
        boolean isLock = lock.tryLock();
        if (!isLock) {
            log.error("获取锁失败 .... 2");
            return;
        }
        try {
            log.info("获取锁成功 .... 2");
            log.info("开始执行业务 ... 2");
        } finally {
            log.warn("准备释放锁 .... 2");
            lock.unlock();
        }
    }
}
