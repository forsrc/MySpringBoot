package com.forsrc.utils;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.*;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Jredis utils.
 */
public class JredisUtils {

    /**
     * The enum Key type.
     */
    public static enum KeyType {
        /**
         * Key type string key type.
         */
        KEY_TYPE_STRING("/key_type_string/"),
        /**
         * Key type string json key type.
         */
        KEY_TYPE_STRING_JSON("/key_type_string_json/"),
        /**
         * Key type hash key type.
         */
        KEY_TYPE_HASH("/key_type_hash/"),
        /**
         * Key type list key type.
         */
        KEY_TYPE_LIST("/key_type_list/"),
        /**
         * Key type set key type.
         */
        KEY_TYPE_SET("/key_type_set/"),
        /**
         * Key type sorted set key type.
         */
        KEY_TYPE_SORTED_SET("/key_type_sorted_set/"),
        /**
         * Key type pub sub key type.
         */
        KEY_TYPE_PUB_SUB("/key_type_pub_sub/");

        private String type;

        KeyType(String type) {
            this.type = type;
        }

        /**
         * Gets type.
         *
         * @return the type
         */
        public String getType() {
            return type;
        }
    }

    private static final Pattern PATTERN_KEY = Pattern.compile("^.+/key_type_.+/.+$");

    private static ThreadLocal<KeyType> _keyType = new ThreadLocal<KeyType>();
    private static ThreadLocal<String> _namespace = new ThreadLocal<String>();
    private static ThreadLocal<String> _key = new ThreadLocal<String>();
    private static ThreadLocal<ShardedJedis> _shardedJedis = new ThreadLocal<ShardedJedis>();

    private static ShardedJedisPool _shardedJedisPool;

    private static class JredisUtilsClass {
        private static JredisUtils INSTANCE = new JredisUtils();
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static JredisUtils getInstance() {
        setShardedJedisPool();
        return JredisUtilsClass.INSTANCE;
    }

    /**
     * Gets instance.
     *
     * @param shardedJedisPool the sharded jedis pool
     * @return the instance
     */
    public static JredisUtils getInstance(ShardedJedisPool shardedJedisPool) {
        setShardedJedisPool(shardedJedisPool);
        return JredisUtilsClass.INSTANCE;
    }

    /**
     * Sets sharded jedis pool.
     */
    public static void setShardedJedisPool() {
        if (_shardedJedisPool == null) {
            synchronized (JredisUtils.class) {
                if (_shardedJedisPool == null) {
                    _shardedJedisPool = getShardedJedisPool();
                }
            }
        }
    }


    /**
     * Sets sharded jedis pool.
     *
     * @param pool the pool
     */
    public static synchronized void setShardedJedisPool(ShardedJedisPool pool) {
        _shardedJedisPool = pool;
    }

    /**
     * Gets sharded jedis pool.
     *
     * @return the sharded jedis pool
     */
    public static ShardedJedisPool getShardedJedisPool() {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("classpath:config/spring/redis/spring_redis_config.xml");
        return (ShardedJedisPool) context.getBean("shardedJedisPool");
    }


    /**
     * Gets sharded jedis.
     *
     * @param pool the pool
     * @return the sharded jedis
     */
    public ShardedJedis getShardedJedis(ShardedJedisPool pool) {
        ShardedJedis jedis = _shardedJedis.get();
        if (jedis == null) {
            synchronized (JredisUtils.class) {
                if (jedis == null) {
                    jedis = pool.getResource();
                    _shardedJedis.set(jedis);
                }
            }
        }
        return jedis;
    }

    /**
     * Close.
     */
    public void close() {
        _keyType.remove();
        _key.remove();
        _namespace.remove();
        ShardedJedis jedis = getShardedJedis();
        if (jedis == null) {
            synchronized (JredisUtils.class) {
                if (jedis != null) {
                    jedis.close();
                    _shardedJedis.remove();
                }
            }
        }
    }

    /**
     * Gets sharded jedis.
     *
     * @return the sharded jedis
     */
    public ShardedJedis getShardedJedis() {
        return getShardedJedis(_shardedJedisPool);
    }

    /**
     * Set jredis utils.
     *
     * @param value the value
     * @return the jredis utils
     * @throws JredisUtilsException the jredis utils exception
     */
    public JredisUtils set(final String value) throws JredisUtilsException {
        ShardedJedis shardedJedis = getShardedJedis();
        final String k = formatKey(_namespace.get(), _keyType.get(), _key.get());
        handle(new Callback<ShardedJedis>() {
            @Override
            public void handle(ShardedJedis shardedJedis) throws JredisUtilsException {

                String statusCodeReply = shardedJedis.set(k, value);
                if (!"OK".equalsIgnoreCase(statusCodeReply)) {
                    throw new JredisUtilsException(statusCodeReply);
                }

            }
        });
        return this;
    }

