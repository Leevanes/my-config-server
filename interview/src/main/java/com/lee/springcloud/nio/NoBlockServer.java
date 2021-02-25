package com.lee.springcloud.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;

public class NoBlockServer {

    public static void main(String[] args) throws Exception {

        ServerSocketChannel server = ServerSocketChannel.open();

        server.configureBlocking(false);

        server.bind(new InetSocketAddress(6666));

        Selector selector = Selector.open();

        server.register(selector, SelectionKey.OP_ACCEPT);

        while (selector.select() > 0) {

            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {

                SelectionKey selectionKey = iterator.next();

                if (selectionKey.isAcceptable()) {

                    SocketChannel client = server.accept();

                    client.configureBlocking(false);

                    client.register(selector, SelectionKey.OP_READ);

                } else if (selectionKey.isReadable()) {
                    SocketChannel client = (SocketChannel) selectionKey.channel();

                    ByteBuffer buffer = ByteBuffer.allocate(1024);

                    FileChannel outChannel = FileChannel.open(Paths.get("2.png"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);

                    while (client.read(buffer) > 0) {
                        buffer.flip();

                        outChannel.write(buffer);

                        buffer.clear();
                    }

                    ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

                    writeBuffer.put("the img is received".getBytes(StandardCharsets.UTF_8));
                    writeBuffer.flip();
                    client.write(writeBuffer);


                }
                iterator.remove();
            }
        }
    }
}
