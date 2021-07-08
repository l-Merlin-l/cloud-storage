package io;

import java.io.*;
import java.net.Socket;

public class ChatHandler implements Runnable {

    private Socket socket;
    private byte[] buffer;
    private DataInputStream inputStream;
    private OutputStream outputStream;
    private FileOutputStream fileOutputStream;

    public ChatHandler(Socket socket) {
        this.socket = socket;
        buffer = new byte[256];
    }

    @Override
    public void run() {
        try {
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = socket.getOutputStream();
            fileOutputStream = new FileOutputStream("copy.png");
            int read;
            while (true) {
                read= inputStream.read(buffer);

                fileOutputStream.write(buffer,0,read);
                fileOutputStream.flush();
            }

        } catch (Exception e) {

        } finally {
            try {
                inputStream.close();
                outputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
