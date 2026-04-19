package bunger.group.client.ethan;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.Nullable;

public class VoremothEntityRenderState extends LivingEntityRenderState {
    public float xRot;
    public float ageInTicks;
    @Nullable
    public LivingEntity laserTarget = null;
    public int laserTimer = 0;
}