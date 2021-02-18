package com.company;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SocketChannelServer {
    public static void runServer(String path_name) throws IOException {
        SocketChannel serverChannel = SocketChannel.open();
        SocketAddress socketAddr = new InetSocketAddress("localhost", 8080);
        serverChannel.connect(socketAddr);
        //из файла пишем  в канал сокета
        Path path = Paths.get(path_name);
        FileChannel fileChannel = FileChannel.open(path);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while(fileChannel.read(buffer) > 0) {
            buffer.flip();
            serverChannel.write(buffer);//пишем в канал данные из буфера
            buffer.compact();//или clear?
        }
        fileChannel.close();
        System.out.println("File Sent");
        serverChannel.close();
    }
}
