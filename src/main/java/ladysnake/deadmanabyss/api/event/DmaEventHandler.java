package ladysnake.deadmanabyss.api.event;

public interface DmaEventHandler {
    int getTicksSinceLastEvent();

    void setTicksSinceLastEvent(int ticksSinceLastEvent);

    float getTransitionProgress();

    void startEvent();

    void stopEvent();

    void tick();

    boolean isEventOccuring();
}
