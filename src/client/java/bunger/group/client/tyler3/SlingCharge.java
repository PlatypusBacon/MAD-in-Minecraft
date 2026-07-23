package bunger.group.client.tyler3;


import bunger.group.tyler.sound.ModSounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

public class SlingCharge extends AbstractTickableSoundInstance {

    private final Player player;

    public SlingCharge(Player player) {
        super(ModSounds.SLING_CHARGE, SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.player = player;
        this.looping = true;
        this.delay = 0;
        this.volume = 1.0F;
        this.pitch = 1.0F;
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
    }

    @Override
    public void tick() {
        if (!player.isAlive() || player.getUseItem().isEmpty()) {
            this.stop();
            return;
        }
        if (!(player.getUseItem().getItem() instanceof bunger.group.tyler2.item.SlingItem)) {
            this.stop();
            return;
        }
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }
}
