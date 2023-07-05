package example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.enzoly.redis.packet.RedisPacket;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ServerUpdatePacket implements RedisPacket {

    private String name;

}
