import java.awt.Color;
import java.util.ArrayList;

// Assignment 6
// partner1- Nguyen, Danielle
// partner1- smurtle
// partner2- Morgan, Kirk
// partner2- captaink

import java.util.Random;

// import javalib.impworld.*;
import javalib.worldimages.*;

// TO DO
// TEST updateToFlooded
// WHAT IS A PINHOLE WRT RENDERING RECTANGLE IMAGES

// THE ORIGIN OF THE BACKGROUND IS AT THE TOP LEFT CORNER OF THE SCREEN

// To represent a single square of the game area
class Cell {
    double height; // Represents the absolute height of this cell, in feet
    int x, y; // The origin of this cell is at the top-left corner of the screen
    Cell left, top, right, bottom; // The four adjacent cells to this cell
    boolean isFlooded; // Is this cell flooded?

    // First cell constructor
    // Initializes all cell neighbors to null
    Cell(double height, int x, int y) {
        this.height = height;
        this.x = x;
        this.y = y;
        this.left = null;
        this.top = null;
        this.right = null;
        this.bottom = null;
        // Is this cell flooded?
        if (height <= 0) {
            this.isFlooded = true;
        } else {
            this.isFlooded = false;
        }
    }

    // Second cell constructor
    Cell(double height, int x, int y, Cell left, Cell top, Cell right,
            Cell bottom, boolean isFlooded) {
        this.height = height;
        this.x = x;
        this.y = y;
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.isFlooded = isFlooded;
    }

    // is this cell surrounded by land on all four sides?
    boolean landlocked() {
        return !this.left.isFlooded && !this.top.isFlooded
                && !this.right.isFlooded && !this.bottom.isFlooded;
    }

    // updates this cell to be flooded and updates other cells'
    // flood status as needed because of this cell
    void updateToFlooded(double height) {
        this.isFlooded = true;
        if (this.left.height <= height && !this.left.landlocked()
                && !this.left.isFlooded) {
            this.left.updateToFlooded(height);
        }
        if (this.top.height <= height && !this.top.landlocked()
                && !this.top.isFlooded) {
            this.top.updateToFlooded(height);
        }
        if (this.right.height <= height && !this.right.landlocked()
                && !this.right.isFlooded) {
            this.right.updateToFlooded(height);
        }
        if (this.bottom.height <= height && !this.bottom.landlocked()
                && !this.bottom.isFlooded) {
            this.bottom.updateToFlooded(height);
        }
    }

    // EFFECT: updates the cell to the left of this cell to be the given cell
    void updateLeft(Cell left) {
        this.left = left;
    }

    // EFFECT: updates the cell to the top of this cell to be the given cell
    void updateTop(Cell top) {
        this.top = top;
    }

    // EFFECT: updates the cell to the right of this cell to be the given cell
    void updateRight(Cell right) {
        this.right = right;
    }

    // EFFECT: updates the cell to the bottom of this cell to be the given cell
    void updateBottom(Cell bottom) {
        this.bottom = bottom;
    }

    // EFFECT: updates this cell's left field to be the given cell
    // updates the given cell's right field to be this cell
    void updateLeftRight(Cell left) {
        this.left = left;
        left.updateRight(this);
    }

    // EFFECT: updates this cell's top field to be the given cell
    // updates the given cell's bottom field to be this cell
    void updateTopBottom(Cell top) {
        this.top = top;
        top.updateBottom(this);
    }

