package ladysnake.deadmanabyss.entity;

public class SoundLocation {
    public static final int MAX_SOUND_MERGE_DISTANCE_SQ = 25;

    private double x, y, z;
    private float weight;

    public SoundLocation(double x, double y, double z, float volume) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.weight = volume;
    }

    public double squareDistanceTo(SoundLocation vec) {
        double d0 = vec.x - this.x;
        double d1 = vec.y - this.y;
        double d2 = vec.z - this.z;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public void merge(SoundLocation that) {
        this.x = ((this.x * this.weight) + (that.x * that.weight)) / (this.weight + that.weight);
        this.y = ((this.y * this.weight) + (that.y * that.weight)) / (this.weight + that.weight);
        this.z = ((this.z * this.weight) + (that.z * that.weight)) / (this.weight + that.weight);
        this.weight += that.weight;
    }

    public float getWeight() {
        return this.weight;
    }

    public void fade(float v) {
        this.weight -= v;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
