import javax.swing.*;
import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.ArrayList;
import javax.swing.*;

public class GameFrame extends JPanel implements ActionListener{

	Timer mainTimer;
	Player player;
	Random rand = new Random();

	static ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
	static ArrayList<Projectile_Blaster> projectiles = new ArrayList<Projectile_Blaster>();

	public int asteroidCount = 2;
	int syncCounter = 0;
	public static int level = 1;
	
	public GameFrame(Player player){
	
		this.player = player;

		setFocusable(true);
		addKeyListener(new KeyAdapt(player));

		mainTimer = new Timer(5, this);
		mainTimer.start();
	}

	public void paint(Graphics g){// draw background, player, asteroids

		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;

		ImageIcon ic = new ImageIcon("bg.png");
		
		g2d.drawImage(ic.getImage(), 0, 0, null);
		g2d.setColor(Color.WHITE);
		g2d.drawString(player.username,(player.x)+15, (player.y)+68);
		player.draw(g2d);

		for(int i=0; i<asteroids.size(); i++){

				Asteroid tempAsteroid = asteroids.get(i);
				tempAsteroid.draw(g2d);

		}

		for(int i=0; i<projectiles.size(); i++){

				Projectile_Blaster tempProjectile = projectiles.get(i);
				tempProjectile.draw(g2d);
 		}

	}

	public void actionPerformed(ActionEvent e){

		player.update();

		if(syncCounter>50&& getPlayerShoot()==true){
			syncCounter = 0;
			addProjectile(new Projectile_Blaster(getPlayerPositionX(), getPlayerPositionY()));
		}

		for(int i=0; i<asteroids.size(); i++){

			Asteroid tempAsteroid = asteroids.get(i);

			if(tempAsteroid.checkCollisions()==true){
				asteroids.remove(i);
				i--;
				continue;
			}

			tempAsteroid.update();

		}

		for(int i=0; i<projectiles.size(); i++){

			Projectile_Blaster tempProjectile = projectiles.get(i);
			if(tempProjectile.checkOffScreen()==true){
				projectiles.remove(i);
				i--;
				continue;
			}

			tempProjectile.update();

		}

		syncCounter++;
		/*
			place end game condition here
		*/

		repaint();

	}

	public static void addAsteroid(Asteroid a){// adds asteroids

		asteroids.add(a);

	}

	public static void addProjectile(Projectile_Blaster a){// adds asteroids

		projectiles.add(a);

	}

	public int getPlayerPositionX(){// adds asteroids

		return player.x;
	}

	public int getPlayerPositionY(){

		return player.y;
	}

	public String getPlayerUsername(){
		return player.username;
	}

	public boolean getPlayerShoot(){

		return player.shoot_yes;
	}

	public static ArrayList<Asteroid> getAsteroidList(){// gets list of asteroids

		return asteroids;

	}

	public static ArrayList<Projectile_Blaster> getProjectileList(){// gets list of asteroids

		return projectiles;

	}

}
