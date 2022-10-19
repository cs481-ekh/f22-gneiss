package com.csalem;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.api.sync.RedisStringCommands;
import io.lettuce.core.api.StatefulRedisConnection;

class Main {

    public static void main(String[] args) {
        RedisClient client = RedisClient.create("redis://password@172.26.106.248:6379/0");
        StatefulRedisConnection connection = client.connect();
        RedisCommands syncCommands = connection.sync();
        System.out.println("Connection successful!");
        connection.close();
        client.shutdown();
    }

    
}