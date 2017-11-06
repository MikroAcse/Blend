package com.mikroacse.blend.screens.objects;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Expo;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mikroacse.blend.Blend;
import com.mikroacse.blend.listeners.interfaces.FieldListener;
import com.mikroacse.blend.media.Assets;
import com.mikroacse.blend.screens.SceneManager;
import com.mikroacse.blend.screens.objects.core.Cell;
import com.mikroacse.blend.screens.objects.core.Cube;
import com.mikroacse.blend.screens.objects.core.Path;
import com.mikroacse.engine.actors.Group;
import com.mikroacse.engine.listeners.core.ListenerSupport;
import com.mikroacse.engine.listeners.core.ListenerSupportFactory;
import com.mikroacse.engine.tween.ActorAccessor;
import com.mikroacse.engine.utils.ColorUtil;
import com.mikroacse.engine.utils.Vector2D;

/**
 * Created by MikroAcse on 11.07.2016.
 */
public class CellField extends Group {
    private final FieldListener listeners;
    private Cube cube;
    private Cell[][] cells;
    private Image finalPoint;
    private PathField path;
    private int size;
    private Vector2D startPosition;
    private Vector2D finalPosition;
    private Vector2D position;
    private Vector2D lastMove;
    private Color currentColor;

    public CellField(int size) {
        super();
        listeners = ListenerSupportFactory.create(FieldListener.class);

        this.size = size;
        init();
    }

