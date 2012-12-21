package com.murdock.tools.distinct.db.redis;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.murdock.tools.distinct.db.MatchDB;

/**
 * <pre>
 * 使用了Spring的aop后该类已经没有必要了
 * 
 * </pre>
 * 
 * @author weipeng 2012-12-6 下午4:42:22
 */
public class GetAndReleaseHandler implements InvocationHandler {

    private MatchDB matchDB = new MatchDBImpl();

    /*
     * (non-Javadoc)
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        try {
            result = method.invoke(matchDB, args);
        } finally {
            // 帮助释放链接
            JedisPool.getPool().releaseConnection();
        }
        return result;
    }

}
