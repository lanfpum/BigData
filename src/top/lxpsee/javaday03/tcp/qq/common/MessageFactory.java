package top.lxpsee.javaday03.tcp.qq.common;

import top.lxpsee.javaday03.tcp.qq.server.QQServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * 努力常态化  2018/7/9 10:18 The world always makes way for the dreamer
 * <p>
 * 消息工厂
 */
public class MessageFactory {

    /**
     * 从流中解析消息 ,解析客户端消息，并直接转换成服务器消息
     */
    public static byte[] parseClientMessageAndSend(Socket socket) {
        try {
            InputStream inputStream = socket.getInputStream();
            byte[] msgTypeBytes = new byte[4];
            inputStream.read(msgTypeBytes);
            Message message = null;

            switch (Utils.bytes2Int(msgTypeBytes)) {
                /* 1.客户端群聊消息
                 *      客户端消息：消息长度和消息内容
                 *      服务端消息：发送者和消息内容
                 *
                 *    1.1:从socket中获取输入流，转换成客户端消息
                 *    1.2:将客户端消息转换成服务器消息
                 *    1.3:将服务器端消息写入内存输出流，广播发送(4 + 4 + n + 4 + n)
                 */
                case Message.CLIENT_TO_SERVER_CHATS: {
                    message = new ClientChatsMessage();
                    byte[] messageLen = new byte[4];
                    inputStream.read(messageLen);
                    byte[] messageBytes = new byte[Utils.bytes2Int(messageLen)];
                    inputStream.read(messageBytes);
                    ((ClientChatsMessage) message).setMessageBytes(messageBytes);

                    ServerChatsMessage serverMessage = new ServerChatsMessage();
                    serverMessage.setSenderInfoBytes(Utils.getUserInfo(socket));
                    serverMessage.setMessageBytes(((ClientChatsMessage) message).getMessageBytes());

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byteArrayOutputStream.write(Utils.int2Bytes(Message.SERVER_TO_CLIENT_CHATS));
                    byteArrayOutputStream.write(Utils.int2Bytes(serverMessage.getSenderInfoBytes().length));
                    byteArrayOutputStream.write(serverMessage.getSenderInfoBytes());
                    byteArrayOutputStream.write(Utils.int2Bytes(serverMessage.getMessageBytes().length));
                    byteArrayOutputStream.write(serverMessage.getMessageBytes());
                    byteArrayOutputStream.close();

                    QQServer.getInstence().broadcast(byteArrayOutputStream.toByteArray());
                }
                break;

                /* 2.客户端刷新好友列表消息,给指定的客户端刷新数据 */
                case Message.CLIENT_TO_SERVER_REFRESH_FRIENDS: {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byteArrayOutputStream.write(Utils.int2Bytes(Message.SERVER_TO_CLIENT_REFRESH_FRIENTS));
                    byteArrayOutputStream.write(Utils.int2Bytes(QQServer.getInstence().getFriendBytes().length));
                    byteArrayOutputStream.write(QQServer.getInstence().getFriendBytes());
                    byteArrayOutputStream.close();

                    QQServer.getInstence().send(byteArrayOutputStream.toByteArray(), Utils.getUserInfo(socket));
                }
                break;

                /* 3.客户端退出,广播刷新好友列表 */
                case Message.CLIENT_TO_SERVER_EXIT: {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byteArrayOutputStream.write(Utils.int2Bytes(Message.SERVER_TO_CLIENT_REFRESH_FRIENTS));
                    byteArrayOutputStream.write(Utils.int2Bytes(QQServer.getInstence().getFriendBytes().length));
                    byteArrayOutputStream.write(QQServer.getInstence().getFriendBytes());
                    byteArrayOutputStream.close();

                    QQServer.getInstence().broadcast(byteArrayOutputStream.toByteArray());
                }
                break;

                /* 4.客户端私聊
                 *       客户端信息：接受者信息和信息内容
                 *       服务端信息：发送者信息，接受者信息，信息内容
                 *   4.1：从socket输入流中读取数据成客户端信息
                 *   4.2：将客户端信息解析成服务端信息
                 *   4.3：发送给指定socket
                 */
                case Message.CLIENT_TO_SERVER_SINGLE_CHAT: {
                    /* 创建消息对象，读取消息长度，读取消息内容，设置消息数组 4 -> 发送者信息 -> 4 -> 信息内容*/
                    message = new ClientSingleChatMessage();
                    byte[] bytes = new byte[4];
                    inputStream.read(bytes);

                    byte[] receverInfoBytes = new byte[Utils.bytes2Int(bytes)];
                    inputStream.read(receverInfoBytes);
                    ((ClientSingleChatMessage) message).setReceverInfoBytes(receverInfoBytes);

                    inputStream.read(bytes);
                    byte[] messageBytes = new byte[Utils.bytes2Int(bytes)];
                    inputStream.read(messageBytes);
                    ((ClientSingleChatMessage) message).setMessageBytes(messageBytes);

                    ServerSingleChatMessage serverMessage = new ServerSingleChatMessage();
                    serverMessage.setSenderInfoBytes(Utils.getUserInfo(socket));
                    serverMessage.setReceverInfoBytes(((ClientSingleChatMessage) message).getReceverInfoBytes());
                    serverMessage.setMessageBytes(((ClientSingleChatMessage) message).getMessageBytes());

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byteArrayOutputStream.write(Message.SERVER_TO_CLIENT_SINGLE_CHAT);
                    byteArrayOutputStream.write(Utils.int2Bytes(serverMessage.getSenderInfoBytes().length));
                    byteArrayOutputStream.write(serverMessage.getSenderInfoBytes());
                    byteArrayOutputStream.write(Utils.int2Bytes(serverMessage.getMessageBytes().length));
                    byteArrayOutputStream.write(serverMessage.getMessageBytes());
                    byteArrayOutputStream.close();

                    QQServer.getInstence().send(byteArrayOutputStream.toByteArray(), serverMessage.getReceverInfoBytes());
                }
                break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
