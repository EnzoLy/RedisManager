package example;

import me.enzoly.redis.RedisCredentials;
import me.enzoly.redis.RedisManger;

public class Main {

    public static void main(String[] args) {
        // Create a new RedisHandler instance.
        RedisCredentials credentials = new RedisCredentials("localhost", 6379, null, "test");

        // Create a new RedisManger instance.
        RedisManger redisManger = new RedisManger(credentials);

        // Register a new listener.
        redisManger.registerListener(new ServerRedisListener());

        // Create a new packet instance.
        redisManger.sendPacket(new ServerUpdatePacket("Test"));
    }

}
