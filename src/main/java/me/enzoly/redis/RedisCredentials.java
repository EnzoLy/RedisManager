package me.enzoly.redis;

import lombok.Getter;
import redis.clients.jedis.JedisPool;

@Getter
public class RedisCredentials {

    private final String host;
    private final int port;
    private final String password;
    private final JedisPool jedis;
    private final String channel;

    /**
     * Constructs a new instance of RedisCredentials with the specified host, port, password, and channel.
     *
     * @param host     The Redis server host.
     * @param port     The Redis server port.
     * @param password The password for authenticating with Redis.
     * @param channel  The channel for communication.
     */
    public RedisCredentials(String host, int port, String password, String channel) {
        this.host = host;
        this.port = port;
        this.password = password;
        this.channel = channel;

        this.jedis = new JedisPool(host, port);
        tryAuth();
    }

    /**
     * Constructs a new instance of RedisCredentials with the specified host, port, and channel.
     * No password authentication will be used.
     *
     * @param host    The Redis server host.
     * @param port    The Redis server port.
     * @param channel The channel for communication.
     */
    public RedisCredentials(String host, int port, String channel) {
        this(host, port, null, channel);
    }

    /**
     * Attempts to authenticate with Redis using the provided password.
     */

    public void tryAuth() {
        if (password == null || password.isEmpty()) {
            return;
        }

        jedis.getResource().auth(password);
    }

}
