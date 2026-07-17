package bunger.group.client.tyler3.deer;


import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class DeerEntityRenderState extends LivingEntityRenderState
{
    public boolean hasRedNose = false;
    public boolean sheared;
    public boolean saddled = false;
    public AnimationState eatGrassAnimationState = new AnimationState();
}