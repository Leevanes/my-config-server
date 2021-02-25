package com.lee.springcloud.nio;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class BasicChannelExample {

    public static void main(String[] args) throws Exception {

        RandomAccessFile aFile = new RandomAccessFile("/Users/lee/Desktop/1.txt", "rw");

        FileChannel inChannel = aFile.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(48);

        int byteRead = inChannel.read(buf);

        while (byteRead != -1) {
            System.out.println("read " + byteRead);
            buf.flip();

            while (buf.hasRemaining()) {

                System.out.println((char) buf.get());

            }

            buf.clear();

            byteRead = inChannel.read(buf);
        }

        aFile.close();


    }

}
