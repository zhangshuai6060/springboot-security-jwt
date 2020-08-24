package com.aaa.zhang.util;

import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author 风行烈
 */
@Component
public class RedisMethod {

    @Autowired
    private RedissonClient redissonClient;

    public String getString(String key) {
        RBucket<Object> result = this.redissonClient.getBucket(key);
        if (result.get() == null) {
            return null;
        }
        return result.get().toString();
    }

    public JwtUser getResult(String key) {
        RBucket<JwtUser> result = this.redissonClient.getBucket(key);
        if (result != null) {
            return result.get();
        }
        return null;
    }

    public Result getTeacherResult(String key) {
        RBucket<Result> result = this.redissonClient.getBucket(key);
        if (result != null) {
            return result.get();
        }
        return null;
    }

    public void setString(String key, Object value) {
        RBucket<Object> result = this.redissonClient.getBucket(key);
        if (!result.isExists()) {
            result.set(value);
        }else {
            //如果存在
            result.delete();
            result.set(value);
        }
    }


    //

    /**
     * 设置key的过期时间 time为秒单位
     *
     * @param key   key
     * @param value value
     * @param time  过期时间
     */
    public void setStringTime(String key, Object value, Long time) {
        RBucket<Object> result = this.redissonClient.getBucket(key);
        if (!result.isExists()) {
            result.set(value, time, TimeUnit.SECONDS);
        }
    }

    /**
     * 删除map中的一个key
     *
     * @param key key
     * @param id  map中的key
     * @return
     */
    public boolean delMap(String key, Long id) {
        //判断这个key存在不存在
        RBucket<Object> result = this.redissonClient.getBucket(key);
        if (result.isExists()) {
            //如果存在就走这里
            RMap<Object, Object> map = this.redissonClient.getMap(key);
            //获得这个人所对应的东西 如果不为null就删除掉
            Object o = map.get(id + key);
            if (o != null) {
                //删除掉这个key
                Object remove = map.remove(id + key);
                if (remove != null) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * 删除key
     *
     * @param key key
     * @return boolean
     */
    public boolean delString(String key) {
        RBucket<Object> result = this.redissonClient.getBucket(key);
        if (result.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除hash中的某个key
     *
     * @param key key
     * @return string
     */
    public String delHashKey(String key) {
        RMap<Object, Object> map = this.redissonClient.getMap("menus");
        RLock lock = map.getLock(key);
        return lock.toString();
    }

    public long incr(String key, long delta) {
        return this.redissonClient.getAtomicLong(key).addAndGet(delta);
    }

    public void lock() {
        RCountDownLatch countDown = redissonClient.getCountDownLatch("aa");
        countDown.trySetCount(1);
        try {
            countDown.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        RCountDownLatch latch = redissonClient.getCountDownLatch("countDownLatchName");
        latch.countDown();
        RReadWriteLock rwlock = redissonClient.getReadWriteLock("lockName");
        rwlock.readLock().lock();
        rwlock.writeLock().lock();
        rwlock.readLock().lock(10, TimeUnit.SECONDS);
        rwlock.writeLock().lock(10, TimeUnit.SECONDS);
        try {
            boolean res = rwlock.readLock().tryLock(100, 10, TimeUnit.SECONDS);
            boolean res1 = rwlock.writeLock().tryLock(100, 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