    private void init() {
        cells = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Cell cell = new Cell();
                cell.setX(i * Assets.getCellWidth());
                cell.setY(j * Assets.getCellHeight());

                this.addActor(cell);
                cells[i][j] = cell;
            }
        }

        finalPosition = new Vector2D();
        finalPoint = new Image(Assets.getTexture(SceneManager.LEVEL, "final-point"));
        this.addActor(finalPoint);

        path = new PathField(size);
        this.addActor(path);

        cube = new Cube();
        this.addActor(cube);

        position = new Vector2D();
        lastMove = new Vector2D();
        startPosition = new Vector2D();
        currentColor = new Color(0, 0, 0, 1f);
    }

    public void reset() {
        path.reset();
        position.set(0, 0);
        lastMove.set(0, 0);
        startPosition.set(0, 0);
        currentColor.set(0, 0, 0, 1f);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Cell cell = cells[i][j];
                cell.turnDefault();
            }
        }

        updateCubePosition();
    }

    public boolean moveCube(int dx, int dy) {
        if (!canMove(position.x + dx, position.y + dy)) {
            return false;
        }
        if (path.pathExists(position.x + dx, position.y + dy)) {
            return false;
        }
        Blend.tweenManager.killTarget(cube);
        updateCubePosition();

        String type;
        if (path.getLength() == 0) {
            type = Path.START;
        } else {
            if (dx != lastMove.x && dy != lastMove.y) {
                type = Path.CORNER;
            } else {
                type = Path.LINE;
            }
        }

        int rotation = 0;
        if (dx != lastMove.x && dy != lastMove.y) {
            if (lastMove.y > 0 && dx > 0 || lastMove.x < 0 && dy < 0) {
                rotation = 1;
            } else if (lastMove.x > 0 && dy > 0 || lastMove.y < 0 && dx < 0) {
                rotation = 3;
            } else if (lastMove.y < 0 && dx > 0 || lastMove.x < 0 && dy > 0) {
                rotation = 2;
            } // default: rotation = 0
        } else {
            if (dx > 0) {
                rotation = 1;
            } else if (dx < 0) {
                rotation = 3;
            } else if (dy > 0) {
                rotation = 2;
            } // default: rotation = 0
        }

        path.setPath(position.x, position.y, type, rotation);
        position.set(position.x + dx, position.y + dy);
        updateCubePosition(true);
        lastMove.set(dx, dy);

        boolean isFinal = finalPosition.equals(position);

        if (!isFinal) {
            Cell cell = cells[position.x][position.y];

            Color newColor = ColorUtil.combine(currentColor, cell.getColor(), 0.2f);
            currentColor.set(newColor);

            Tween.to(cube, ActorAccessor.COLOR_RGB, 0.3f)
                    .target(newColor.r, newColor.g, newColor.b)
                    .start(Blend.tweenManager);
        }

        listeners.onMove();
        if (isFinal) {
            listeners.onFinalCell();
        }
        return true;
    }

    public void addListener(FieldListener listener) {
        ((ListenerSupport<FieldListener>) listeners).addListener(listener);
    }

    public void removeListener(FieldListener listener) {
        ((ListenerSupport<FieldListener>) listeners).removeListener(listener);
    }

    public void clearListeners() {
        ((ListenerSupport<FieldListener>) listeners).clearListeners();
    }

    public void dispose() {
        clearListeners();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Cell cell = cells[i][j];
                if (cell == null) continue;

                this.removeActor(cell);
            }
        }
        cells = null;
        this.removeActor(cube);
        this.removeActor(path);
        this.removeActor(finalPoint);

        path.dispose();
        cube.dispose();
    }

    public void setStartPoint(int x, int y, Color color) {
        startPosition.set(x, y);

        position.set(startPosition);

        Cell cell = getCell(position);
        cell.setColor(color);
        currentColor.set(color);
        cube.setColor(color);

        updateCubePosition();
    }

    public void setFinalPoint(int x, int y, Color color) {
        finalPosition.set(x, y);
        finalPoint.setPosition(x * Assets.getCellWidth(), y * Assets.getCellHeight());
        getCell(x, y).setColor(color);
    }

    public int getPathLength() {
        return path.getLength();
    }

    public Cell getCell(Vector2D position) {
        return cells[position.x][position.y];
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public void turnDefault(int x, int y) {
        getCell(x, y).turnDefault();
    }

    public void turnPale(int x, int y) {
        getCell(x, y).turnPale();
    }

    public boolean isPale(int x, int y) {
        return getCell(x, y).isPale();
    }

    public boolean isDefault(int x, int y) {
        return !isPale(x, y);
    }

    public void setColor(int x, int y, String colorname) {
        getCell(x, y).setColor(colorname);
    }

    public String getColorName(int x, int y) {
        return getCell(x, y).getColorName();
    }

    public Color getColor(int x, int y) {
        return getCell(x, y).getColor();
    }

    public int getColorValue(int x, int y) {
        Color color = getCell(x, y).getColor();
        return Color.rgb888(color.r, color.g, color.b);
    }

    public Color getCubeColor() {
        return currentColor;
    }

    @Override
    public float getRealWidth() {
        return size * Assets.getCellWidth() * getScaleX();
    }

    @Override
    public float getRealHeight() {
        return size * Assets.getCellHeight() * getScaleY();
    }

    private boolean canMove(int x, int y) {
        return x >= 0 && y >= 0 && x < size && y < size;
    }

    private void updateCubePosition(boolean animate) {
        int x = position.x * Assets.getCellWidth();
        int y = position.y * Assets.getCellHeight();
        final Cell cell = getCell(position);

        if (!animate) {
            cube.setPosition(x, y);
            if (!finalPosition.equals(position) && !startPosition.equals(position)) {
                cell.turnPale();
            }
        } else {
            Tween.to(cube, ActorAccessor.POSITION, 0.3f)
                    .ease(Expo.OUT)
                    .target(x, y)
                    .setCallback(new TweenCallback() {
                        @Override
                        public void onEvent(int type, BaseTween<?> source) {
                            if (type == TweenCallback.COMPLETE) {
                                if (!finalPosition.equals(position) && !startPosition.equals(position)) {
                                    cell.turnPale();
                                }
                            }
                        }
                    })
                    .start(Blend.tweenManager);
        }
    }

    private void updateCubePosition() {
        updateCubePosition(false);
    }
}
