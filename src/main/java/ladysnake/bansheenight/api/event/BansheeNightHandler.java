package ladysnake.bansheenight.api.event;

public interface BansheeNightHandler {
    int getTicksSinceLastNight();

    void startBansheeNight();

    void stopBansheeNight();

    void tick();

    boolean isBansheeNightOccurring();
}
