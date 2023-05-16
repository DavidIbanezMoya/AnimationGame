package com.davidiba.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.WebSockets;

public class AnimationGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img,background,typhlosion;
	public Animation<TextureRegion> runningAnimation;
	TextureRegion up[] = new TextureRegion[4];
	TextureRegion left[] = new TextureRegion[4];
	TextureRegion right[] = new TextureRegion[4];
	TextureRegion down[] = new TextureRegion[4];
	TextureRegion upPoke[] = new TextureRegion[4];
	TextureRegion leftPoke[] = new TextureRegion[4];
	TextureRegion rightPoke[] = new TextureRegion[4];
	TextureRegion downPoke[] = new TextureRegion[4];
	private static OrthographicCamera camera;
	WebSocket socket;
	String address = "localhost";
	int port = 8888;
	Animation<TextureRegion> gold;
	Animation<TextureRegion> typhlosionRegion;
	Float stateTime = 0.0f;
	Float lastSend = 0.0f;
	public Rectangle player,pokemon;
	String direction = "",currentDirection = direction;

	Rectangle upPad, downPad, leftPad, rightPad;
	final int IDLE=0, UP=1, DOWN=2, LEFT=3, RIGHT=4;


//...

	//runningAnimation = new Animation<TextureRegion>(0.033f, atlas.findRegions("running"), Animation.PlayMode.LOOP);

	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 400);
		batch = new SpriteBatch();
		img = new Texture("goldSprite.jpg");
		typhlosion = new Texture("typhlosionSprite.jpg");
		//img = new Texture("sprite-animation4.jpg");
		player = new Rectangle();
		player.width = 64;
		player.height = 64;
		player.x = 800 / 2 - 64 / 2;
		player.y = 20;
		pokemon = new Rectangle();
		pokemon.width = 64;
		pokemon.height = 64;
		pokemon.x = (800 / 2-64)   - 64 / 2;
		pokemon.y = 20;
		direction = "right";
		currentDirection = "right";
		// per cada frame cal indicar x,y,amplada,alçada

		//Posar les direccions segons la textureRegion
		down[0] = new TextureRegion(img,0,0,64,64);
		down[1] = new TextureRegion(img,64,0,64,64);
		down[2] = new TextureRegion(img,128,0,64,64);
		down[3] = new TextureRegion(img,192,0,64,64);

		left[0] = new TextureRegion(img,0,64,64,64);
		left[1] = new TextureRegion(img,64,64,64,64);
		left[2] = new TextureRegion(img,128,64,64,64);
		left[3] = new TextureRegion(img,192,64,64,64);

		right[0] = new TextureRegion(img,0,128,64,64);
		right[1] = new TextureRegion(img,64,128,64,64);
		right[2] = new TextureRegion(img,128,128,64,64);
		right[3] = new TextureRegion(img,192,128,64,64);

		up[0] = new TextureRegion(img,0,192,64,64);
		up[1] = new TextureRegion(img,64,192,64,64);
		up[2] = new TextureRegion(img,128,192,64,64);
		up[3] = new TextureRegion(img,192,192,64,64);

		//Poke TextureRegion
		downPoke[0] = new TextureRegion(typhlosion,0,0,64,64);
		downPoke[1] = new TextureRegion(typhlosion,64,0,64,64);
		downPoke[2] = new TextureRegion(typhlosion,128,0,64,64);
		downPoke[3] = new TextureRegion(typhlosion,192,0,64,64);

		leftPoke[0] = new TextureRegion(typhlosion,0,64,64,64);
		leftPoke[1] = new TextureRegion(typhlosion,64,64,64,64);
		leftPoke[2] = new TextureRegion(typhlosion,128,64,64,64);
		leftPoke[3] = new TextureRegion(typhlosion,192,64,64,64);

		rightPoke[0] = new TextureRegion(typhlosion,0,128,64,64);
		rightPoke[1] = new TextureRegion(typhlosion,64,128,64,64);
		rightPoke[2] = new TextureRegion(typhlosion,128,128,64,64);
		rightPoke[3] = new TextureRegion(typhlosion,192,128,64,64);

		upPoke[0] = new TextureRegion(typhlosion,0,192,64,64);
		upPoke[1] = new TextureRegion(typhlosion,64,192,64,64);
		upPoke[2] = new TextureRegion(typhlosion,128,192,64,64);
		upPoke[3] = new TextureRegion(typhlosion,192,192,64,64);

		gold = new Animation<TextureRegion>(0.25f,right);
		typhlosionRegion = new Animation<TextureRegion>(0.25f,rightPoke);

		//Background
		background = new Texture(Gdx.files.internal("Pueblo_Primavera_HGSS_Redimensionat.png"));
		background.setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);

		//TouchPad
		// facilities per calcular el "touch"
		upPad = new Rectangle(0, 400*2/3, 800, 400/3);
		downPad = new Rectangle(0, 0, 800, 400/3);
		leftPad = new Rectangle(0, 0, 800/3, 400);
		rightPad = new Rectangle(800*2/3, 0, 800/3, 400);
		if( Gdx.app.getType()== Application.ApplicationType.Android )
			// en Android el host és accessible per 10.0.2.2
			address = "10.0.2.2";
		socket = WebSockets.newSocket(WebSockets.toWebSocketUrl(address, port));
		socket.setSendGracefully(false);
		socket.addListener((WebSocketListener) new MyWSListener());
		socket.connect();
		socket.send("Enviar dades");
	}

	@Override
	public void render () {
		//ScreenUtils.clear(1, 0, 0, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {

			direction = "left";
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))  {

			direction = "right";

		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {

			direction = "up";
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {

			direction = "down";
		}
		currentDirection = direction;

		//GetDirection
		direction = virtual_joystick_control();
		walkDirection(direction);

		//Limit screen movement
		if(player.x < 0) player.x = 0;
		if(player.x > 800 - 64) player.x = 800 - 64;
		if(player.y < 0) player.y = 0;
		if(player.y > 400 - 64) player.y = 400 - 64;
		if(pokemon.x < 0) pokemon.x = 0;
		if(pokemon.x > 800 - 64) pokemon.x = 800 - 64;
		if(pokemon.y < 0) pokemon.y = 0;
		if(pokemon.y > 400 - 64) pokemon.y = 400 - 64;


		stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
		TextureRegion frame = gold.getKeyFrame(stateTime,true);
		TextureRegion framePoke = typhlosionRegion.getKeyFrame(stateTime,true);


		batch.begin();

		//Calcular la direccio

// si volem invertir el sentit, ho podem fer amb el paràmetre scaleX=-1
		batch.draw(background,0,0);
		batch.draw(frame, player.x, player.y, 0, 0,
				frame.getRegionWidth(),frame.getRegionHeight(),1,1,0);
		batch.draw(framePoke,pokemon.x,pokemon.y,0,0, framePoke.getRegionWidth(),framePoke.getRegionHeight(),1,1,0);
		batch.end();

		if( stateTime-lastSend > 1.0f ) {
			lastSend = stateTime;
			socket.send(direction);
		}
	}

	@Override
	public void dispose () {
		background.dispose();
		batch.dispose();
		img.dispose();
		typhlosion.dispose();
	}

	public void walkDirection(String direction) {


		if (direction.equals("right")) {
			gold = new Animation<TextureRegion>(0.25f,right);
			typhlosionRegion = new Animation<TextureRegion>(0.25f,rightPoke);
			if (player.x != 800-64) {

				pokemon.x = player.x-48;
				pokemon.y = player.y;

			} else {
				pokemon.x += 0 * Gdx.graphics.getDeltaTime();
			}
			player.x += 50 * Gdx.graphics.getDeltaTime();


		}
		else if (direction.equals("left")) {
			gold = new Animation<TextureRegion>(0.25f,left);
			typhlosionRegion = new Animation<TextureRegion>(0.25f,leftPoke);

			if (player.x != 800-64) {
				pokemon.x = player.x+48;
				pokemon.y = player.y;

			} else {
				pokemon.x -= 0 * Gdx.graphics.getDeltaTime();
			}
			player.x -= 50 * Gdx.graphics.getDeltaTime();

		}
		else if (direction.equals("up")) {
			gold = new Animation<TextureRegion>(0.25f,up);
			typhlosionRegion = new Animation<TextureRegion>(0.25f,upPoke);
			if (player.y != 400-64) {
				pokemon.y = player.y-48;
				pokemon.x = player.x;

			} else {
				pokemon.y += 0 * Gdx.graphics.getDeltaTime();
			}
			player.y += 50 * Gdx.graphics.getDeltaTime();

		}
		else if (direction.equals("down")) {
			gold = new Animation<TextureRegion>(0.25f,down);
			typhlosionRegion = new Animation<TextureRegion>(0.25f,downPoke);
			if (player.y != 400-64) {
				//Quan es prem el botó anirá directament enrere
				pokemon.y = player.y+48;
				pokemon.x = player.x;

			} else {
				pokemon.y -= 0 * Gdx.graphics.getDeltaTime();
			}
			player.y -= 50 * Gdx.graphics.getDeltaTime();

		}
	}
	protected String virtual_joystick_control() {
		// iterar per multitouch
		// cada "i" és un possible "touch" d'un dit a la pantalla
		for(int i=0;i<10;i++)
			if (Gdx.input.isTouched(i)) {
				Vector3 touchPos = new Vector3();
				touchPos.set(Gdx.input.getX(i), Gdx.input.getY(i), 0);
				// traducció de coordenades reals (depen del dispositiu) a 800x480
				AnimationGame.camera.unproject(touchPos);
				if (upPad.contains(touchPos.x, touchPos.y)) {
					return "up";
				} else if (downPad.contains(touchPos.x, touchPos.y)) {
					return "down";
				} else if (leftPad.contains(touchPos.x, touchPos.y)) {
					return "left";
				} else if (rightPad.contains(touchPos.x, touchPos.y)) {
					return "right";
				}
			}
		//Revisar per el Key input
		return currentDirection;
	}

	class MyWSListener implements WebSocketListener {

		@Override
		public boolean onOpen(WebSocket webSocket) {
			System.out.println("Opening...");
			return false;
		}

		@Override
		public boolean onClose(WebSocket webSocket, int closeCode, String reason) {
			System.out.println("Closing...");
			return false;
		}

		@Override
		public boolean onMessage(WebSocket webSocket, String packet) {
			System.out.println("Message:");
			return false;
		}

		@Override
		public boolean onMessage(WebSocket webSocket, byte[] packet) {
			System.out.println("Message:");
			return false;
		}

		@Override
		public boolean onError(WebSocket webSocket, Throwable error) {
			System.out.println("ERROR:"+error.toString());
			return false;
		}
	}
}
