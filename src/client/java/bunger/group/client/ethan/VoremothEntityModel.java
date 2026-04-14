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
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;

public class VoremothEntityModel extends EntityModel<VoremothEntityRenderState> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "voremoth"), "main");
	private final ModelPart body;
	private final ModelPart body2;
	private final ModelPart body3;
	private final ModelPart body4;
	private final ModelPart body5;
	private final ModelPart body6;
	private final ModelPart body7;
	private final ModelPart body8;
	private final ModelPart body9;
	private final ModelPart body10;
	private final ModelPart body11;
	private final ModelPart body12;
	private final ModelPart body13;
	private final ModelPart body14;
	private final ModelPart body15;
	private final ModelPart body16;
	private final ModelPart body17;
	private final ModelPart body18;

	public VoremothEntityModel(ModelPart root) {
        super(root);
		this.body = root.getChild("body");
		this.body2 = root.getChild("body2");
		this.body3 = root.getChild("body3");
		this.body4 = root.getChild("body4");
		this.body5 = root.getChild("body5");
		this.body6 = root.getChild("body6");
		this.body7 = root.getChild("body7");
		this.body8 = root.getChild("body8");
		this.body9 = root.getChild("body9");
		this.body10 = root.getChild("body10");
		this.body11 = root.getChild("body11");
		this.body12 = root.getChild("body12");
		this.body13 = root.getChild("body13");
		this.body14 = root.getChild("body14");
		this.body15 = root.getChild("body15");
		this.body16 = root.getChild("body16");
		this.body17 = root.getChild("body17");
		this.body18 = root.getChild("body18");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(1244, 1466).addBox(-7.0F, -7.0F, -24.0F, 15.0F, 7.0F, 48.0F, new CubeDeformation(0.0F))
		.texOffs(304, 1651).addBox(8.0F, -7.0F, -16.0F, 8.0F, 7.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(1722, 497).addBox(-15.0F, -7.0F, -16.0F, 8.0F, 7.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(936, 1208).addBox(16.0F, -7.0F, -8.0F, 8.0F, 7.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(936, 1231).addBox(-23.0F, -7.0F, -8.0F, 8.0F, 7.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body2 = partdefinition.addOrReplaceChild("body2", CubeListBuilder.create().texOffs(1822, 1313).addBox(-15.0F, -7.0F, -32.0F, 31.0F, 7.0F, 64.0F, new CubeDeformation(0.0F))
		.texOffs(1076, 1587).addBox(0.0F, -7.0F, -24.0F, 24.0F, 7.0F, 48.0F, new CubeDeformation(0.0F))
		.texOffs(670, 1776).addBox(-23.0F, -7.0F, -24.0F, 24.0F, 7.0F, 48.0F, new CubeDeformation(0.0F))
		.texOffs(1244, 1521).addBox(8.0F, -7.0F, -16.0F, 24.0F, 7.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(1546, 1140).addBox(-31.0F, -7.0F, -16.0F, 24.0F, 7.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 17.0F, 0.0F));

		PartDefinition body3 = partdefinition.addOrReplaceChild("body3", CubeListBuilder.create().texOffs(0, 1738).addBox(-23.0F, -7.0F, -41.0F, 47.0F, 7.0F, 80.0F, new CubeDeformation(0.0F))
		.texOffs(1348, 685).addBox(-8.0F, -7.0F, -32.0F, 40.0F, 7.0F, 64.0F, new CubeDeformation(0.0F))
		.texOffs(844, 1690).addBox(-31.0F, -7.0F, -32.0F, 40.0F, 7.0F, 64.0F, new CubeDeformation(0.0F))
		.texOffs(1822, 1455).addBox(0.0F, -7.0F, -24.0F, 40.0F, 7.0F, 48.0F, new CubeDeformation(0.0F))
		.texOffs(1822, 1510).addBox(-39.0F, -7.0F, -24.0F, 40.0F, 7.0F, 48.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, 0.0F));

		PartDefinition body4 = partdefinition.addOrReplaceChild("body4", CubeListBuilder.create().texOffs(1610, 206).addBox(-31.0F, -7.0F, -49.0F, 63.0F, 7.0F, 95.0F, new CubeDeformation(0.0F))
		.texOffs(1076, 1676).addBox(-16.0F, -7.0F, -40.0F, 56.0F, 7.0F, 79.0F, new CubeDeformation(0.0F))
		.texOffs(1346, 1676).addBox(-39.0F, -7.0F, -40.0F, 56.0F, 7.0F, 79.0F, new CubeDeformation(0.0F))
		.texOffs(1110, 685).addBox(-8.0F, -7.0F, -32.0F, 56.0F, 7.0F, 63.0F, new CubeDeformation(0.0F))
		.texOffs(844, 1762).addBox(-47.0F, -7.0F, -32.0F, 56.0F, 7.0F, 63.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 0.0F));

		PartDefinition body5 = partdefinition.addOrReplaceChild("body5", CubeListBuilder.create().texOffs(1546, 902).addBox(-39.0F, -7.0F, -58.0F, 79.0F, 7.0F, 112.0F, new CubeDeformation(0.0F))
		.texOffs(404, 1587).addBox(-24.0F, -7.0F, -49.0F, 72.0F, 7.0F, 96.0F, new CubeDeformation(0.0F))
		.texOffs(740, 1587).addBox(-47.0F, -7.0F, -49.0F, 72.0F, 7.0F, 96.0F, new CubeDeformation(0.0F))
		.texOffs(1610, 410).addBox(-16.0F, -7.0F, -41.0F, 72.0F, 7.0F, 80.0F, new CubeDeformation(0.0F))
		.texOffs(1616, 1571).addBox(-55.0F, -7.0F, -41.0F, 72.0F, 7.0F, 80.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

		PartDefinition body6 = partdefinition.addOrReplaceChild("body6", CubeListBuilder.create().texOffs(0, 1272).addBox(-47.0F, -7.0F, -67.0F, 95.0F, 7.0F, 130.0F, new CubeDeformation(0.0F))
		.texOffs(436, 1466).addBox(-32.0F, -7.0F, -58.0F, 88.0F, 7.0F, 114.0F, new CubeDeformation(0.0F))
		.texOffs(840, 1466).addBox(-55.0F, -7.0F, -58.0F, 88.0F, 7.0F, 114.0F, new CubeDeformation(0.0F))
		.texOffs(1564, 685).addBox(-24.0F, -7.0F, -50.0F, 88.0F, 7.0F, 98.0F, new CubeDeformation(0.0F))
		.texOffs(1564, 790).addBox(-63.0F, -7.0F, -50.0F, 88.0F, 7.0F, 98.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.0F, 0.0F));

		PartDefinition body7 = partdefinition.addOrReplaceChild("body7", CubeListBuilder.create().texOffs(532, 918).addBox(-55.0F, -7.0F, -76.0F, 111.0F, 7.0F, 146.0F, new CubeDeformation(0.0F))
		.texOffs(1110, 548).addBox(-40.0F, -7.0F, -67.0F, 104.0F, 7.0F, 130.0F, new CubeDeformation(0.0F))
		.texOffs(0, 1135).addBox(-63.0F, -7.0F, -67.0F, 104.0F, 7.0F, 130.0F, new CubeDeformation(0.0F))
		.texOffs(450, 1345).addBox(-32.0F, -7.0F, -59.0F, 104.0F, 7.0F, 114.0F, new CubeDeformation(0.0F))
		.texOffs(1386, 1329).addBox(-71.0F, -7.0F, -59.0F, 104.0F, 7.0F, 114.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -18.0F, 0.0F));

		PartDefinition body8 = partdefinition.addOrReplaceChild("body8", CubeListBuilder.create().texOffs(0, 0).addBox(-63.0F, -3.0F, -85.0F, 127.0F, 7.0F, 162.0F, new CubeDeformation(0.0F))
		.texOffs(578, 0).addBox(-48.0F, -3.0F, -76.0F, 120.0F, 7.0F, 146.0F, new CubeDeformation(0.0F))
		.texOffs(578, 153).addBox(-71.0F, -3.0F, -76.0F, 120.0F, 7.0F, 146.0F, new CubeDeformation(0.0F))
		.texOffs(1046, 918).addBox(-40.0F, -3.0F, -68.0F, 120.0F, 7.0F, 130.0F, new CubeDeformation(0.0F))
		.texOffs(1046, 1055).addBox(-79.0F, -3.0F, -68.0F, 120.0F, 7.0F, 130.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -29.0F, 0.0F));

		PartDefinition body9 = partdefinition.addOrReplaceChild("body9", CubeListBuilder.create().texOffs(0, 169).addBox(-63.0F, -3.0F, -85.0F, 127.0F, 7.0F, 162.0F, new CubeDeformation(0.0F))
		.texOffs(578, 306).addBox(-48.0F, -3.0F, -76.0F, 120.0F, 7.0F, 146.0F, new CubeDeformation(0.0F))
		.texOffs(578, 459).addBox(-71.0F, -3.0F, -76.0F, 120.0F, 7.0F, 146.0F, new CubeDeformation(0.0F))
		.texOffs(1064, 765).addBox(-40.0F, -3.0F, -68.0F, 120.0F, 7.0F, 130.0F, new CubeDeformation(0.0F))
		.texOffs(514, 1071).addBox(-79.0F, -3.0F, -68.0F, 120.0F, 7.0F, 130.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -36.0F, 0.0F));

		PartDefinition body10 = partdefinition.addOrReplaceChild("body10", CubeListBuilder.create().texOffs(0, 338).addBox(-63.0F, -3.0F, -85.0F, 127.0F, 7.0F, 162.0F, new CubeDeformation(0.0F))
		.texOffs(578, 612).addBox(-48.0F, -3.0F, -76.0F, 120.0F, 7.0F, 146.0F, new CubeDeformation(0.0F))
		.texOffs(0, 676).addBox(-71.0F, -3.0F, -76.0F, 120.0F, 7.0F, 146.0F, new CubeDeformation(0.0F))
		.texOffs(1110, 0).addBox(-40.0F, -3.0F, -68.0F, 120.0F, 7.0F, 130.0F, new CubeDeformation(0.0F))
		.texOffs(1110, 137).addBox(-79.0F, -3.0F, -68.0F, 120.0F, 7.0F, 130.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -43.0F, 0.0F));

		PartDefinition body11 = partdefinition.addOrReplaceChild("body11", CubeListBuilder.create().texOffs(0, 507).addBox(-63.0F, -3.0F, -85.0F, 127.0F, 7.0F, 162.0F, new CubeDeformation(0.0F))
		.texOffs(532, 765).addBox(-48.0F, -3.0F, -76.0F, 120.0F, 7.0F, 146.0F, new CubeDeformation(0.0F))
		.texOffs(0, 829).addBox(-71.0F, -3.0F, -76.0F, 120.0F, 7.0F, 146.0F, new CubeDeformation(0.0F))
		.texOffs(1110, 274).addBox(-40.0F, -3.0F, -68.0F, 120.0F, 7.0F, 130.0F, new CubeDeformation(0.0F))
		.texOffs(1110, 411).addBox(-79.0F, -3.0F, -68.0F, 120.0F, 7.0F, 130.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -50.0F, 0.0F));

		PartDefinition body12 = partdefinition.addOrReplaceChild("body12", CubeListBuilder.create().texOffs(0, 982).addBox(-55.0F, -7.0F, -76.0F, 111.0F, 7.0F, 146.0F, new CubeDeformation(0.0F))
		.texOffs(1014, 1192).addBox(-40.0F, -7.0F, -67.0F, 104.0F, 7.0F, 130.0F, new CubeDeformation(0.0F))
		.texOffs(468, 1208).addBox(-63.0F, -7.0F, -67.0F, 104.0F, 7.0F, 130.0F, new CubeDeformation(0.0F))
		.texOffs(0, 1409).addBox(-32.0F, -7.0F, -59.0F, 104.0F, 7.0F, 114.0F, new CubeDeformation(0.0F))
		.texOffs(1386, 1450).addBox(-71.0F, -7.0F, -59.0F, 104.0F, 7.0F, 114.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -53.0F, 0.0F));

		PartDefinition body13 = partdefinition.addOrReplaceChild("body13", CubeListBuilder.create().texOffs(1134, 1832).addBox(-7.0F, -7.0F, -24.0F, 15.0F, 7.0F, 48.0F, new CubeDeformation(0.0F))
		.texOffs(1770, 1140).addBox(8.0F, -7.0F, -16.0F, 8.0F, 7.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(1802, 497).addBox(-15.0F, -7.0F, -16.0F, 8.0F, 7.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(936, 1254).addBox(16.0F, -7.0F, -8.0F, 8.0F, 7.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(936, 1277).addBox(-23.0F, -7.0F, -8.0F, 8.0F, 7.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -95.0F, 0.0F));

		PartDefinition body14 = partdefinition.addOrReplaceChild("body14", CubeListBuilder.create().texOffs(1822, 1384).addBox(-15.0F, -7.0F, -32.0F, 31.0F, 7.0F, 64.0F, new CubeDeformation(0.0F))
		.texOffs(846, 1832).addBox(0.0F, -7.0F, -24.0F, 24.0F, 7.0F, 48.0F, new CubeDeformation(0.0F))
		.texOffs(990, 1832).addBox(-23.0F, -7.0F, -24.0F, 24.0F, 7.0F, 48.0F, new CubeDeformation(0.0F))
		.texOffs(1610, 497).addBox(8.0F, -7.0F, -16.0F, 24.0F, 7.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(1658, 1140).addBox(-31.0F, -7.0F, -16.0F, 24.0F, 7.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -88.0F, 0.0F));

		PartDefinition body15 = partdefinition.addOrReplaceChild("body15", CubeListBuilder.create().texOffs(1616, 1745).addBox(-23.0F, -7.0F, -41.0F, 47.0F, 7.0F, 80.0F, new CubeDeformation(0.0F))
		.texOffs(254, 1776).addBox(-8.0F, -7.0F, -32.0F, 40.0F, 7.0F, 64.0F, new CubeDeformation(0.0F))
		.texOffs(462, 1776).addBox(-31.0F, -7.0F, -32.0F, 40.0F, 7.0F, 64.0F, new CubeDeformation(0.0F))
		.texOffs(0, 1825).addBox(0.0F, -7.0F, -24.0F, 40.0F, 7.0F, 48.0F, new CubeDeformation(0.0F))
		.texOffs(670, 1832).addBox(-39.0F, -7.0F, -24.0F, 40.0F, 7.0F, 48.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -81.0F, 0.0F));

		PartDefinition body16 = partdefinition.addOrReplaceChild("body16", CubeListBuilder.create().texOffs(1610, 308).addBox(-31.0F, -7.0F, -49.0F, 63.0F, 7.0F, 95.0F, new CubeDeformation(0.0F))
		.texOffs(304, 1690).addBox(-16.0F, -7.0F, -40.0F, 56.0F, 7.0F, 79.0F, new CubeDeformation(0.0F))
		.texOffs(574, 1690).addBox(-39.0F, -7.0F, -40.0F, 56.0F, 7.0F, 79.0F, new CubeDeformation(0.0F))
		.texOffs(1082, 1762).addBox(-8.0F, -7.0F, -32.0F, 56.0F, 7.0F, 63.0F, new CubeDeformation(0.0F))
		.texOffs(1320, 1762).addBox(-47.0F, -7.0F, -32.0F, 56.0F, 7.0F, 63.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -74.0F, 0.0F));

		PartDefinition body17 = partdefinition.addOrReplaceChild("body17", CubeListBuilder.create().texOffs(1546, 1021).addBox(-39.0F, -7.0F, -58.0F, 79.0F, 7.0F, 112.0F, new CubeDeformation(0.0F))
		.texOffs(1610, 0).addBox(-24.0F, -7.0F, -49.0F, 72.0F, 7.0F, 96.0F, new CubeDeformation(0.0F))
		.texOffs(1610, 103).addBox(-47.0F, -7.0F, -49.0F, 72.0F, 7.0F, 96.0F, new CubeDeformation(0.0F))
		.texOffs(0, 1651).addBox(-16.0F, -7.0F, -41.0F, 72.0F, 7.0F, 80.0F, new CubeDeformation(0.0F))
		.texOffs(1616, 1658).addBox(-55.0F, -7.0F, -41.0F, 72.0F, 7.0F, 80.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -67.0F, 0.0F));

		PartDefinition body18 = partdefinition.addOrReplaceChild("body18", CubeListBuilder.create().texOffs(936, 1329).addBox(-47.0F, -7.0F, -67.0F, 95.0F, 7.0F, 130.0F, new CubeDeformation(0.0F))
		.texOffs(1482, 1192).addBox(-32.0F, -7.0F, -58.0F, 88.0F, 7.0F, 114.0F, new CubeDeformation(0.0F))
		.texOffs(0, 1530).addBox(-55.0F, -7.0F, -58.0F, 88.0F, 7.0F, 114.0F, new CubeDeformation(0.0F))
		.texOffs(1244, 1571).addBox(-24.0F, -7.0F, -50.0F, 88.0F, 7.0F, 98.0F, new CubeDeformation(0.0F))
		.texOffs(1578, 548).addBox(-63.0F, -7.0F, -50.0F, 88.0F, 7.0F, 98.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -60.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 2048, 2048);
	}

	// @Override
	// public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	// }

    @Override
    public void setupAnim(VoremothEntityRenderState renderState) {
        super.setupAnim(renderState);
        this.root().yRot = renderState.ageInTicks * 0.003F;
    }

	//@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		body2.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		body3.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		body4.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		body5.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		body6.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		body7.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		body8.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		body9.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		body10.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		body11.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		body12.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		body13.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		body14.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		body15.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		body16.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		body17.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		body18.render(poseStack, vertexConsumer, packedLight, packedOverlay);
	}
}