package goblinbob.mobends.core.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import goblinbob.mobends.lib.math.SmoothOrientation;
import goblinbob.mobends.lib.math.matrix.IMat4x4d;
import goblinbob.mobends.lib.math.vector.IVec3f;

public interface IModelPart
{

	void applyPreTransform(MatrixStack poseStack, float scale);

	void applyPreTransform(float scale, IMat4x4d dest);

	void applyLocalTransform(MatrixStack poseStack, float scale);

	void applyLocalTransform(float scale, IMat4x4d dest);

	default void applyCharacterTransform(MatrixStack poseStack, float scale)
	{
		if (this.getParent() != null)
		{
			this.getParent().applyCharacterTransform(poseStack, scale * getOffsetScale());
		}
		this.applyPreTransform(poseStack, scale);
		this.applyLocalTransform(poseStack, scale);
	}

	default void applyCharacterTransform(float scale, IMat4x4d dest)
	{
		if (this.getParent() != null)
		{
			this.getParent().applyCharacterTransform(scale * getOffsetScale(), dest);
		}
		this.applyPreTransform(scale, dest);
		this.applyLocalTransform(scale, dest);
	}

	default void propagateTransform(MatrixStack poseStack, float scale)
	{
		this.applyLocalTransform(poseStack, scale);
		this.applyPostTransform(poseStack, scale);
	}

	void applyPostTransform(MatrixStack poseStack, float scale);

	void renderPart(MatrixStack poseStack, float scale);
	void renderJustPart(MatrixStack poseStack, float scale);
	void update(float ticksPerFrame);
	void syncUp(IModelPart part);
	void setVisible(boolean showModel);
	IVec3f getPosition();
	IVec3f getScale();
	IVec3f getOffset();
	SmoothOrientation getRotation();
	float getOffsetScale();
	IVec3f getGlobalOffset();
	IModelPart getParent();
	boolean isShowing();

}
