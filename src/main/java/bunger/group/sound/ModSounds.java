package bunger.group.sound;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {

    public static final SoundEvent SQUIRREL_AMBIENT  = registerSound("squirrel_ambient");
    public static final SoundEvent GUN_FIRE          = registerSound("gun_fire");
    public static final SoundEvent GOD_IS_COMING     = registerSound("god_is_coming");
    public static final SoundEvent GOD_IS_HERE        = registerSound("god_is_here");

    private static SoundEvent registerSound(String name) {
        ResourceLocation id = new ResourceLocation(MutuallyAssuredDestruction.MOD_ID, name);
        return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(id));
    }

    public static void register() {}
}