    // Renders this cell on top of the given background with the
    // appropriate color depending on the waterHeight given
    WorldImage drawCell(WorldImage background, double seaLevel) {
        // calculates this cell's height above sea level
        double heightFromSea = Math.abs(seaLevel - this.height);
        int depth = (int) (180 * (1 - Math.exp(-1 * heightFromSea
                / (.2 * (ForbiddenIslandWorld.ISLAND_SIZE)))));

        // to render cells that have flooded
        if (this.isFlooded) {
            return background.overlayImages(new RectangleImage(new Posn(this.x
                    * ForbiddenIslandWorld.PIXEL_SIZE
                    + ForbiddenIslandWorld.PIXEL_SIZE / 2, this.y
                    * ForbiddenIslandWorld.PIXEL_SIZE
                    + ForbiddenIslandWorld.PIXEL_SIZE / 2),
                    ForbiddenIslandWorld.PIXEL_SIZE,
                    ForbiddenIslandWorld.PIXEL_SIZE, new Color(0, 0,
                            155 - depth))); // recheck color
        }

        // to render cells that haven't flooded but are below sea level
        else if (seaLevel > this.height) {
            return background.overlayImages(new RectangleImage(new Posn(this.x
                    * ForbiddenIslandWorld.PIXEL_SIZE
                    + ForbiddenIslandWorld.PIXEL_SIZE / 2, this.y
                    * ForbiddenIslandWorld.PIXEL_SIZE
                    + ForbiddenIslandWorld.PIXEL_SIZE / 2),
                    ForbiddenIslandWorld.PIXEL_SIZE,
                    ForbiddenIslandWorld.PIXEL_SIZE, new Color(50,
                            90 - depth / 5, 0))); // recheck color
        }

        // to render cells non-flooded cells that are above sea level
        else {
            return background.overlayImages(new RectangleImage(new Posn(this.x
                    * ForbiddenIslandWorld.PIXEL_SIZE
                    + ForbiddenIslandWorld.PIXEL_SIZE / 2, this.y
                    * ForbiddenIslandWorld.PIXEL_SIZE
                    + ForbiddenIslandWorld.PIXEL_SIZE / 2),
                    ForbiddenIslandWorld.PIXEL_SIZE,
                    ForbiddenIslandWorld.PIXEL_SIZE, new Color(0, 90 + depth,
                            70))); // recheck color
        }
    }
}

// To represent a single square of the game area that is ocean
class OceanCell extends Cell {

    // First ocean cell constructor
    // Defines an ocean cell to always be flooded
    OceanCell(int x, int y) {
        super(-1, x, y);
        this.isFlooded = true;
    }

    // Second ocean cell constructor
    OceanCell(double height, int x, int y, Cell left, Cell top, Cell right,
            Cell bottom, boolean isFlooded) {
        super(-1, x, y, left, top, right, bottom, true);
    }

    // Renders this ocean cell on top of the given background image
    WorldImage drawCell(WorldImage background, double waterHeight) {
        return background.overlayImages(new RectangleImage(new Posn(this.x
                * ForbiddenIslandWorld.PIXEL_SIZE
                + ForbiddenIslandWorld.PIXEL_SIZE / 2, this.y
                * ForbiddenIslandWorld.PIXEL_SIZE
                + ForbiddenIslandWorld.PIXEL_SIZE / 2),
                ForbiddenIslandWorld.PIXEL_SIZE,
                ForbiddenIslandWorld.PIXEL_SIZE, new Color(0, 0, 125)));
    }
}

// To represent the player in the game
class Player {
    Posn posn;

    Player(Posn posn) {
        this.posn = posn;
    }

    // Constructs a random Player on a valid cell on the given World
    Player(ForbiddenIslandWorld world) {
        Posn posn = new Posn(
                new Random().nextInt(ForbiddenIslandWorld.ISLAND_SIZE),
                (new Random().nextInt(ForbiddenIslandWorld.ISLAND_SIZE)));
        Cell c = world.findCell(posn);
        while (c.isFlooded || c.height < ForbiddenIslandWorld.ISLAND_SIZE / 8) {
            posn = new Posn(
                    new Random().nextInt(ForbiddenIslandWorld.ISLAND_SIZE),
                    (new Random().nextInt(ForbiddenIslandWorld.ISLAND_SIZE)));
            c = world.findCell(posn);
        }
        this.posn = posn;
    }

