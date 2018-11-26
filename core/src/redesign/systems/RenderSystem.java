package redesign.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import redesign.components.AnimationComponent;
import redesign.components.Mapper;
import redesign.components.PositionComponent;
import redesign.components.TextureComponent;

public class RenderSystem extends EntitySystem {
	
	private static final float ASPECT_RATIO = Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
	private static final float WORLD_WIDTH = 32;
	private static final float WORLD_HEIGHT = 18;
	private static final float WORLD_UNIT = 16;
	private static final float WORLD_TO_SCREEN = 1.0f / WORLD_UNIT;
	
	private ImmutableArray<Entity> entities;
	private Entity map;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private FitViewport viewport;

	public RenderSystem(int priority, SpriteBatch batch) {
		super(priority);
		this.batch = batch;
		camera = new OrthographicCamera();
		viewport = new FitViewport(WORLD_WIDTH * ASPECT_RATIO, WORLD_HEIGHT, camera);
	}

	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family
				.all(PositionComponent.class)
				.one(TextureComponent.class, AnimationComponent.class)
				.get());
		//map = engine.getEntitiesFor(Family.all(MapComponent.class).get()).first();
	}

	@Override
	public void update(float deltaTime) {
		// Draw map
		
		// Draw Game Objects
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (Entity entity : entities) {
			PositionComponent position = Mapper.position.get(entity);
			AnimationComponent animation = Mapper.animation.get(entity);
			
			animation.stateTime += deltaTime; 
			
			batch.draw(animation.animation.getKeyFrame(animation.stateTime, true),
					(position.x * WORLD_TO_SCREEN) * WORLD_UNIT, (position.y * WORLD_TO_SCREEN) * WORLD_UNIT,
					0, 0,
					16 * WORLD_TO_SCREEN, 16 * WORLD_TO_SCREEN,
					1.0f, 1.0f,
					0.0f);
		}
		batch.end();
	}
	
	public Viewport getViewport() {
		return viewport;
	}
	
}
