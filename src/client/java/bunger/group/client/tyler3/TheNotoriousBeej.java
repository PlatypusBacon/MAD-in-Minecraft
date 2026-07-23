package bunger.group.client.tyler3;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import bunger.group.tyler3.sounds.ModSounds;

import java.util.Random;

import static java.lang.Math.random;

public class TheNotoriousBeej {

    private static final Random RANDOM = new Random();
    private static final Identifier JUMPSCARE_TEXTURE_1 =
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "textures/notorious_beej_1.png");
    private static final Identifier JUMPSCARE_TEXTURE_2 =
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "textures/notorious_beej_2.png");

    private static final Identifier HUD_ID =
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "jumpscare_overlay");

    private static final int JUMPSCARE_DURATION = 60;

    private static int frameCounter = 0;
    private static int jumpscareFrameTarget = newTarget();
    private static boolean jumpscareActive = false;
    private static int jumpscareDisplayCounter = 0;
    private static boolean soundPlayed = false;
    private static Identifier chosenTexture = JUMPSCARE_TEXTURE_1; // add this

    public static void register() {
        HudElementRegistry.attachElementAfter(
                VanillaHudElements.HOTBAR,
                HUD_ID,
                new HudElement() {
                    @Override
                    public void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
                        frameCounter++;

                        if (!jumpscareActive && frameCounter >= jumpscareFrameTarget) {
                            jumpscareActive = true;
                            jumpscareDisplayCounter = 0;
                            soundPlayed = false;
                            chosenTexture = RANDOM.nextBoolean() ? JUMPSCARE_TEXTURE_1 : JUMPSCARE_TEXTURE_2; // add this
                        }

                        if (!jumpscareActive) return;

                        int w = graphics.guiWidth();
                        int h = graphics.guiHeight();
                        graphics.blit(
                                RenderPipelines.GUI_TEXTURED,
                                chosenTexture,
                                0, 0,
                                0.0f, 0.0f,
                                w, h,
                                w, h
                        );


                        if (!soundPlayed) {
                            Minecraft minecraft = Minecraft.getInstance();
                            if (minecraft.player != null) {
                                minecraft.getSoundManager().play(
                                        net.minecraft.client.resources.sounds.SimpleSoundInstance.forUI(
                                                ModSounds.VINE_BOOM,
                                                1.0F,
                                                1.0F
                                        )
                                );
                            }
                            soundPlayed = true;
                        }

                        jumpscareDisplayCounter++;

                        if (jumpscareDisplayCounter >= JUMPSCARE_DURATION) {
                            jumpscareActive = false;
                            frameCounter = 0;
                            jumpscareFrameTarget = newTarget();
                        }
                    }
                }
        );
    }

    private static int newTarget() {
        return RANDOM.nextInt(300000) + 20000;
    }
}