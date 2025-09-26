/*
 */
package io.github.ayala;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationManager {
    private Animation<TextureRegion> currentAnimation;
    private float stateTime;
    private boolean facingRight = true; 
    private boolean loop = true; 

    public Animation<TextureRegion> loadAnimation(String texturePath, int frameCols, int frameRows, float frameDuration) {
        Texture sheet = new Texture(texturePath);
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / frameCols, sheet.getHeight() / frameRows);
        TextureRegion[] frames = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        return new Animation<>(frameDuration, frames);
    }

    public void setAnimation(Animation<TextureRegion> animation, boolean loop) {
        if (currentAnimation != animation) {
            currentAnimation = animation;
            stateTime = 0;
            this.loop = loop;
        }
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
    }

    public TextureRegion getCurrentFrame() {
        if (currentAnimation == null) return null;

        TextureRegion frame;
        if (!loop && currentAnimation.isAnimationFinished(stateTime)) {
            frame = currentAnimation.getKeyFrame(currentAnimation.getAnimationDuration() - 0.01f); 
        } else {
            frame = currentAnimation.getKeyFrame(stateTime, loop);
        }

        
        if (!facingRight && !frame.isFlipX()) {
            frame.flip(true, false);
        } else if (facingRight && frame.isFlipX()) {
            frame.flip(true, false);
        }

        return frame;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public void flipDirection() {
        facingRight = !facingRight;
    }
    
    public Animation<TextureRegion> getCurrentAnimation() {
        return currentAnimation;
    }
    
    public boolean isLastFrame(Animation<TextureRegion> animation) {
        if (animation == null) return false;
        int lastFrameIndex = animation.getKeyFrames().length - 1;
        int currentFrameIndex = animation.getKeyFrameIndex(stateTime);
        return currentFrameIndex == lastFrameIndex;
    }

    public boolean isAnimationFinished() {
        return !loop && currentAnimation.isAnimationFinished(stateTime);
    }
}
