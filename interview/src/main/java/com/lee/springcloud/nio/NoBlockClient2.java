package com.lee.springcloud.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;

public class NoBlockClient2 {

    public static void main(String[] args) throws Exception {

        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 6666));

        socketChannel.configureBlocking(false);

        Selector selector = Selector.open();

        socketChannel.register(selector, SelectionKey.OP_READ);

        FileChannel fileChannel = FileChannel.open(Paths.get("/Users/lee/Desktop/截屏2021-01-20 上午10.31.47.png"), StandardOpenOption.READ);

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (fileChannel.read(buffer) != -1) {
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();
        }

        while (selector.select() > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();

                if (selectionKey.isReadable()) {
                    SocketChannel client = (SocketChannel) selectionKey.channel();

                    ByteBuffer resBuffer = ByteBuffer.allocate(1024);

                    int resBytes = client.read(resBuffer);

                    if (resBytes > 0) {
                        resBuffer.flip();

                        System.out.println(new String(resBuffer.array(), 0, resBytes));
                    }
                }
                iterator.remove();
            }
        }

    }
}
