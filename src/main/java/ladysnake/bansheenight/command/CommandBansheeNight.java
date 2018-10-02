package ladysnake.bansheenight.command;

import ladysnake.bansheenight.api.event.BansheeNightHandler;
import ladysnake.bansheenight.capability.CapabilityBansheeNight;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public class CommandBansheeNight extends CommandBase {
    @Override
    public String getName() {
        return "banshee_night";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command";
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return getListOfStringsMatchingLastWord(args, "on", "off");
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
            } else {
                throw new CommandException("bansheenight.command.no_cap");
            }
        }
    }
}
