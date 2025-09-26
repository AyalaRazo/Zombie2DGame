package io.github.ayala;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Player player;
    private Texture background;
    private List<Zombie> zombies;

    private RoundManager roundManager;
    Music melodia;

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("background.jpg"); 
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        
        melodia = Gdx.audio.newMusic(Gdx.files.internal("melody.mp3"));
        melodia.setLooping(true);
        melodia.play();
        
        player = new Player(new Vector2(0, 0), 200);
        player.setMain(this); 
        
        zombies = new ArrayList<>();

        roundManager = new RoundManager(this, player);
    }

    @Override
    public void render() {
        
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();
        
        handleInput(deltaTime); 
        player.update(deltaTime);
        roundManager.update(deltaTime);
        for (Zombie zombie : zombies) {
            zombie.update(deltaTime);
        }

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        player.render(batch);
        for (Zombie zombie : zombies) {
            zombie.render(batch);
        }

        batch.end();
        if (roundManager.isFinalRound() && zombies.isEmpty()) {
            JOptionPane.showMessageDialog(null, "¡Has ganado! - Score: " + player.score);
            Gdx.app.exit();
        }
    }

    private void handleInput(float deltaTime) {
        float deltaX = 0;
        float deltaY = 0;
        
        if(player.isAlive){
            if (Gdx.input.isKeyPressed(Input.Keys.W)) deltaY = 1;
            if (Gdx.input.isKeyPressed(Input.Keys.S)) deltaY = -1;
            if (Gdx.input.isKeyPressed(Input.Keys.A)) deltaX = -1;
            if (Gdx.input.isKeyPressed(Input.Keys.D)) deltaX = 1;
        }

        player.move(deltaX, deltaY, deltaTime);

        
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player.setAttacking(true);
        }

        for (int i = 0; i < zombies.size(); i++) {
            if(zombies.get(i).isAlive == false){
                zombies.remove(i);
            }
        }
    }
    
    public void addZombie(Zombie zombie) {
        zombies.add(zombie);
    }
    
    public List<Zombie> getZombies() {
        return zombies;
    }
    
    public Player getPlayer() {
        return player;
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}


