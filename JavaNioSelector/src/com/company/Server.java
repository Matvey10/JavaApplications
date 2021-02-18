package com.company;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Server {
    public static void runServer(String text) throws IOException {
        SocketChannel serverChannel = SocketChannel.open();
        SocketAddress socketAddr = new InetSocketAddress("localhost", 8080);
        serverChannel.connect(socketAddr);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(text.getBytes());
        buffer.flip();
        serverChannel.write(buffer);//пишем в канал данные из буфера
        buffer.compact();//или clear?
        //serverChannel.close();
    }
}
