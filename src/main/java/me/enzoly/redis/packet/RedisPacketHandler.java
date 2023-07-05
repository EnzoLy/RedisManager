package me.enzoly.redis.packet;

import me.enzoly.redis.listener.RedisListener;

import java.lang.reflect.Method;

/**
 * Represents a handler for a specific Redis packet.
 */
public record RedisPacketHandler(Method method, RedisListener listener) {

    /**
     * Constructs a new RedisPacketHandler with the specified method and listener.
     *
     * @param method   The method that handles the Redis packet.
     * @param listener The listener associated with the packet handler.
     */
    public RedisPacketHandler(Method method, RedisListener listener) {
        this.method = method;
        this.listener = listener;
    }
}
