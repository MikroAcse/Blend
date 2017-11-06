package com.mikroacse.blend.screens.objects;


import com.mikroacse.blend.media.Assets;
import com.mikroacse.blend.screens.objects.core.Path;
import com.mikroacse.engine.actors.Group;

/**
 * Created by MikroAcse on 11.07.2016.
 */
public class PathField extends Group {
    private Path[][] paths;
    private int size;
    private int length;

    public PathField(int size) {
        super();

        this.size = size;
        init();
    }

    private void init() {
        paths = new Path[size][size];
        length = 0;
    }

    public void reset() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Path path = paths[i][j];
                if (path == null) continue;
                this.removeActor(path);
                path.dispose();
            }
        }
        paths = new Path[size][size];
        length = 0;
    }

    public void setPath(int x, int y, String type, int rotation) {
        Path path = paths[x][y];
        if (path == null) {
            path = new Path();
            paths[x][y] = path;

            path.setX(x * Assets.getCellWidth());
            path.setY(y * Assets.getCellHeight());

            this.addActor(path);
            length++;
        }

        path.setType(type);
        path.setRotation(rotation);
    }

    public void dispose() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Path path = paths[i][j];
                if (path == null) continue;
                this.removeActor(path);
                path.dispose();
            }
        }
        paths = null;
    }

    public boolean pathExists(int x, int y) {
        return paths[x][y] != null;
    }

    public int getLength() {
        return length;
    }
}
