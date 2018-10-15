package ladysnake.bansheenight.command;

import ladysnake.bansheenight.api.event.DmaEventHandler;
import ladysnake.bansheenight.capability.CapabilityDmaEvent;
import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class CommandDma extends CommandBase {
    @Override
    public String getName() {
        return "dma";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "dma.command.usage";
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if(args.length == 1) return getListOfStringsMatchingLastWord(args, "on", "off");
        else if(args.length == 2) return getListOfStringsMatchingLastWord(args, Arrays.stream(server.worlds).filter(w -> w.getCapability(CapabilityDmaEvent.CAPABILITY_DMA_EVENT, null) != null).map(w -> w.provider.getDimension()).collect(Collectors.toList()));
        else return Collections.emptyList();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0) {
            boolean flag;
            if (args[0].equals("on")) {
                flag = true;
            } else if (args[0].equals("off")) {
                flag = false;
            }
            else throw new CommandException(getUsage(sender));

            World world = args.length == 2 ? server.getWorld(parseInt(args[1])) : sender.getEntityWorld();
            DmaEventHandler cap = world.getCapability(CapabilityDmaEvent.CAPABILITY_DMA_EVENT, null);
            if (cap != null) {
                if(flag) cap.startEvent();
                else cap.stopEvent();
                sender.sendMessage(new TextComponentTranslation("dma.command.status." + (cap.isEventOccuring() ? "on" : "off")));
            }
            else {
                throw new CommandException("dma.command.no_cap");
            }
        }
    }
}