    // Moves the player on the given board according to the given key
    Player movePlayer(ForbiddenIslandWorld board, String ke) {
        Cell currCell = board.findCell(this.posn);
        if (ke.equals("up")) {
            posn = new Posn(this.posn.x, this.posn.y - 1);
            if (!currCell.isFlooded) {
                return new Player(posn);
            }
        }
        if (ke.equals("down")) {
            posn = new Posn(this.posn.x, this.posn.y + 1);
            if (!currCell.isFlooded) {
                return new Player(posn);
            }
        }
        if (ke.equals("left")) {
            posn = new Posn(this.posn.x - 1, this.posn.y);
            if (!currCell.isFlooded) {
                return new Player(posn);
            }
        }
        if (ke.equals("right")) {
            posn = new Posn(this.posn.x + 1, this.posn.y);
            if (!currCell.isFlooded) {
                return new Player(posn);
            }
        }
        return this;
    }

    // Renders the icon representing the player on the board
    WorldImage drawPlayer(WorldImage background) {
        return background.overlayImages(new FromFileImage(new Posn(this.posn.x
                * ForbiddenIslandWorld.PIXEL_SIZE
                + ForbiddenIslandWorld.PIXEL_SIZE / 2, this.posn.y
                * ForbiddenIslandWorld.PIXEL_SIZE
                + ForbiddenIslandWorld.PIXEL_SIZE / 2), "pilot.png"));
    }
}

// To represent the helicopter in the game
class Helicopter {
    Posn posn;

    Helicopter(Posn posn) {
        this.posn = posn;
    }

    Helicopter(ForbiddenIslandWorld world) {
        double maxHeight = 0;
        ArrayList<Cell> highPoints = new ArrayList<Cell>();
        for (int i = 0; i < ForbiddenIslandWorld.ISLAND_SIZE; i++) {
            for (int j = 0; j < ForbiddenIslandWorld.ISLAND_SIZE; j++) {
                if (world.board.get(i).get(j).height > maxHeight) {
                    highPoints.clear();
                    highPoints.add(world.board.get(i).get(j));
                    maxHeight = world.board.get(i).get(j).height;
                } else if (world.board.get(i).get(j).height == maxHeight) {
                    highPoints.add(world.board.get(i).get(j));
                }
            }
        }
        Cell peak = highPoints.get(0);
        this.posn = new Posn(peak.x, peak.y);
    }

    // Renders the icon representing the player on the board
    WorldImage drawHelicopter(WorldImage background) {
        return background.overlayImages(new FromFileImage(new Posn(this.posn.x
                * ForbiddenIslandWorld.PIXEL_SIZE
                + ForbiddenIslandWorld.PIXEL_SIZE / 2, this.posn.y
                * ForbiddenIslandWorld.PIXEL_SIZE
                + ForbiddenIslandWorld.PIXEL_SIZE / 2), "helicopter.png"));
    }
}

// To represent a broken helicopter part in the game
class Part {
    Posn posn;

    Part(Posn posn) {
        this.posn = posn;
    }

    Part(ForbiddenIslandWorld world) {
        Posn posn = new Posn(
                new Random().nextInt(ForbiddenIslandWorld.ISLAND_SIZE),
                new Random().nextInt(ForbiddenIslandWorld.ISLAND_SIZE));
        Cell c = world.findCell(posn);
        while (c.isFlooded || c.height < ForbiddenIslandWorld.ISLAND_SIZE / 8) {
            posn = new Posn(
                    new Random().nextInt(ForbiddenIslandWorld.ISLAND_SIZE),
                    new Random().nextInt(ForbiddenIslandWorld.ISLAND_SIZE));
            c = world.findCell(posn);
        }
        this.posn = posn;
    }

    // Renders a disk to represent a helicopter part on the board
    WorldImage drawPart(WorldImage background) {
        return background.overlayImages(new CircleImage(new Posn(this.posn.x
                * ForbiddenIslandWorld.PIXEL_SIZE
                + ForbiddenIslandWorld.PIXEL_SIZE / 2, this.posn.y
                * ForbiddenIslandWorld.PIXEL_SIZE
                + ForbiddenIslandWorld.PIXEL_SIZE / 2),
                ForbiddenIslandWorld.PIXEL_SIZE / 2, new Color(255, 0, 0)));
    }
}