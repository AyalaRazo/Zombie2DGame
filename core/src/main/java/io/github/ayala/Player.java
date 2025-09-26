package io.github.ayala;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import javax.swing.JOptionPane;

public class Player extends Entity {
    private AnimationManager animationManager;
    private Main main;
    private boolean isAttacking;
    private float stateTimer;
    private int health;
    private boolean isDamaged;
    private boolean attackExecuted;
    private boolean gameOverShown;

    private Texture lifeBarSheet; 
    private TextureRegion[] lifeBarFrames; 
    private int currentFrameIndex; 
    private int totalFrames; 
    private int playerMaxHealth;
    
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> attackAnimation;
    private Animation<TextureRegion> hurtAnimation;
    private Animation<TextureRegion> deathAnimation;

    private Vector2 lastDirection = new Vector2(1, 0); 
    private Rectangle lastAttackHitbox;
    
    private float damageTimer = 0f; 
    
    int score;
    Music sound_attack;
    

    public Player(Vector2 position, float speed) {
        super(position, speed);
        
        sound_attack = Gdx.audio.newMusic(Gdx.files.internal("axe_swing.mp3"));
        
        animationManager = new AnimationManager();
        lifeBarSheet = new Texture("health5.png");
        playerMaxHealth = 100;
        totalFrames = 1 * 6;
        
        TextureRegion[][] tmp = TextureRegion.split(lifeBarSheet,
                lifeBarSheet.getWidth() / 1,
                lifeBarSheet.getHeight() / 6);

        
        lifeBarFrames = new TextureRegion[totalFrames];
        int index = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 1; j++) {
                lifeBarFrames[index++] = tmp[i][j];
            }
        }
        
        currentFrameIndex = totalFrames - 1;
        

        
       
        idleAnimation = animationManager.loadAnimation("Player_idle.png", 6, 1, 0.1f);
        walkAnimation = animationManager.loadAnimation("Player_run.png", 6, 1, 0.1f);
        attackAnimation = animationManager.loadAnimation("Player_attack.png", 4, 1, 0.2f);
        hurtAnimation = animationManager.loadAnimation("Player_hurt.png", 2, 1, 0.1f);
        deathAnimation = animationManager.loadAnimation("Player_dead.png", 4, 1, 0.1f);

        animationManager.setAnimation(idleAnimation, true);
        health = 100;
        score = 0;
        isDamaged = false;
        attackExecuted = false;
        gameOverShown = false;
    }

    public void setMain(Main main) {
        this.main = main;
    }
    
    public void setAttacking(boolean attacking) {
        if (isDamaged) {
            isAttacking = false;
            attackExecuted = false;
            animationManager.setAnimation(idleAnimation, true); 
            return;
        }

        if (!isAttacking) { 
            isAttacking = attacking;
            stateTimer = attackAnimation.getAnimationDuration();
            animationManager.setAnimation(attackAnimation, false); 
        }
    }

    @Override
    public void update(float deltaTime) {
        float healthPercentage = (float) health / playerMaxHealth;
        currentFrameIndex = Math.max(0, (int) (healthPercentage * (totalFrames - 1)));
        
        if (!isAlive) {
            
            if (!animationManager.isAnimationFinished()) {
                animationManager.update(deltaTime);
            } else if (!gameOverShown) {
                
                JOptionPane.showMessageDialog(null, "Game Over - Score: " + score);
                gameOverShown = true; 
                Gdx.app.exit();
            }
            return;
        }

        
        if (isDamaged) {
            damageTimer -= deltaTime;
            animationManager.setAnimation(hurtAnimation, true);
            if (damageTimer <= 0) {
                isDamaged = false;
            }
        }

        if (isAttacking) {
            if (!attackExecuted && animationManager.isLastFrame(attackAnimation)) {
                performAttack();
                sound_attack.play();
                attackExecuted = true;
            }

            stateTimer -= deltaTime;
            if (stateTimer <= 0 || isDamaged) { 
                isAttacking = false;
                attackExecuted = false;
                if (!isDamaged) { 
                    animationManager.setAnimation(idleAnimation, true);
                }
            }
        }

        animationManager.update(deltaTime);
    }

    private void performAttack() {
        if (main == null || main.getZombies().isEmpty() || isDamaged) return;

        
        float attackX = position.x + lastDirection.x * 50; 
        float attackY = position.y + lastDirection.y * 50;

        lastAttackHitbox = new Rectangle(attackX, attackY, 50, 20); 

        
        for (Zombie zombie : main.getZombies()) {
           if (zombie.getBoundingBox().overlaps(lastAttackHitbox)) {
               zombie.takeDamage(10); 
               if (zombie instanceof WildZombie) {
                   score += 25; 
               } else {
                   score += 10; 
               }
           }
       }
    }


    public void move(float deltaX, float deltaY, float deltaTime) {
        if (!isAlive || isDamaged || isAttacking) return;

        if (deltaX != 0 || deltaY != 0) {
            lastDirection.set(deltaX, deltaY).nor();

            
            if (deltaX > 0 && !animationManager.isFacingRight()) {
                animationManager.flipDirection();
            } else if (deltaX < 0 && animationManager.isFacingRight()) {
                animationManager.flipDirection();
            }

            
            if (animationManager.getCurrentAnimation() != walkAnimation) {
                animationManager.setAnimation(walkAnimation, true);
            }
        } else {
            
            if (animationManager.getCurrentAnimation() != idleAnimation) {
                animationManager.setAnimation(idleAnimation, true);
            }
        }
 
        
        position.x += deltaX * speed * deltaTime;
        position.y += deltaY * speed * deltaTime;

        
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        if (position.x < 0) position.x = 0;
        if (position.x + animationManager.getCurrentFrame().getRegionWidth() > screenWidth)
            position.x = screenWidth - animationManager.getCurrentFrame().getRegionWidth();
        if (position.y < 0) position.y = 0;
        if (position.y + animationManager.getCurrentFrame().getRegionHeight() > screenHeight)
            position.y = screenHeight - animationManager.getCurrentFrame().getRegionHeight();
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion frame = animationManager.getCurrentFrame();

        if (isDamaged) {
            batch.setColor(1, 0, 0, 1); 
        } else {
            batch.setColor(1, 1, 1, 1); 
        }
        
        if (damageTimer > 0 && isAlive) {
            
            batch.setColor(1, 0, 0, 1);
        }

        batch.draw(frame, position.x, position.y);

        
        batch.setColor(1, 1, 1, 1);
        
         
        
        
        batch.draw(lifeBarFrames[currentFrameIndex], position.x + 25, position.y + 45);
        
    }

    public void takeDamage(int damage) {
        if (!isAlive) return;
        
        health -= damage;
        if (health <= 0) {
            die();
        } else {
            isDamaged = true;
            damageTimer = 0.25f; 
            
            if (isAttacking) {
                isAttacking = false;
                attackExecuted = false;
                animationManager.setAnimation(idleAnimation, true); 
            }
        }
    }



    private void die() {
        isAlive = false;
        animationManager.setAnimation(deathAnimation, false); 
        if(animationManager.isAnimationFinished()){
            Gdx.app.exit();
        }
    }

    public Rectangle getBoundingBox() {
        
        TextureRegion currentFrame = animationManager.getCurrentFrame();
        float width = currentFrame != null ? currentFrame.getRegionWidth() : 50;
        float height = currentFrame != null ? currentFrame.getRegionHeight() : 50;
        return new Rectangle(position.x, position.y, width, height);
    }
    
    private int getHealthFrameIndex() {
        
        return (int) ((health / 100f) * 5);
    }
}