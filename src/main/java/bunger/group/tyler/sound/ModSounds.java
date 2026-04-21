package bunger.group.tyler.sound;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {
    private ModSounds() {
        // private empty constructor to avoid accidental instantiation
    }

    // ITEM_METAL_WHISTLE is the name of the custom sound event
    // and is called in the mod to use the custom sound
    public static final SoundEvent SQUIRREL_AMBIENT = registerSound("squirrel_ambient");
    public static final SoundEvent GUN_FIRE = registerSound("gun_fire");
    public static final SoundEvent GOD_IS_HERE = registerSound("god_is_here");
    public static final SoundEvent GOD_IS_COMING = registerSound("god_is_coming");
    public static final SoundEvent BEAR_ACTIVE = registerSound("bear_active");
    public static final SoundEvent RELOAD = registerSound("reload");
    public static final SoundEvent BEAR_IDLE = registerSound("bear_idle");
    public static final SoundEvent SLIP = registerSound("slip");
    // actual registration of all the custom SoundEvents
    private static SoundEvent registerSound(String id) {
        Identifier identifier = Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, id);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
    }

    // This static method starts class initialization, which then initializes
    // the static class variables (e.g. ITEM_METAL_WHISTLE).
    public static void initialize() {
        MutuallyAssuredDestruction.LOGGER.info("Registering " + MutuallyAssuredDestruction.MOD_ID + " Sounds");
        // Technically this method can stay empty, but some developers like to notify
        // the console, that certain parts of the mod have been successfully initialized
    }
}