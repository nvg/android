package com.sampler;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sampler.common.SampleBase;
import com.sampler.common.SampleInfo;
import com.sampler.utils.GdxUtils;

public class SpriteBatchSample extends SampleBase {

    public static final SampleInfo INFO = new SampleInfo(SpriteBatchSample.class);

    private static final float W = 10.8f;
    private static final float H = 7.2f;

    private OrthographicCamera camera;
    private Viewport viewport; // handles screen scaling
    private SpriteBatch batch;

    private Color color;
    private Texture texture;
    private BitmapFont font;

    private int w = 1;
    private int h = 1;

    private ArrayMap<String, Viewport> viewPorts = new ArrayMap<>();

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        camera = new OrthographicCamera();
        viewport = new FitViewport(W, H, camera);
        batch = new SpriteBatch();

        color = new Color();
        texture = new Texture(Gdx.files.internal("raw/character.png"));
        font = new BitmapFont(Gdx.files.internal("fonts/oswald-32.fnt"));

        Gdx.app.log("", "Create done");
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }


    @Override
    public void dispose() {
        batch.dispose();
        texture.dispose();
        font.dispose();
    }

    @Override
    public void render() {
        GdxUtils.clearScreen();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(texture,
                1, 1, // x, y
                w / 2, h / 2, // origin
                w, h, // width & height
                2.0f, 2.0f, // scale
                0f, // rotation
                0, 0, // scrX/Y
                texture.getWidth(), texture.getHeight(), // src dims
                false, false); // flips
        font.draw(batch, "Sprite Batch Sample", 50, 100);
        batch.end();

        Gdx.app.log("", "Create render done " + System.currentTimeMillis());
    }
}
