package bunger.group.client.ethan;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.Identifier;

// Made with Blockbench 5.1.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class ProphetEntityModel extends EntityModel<ProphetEntityRenderState> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "prophet"), "main");
	private final ModelPart head;
	private final ModelPart neck;
	private final ModelPart body;
	private final ModelPart arms;
	private final ModelPart legs;

	public ProphetEntityModel(ModelPart root) {
		super(root); //?????????????
		this.head = root.getChild("head");
		this.neck = root.getChild("neck");
		this.body = root.getChild("body");
		this.arms = root.getChild("arms");
		this.legs = root.getChild("legs");
	}

	public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", 
            CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.0F, -13.0F, -6.0F, 8.0F, 7.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-3.0F, -16.0F, -5.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(24, 16).addBox(-3.0F, -6.0F, -5.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)),
            PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition neck = partdefinition.addOrReplaceChild("neck",
            CubeListBuilder.create()
            .texOffs(32, 9).addBox(1.0F, -19.0F, -3.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		    .texOffs(12, 35).addBox(-2.0F, -19.0F, -3.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		    .texOffs(16, 35).addBox(0.0F, -19.0F, -1.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(0.0F, 16.0F, -2.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 26).addBox(-7.0F, -3.0F, -1.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(24, 26).addBox(-9.0F, -4.0F, -2.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(32, 0).addBox(-8.0F, -3.0F, -1.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(24, 26).addBox(0.0F, -4.0F, -2.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(32, 0).addBox(-1.0F, -3.0F, -1.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(32, 7).addBox(-8.0F, -4.0F, 6.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(32, 7).addBox(-8.0F, -3.0F, 5.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(32, 7).addBox(-8.0F, -4.0F, -3.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(32, 7).addBox(-8.0F, -3.0F, -2.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 6.0F, 2.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition arms = partdefinition.addOrReplaceChild("arms", CubeListBuilder.create().texOffs(6, 35).addBox(4.0F, -13.0F, -3.0F, 1.0F, 18.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(6, 35).addBox(-5.0F, -13.0F, -3.0F, 1.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.0F, -2.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition legs = partdefinition.addOrReplaceChild("legs", CubeListBuilder.create().texOffs(6, 35).addBox(1.0F, -10.0F, -3.0F, 1.0F, 18.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 35).addBox(-2.0F, -11.0F, -3.0F, 1.0F, 19.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.0F, -2.0F, 0.0F, 3.1416F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	//@Override
	// public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	// }
    @Override
    public void setupAnim(ProphetEntityRenderState renderState) {
        super.setupAnim(renderState);
        head.xRot = -(renderState.xRot * ((float) Math.PI / 180F));
    }

	//@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		neck.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		arms.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		legs.render(poseStack, vertexConsumer, packedLight, packedOverlay);
	}
}
