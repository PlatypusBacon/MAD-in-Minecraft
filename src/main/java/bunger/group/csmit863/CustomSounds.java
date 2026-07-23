package bunger.group.csmit863;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class CustomSounds {
    private CustomSounds() {
        // private empty constructor to avoid accidental instantiation
    }

    // MUSIC_TO_MY_EARS is the name of the custom sound event
    // and is called in the mod to use the custom sound
    public static final SoundEvent MUSIC_TO_MY_EARS = registerSound("musictomyears");
    public static final SoundEvent SHROOMJAK_ANGRY1 = registerSound("shroomjak_angry1");
    public static final SoundEvent SHROOMJAK_ANGRY2 = registerSound("shroomjak_angry2");
    public static final SoundEvent SHROOMJAK_BREED1 = registerSound("shroomjak_breed1");
    public static final SoundEvent SHROOMJAK_FART1 = registerSound("shroomjak_fart1");
    public static final SoundEvent SHROOMJAK_NORMAL1 = registerSound("shroomjak_normal1");
    public static final SoundEvent SHROOMJAK_PLANT = registerSound("shroomjak_plant");
    public static final SoundEvent SHROOM_NOISE = registerSound("shroom_noise");
    public static final SoundEvent SHROOM_TERROR = registerSound("shroom_terror");
    public static final SoundEvent THE_ALIEN = registerSound("the_alien");
    public static final SoundEvent OVERSEER_HELLO = registerSound("overseer_hello");
    public static final SoundEvent OVERSEER_HELLO2 = registerSound("overseer_hello2");

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