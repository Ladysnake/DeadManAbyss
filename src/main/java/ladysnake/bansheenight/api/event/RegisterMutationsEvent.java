package ladysnake.bansheenight.api.event;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.Map;

public class RegisterMutationsEvent extends Event {
    private final Map<ResourceLocation, ResourceLocation> mutations;

    public RegisterMutationsEvent(Map<ResourceLocation, ResourceLocation> mutations) {
        this.mutations = mutations;
    }

    public void registerMutation(ResourceLocation from, ResourceLocation to) {
        mutations.put(from, to);
    }
}
