package ladysnake.deadmanabyss.api.capability;

public interface DmaEventHandler {
    int getTicksSinceLastEvent();

    void setTicksSinceLastEvent(int ticksSinceLastEvent);

    float getTransitionProgress();

    void startEvent();

    void stopEvent();

    void tick();

    boolean isEventOccuring();
}
