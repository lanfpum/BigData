package top.lxpsee.javaday03.screenbroadcast;

import java.util.ArrayList;
import java.util.List;

/**
 * udp的发送方
 * <p>
 * 努力常态化  2018/7/4 14:46 The world always makes way for the dreamer
 */
public class Teacher {

    private final static int FRAME_UNIT_MAX = 60 * 1024;

    public static void main(String[] args) {
        while (true) {
            sendOneScreenData();
        }
    }

    /**
     * 发送一屏数据
     * 1.截图
     * 2.切图
     * 3.组装UdpPacket(8+1+1+4+n(max :60))
     * 4.发送
     */
    private static void sendOneScreenData() {
        byte[] frame = ScreenBroadCastUtils.captrueScreen();
        List<FrameUnit> frameUnitList = splitFrame(frame);
        new ScreenBroadCastSender().send(frameUnitList);
    }

    /**
     * 切图,将屏幕截图切分成frameUnit集合
     * <p>
     * 注：1.判断是否被整除  2.最后一个的数据
     */
    private static List<FrameUnit> splitFrame(byte[] frame) {
        int count;
        boolean isDivision = (frame.length % FRAME_UNIT_MAX == 0);

        if (isDivision) {
            count = frame.length / FRAME_UNIT_MAX;
        } else {
            count = frame.length / FRAME_UNIT_MAX + 1;
        }

        FrameUnit frameUnit;
        long frameId = System.currentTimeMillis();
        List<FrameUnit> frameUnitList = new ArrayList<FrameUnit>();

        for (int i = 0; i < count; i++) {
            frameUnit = new FrameUnit();
            frameUnit.setFrameId(frameId);
            frameUnit.setFrameUnitCount(count);
            frameUnit.setFrameUnitNo(i);

            if (i == count - 1 && !isDivision) {
                int lastFrameUnitLen = frame.length % FRAME_UNIT_MAX;
                frameUnit.setDataLen(lastFrameUnitLen);
                byte[] unitDataBetys = new byte[lastFrameUnitLen];
                System.arraycopy(frame, i * FRAME_UNIT_MAX, unitDataBetys, 0, lastFrameUnitLen);
                frameUnit.setDataBytes(unitDataBetys);
            } else {
                frameUnit.setDataLen(FRAME_UNIT_MAX);
                byte[] unitDataBetys = new byte[FRAME_UNIT_MAX];
                System.arraycopy(frame, i * FRAME_UNIT_MAX, unitDataBetys, 0, FRAME_UNIT_MAX);
                frameUnit.setDataBytes(unitDataBetys);
            }

            frameUnitList.add(frameUnit);
        }

        return frameUnitList;
    }

}

