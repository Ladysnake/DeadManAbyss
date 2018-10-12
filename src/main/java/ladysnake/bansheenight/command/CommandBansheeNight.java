package ladysnake.bansheenight.command;

import ladysnake.bansheenight.api.event.BansheeNightHandler;
import ladysnake.bansheenight.capability.CapabilityBansheeNight;
import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class CommandBansheeNight extends CommandBase {
    @Override
    public String getName() {
        return "banshee_night";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "bansheenight.command.usage";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("bn");
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if(args.length == 1) return getListOfStringsMatchingLastWord(args, "on", "off");
        else if(args.length == 2) return getListOfStringsMatchingLastWord(args, Arrays.stream(server.worlds).filter(w -> w.getCapability(CapabilityBansheeNight.CAPABILITY_BANSHEE_NIGHT, null) != null).map(w -> w.provider.getDimension()).collect(Collectors.toList()));
        else return Collections.emptyList();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0) {
            BansheeNightHandler cap = sender.getEntityWorld().getCapability(CapabilityBansheeNight.CAPABILITY_BANSHEE_NIGHT, null);
            if (cap != null) {
                if (args[0].equals("on")) {
                    cap.startBansheeNight();
                } else if (args[0].equals("off")) {
                    cap.stopBansheeNight();
                }
                sender.sendMessage(new TextComponentTranslation("bansheenight.command.status." + (cap.isBansheeNightOccurring() ? "on" : "off")));
            } else {
                throw new CommandException("bansheenight.command.no_cap");
            }
        }
    }
}
