package top.lxpsee.javaday03.tcp.qq.common;

import java.io.IOException;
import java.io.InputStream;

/**
 * 努力常态化  2018/7/9 10:18 The world always makes way for the dreamer
 * <p>
 * 消息工厂
 */
public class MessageFactory {

    /**
     * 从流中解析消息 ,解析客户端消息，并直接转换成服务器消息
     */
    public static Message parseMessage(InputStream inputStream) {
        try {
            byte[] msgTypeBytes = new byte[4];
            inputStream.read(msgTypeBytes);
            Message message = null;

            switch (Utils.bytes2Int(msgTypeBytes)) {
                /* client --> server */
                /* 1.客户端群聊消息 */
                case Message.CLIENT_TO_SERVER_CHATS: {
                    message = new ClientChatsMessage();
                    byte[] messageLen = new byte[4];
                    inputStream.read(messageLen);
                    byte[] messageBytes = new byte[Utils.bytes2Int(messageLen)];
                    inputStream.read(messageBytes);
                    ((ClientChatsMessage) message).setMessageBytes(messageBytes);
                }
                break;

                /* 2.客户端刷新好友列表消息 */
                case Message.CLIENT_TO_SERVER_REFRESH_FRIENDS:
                    message = new ClientRefreshFroendsSMessage();
                    break;

                /* 3.客户端退出 */
                case Message.CLIENT_TO_SERVER_EXIT:
                    message = new ClientExitMessage();
                    break;

                /* 4.客户端私聊 */
                case Message.CLIENT_TO_SERVER_SINGLE_CHAT: {
                    /* 创建消息对象，读取消息长度，读取消息内容，设置消息数组 */
                    message = new ClientSingleChatMessage();
                    byte[] messageLen = new byte[4];
                    inputStream.read(messageLen);
                    byte[] messageBytes = new byte[Utils.bytes2Int(messageLen)];
                    inputStream.read(messageBytes);
                    ((ClientSingleChatMessage) message).setMessageBytes(messageBytes);
                }
                break;

                case Message.SERVER_TO_CLIENT_CHATS:
                    break;
                case Message.SERVER_TO_CLIENT_SINGLE_CHAT:
                    break;
                case Message.SERVER_TO_CLIENT_REFRESH_FRIENTS:
                    break;
            }

            return message;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
