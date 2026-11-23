package com.verdantartifice.primalmagick.client.fx.particles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.common.util.LineSegment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.ParticleGroupRenderState;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.List;

/**
 * Particle group render state responsible for submitting spell bolt particle geometry to the node collector.
 *
 * @author Daedalus4096
 */
public record SpellBoltParticleGroupRenderState(List<SpellBoltParticle.RenderState> stateList) implements ParticleGroupRenderState {
    private static final float WIDTH = 12F;

    @Override
    public void submit(@NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState cameraRenderState) {
        // Since no pose stack is passed in to us, create a new one. Submitted geometry is in camera-relative world
        // space; the level renderer applies the view rotation itself, as with vanilla's particle groups.
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();

        // Process each individual bolt in the group
        this.stateList.forEach(state -> {
            // Translate the pose based on the current camera position
            poseStack.pushPose();
            poseStack.translate(state.start().subtract(cameraRenderState.pos));

            // Submit the bolt geometry
            submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.LINES_TRANSLUCENT, (pose, lineBuilder) -> {
                // Draw each line segment
                for (int index = 0; index < state.segmentList().size(); index++) {
                    // Extract the current bolt line segment
                    LineSegment segment = state.segmentList().get(index);

                    // Move the endpoints of each segment along their computed motion path before rendering to make the bolt move
                    segment.perturb(state.perturbList().get(index), state.perturbList().get(index + 1));

                    // Add the segment vertices. The lines vertex format requires a normal (the line direction, used to
                    // extrude the line in screen space) and a line width on every vertex, as in vanilla's ShapeRenderer.
                    Vector3f normal = segment.getDelta().toVector3f().normalize();
                    lineBuilder.addVertex(pose, segment.getStart().toVector3f()).setColor(state.color()).setNormal(pose, normal).setLineWidth(WIDTH);
                    lineBuilder.addVertex(pose, segment.getEnd().toVector3f()).setColor(state.color()).setNormal(pose, normal).setLineWidth(WIDTH);
                }
            });

            poseStack.popPose();
        });

        poseStack.popPose();
    }
}
