package com.lee.springcloud.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class NoBlockClient {

    public static void main(String[] args) throws Exception {

        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 6666));

        socketChannel.configureBlocking(false);

        FileChannel fileChannel = FileChannel.open(Paths.get("/Users/lee/Desktop/截屏2021-01-20 上午10.31.47.png"), StandardOpenOption.READ);

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (fileChannel.read(buffer) != -1) {

            buffer.flip();

            socketChannel.write(buffer);

            buffer.clear();
        }

        fileChannel.close();
        socketChannel.close();
    }

}