    /**
     * Sets key type.
     *
     * @param keyType the key type
     * @return the key type
     */
    public JredisUtils setKeyType(KeyType keyType) {
        _keyType.set(keyType);
        return this;
    }

    /**
     * Sets namespace.
     *
     * @param namespace the namespace
     * @return the namespace
     */
    public JredisUtils setNamespace(String namespace) {
        _namespace.set(namespace);
        return this;
    }

    /**
     * Sets key.
     *
     * @param key the key
     * @return the key
     */
    public JredisUtils setKey(String key) {
        _key.set(key);
        return this;
    }

    /**
     * Sets key.
     *
     * @param namespace the namespace
     * @param keyType   the key type
     * @param key       the key
     * @return the key
     */
    public JredisUtils setKey(String namespace, KeyType keyType, String key) {
        _namespace.set(namespace);
        _keyType.set(keyType);
        _key.set(key);
        return this;
    }

    /**
     * Get jredis utils.
     *
     * @param callback the callback
     * @return the jredis utils
     * @throws JredisUtilsException the jredis utils exception
     */
    public JredisUtils get(final Callback<String> callback) throws JredisUtilsException {
        final String k = formatKey(_namespace.get(), _keyType.get(), _key.get());
        ShardedJedis shardedJedis = getShardedJedis();
        handle(new Callback<ShardedJedis>() {
            @Override
            public void handle(ShardedJedis shardedJedis) throws JredisUtilsException {
                String value = shardedJedis.get(k);
                callback.handle(value);
            }
        });

        return this;
    }

    /**
     * Delete jredis utils.
     *
     * @return the jredis utils
     * @throws JredisUtilsException the jredis utils exception
     */
    public JredisUtils delete() throws JredisUtilsException {
        final String k = formatKey(_namespace.get(), _keyType.get(), _key.get());
        ShardedJedis shardedJedis = getShardedJedis();
        handle(new Callback<ShardedJedis>() {
            @Override
            public void handle(ShardedJedis shardedJedis) throws JredisUtilsException {
                Long integerReply = shardedJedis.del(k);
                if (integerReply == null || integerReply < 0) {
                    throw new JredisUtilsException(
                            MessageFormat.format("Delete key '{0}' integerReply: {1}", k, integerReply));
                }
            }
        });
        return this;
    }


    /**
     * Format key string.
     *
     * @param namespace the namespace
     * @param keyType   the key type
     * @param key       the key
     * @return the string
     * @throws JredisUtilsException the jredis utils exception
     */
    public static String formatKey(final String namespace, final KeyType keyType, final String key) throws JredisUtilsException {
        String k = namespace + keyType.getType() + key;
        if (MyStringUtils.isBlank(namespace)) {
            throw new IllegalArgumentException("Namespace is blank. -> " + k);
        }
        if (MyStringUtils.isBlank(key)) {
            throw new IllegalArgumentException("Key is blank. -> " + k);
        }
        checkKey(k);
        return k;
    }

    /**
     * Handle jredis utils.
     *
     * @param callback the callback
     * @return the jredis utils
     * @throws JredisUtilsException the jredis utils exception
     */
    public final JredisUtils handle(final Callback<ShardedJedis> callback) throws JredisUtilsException {
        ShardedJedis shardedJedis = getShardedJedis();
        shardedJedis.getShard("");

        try {
            callback.handle(shardedJedis);
        } catch (Exception e) {
            close();
            throw new JredisUtilsException(e);
        } finally {

        }
        return this;
    }


    /**
     * Handle transaction jredis utils.
     *
     * @param jedisKey the jedis key
     * @param callback the callback
     * @return the jredis utils
     * @throws JredisUtilsException the jredis utils exception
     */
    public final JredisUtils handleTransaction(String jedisKey, final Callback<Jedis> callback) throws JredisUtilsException {
        ShardedJedis shardedJedis = getShardedJedis();
        Jedis jedis = shardedJedis.getShard(jedisKey);
        Transaction transaction = jedis.multi();
        try {
            callback.handle(jedis);
            transaction.exec();
            transaction.close();
        } catch (Exception e) {
            transaction.discard();
            try {
                transaction.close();
            } catch (IOException e1) {
                throw new JredisUtilsException(e1);
            } finally {
                close();
            }
            throw new JredisUtilsException(e);
        } finally {

        }
        return this;
    }

    /**
     * Handle jedis jredis utils.
     *
     * @param jedisKey the jedis key
     * @param callback the callback
     * @return the jredis utils
     * @throws JredisUtilsException the jredis utils exception
     */
    public final JredisUtils handleJedis(String jedisKey, final Callback<Jedis> callback) throws JredisUtilsException {
        ShardedJedis shardedJedis = getShardedJedis();
        Jedis jedis = shardedJedis.getShard(jedisKey);
        try {
            callback.handle(jedis);
        } catch (Exception e) {
            close();
            throw new JredisUtilsException(e);
        } finally {

        }
        return this;
    }


