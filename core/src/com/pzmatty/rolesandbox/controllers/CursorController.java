package com.pzmatty.rolesandbox.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.pzmatty.rolesandbox.managers.AssetsManager;
import com.pzmatty.rolesandbox.managers.DatabaseManager;
import com.pzmatty.rolesandbox.managers.SoundManager;
import com.pzmatty.rolesandbox.managers.TiledMapManager;
import com.pzmatty.rolesandbox.managers.TiledMapManager.ActionState;
import com.pzmatty.rolesandbox.managers.UIManager;
import com.pzmatty.rolesandbox.objects.entities.StaticEntity;
import com.pzmatty.rolesandbox.screens.ScreenGame;
import com.pzmatty.rolesandbox.ui.InfoGroupUI;
import com.pzmatty.rolesandbox.ui.InfoMonsterUI;

public class CursorController extends InputAdapter {

	private static StaticEntity cursor = new StaticEntity(
			new TextureRegion(AssetsManager.get(DatabaseManager.getConstant("CURSOR"), Texture.class)),
			new Rectangle(
					new Rectangle(0, 0, 16 * TiledMapManager.WORLD_TO_SCREEN, 16 * TiledMapManager.WORLD_TO_SCREEN)),
			false, "Cursor");

	public static StaticEntity getCursor() {
		return cursor;
	};

	private TiledMapManager tilemap;
	private ScreenGame game;

	private UIManager ui;

	public CursorController(ScreenGame game) {
		this.game = game;
		this.tilemap = game.getTiledMap();
		this.ui = game.getUI();
	}

	@Override
	public boolean keyDown(int keycode) {
		int x = 0, y = 0;

		switch (keycode) {
		case Keys.UP:
			y = 1;
			break;
		case Keys.LEFT:
			x = -1;
			break;
		case Keys.DOWN:
			y = -1;
			break;
		case Keys.RIGHT:
			x = 1;
			break;
		case Keys.NUMPAD_1:
			x = -1;
			y = -1;
			break;
		case Keys.NUMPAD_2:
			y = -1;
			break;
		case Keys.NUMPAD_3:
			x = 1;
			y = -1;
			break;
		case Keys.NUMPAD_4:
			x = -1;
			break;
		case Keys.NUMPAD_6:
			x = 1;
			break;
		case Keys.NUMPAD_7:
			x = -1;
			y = 1;
			break;
		case Keys.NUMPAD_8:
			y = 1;
			break;
		case Keys.NUMPAD_9:
			x = 1;
			y = 1;
			break;
		case Keys.ESCAPE:
			SoundManager.playSound("CURSOR_CLOSE");
			game.getUI().getActor("Info", InfoGroupUI.class).clearInfo();
			game.getUI().getActor("Info", InfoGroupUI.class).setVisible(false);
			game.getUI().getActor("Monster", InfoMonsterUI.class).clearInfo();
			game.getUI().getActor("Monster", InfoMonsterUI.class).setVisible(false);
			Gdx.input.setInputProcessor(game.getPlayerController().set());
			tilemap.setState(ActionState.PLAYER);
			break;
		}

		if (x != 0 || y != 0) {
			SoundManager.playSound("CURSOR_MOVE");
			cursor.translate(new Vector2(x, y));
 			tilemap.setCameraPosition(cursor.getPosition());
 			tilemap.getCamera().update();
		}

		if (keycode != Keys.ESCAPE)
			showTileInfo();

		return true;
	}

	public CursorController set(Vector2 position) {
		CursorController.getCursor().setPosition(position);
		showTileInfo();
		return this;
	}

	private void showTileInfo() {
		Array<String> info = tilemap.getTileInfo(cursor.getPosition());
		ui.getActor("Info", InfoGroupUI.class).setInfo(info);
		
		Array<String> monster = tilemap.getMonsterInfo(cursor.getPosition());
		if (monster != null ) {
			ui.getActor("Monster", InfoMonsterUI.class).setInfo(monster);
			ui.getActor("Monster", InfoMonsterUI.class).show();
		} else {
			ui.getActor("Monster", InfoMonsterUI.class).clearInfo();
			ui.getActor("Monster", InfoMonsterUI.class).hide();
			if (info.size == 0)
				info.add("Void");
		}
	}

}
