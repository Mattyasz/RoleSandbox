package redesign;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import redesign.managers.GameFactory;
import redesign.systems.AnimationSystem;
import redesign.systems.PlayerInputSystem;
import redesign.systems.RenderSystem;

public class ScreenGame extends ScreenAdapter {
	
	private SpriteBatch batch;
	
	private Engine engine;
	private RenderSystem renderSystem;
	private AnimationSystem animationSystem;
	private PlayerInputSystem inputSystem;
	
	public ScreenGame(LostLib game) {
		
		GameFactory.setAssetManager(game.getAssets());
		GameFactory.setDatabaseManager(game.getDatabase());
		
		batch = new SpriteBatch();
		
		engine = new Engine();
		
		renderSystem = new RenderSystem(batch);
		inputSystem = new PlayerInputSystem();
		animationSystem = new AnimationSystem();
		
		engine.addEntity(GameFactory.setPlayer(GameFactory.createMonster("Soldier", 0, 0)));
		engine.addEntity(GameFactory.createMonster("Paesant", 6, 2));
		engine.addEntity(GameFactory.createMonster("Pirate", 4, 1));
		engine.addEntity(GameFactory.createMonster("Squire", 2, 3));
		
		engine.addSystem(renderSystem);
		engine.addSystem(animationSystem);
		engine.addSystem(inputSystem);
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(.0f, .0f, .0f, 1.0f);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		engine.update(delta);
	}


	@Override
	public void resize(int width, int height) {
		renderSystem.getViewport().update(width, height);
		renderSystem.getViewport().getCamera().update();
	}
	
}
