/*
 */
package io.github.ayala;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author ayala
 */
public class WildZombie extends Zombie{
    
    public WildZombie(Vector2 position, float speed, AnimationManager animationManager, Player target) {
        super(position, speed, animationManager, target);
        
        sound_bite = Gdx.audio.newMusic(Gdx.files.internal("zombie_bite.mp3"));
                
        this.animationManager = animationManager;
        this.target = target;
        
        idleAnimation = animationManager.loadAnimation("WZombie_idle.png", 9, 1, 0.1f);
        walkAnimation = animationManager.loadAnimation("WZombie_run.png", 8, 1, 0.1f);
        attackAnimation = animationManager.loadAnimation("WZombie_attack.png", 4, 1, 0.2f);
        hurtAnimation = animationManager.loadAnimation("WZombie_hurt.png", 5, 1, 0.15f); 
        deathAnimation = animationManager.loadAnimation("WZombie_dead.png", 5, 1, 0.2f);
        
        animationManager.setAnimation(idleAnimation, true);
        health = 30;
        isAttacking = false;
        isDamaged = false;
        attackExecuted = false;
        isDying = false;
    }
    
    
   @Override
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
            target.takeDamage(10);
            sound_bite.play();
            
            cooldownTimer = attackCooldown;
        }
    }
}
