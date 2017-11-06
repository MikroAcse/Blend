package com.mikroacse.engine.media;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.mikroacse.engine.media.core.ShaderLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MikroAcse on 09.07.2016.
 */
public class Assets {
    protected TextureLoader.TextureParameter textureParameter;
    protected BitmapFontLoader.BitmapFontParameter fontParameter;
    protected AssetManager manager;
    protected Map<String, String> aliases;

    public Assets() {
        init();
    }

    public void init() {
        manager = new AssetManager();
        manager.setLoader(ShaderProgram.class, new ShaderLoader(new InternalFileHandleResolver()));

        textureParameter = new TextureLoader.TextureParameter();
        textureParameter.genMipMaps = false;
        textureParameter.minFilter = Texture.TextureFilter.Linear;
        textureParameter.magFilter = Texture.TextureFilter.Linear;

        fontParameter = new BitmapFontLoader.BitmapFontParameter();
        fontParameter.flip = false;
        fontParameter.genMipMaps = true;
        fontParameter.minFilter = Texture.TextureFilter.MipMapLinearNearest;
        fontParameter.magFilter = Texture.TextureFilter.Linear;

        aliases = new HashMap<>();
    }

    public float getProgress() {
        return manager.getProgress();
    }

    public boolean isLoaded() {
        return manager.update();
    }

    public void finishLoading() {
        manager.finishLoading();
    }

    public Texture getTexture(String name) {
        return manager.get(aliases.get(name), Texture.class);
    }

    public Sound getSound(String name) {
        return manager.get(aliases.get(name), Sound.class);
    }

    public Music getMusic(String name) {
        return manager.get(aliases.get(name), Music.class);
    }

    public BitmapFont getFont(String name) {
        return manager.get(aliases.get(name), BitmapFont.class);
    }

    public TextureAtlas getAtlas(String name) {
        return manager.get(aliases.get(name), TextureAtlas.class);
    }

    public ShaderProgram getShader(String name) {
        return manager.get(aliases.get(name), ShaderProgram.class);
    }

    public void unloadSounds() {
        Array<Sound> sounds = new Array<>();
        manager.getAll(Sound.class, sounds);
        for (Sound sound : sounds) {
            sound.dispose();
        }
        sounds.clear();
    }

    public void unloadMusic() {
        Array<Music> musics = new Array<>();
        manager.getAll(Music.class, musics);
        for (Music music : musics) {
            music.dispose();
        }
        musics.clear();
    }

    public void unloadTextures() {
        Array<Texture> textures = new Array<>();
        manager.getAll(Texture.class, textures);
        for (Texture texture : textures) {
            texture.dispose();
        }
        textures.clear();
    }

    public void unloadAtlases() {
        Array<TextureAtlas> atlases = new Array<>();
        manager.getAll(TextureAtlas.class, atlases);
        for (TextureAtlas atlas : atlases) {
            atlas.dispose();
        }
        atlases.clear();
    }

    public void unloadFonts() {
        Array<BitmapFont> fonts = new Array<>();
        manager.getAll(BitmapFont.class, fonts);
        for (BitmapFont font : fonts) {
            font.dispose();
        }
        fonts.clear();
    }

    public void unloadShaders() {
        Array<ShaderProgram> shaders = new Array<>();
        manager.getAll(ShaderProgram.class, shaders);
        for (ShaderProgram shader : shaders) {
            shader.dispose();
        }
        shaders.clear();
    }

    protected void addFont(String name, String path) {
        manager.load(path, BitmapFont.class, fontParameter);
        aliases.put(name, path);
    }

    protected void addSound(String name, String path) {
        manager.load(path, Sound.class);
        aliases.put(name, path);
    }

    protected void addMusic(String name, String path) {
        manager.load(path, Music.class);
        aliases.put(name, path);
    }

    protected void addTexture(String name, String path) {
        manager.load(path, Texture.class, textureParameter);
        aliases.put(name, path);
    }

    protected void addAtlas(String name, String path) {
        manager.load(path, TextureAtlas.class);
        aliases.put(name, path);
    }

    protected void addShader(String name, String path) {
        manager.load(path, ShaderProgram.class);
        aliases.put(name, path);
    }
}
