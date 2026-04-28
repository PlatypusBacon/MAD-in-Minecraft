// Made with Blockbench 5.1.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
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




public class VoremothEntityModel extends EntityModel<VoremothEntityRenderState> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "voremoth"), "main");
	private final ModelPart eye;
	private final ModelPart ring1;
	private final ModelPart ring2;
	private final ModelPart ring3;

	public VoremothEntityModel(ModelPart root) {
	 	super(root);
		this.eye = root.getChild("eye");
		this.ring1 = root.getChild("ring1");
		this.ring2 = root.getChild("ring2");
		this.ring3 = root.getChild("ring3");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition eye = partdefinition.addOrReplaceChild("eye", CubeListBuilder.create().texOffs(1, 1).addBox(-1.0F, 2.0F, 2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(-1, 0).addBox(-2.0F, 4.0F, -1.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-3.0F, 3.0F, -1.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(1, 1).addBox(-1.0F, 4.0F, -2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(1, 1).addBox(-1.0F, 4.0F, 1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(1, 1).addBox(-1.0F, 3.0F, -3.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(1, 1).addBox(-1.0F, 3.0F, 2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 1).addBox(-2.0F, 3.0F, 1.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 1).addBox(-2.0F, 3.0F, -2.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 1).addBox(-2.0F, 2.0F, -3.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 19.0F, 0.0F));

		PartDefinition cube_r1 = eye.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 1).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 2.0F, 0.0F, 0.0F, 1.5708F, -1.5708F));

		PartDefinition cube_r2 = eye.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 1).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -1.0F, 0.0F, 0.0F, 1.5708F, -1.5708F));

		PartDefinition cube_r3 = eye.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(1, 1).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -2.0F, 0.0F, 0.0F, 1.5708F, -1.5708F));

		PartDefinition cube_r4 = eye.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(1, 1).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 3.0F, 0.0F, 0.0F, 1.5708F, -1.5708F));

		PartDefinition cube_r5 = eye.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(1, 1).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -1.0F, 0.0F, 0.0F, 1.5708F, -1.5708F));

		PartDefinition cube_r6 = eye.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(1, 1).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 2.0F, 0.0F, 0.0F, 1.5708F, -1.5708F));

		PartDefinition cube_r7 = eye.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -1.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.0F, 2.0F, 0.0F, 1.5708F, -1.5708F));

		PartDefinition cube_r8 = eye.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(-1, 0).addBox(-2.0F, -1.0F, -1.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 0.0F, 0.0F, 0.0F, 1.5708F, -1.5708F));

		PartDefinition cube_r9 = eye.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 1).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -3.0F, -1.5708F, 0.0F, 3.1416F));

		PartDefinition cube_r10 = eye.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 1).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, -3.0F, -1.5708F, 0.0F, 3.1416F));

		PartDefinition cube_r11 = eye.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(0, 1).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, -4.0F, -1.5708F, 0.0F, -1.5708F));

		PartDefinition cube_r12 = eye.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 1).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.0F, -4.0F, -1.5708F, 0.0F, -1.5708F));

		PartDefinition cube_r13 = eye.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 1).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, -4.0F, -1.5708F, 0.0F, 3.1416F));

		PartDefinition cube_r14 = eye.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(0, 1).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, -4.0F, -1.5708F, 0.0F, 3.1416F));

		PartDefinition cube_r15 = eye.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(1, 1).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -4.0F, -1.5708F, 0.0F, 3.1416F));

		PartDefinition cube_r16 = eye.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(1, 1).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, -4.0F, -1.5708F, 0.0F, 3.1416F));

		PartDefinition cube_r17 = eye.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(1, 1).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, -5.0F, -1.5708F, 0.0F, 3.1416F));

		PartDefinition cube_r18 = eye.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(1, 1).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, -5.0F, -1.5708F, 0.0F, 3.1416F));

		PartDefinition cube_r19 = eye.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -1.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, -4.0F, -1.5708F, 0.0F, 3.1416F));

		PartDefinition cube_r20 = eye.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(-1, 0).addBox(-2.0F, -1.0F, -1.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -5.0F, -1.5708F, 0.0F, 3.1416F));

		PartDefinition cube_r21 = eye.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(0, 1).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 0.0F, 2.0F, 3.1416F, 0.0F, -1.5708F));

		PartDefinition cube_r22 = eye.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(0, 1).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 0.0F, -3.0F, 3.1416F, 0.0F, -1.5708F));

		PartDefinition cube_r23 = eye.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(0, 1).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 3.0F, 0.0F, 0.0F, -1.5708F, 1.5708F));

		PartDefinition cube_r24 = eye.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(0, 1).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -2.0F, 0.0F, 0.0F, -1.5708F, 1.5708F));

		PartDefinition cube_r25 = eye.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(0, 1).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 2.0F, 0.0F, 0.0F, -1.5708F, 1.5708F));

		PartDefinition cube_r26 = eye.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(0, 1).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -1.0F, 0.0F, 0.0F, -1.5708F, 1.5708F));

		PartDefinition cube_r27 = eye.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(1, 1).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -2.0F, 0.0F, 0.0F, -1.5708F, 1.5708F));

		PartDefinition cube_r28 = eye.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(1, 1).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 3.0F, 0.0F, 0.0F, -1.5708F, 1.5708F));

		PartDefinition cube_r29 = eye.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(1, 1).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -1.0F, 0.0F, 0.0F, -1.5708F, 1.5708F));

		PartDefinition cube_r30 = eye.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(1, 1).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 2.0F, 0.0F, 0.0F, -1.5708F, 1.5708F));

		PartDefinition cube_r31 = eye.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -1.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 0.0F, -2.0F, 0.0F, -1.5708F, 1.5708F));

		PartDefinition cube_r32 = eye.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(-1, 0).addBox(-2.0F, -1.0F, -1.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 1.5708F));

		PartDefinition cube_r33 = eye.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(0, 1).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 3.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r34 = eye.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(0, 1).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 3.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r35 = eye.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(0, 1).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, 4.0F, 1.5708F, 0.0F, -1.5708F));

		PartDefinition cube_r36 = eye.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(0, 1).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, 4.0F, 1.5708F, 0.0F, -1.5708F));

		PartDefinition cube_r37 = eye.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(0, 1).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 4.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r38 = eye.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(0, 1).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 4.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r39 = eye.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(1, 1).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 4.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r40 = eye.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(1, 1).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 4.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r41 = eye.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(1, 1).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 5.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r42 = eye.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(1, 1).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 5.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r43 = eye.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -1.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.0F, 4.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r44 = eye.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(-1, 0).addBox(-2.0F, -1.0F, -1.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 5.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r45 = eye.addOrReplaceChild("cube_r45", CubeListBuilder.create().texOffs(0, 1).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 1).addBox(-2.0F, -1.0F, -5.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 2.0F, 0.0F, 0.0F, -3.1416F));

		PartDefinition cube_r46 = eye.addOrReplaceChild("cube_r46", CubeListBuilder.create().texOffs(0, 1).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -4.0F, 0.0F, 0.0F, -1.5708F, -3.1416F));

		PartDefinition cube_r47 = eye.addOrReplaceChild("cube_r47", CubeListBuilder.create().texOffs(0, 1).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -4.0F, 0.0F, 0.0F, -1.5708F, -3.1416F));

		PartDefinition cube_r48 = eye.addOrReplaceChild("cube_r48", CubeListBuilder.create().texOffs(0, 1).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 1).addBox(-2.0F, -1.0F, 3.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(1, 1).addBox(-1.0F, -1.0F, 4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(1, 1).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, -2.0F, 0.0F, 0.0F, -3.1416F));

		PartDefinition cube_r49 = eye.addOrReplaceChild("cube_r49", CubeListBuilder.create().texOffs(1, 1).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(1, 1).addBox(-1.0F, -1.0F, -3.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(-1, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.0F, 1.0F, 0.0F, 0.0F, -3.1416F));

		PartDefinition cube_r50 = eye.addOrReplaceChild("cube_r50", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -1.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -4.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		PartDefinition cube_r51 = eye.addOrReplaceChild("cube_r51", CubeListBuilder.create().texOffs(0, 1).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 4.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r52 = eye.addOrReplaceChild("cube_r52", CubeListBuilder.create().texOffs(0, 1).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 4.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition ring1 = partdefinition.addOrReplaceChild("ring1", CubeListBuilder.create().texOffs(-2, 1).addBox(-4.0F, -1.0F, 7.0F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(-2, 1).addBox(-4.0F, -1.0F, -8.0F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(5.0F, -1.0F, 5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(6.0F, -1.0F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(4.0F, -1.0F, 6.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(-5.0F, -1.0F, 6.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(-6.0F, -1.0F, 5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(-7.0F, -1.0F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(-5.0F, -1.0F, -7.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(-6.0F, -1.0F, -6.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(-7.0F, -1.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(4.0F, -1.0F, -7.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(6.0F, -1.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(5.0F, -1.0F, -6.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 19.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r53 = ring1.addOrReplaceChild("cube_r53", CubeListBuilder.create().texOffs(-2, 1).addBox(-3.0F, -1.0F, -1.0F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 0.0F, 1.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r54 = ring1.addOrReplaceChild("cube_r54", CubeListBuilder.create().texOffs(-2, 1).addBox(-4.0F, -6.0F, -1.0F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, 5.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition ring2 = partdefinition.addOrReplaceChild("ring2", CubeListBuilder.create().texOffs(-2, 1).addBox(-4.0F, -1.0F, 7.0F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(-2, 1).addBox(-4.0F, -1.0F, -8.0F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(5.0F, -1.0F, 5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(6.0F, -1.0F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(4.0F, -1.0F, 6.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(-5.0F, -1.0F, 6.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(-6.0F, -1.0F, 5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(-7.0F, -1.0F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(-5.0F, -1.0F, -7.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(-6.0F, -1.0F, -6.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(-7.0F, -1.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(4.0F, -1.0F, -7.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(6.0F, -1.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(5.0F, -1.0F, -6.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 19.0F, 0.0F));

		PartDefinition cube_r55 = ring2.addOrReplaceChild("cube_r55", CubeListBuilder.create().texOffs(-2, 1).addBox(-3.0F, -1.0F, -1.0F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 0.0F, 1.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r56 = ring2.addOrReplaceChild("cube_r56", CubeListBuilder.create().texOffs(-2, 1).addBox(-4.0F, -6.0F, -1.0F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, 5.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition ring3 = partdefinition.addOrReplaceChild("ring3", CubeListBuilder.create().texOffs(-2, 1).addBox(-4.0F, -1.0F, 7.0F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(-2, 1).addBox(-4.0F, -1.0F, -8.0F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(5.0F, -1.0F, 5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(6.0F, -1.0F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(4.0F, -1.0F, 6.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(-5.0F, -1.0F, 6.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(-6.0F, -1.0F, 5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(-7.0F, -1.0F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(-5.0F, -1.0F, -7.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(-6.0F, -1.0F, -6.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(-7.0F, -1.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(4.0F, -1.0F, -7.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(6.0F, -1.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(5.0F, -1.0F, -6.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 19.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition cube_r57 = ring3.addOrReplaceChild("cube_r57", CubeListBuilder.create().texOffs(-2, 1).addBox(-3.0F, -1.0F, -1.0F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 0.0F, 1.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r58 = ring3.addOrReplaceChild("cube_r58", CubeListBuilder.create().texOffs(-2, 1).addBox(-4.0F, -6.0F, -1.0F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, 5.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
    public void setupAnim(VoremothEntityRenderState renderState) {
        super.setupAnim(renderState);
		eye.xRot = (renderState.xRot * ((float) Math.PI / 180F) * 0.75F);
        ring1.yRot = renderState.ageInTicks * 0.01F;
		ring2.xRot = renderState.ageInTicks * 0.01F;
		ring3.zRot = renderState.ageInTicks * 0.01F;
    }


	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		eye.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		ring1.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		ring2.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		ring3.render(poseStack, vertexConsumer, packedLight, packedOverlay);
	}
}