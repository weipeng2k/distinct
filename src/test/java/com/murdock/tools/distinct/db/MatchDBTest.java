package com.murdock.tools.distinct.db;

import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.murdock.tools.distinct.db.redis.GetAndReleaseHandler;

public class MatchDBTest {

    @Test
    public void test() throws Exception {
        final MatchDB matchDB = (MatchDB) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                                                                 new Class[] { MatchDB.class },
                                                                 new GetAndReleaseHandler());
        final AtomicInteger ai = new AtomicInteger();

        for (int i = 0; i < 10; i++) {

            Thread thread = new Thread() {

                public void run() {
                    int i = 0;
                    while ((i = ai.incrementAndGet()) < 10000) {
                        matchDB.insert(String.valueOf(i));
                    }
                }
            };
            thread.start();
        }

    }
}
