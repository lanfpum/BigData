package top.lxpsee.javaday03.tcp.qq.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 努力常态化  2018/7/9 9:42 The world always makes way for the dreamer
 */
public class QQServer {
    /**
     * 维护所有socket连接，对象
     * key:remoteIP + ":" + remotePort
     */
    private Map<String, Socket> allSockets = new HashMap<String, Socket>();

    /**
     * 启动服务器
     */
    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);

            while (true) {
                // 方法是阻塞的
                Socket socket = serverSocket.accept();
                InetSocketAddress remoteSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
                // 远程ip和远程端口
                String remoteIp = remoteSocketAddress.getAddress().getHostAddress();
                int remotePort = remoteSocketAddress.getPort();
                String key = remoteIp + ":" + remotePort;
                allSockets.put(key, socket);
                // 开起服务器端通信线程
                new CommonThread(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
