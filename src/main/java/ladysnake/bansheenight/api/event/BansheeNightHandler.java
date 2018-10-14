package ladysnake.bansheenight.api.event;

public interface BansheeNightHandler {
    int getTicksSinceLastNight();

    void setTicksSinceLastNight(int ticksSinceLastNight);

    float getTransitionProgress();

    void startBansheeNight();

    void stopBansheeNight();

    void tick();

    boolean isBansheeNightOccurring();
}
