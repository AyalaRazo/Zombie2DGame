package io.github.ayala;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
    protected Vector2 position;
    protected float speed;
    protected boolean isAlive;

    public Entity(Vector2 position, float speed) {
        this.position = position;
        this.speed = speed;
        this.isAlive = true;
    }

    public abstract void update(float deltaTime);

    public abstract void render(SpriteBatch batch);

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }
}
