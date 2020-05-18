package com.sampler;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sampler.common.SampleBase;
import com.sampler.common.SampleInfo;
import com.sampler.utils.GdxUtils;

public class ViewportSample extends SampleBase {

    public static final SampleInfo INFO = new SampleInfo(ViewportSample.class);

    private static final float W = 800.0f;
    private static final float H = 600.0f;

    private OrthographicCamera camera;
    private Viewport viewport; // handles screen scaling
    private SpriteBatch batch;
    private Texture texture;
    private BitmapFont font;

    private int currentViewportIndex;
    private String viewportName;

    private ArrayMap<String, Viewport> viewPorts = new ArrayMap<>();

    @Override

    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        camera = new OrthographicCamera();
        batch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("raw/level-bg-small.png"));
        font = new BitmapFont(Gdx.files.internal("fonts/oswald-32.fnt"));

        createViewports();
        selectNextViewport();

        Gdx.input.setInputProcessor(this);
    }

    private void createViewports() {
        viewPorts.put(StretchViewport.class.getSimpleName(), new StretchViewport(W, H, camera));
        viewPorts.put(FitViewport.class.getSimpleName(), new FitViewport(W, H, camera));
        viewPorts.put(FillViewport.class.getSimpleName(), new FillViewport(W, H, camera));
        viewPorts.put(ScreenViewport.class.getSimpleName(), new ScreenViewport(camera));
        viewPorts.put(ExtendViewport.class.getSimpleName(), new ExtendViewport(W, H, camera));

        currentViewportIndex = -1;
    }

    private void selectNextViewport() {
        currentViewportIndex = (currentViewportIndex + 1) % viewPorts.size;
        viewport = viewPorts.getValueAt(currentViewportIndex);

        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewportName = viewPorts.getKeyAt(currentViewportIndex);

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        selectNextViewport();
        return true;
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
        batch.draw(texture, 0, 0, W, H);
        font.draw(batch, viewportName, 50, 100);
        batch.end();
    }
}
