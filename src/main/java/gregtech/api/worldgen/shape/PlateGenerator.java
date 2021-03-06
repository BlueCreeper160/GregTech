package gregtech.api.worldgen.shape;

import com.google.gson.JsonObject;
import crafttweaker.annotations.ZenRegister;
import gregtech.api.worldgen.config.OreConfigUtils;
import gregtech.api.worldgen.generator.IBlockGeneratorAccess;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;

import java.util.Random;

@ZenClass("mods.gregtech.ore.generator.PlateGenerator")
@ZenRegister
public class PlateGenerator extends ShapeGenerator {

    private int minLength;
    private int maxLength;
    private int minDepth;
    private int maxDepth;
    private int minHeight;
    private int maxHeight;
    private float floorSharpness;
    private float roofSharpness;

    public PlateGenerator() {
    }

    @ZenGetter("minLength")
    public int getMinLength() {
        return minLength;
    }

    @ZenGetter("maxLength")
    public int getMaxLength() {
        return maxLength;
    }

    @ZenGetter("minDepth")
    public int getMinDepth() {
        return minDepth;
    }

    @ZenGetter("maxDepth")
    public int getMaxDepth() {
        return maxDepth;
    }

    @ZenGetter("minHeight")
    public int getMinHeight() {
        return minHeight;
    }

    @ZenGetter("maxHeight")
    public int getMaxHeight() {
        return maxHeight;
    }

    @ZenGetter("floorSharpness")
    public float getFloorSharpness() {
        return floorSharpness;
    }

    @ZenGetter("rootSharpness")
    public float getRoofSharpness() {
        return roofSharpness;
    }

    @Override
    public void loadFromConfig(JsonObject object) {
        int[] length = OreConfigUtils.getIntRange(object.get("length"));
        int[] depth = OreConfigUtils.getIntRange(object.get("depth"));
        int[] height = OreConfigUtils.getIntRange(object.get("height"));
        this.minLength = length[0];
        this.maxLength = length[1];
        this.minDepth = depth[0];
        this.maxDepth = depth[1];
        this.minHeight = height[0];
        this.maxHeight = height[1];
        if(object.has("floor_sharpness")) {
            this.floorSharpness = object.get("floor_sharpness").getAsFloat();
        } else this.floorSharpness = 0.3f;
        if(object.has("roof_sharpness")) {
            this.roofSharpness = object.get("roof_sharpness").getAsFloat();
        } else this.roofSharpness = 0.7f;
    }

    @Override
    public void generate(Random gridRandom, IBlockGeneratorAccess relativeBlockAccess) {
        int length = (minLength >= maxLength ? minLength : gridRandom.nextInt(maxLength - minLength)) / 2;
        int depth = (minDepth >= maxDepth ? minDepth : gridRandom.nextInt(maxDepth - minDepth)) / 2;
        int height = (minHeight >= maxHeight ? minHeight : gridRandom.nextInt(maxHeight - minHeight)) / 2;
        for(int x = -length; x <= length; x++) {
            for(int z = -depth; z <= depth; z++) {
                boolean hasFloorSub = floorSharpness > gridRandom.nextFloat();
                boolean hasRoofSub = roofSharpness > gridRandom.nextFloat();
                for(int y = -height; y <= height; y++) {
                    if(hasRoofSub && (y == height || gridRandom.nextBoolean())) {
                        continue;
                    } else hasRoofSub = false;
                    if(hasFloorSub && y == -height)
                        continue;
                    relativeBlockAccess.generateBlock(x, y, z);
                }
            }
        }


    }
}
