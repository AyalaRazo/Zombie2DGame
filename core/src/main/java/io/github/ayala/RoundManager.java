package io.github.ayala;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.github.Client.Client;

    
public class RoundManager{
private int round = 1;
private int maxZombiesPerRound = 9; 
    private int zombiesSpawned = 0;     
    private int zombiesDefeated = 0;    
    private Main main;                  
    private Player player;
    private Client client;
    
    Music sound_screaming;
    
    public RoundManager(Main main, Player player) {
        this.main = main;
        this.player = player;
        
        sound_screaming = Gdx.audio.newMusic(Gdx.files.internal("zombie_screaming.mp3"));
        
        client = new Client(this);
        Thread clientT = new Thread(client);
        clientT.start();
    }

    public void update(float deltaTime) {
        
        if (zombiesSpawned < maxZombiesPerRound) {
            spawnZombies();
        }

        
        checkZombiesDefeated();
    }

    private void spawnZombies() {
        int spawnBatch = 2; 
        for (int i = 0; i < spawnBatch && zombiesSpawned < maxZombiesPerRound; i++) {
            Zombie zombie;
            if (round == 1) {
                
                zombie = new Zombie(generateRandomPosition(), 45, new AnimationManager(), player);
                main.addZombie(zombie);
            } else if (round == 2) {
                
                if (Math.random() < 0.5) {
                    zombie = new Zombie(generateRandomPosition(), 55, new AnimationManager(), player);
                    main.addZombie(zombie);
                } else {
                    WildZombie wildZombie = new WildZombie(generateRandomPosition(), 65, new AnimationManager(), player);
                    main.addZombie(wildZombie); 
                }
            } else {
                sound_screaming.play();
                WildZombie wildZombie = new WildZombie(generateRandomPosition(), 85, new AnimationManager(), player);
                client.enviar("Wild_Zombie_Faster");
                main.addZombie(wildZombie);
            }
            zombiesSpawned++;
        }
    }

    private void checkZombiesDefeated() {
        int totalZombies = main.getZombies().size();

        
        zombiesDefeated = maxZombiesPerRound - (totalZombies);

        
        if (zombiesDefeated >= maxZombiesPerRound) {
            round++;
            zombiesSpawned = 0;
            zombiesDefeated = 0;
            maxZombiesPerRound = Math.max(3, maxZombiesPerRound - 2); 
            System.out.println("Avanzando a la ronda: " + round);
        }
    }

    private Vector2 generateRandomPosition() {
        float x = MathUtils.random(0, Gdx.graphics.getWidth());
        float y = MathUtils.random(0, Gdx.graphics.getHeight());
        return new Vector2(x, y);
    }
    
    public void addFasterWildZombie() {

        Vector2 spawnPosition = generateRandomPosition();
        WildZombie fasterWildZombie = new WildZombie(spawnPosition, 105, new AnimationManager(), player);
        main.addZombie(fasterWildZombie);
    }


    public boolean isFinalRound() {
        return round > 5; 
    }
}
