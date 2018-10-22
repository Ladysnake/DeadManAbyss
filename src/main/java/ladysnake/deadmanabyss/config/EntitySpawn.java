package ladysnake.deadmanabyss.config;

import net.minecraftforge.common.config.Config;

public class EntitySpawn {

    @Config.RangeInt(min = 0, max = 100)
    @Config.Name("Spawn Rate")
    @Config.Comment("Spawn Weight")
    public int spawnRate;

    @Config.RangeInt(min = 0, max = 100)
    @Config.Name("MIN per group")
    @Config.Comment("minimum group size")
    public int minGroupSize;

    @Config.RangeInt(min = 0, max = 100)
    @Config.Name("MAX per group")
    @Config.Comment("maximum group size")
    public int maxGroupSize;

    public EntitySpawn(int spawnRate, int minGroupSize, int maxGroupSize) {
        this.spawnRate = spawnRate;
        this.minGroupSize = minGroupSize;
        this.maxGroupSize = maxGroupSize;
    }
}
