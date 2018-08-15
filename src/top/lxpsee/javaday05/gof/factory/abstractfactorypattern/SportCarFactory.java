package top.lxpsee.javaday05.gof.factory.abstractfactorypattern;

/**
 * The world always makes way for the dreamer
 * Created by 努力常态化 on 2018/7/16 16:26.
 */

public class SportCarFactory implements CarFactory {
    @Override
    public BenzCar getBenzCar() {
        return new BenzSportCar();
    }

    @Override
    public TeslaCar getTeslaCar() {
        return new TeslaSportCar();
    }
}
