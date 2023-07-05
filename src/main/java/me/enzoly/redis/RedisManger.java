package me.enzoly.redis;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Getter;
import me.enzoly.redis.listener.RedisHandler;
import me.enzoly.redis.listener.RedisListener;
import me.enzoly.redis.packet.RedisPacketHandler;
import me.enzoly.redis.packet.RedisPacket;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Class for managing the connection and communication with Redis.
 */
public class RedisManger {

    private final RedisCredentials credentials;
    private final JedisPool jedis;
    private final ScheduledThreadPoolExecutor redisExecutor;
    @Getter private final Kryo kryo;

    @Getter private final Map<String, List<RedisPacketHandler>> packetMethods = Maps.newConcurrentMap();

    /**
     * Constructs a new RedisManager object with the given Redis credentials.
     *
     * @param credentials The Redis credentials.
     */
    public RedisManger(RedisCredentials credentials) {
        this.credentials = credentials;

        this.jedis = credentials.getJedis();

        this.redisExecutor = new ScheduledThreadPoolExecutor(4, new ThreadFactoryBuilder().setNameFormat("Redis-Packet-Manager").build());
        this.kryo = new Kryo();

        redisExecutor.scheduleAtFixedRate(() -> {
            try {
                jedis.getResource().psubscribe(new RedisSubscriber(this), credentials.getChannel().getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.SECONDS);

    }

    /**
     * Registers a RedisListener to handle Redis packets.
     *
     * @param listener The RedisListener to register.
     */
    public void registerListener(RedisListener listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(RedisHandler.class)) {
                if (method.getParameterCount() != 1 || !RedisPacket.class.isAssignableFrom(method.getParameterTypes()[0])) {
                    throw new IllegalArgumentException("RedisHandler method must have exactly one parameter of type RedisPacket.");
                }

                Class<? extends RedisPacket> packetClass = (Class<? extends RedisPacket>) method.getParameterTypes()[0];
                this.kryo.register(packetClass);

                List<RedisPacketHandler> handlers = packetMethods.computeIfAbsent(packetClass.getName(), key -> new ArrayList<>());
                handlers.add(new RedisPacketHandler(method, listener));
            }
        }
    }

    /**
     * Sends a RedisPacket to Redis for processing.
     *
     * @param redisPacket The RedisPacket to send.
     */
    public void sendPacket(RedisPacket redisPacket) {
        redisExecutor.submit(() -> {
            try (Jedis jedis = this.jedis.getResource()) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                Output output = new Output(outputStream);

                kryo.writeClassAndObject(output, redisPacket);
                output.close();

                jedis.publish(credentials.getChannel().getBytes(), outputStream.toByteArray());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Shuts down the RedisManager, stopping the thread executor and closing the Redis connection.
     */
    public void shutdown() {
        this.redisExecutor.shutdown();

        jedis.close();
    }

}
