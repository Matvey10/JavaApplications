package com.company;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;

public class FileWork {
    public static void writeFileChannel(ByteBuffer byteBuffer, String path_name) throws IOException {
        Set<StandardOpenOption> options = new HashSet<>();
        options.add(StandardOpenOption.CREATE);
        options.add(StandardOpenOption.APPEND);
        System.out.println("path_name: " + path_name);
        Path path = Paths.get(path_name);
        FileChannel fileChannel = FileChannel.open(path, options);
        fileChannel.write(byteBuffer);
        fileChannel.close();
    }
    public static void readFileChannel(Path path) throws IOException {
        String path_name = path.toString();
        RandomAccessFile randomAccessFile = new RandomAccessFile(path_name, "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        Charset charset = Charset.forName("US-ASCII");
        while (fileChannel.read(byteBuffer) > 0) {
            byteBuffer.rewind();//позиция ставится на 0
            System.out.print(charset.decode(byteBuffer));
            byteBuffer.flip();// переключает режим буфера с режима записи на режим чтения
        }
        fileChannel.close();
        randomAccessFile.close();
    }
}
