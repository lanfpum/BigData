package top.lxpsee.javaday05.gof.factory.factorymethodpattern;

import top.lxpsee.javaday05.gof.factory.base.MulOperation;
import top.lxpsee.javaday05.gof.factory.base.Operation;

/**
 * The world always makes way for the dreamer
 * Created by 努力常态化 on 2018/7/16 16:08.
 * <p>
 * 乘法类工厂
 */

public class MulFactory implements IFactory {
    @Override
    public Operation CreateOption() {
        return new MulOperation();
    }
}
