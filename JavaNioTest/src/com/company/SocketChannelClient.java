package com.company;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

public class SocketChannelClient {
    public static void runClient(String path_name) throws IOException {
        ServerSocketChannel serverSocket = null;
        SocketChannel clientChannel = null;
        serverSocket = ServerSocketChannel.open();
        //получаем сокет сервера связанный с этим каналом и привязываем к локальному адрессу
        serverSocket.socket().bind(new InetSocketAddress(8080));
        //получаем соединение с каналом с сервера для прослушивания
        clientChannel = serverSocket.accept();
        System.out.println("Connection Set:  " + clientChannel.getRemoteAddress());
        Path path = Paths.get(path_name);
        FileChannel fileChannel = FileChannel.open(path,
                EnumSet.of(StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.WRITE)
        );
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //записываем в буфер данные получаемые от сервера и читаем из него в файл
        while(clientChannel.read(buffer) > 0) {
            buffer.flip();
            fileChannel.write(buffer);
            buffer.clear();
        }
        fileChannel.close();
        System.out.println("File Received");
        clientChannel.close();
    }
}