    /**
     * Handle pipeline jredis utils.
     *
     * @param callback the callback
     * @return the jredis utils
     * @throws JredisUtilsException the jredis utils exception
     */
    public final JredisUtils handlePipeline(final Callback<ShardedJedisPipeline> callback) throws JredisUtilsException {
        ShardedJedis shardedJedis = getShardedJedis();
        ShardedJedisPipeline pipelined = shardedJedis.pipelined();
        try {
            callback.handle(pipelined);
            pipelined.sync();
        } catch (Exception e) {
            close();
            throw new JredisUtilsException(e);
        } finally {

        }
        return this;
    }

    /**
     * Handle jredis utils.
     *
     * @param callback the callback
     * @return the jredis utils
     * @throws JredisUtilsException the jredis utils exception
     */
    public final JredisUtils handle(final CallbackWithKey<ShardedJedis> callback) throws JredisUtilsException {
        ShardedJedis shardedJedis = getShardedJedis();
        final String k = formatKey(_namespace.get(), _keyType.get(), _key.get());
        try {
            callback.handle(k, shardedJedis);
        } catch (Exception e) {
            close();
            throw new JredisUtilsException(e);
        } finally {

        }
        return this;
    }

    /**
     * Call.
     *
     * @param namespace the namespace
     * @param type      the type
     * @param key       the key
     * @param callback  the callback
     * @throws JredisUtilsException the jredis utils exception
     */
    public static final void call(final String namespace, final KeyType type, final String key, final CallbackWithKey<ShardedJedis> callback) throws JredisUtilsException {
        JredisUtils jredisUtils = JredisUtils.getInstance();
        final String k = jredisUtils.formatKey(namespace, type, key);
        jredisUtils.setKey(namespace, type, key);
        jredisUtils.handle(callback);
    }

    /**
     * Call.
     *
     * @param <T>      the type parameter
     * @param callback the callback
     * @throws JredisUtilsException the jredis utils exception
     */
    public static final <T> void call(final Callback<ShardedJedis> callback) throws JredisUtilsException {
        JredisUtils jredisUtils = JredisUtils.getInstance();
        jredisUtils.handle(callback);
    }

    /**
     * Check reply.
     *
     * @param actual   the actual
     * @param expected the expected
     * @throws JredisUtilsException the jredis utils exception
     */
    public static void checkReply(Long actual, long expected) throws JredisUtilsException {
        if (actual == null) {
            throw new JredisUtilsException("Actual reply is null, expected : " + expected);
        }
        if (actual.equals(expected)) {
            throw new JredisUtilsException(
                    MessageFormat.format("Actual reply is {0}, expected : {1}", actual, expected)
            );
        }
    }

    /**
     * Check reply.
     *
     * @param actual the actual
     * @throws JredisUtilsException the jredis utils exception
     */
    public static void checkReply(String actual) throws JredisUtilsException {
        if (actual == null) {
            throw new JredisUtilsException("Actual reply is null, expected : OK");
        }
        if (!actual.equalsIgnoreCase("OK") && !actual.equalsIgnoreCase("QUEUED")) {
            throw new JredisUtilsException(
                    MessageFormat.format("Actual reply is {0}, expected : OK", actual)
            );
        }
    }

    /**
     * Check key.
     *
     * @param key the key
     * @throws JredisUtilsException the jredis utils exception
     */
    public static void checkKey(String key) throws JredisUtilsException {
        if (key == null) {
            throw new JredisUtilsException("The key is null.");
        }
        if (key.indexOf("/key_type_") < 0) {
            throw new JredisUtilsException("Incorrect key type in the key: " + key);
        }
        if (key.indexOf("/key_type_") == 0) {
            throw new JredisUtilsException("No namespace in the key : " + key);
        }
        Matcher matcher = PATTERN_KEY.matcher(key);
        if (!matcher.matches()) {
            throw new JredisUtilsException("Incorrect key format: " + key);

        }
    }

    /**
     * The interface Callback.
     *
     * @param <T> the type parameter
     */
    public static interface Callback<T> {
        /**
         * Handle.
         *
         * @param t the t
         * @throws JredisUtilsException the jredis utils exception
         */
        public void handle(final T t) throws JredisUtilsException;
    }

    /**
     * The interface Callback with key.
     *
     * @param <T> the type parameter
     */
    public static interface CallbackWithKey<T> {
        /**
         * Handle.
         *
         * @param key the key
         * @param t   the t
         * @throws JredisUtilsException the jredis utils exception
         */
        public void handle(final String key, final T t) throws JredisUtilsException;
    }


    /**
     * The type Jredis utils exception.
     */
    public static class JredisUtilsException extends Exception {
        /**
         * Instantiates a new Jredis utils exception.
         *
         * @param msg the msg
         */
        public JredisUtilsException(String msg) {
            super(msg);
        }

        /**
         * Instantiates a new Jredis utils exception.
         *
         * @param e the e
         */
        public JredisUtilsException(Exception e) {
            super(e);
        }

    }

}
