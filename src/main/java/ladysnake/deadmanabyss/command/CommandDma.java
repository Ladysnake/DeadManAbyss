package ladysnake.deadmanabyss.command;

import ladysnake.deadmanabyss.api.event.DmaEventHandler;
import ladysnake.deadmanabyss.capability.CapabilityDmaEvent;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
        if (args.length == 1) return getListOfStringsMatchingLastWord(args, "on", "off");
        else if (args.length == 2)
            return getListOfStringsMatchingLastWord(args, Arrays.stream(server.worlds).filter(w -> w.getCapability(CapabilityDmaEvent.CAPABILITY_DMA_EVENT, null) != null).map(w -> w.provider.getDimension()).collect(Collectors.toList()));
        else return Collections.emptyList();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0) {
            Boolean flag;
            switch (args[0]) {
                case "on":
                    flag = true;
                    break;
                case "off":
                    flag = false;
                    break;
                case "query":
                    // Just show whether the event is occuring
                    flag = null;
                    break;
                default:
                    throw new CommandException(getUsage(sender));
            }

            World world = args.length == 2 ? server.getWorld(parseInt(args[1])) : sender.getEntityWorld();
            DmaEventHandler cap = world.getCapability(CapabilityDmaEvent.CAPABILITY_DMA_EVENT, null);
            if (cap != null) {
                if (flag != null) {
                    if (flag) cap.startEvent();
                    else cap.stopEvent();
                }
                sender.sendMessage(new TextComponentTranslation("dma.command.status." + (cap.isEventOccuring() ? "on" : "off")));
            } else {
                throw new CommandException("dma.command.no_cap");
            }
        } else throw new WrongUsageException(getUsage(sender));
    }
}
