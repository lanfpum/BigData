package top.lxpsee.javaday03.tcp.qq.server;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * 努力常态化  2018/7/9 9:38 The world always makes way for the dreamer
 */
public class QQServerMain {

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            while (true) {
                Socket socket = serverSocket.accept();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
