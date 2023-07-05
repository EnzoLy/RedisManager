package me.enzoly.redis;

import com.esotericsoftware.kryo.io.Input;
import me.enzoly.redis.packet.RedisPacket;
import redis.clients.jedis.BinaryJedisPubSub;

import java.lang.reflect.InvocationTargetException;

public class RedisSubscriber extends BinaryJedisPubSub {

    private final RedisManger redisManager;

    /**
     * Constructs a new RedisSubscriber with the specified RedisManager.
     *
     * @param redisManager The RedisManager instance.
     */
    public RedisSubscriber(RedisManger redisManager) {
        this.redisManager = redisManager;
    }

    /**
     * Handles the incoming pattern-based message from Redis.
     *
     * @param pattern The pattern of the subscribed channel.
     * @param channel The channel that the message was sent to.
     * @param message The message data sent by Redis.
     */
    @Override
    public void onPMessage(byte[] pattern, byte[] channel, byte[] message) {
        Input input = new Input(message);

        RedisPacket deserializedPacket = (RedisPacket) redisManager.getKryo().readClassAndObject(input);
        input.close();
        if (deserializedPacket == null) {
            return;
        }

        if (!redisManager.getPacketMethods().containsKey(deserializedPacket.getClass().getName())) {
            return;
        }

        redisManager.getPacketMethods().get(deserializedPacket.getClass().getName()).forEach(packetHandler -> {
            try {
                packetHandler.method().invoke(packetHandler.listener(), deserializedPacket);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
