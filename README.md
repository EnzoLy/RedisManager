# RedisManager

RedisManager is a Java library for managing the connection and communication with Redis. It provides an easy-to-use interface for sending and receiving Redis packets, along with handling Redis listeners and packet handlers.

## Features

- Simplified management of connection and communication with Redis.
- Support for sending and receiving Redis packets.
- Register listeners and packet handlers to handle incoming Redis packets.
- Uses the Kryo library for serialization and deserialization of Redis packets.

## Getting Started

### Prerequisites

- Java 8 or higher.
- Redis server running.

### Installation

1. Clone the repository:

```shell
git clone https://github.com/EnzoLy/RedisManager.git
```

2. Build the project using your favorite Java build tool:

```shell
mvn clean install
```

### Usage

Here's a quick example to get you started with RedisManager:

``` java
// Create Redis credentials
RedisCredentials credentials = new RedisCredentials("localhost", 6379, "my_channel");

// Create RedisManager
RedisManager redisManager = new RedisManager(credentials);

// Create a RedisListener
RedisListener listener = new MyRedisListener();

// Register the listener
redisManager.registerListener(listener);

// Create a RedisPacket to send
RedisPacket packet = new MyCustomPacket("Hello Redis!");

// Send the packet
redisManager.sendPacket(packet);
```

### Creating Custom Packets

To create custom packets, implement the `RedisPacket` interface and ensure the class has an empty constructor. Here's an example:

```java
public class MyCustomPacket implements RedisPacket {

    private String message;

    // Empty constructor is required for serialization
    public MyCustomPacket() {
        // Empty constructor
    }

    public MyCustomPacket(String message) {
        this.message = message;
    }

    // Getters and setters

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
```  
In the example above, we create a `MyCustomPacket` class that implements the `RedisPacket` interface. It has an empty constructor, which is necessary for serialization and deserialization. It also has additional constructors and getters/setters for customizing the packet's content.

When creating custom packet classes, make sure to include an empty constructor to ensure proper serialization and deserialization by the Kryo library used in RedisManager.

### Creating RedisListeners

To handle incoming Redis packets, you need to create a RedisListener and annotate the corresponding methods with `@RedisHandler`. Here's an example:

```java
public class MyRedisListener implements RedisListener {

    @RedisHandler
    public void onCustomPacket(MyCustomPacket packet) {
        // Handle custom packet
        System.out.println("Received custom packet: " + packet);
    }
}
```

## Additional Info

- Make sure you have the Redis server up and running before using RedisManager.
- RedisManager uses the Kryo library for serialization and deserialization of Redis packets. You can customize the serialization process by registering additional classes with the Kryo instance in RedisManager.
- Feel free to explore the source code for more advanced usage and customization options.
- Contributions are welcome! If you encounter any issues or have suggestions for improvements, please create an issue or submit a pull request.

## License

This project is licensed under the [MIT License](LICENSE).

## Acknowledgements

The RedisManager library utilizes the following open-source libraries:

- [Jedis](https://github.com/redis/jedis) - Java Redis client.
- [Kryo](https://github.com/EsotericSoftware/kryo) - Fast and efficient object graph serialization framework.

## Conclusion

RedisManager simplifies the management of Redis connections and packet handling in Java applications. It provides an easy-to-use interface for sending and receiving Redis packets, along with support for registering listeners and handlers. Whether you're building a real-time messaging system, caching layer, or distributed task management, RedisManager can enhance your Redis integration.

## Contributing

Contributions are welcome! If you encounter any issues or have suggestions for improvements, please create an issue or submit a pull request. When contributing, please follow the [contribution guidelines](CONTRIBUTING.md).

## License

This project is licensed under the [MIT License](LICENSE).
