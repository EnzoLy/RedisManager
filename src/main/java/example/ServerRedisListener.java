package example;

import me.enzoly.redis.listener.RedisHandler;
import me.enzoly.redis.listener.RedisListener;

public class ServerRedisListener implements RedisListener {

    @RedisHandler
    public void onServerUpdate(ServerUpdatePacket packet) {
        System.out.println("Received server update packet with name " + packet.getName() + ".");
    }

}
