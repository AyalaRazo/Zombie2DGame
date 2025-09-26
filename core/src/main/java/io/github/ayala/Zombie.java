/*
 */
package io.github.ayala;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.audio.Music;

public class Zombie extends Entity {
    protected AnimationManager animationManager;
    protected Player target;
    
    protected boolean isAttacking;
    protected float stateTimer;
    protected boolean isDamaged;
    protected boolean attackExecuted;
    protected boolean isDying;
    
    protected float attackRange = 50f;
    protected float attackCooldown = 1.5f; 
    protected float cooldownTimer = 0;
    protected int health;
    
   protected Music sound_bite;
   
    
    protected float damageTimer = 0f;

    protected Animation<TextureRegion> idleAnimation;
    protected Animation<TextureRegion> walkAnimation;
    protected Animation<TextureRegion> attackAnimation;
    protected Animation<TextureRegion> hurtAnimation;
    protected Animation<TextureRegion> deathAnimation;
   
    


    public Zombie(Vector2 position, float speed, AnimationManager animationManager, Player target) {
        super(position, speed);
        
        sound_bite = Gdx.audio.newMusic(Gdx.files.internal("zombie_bite.mp3"));
                
        this.animationManager = animationManager;
        this.target = target;

       
        idleAnimation = animationManager.loadAnimation("Idle.png", 8, 1, 0.2f);
        walkAnimation = animationManager.loadAnimation("Walk.png", 8, 1, 0.1f);
        attackAnimation = animationManager.loadAnimation("Attack_1.png", 5, 1, 0.2f);
        hurtAnimation = animationManager.loadAnimation("Hurt.png", 3, 1, 0.2f); 
        deathAnimation = animationManager.loadAnimation("Dead.png", 5, 1, 0.2f);
        
        animationManager.setAnimation(idleAnimation, true);
        health = 20;
        isAttacking = false;
        isDamaged = false;
        attackExecuted = false;
        isDying = false;
    }


    @Override
    public void update(float deltaTime) {
        if (target == null) return;
        if (isDying) {
            animationManager.update(deltaTime);
            if (animationManager.isAnimationFinished()) {
                isAlive = false;
            }
        } else if (isDamaged) {
            damageTimer -= deltaTime;
            animationManager.setAnimation(hurtAnimation, true);
            if (damageTimer <= 0) {
                isDamaged = false; 
                animationManager.setAnimation(walkAnimation, true);
            }
        } else {
            if (isAttacking) {
               if (!attackExecuted && animationManager.isLastFrame(attackAnimation)) {
                   attackPlayer(deltaTime);
                   attackExecuted = true;
               }

               stateTimer -= deltaTime;
               if (stateTimer <= 0) {
                   isAttacking = false;
                   attackExecuted = false;
                   animationManager.setAnimation(idleAnimation, true);
               }
           }

        
        float distance = position.dst(target.getPosition());

        if (distance > attackRange) {
            moveToTarget(deltaTime);
        } else {
            
            animationManager.setAnimation(attackAnimation, true);
            attackPlayer(deltaTime);
        }
        
        animationManager.update(deltaTime);
        }
    }

    public void attackPlayer(float deltaTime) {
        if (cooldownTimer > 0) {
            cooldownTimer -= deltaTime;
            return;
        }

        if (target == null) return;

        
        Vector2 directionToPlayer = target.getPosition().cpy().sub(position).nor();

        
        float attackX = position.x + directionToPlayer.x * 50;
        float attackY = position.y + directionToPlayer.y * 50;

        Rectangle attackHitbox = new Rectangle(attackX, attackY, 50, 20);

        
        if (target.getBoundingBox().overlaps(attackHitbox)) {
            target.takeDamage(5);
            sound_bite.play();
            
            cooldownTimer = attackCooldown;
        }
    }

    public void moveToTarget(float deltaTime) {
        if (isDamaged || isAttacking) {
            animationManager.setAnimation(idleAnimation, true);
            return;
        }

        Vector2 direction = target.getPosition().cpy().sub(position).nor();
        float distance = position.dst(target.getPosition());

        if (distance > attackRange) {
            position.x += direction.x * speed * deltaTime;
            position.y += direction.y * speed * deltaTime;
            animationManager.setAnimation(walkAnimation, true);
        }

        if (animationManager.getCurrentAnimation() != walkAnimation) {
            animationManager.setAnimation(walkAnimation, true);
        }

       
        if (direction.x > 0 && !animationManager.isFacingRight()) {
            animationManager.flipDirection();
        } else if (direction.x < 0 && animationManager.isFacingRight()) {
            animationManager.flipDirection();
        }
    }

    public void takeDamage(int damage) {
        if (!isAlive) return;

        health -= damage;
        if (health <= 0 && !isDying) {
            die();
        } else {
            isDamaged = true;
            damageTimer = 0.25f; 
        }
    }

    public void die() {
        if (!isDying) {
            isDying = true;
            animationManager.setAnimation(deathAnimation, false); 
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion frame = animationManager.getCurrentFrame();

        if (isDamaged) {
            batch.setColor(1, 0, 0, 1);
        } else {
            batch.setColor(1, 1, 1, 1);
        }

        batch.draw(frame, position.x, position.y);
        batch.setColor(1, 1, 1, 1);
    }

    public Rectangle getBoundingBox() {
        TextureRegion currentFrame = animationManager.getCurrentFrame();
        float width = currentFrame != null ? currentFrame.getRegionWidth() : 50;
        float height = currentFrame != null ? currentFrame.getRegionHeight() : 50;
        return new Rectangle(position.x, position.y, width, height);
    }
}

