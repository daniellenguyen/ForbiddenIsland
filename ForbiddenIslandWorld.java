import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import javalib.impworld.*;
import javalib.worldimages.*;

// To represent the world of the game
class ForbiddenIslandWorld extends World {
    ArrayList<ArrayList<Cell>> board; // To represent all the cells in the game
    int waterHeight; // To represent the current height of the ocean
    Player player; // To represent the player in the game
    ArrayList<Part> parts; // To represent the parts the player must collect
    Helicopter helicopter; // To represent the getaway helicopter
    int counter; // Counts the time on the island starting at 0

    // Initializes an empty, arbitrary world
    ForbiddenIslandWorld() {
        this.board = new ArrayList<ArrayList<Cell>>();
        this.waterHeight = 0;
        this.player = new Player(new Posn(0, 0));
        this.parts = new ArrayList<Part>();
        this.helicopter = new Helicopter(new Posn(1, 1));
        this.counter = 0;
    }

    // Constructs a regular world that is given all fields
    ForbiddenIslandWorld(ArrayList<ArrayList<Cell>> board, int waterHeight,
            Player player, ArrayList<Part> parts, Helicopter helicopter,
            int counter) {
        this.board = board;
        this.waterHeight = waterHeight;
        this.player = player;
        this.parts = parts;
        this.helicopter = helicopter;
        this.counter = counter;
    }

    // World constructor given board, waterHeight, and counter
    // initializes the player and helicopter to a valid random position
    // on the board, generates a list of size NUM_PARTS of parts at random
    // valid positions on the board
    ForbiddenIslandWorld(ArrayList<ArrayList<Cell>> board, int waterHeight,
            int counter) {
        this.board = board;
        this.waterHeight = waterHeight;
        this.player = new Player(this);
        this.parts = this.makeParts(NUM_PARTS);
        this.helicopter = new Helicopter(this);
        this.counter = counter;
    }

    // The size of the island
    static final int ISLAND_SIZE = 64;

    // Half the size of the island
    static final double HALF_ISLAND_SIZE = ISLAND_SIZE / 2;

    // Size of the side of a cell in pixels
    static final int PIXEL_SIZE = 10;

    // Number of parts in the game
    static final int NUM_PARTS = 3;

    // Makes a given number of parts scattered around the board randomly
    ArrayList<Part> makeParts(int n) {
        ArrayList<Part> result = new ArrayList<Part>();
        for (int i = 0; i < n; i++) {
            result.add(new Part(this));
        }
        return result;
    }

    // Creates the mountain island shape
    ArrayList<ArrayList<Double>> perfectMountain() {
        ArrayList<ArrayList<Double>> finalResult = new ArrayList<ArrayList<Double>>();
        for (int i = 0; i < ISLAND_SIZE; i++) {
            ArrayList<Double> rowResult = new ArrayList<Double>();
            for (int j = 0; j < ISLAND_SIZE; j++) {
                Double cellHeight = (HALF_ISLAND_SIZE
                        - Math.abs(HALF_ISLAND_SIZE - i) - Math
                        .abs(HALF_ISLAND_SIZE - j));
                rowResult.add(cellHeight);
            }
            finalResult.add(rowResult);
        }
        return finalResult;
    }

    // Creates the mountain island with random heights
    ArrayList<ArrayList<Double>> randomMountain() {
        ArrayList<ArrayList<Double>> finalResult = new ArrayList<ArrayList<Double>>();
        for (int i = 0; i < ISLAND_SIZE; i++) {
            ArrayList<Double> rowResult = new ArrayList<Double>();
            for (int j = 0; j < ISLAND_SIZE; j++) {
                Double cellHeight = (HALF_ISLAND_SIZE
                        - Math.abs(HALF_ISLAND_SIZE - i) - Math
                        .abs(HALF_ISLAND_SIZE - j));
                if (cellHeight > 0) {
                    cellHeight = (double) Math.floor(Math.random()
                            * (HALF_ISLAND_SIZE - 1) + 1);
                }
                rowResult.add(cellHeight);
            }
            finalResult.add(rowResult);
        }
        return finalResult;
    }

    // Creates a new random terrain
    ArrayList<ArrayList<Double>> randomTerrain() {
        ArrayList<ArrayList<Double>> terrain = new ArrayList<ArrayList<Double>>(
                ISLAND_SIZE);
        for (int i = 0; i < ISLAND_SIZE; i += 1) {
            ArrayList<Double> row = new ArrayList<Double>(ISLAND_SIZE);
            for (int j = 0; j < ISLAND_SIZE; j += 1) {
                if (Math.abs(i - HALF_ISLAND_SIZE) <= .5
                        && Math.abs(j - HALF_ISLAND_SIZE) <= .5) {
                    row.add(HALF_ISLAND_SIZE);
                } 
                else {
                    row.add(0.0);
                }
            }
            terrain.add(row);
        }
        int lowerBound = (int) Math.floor(HALF_ISLAND_SIZE);
        int upperBound = (int) Math.ceil(HALF_ISLAND_SIZE);
        this.processTerrain(terrain, 0, 0, upperBound, ISLAND_SIZE / 2);
        this.processTerrain(terrain, 0, lowerBound, upperBound,
                ISLAND_SIZE - 1);
        this.processTerrain(terrain, lowerBound, 0, ISLAND_SIZE - 1,
                upperBound);
        this.processTerrain(terrain, lowerBound, lowerBound, ISLAND_SIZE - 1,
                ISLAND_SIZE - 1);
        return terrain;
    }

    // EFFECT: recursively updates the height matrix of the terrain
    void processTerrain(ArrayList<ArrayList<Double>> mountain, int topleftX,
            int topleftY, int botrightX, int botrightY) {
        if (((botrightX - topleftX) >= 2) || ((botrightY - topleftY) >= 2)) {
            Random rand = new Random();
            double scalar = .15 * Math.sqrt(botrightX - topleftX)
                    * (botrightY - topleftY);
            double tl = mountain.get(topleftY).get(topleftX);
            double tr = mountain.get(topleftY).get(botrightX);
            double bl = mountain.get(botrightY).get(topleftX);
            double br = mountain.get(botrightY).get(botrightX);
            int midX = (topleftX + botrightX) / 2;
            int midY = (topleftY + botrightY) / 2;
            double t = (tl + tr) / 2 + (rand.nextDouble() - .8) * scalar;
            double b = (bl + br) / 2 + (rand.nextDouble() - .8) * scalar;
            double l = (tl + bl) / 2 + (rand.nextDouble() - .8) * scalar;
            double r = (tr + br) / 2 + (rand.nextDouble() - .8) * scalar;
            double m = (tl + tr + bl + br) / 4 + (rand.nextDouble() - .8)
                    * scalar;
            ArrayList<Double> top = mountain.get(topleftY);
            ArrayList<Double> mid = mountain.get(midY);
            ArrayList<Double> bot = mountain.get(botrightY);
            if (top.get(midX) != 0) {
                top.set(midX, (t + top.get(midX)) / 2);
            } 
            else {
                top.set(midX, t);
            }
            if (mid.get(topleftX) != 0) {
                mid.set(topleftX, (l + mid.get(topleftX)) / 2);
            } 
            else {
                mid.set(topleftX, l);
            }
            if (mid.get(midX) != 0) {
                mid.set(midX, (m + mid.get(midX)) / 2);
            } 
            else {
                mid.set(midX, m);
            }
            if (mid.get(botrightX) != 0) {
                mid.set(botrightX, (r + mid.get(botrightX)) / 2);
            } 
            else {
                mid.set(botrightX, r);
            }
            if (bot.get(midX) != 0) {
                bot.set(midX, (b + bot.get(midX)) / 2);
            } 
            else {
                bot.set(midX, b);
            }
            mountain.set(topleftY, top);
            mountain.set(midY, mid);
            mountain.set(botrightY, bot);
            this.processTerrain(mountain, topleftX, topleftY, midX, midY);
            this.processTerrain(mountain, topleftX, midY, midX, botrightY);
            this.processTerrain(mountain, midX, topleftY, botrightX, midY);
            this.processTerrain(mountain, midX, midY, botrightX, botrightY);
        }
    }

    // Turn an ArrayList<ArrayList<Double>> into an ArrayList<ArrayList<Cell>>
    ArrayList<ArrayList<Cell>> cellConverter(ArrayList<ArrayList<Double>> arr) {
        ArrayList<ArrayList<Cell>> finalMatrix = new ArrayList<ArrayList<Cell>>();
        for (int i = 0; i < ISLAND_SIZE; i++) {
            ArrayList<Cell> row = new ArrayList<Cell>();
            for (int j = 0; j < ISLAND_SIZE; j++) {
                Cell c;
                if (arr.get(i).get(j) <= 0) {
                    c = new OceanCell(i, j); // under 0 means ocean cell
                } else {
                    c = new Cell(arr.get(i).get(j), i, j); // reg. cell height
                }
                if (i != 0) {
                    c.updateTopBottom(finalMatrix.get(i - 1).get(j));
                } else {
                    c.updateTop(c); // updates top to itself
                }
                if (j != 0) {
                    c.updateLeftRight(row.get(j - 1));
                } else {
                    c.updateLeft(c); // updates left to itself
                }
                if (j >= ISLAND_SIZE - 1) {
                    c.updateRight(c); // updates right to itself
                }
                if (i >= ISLAND_SIZE - 1) {
                    c.updateBottom(c); // updates bottom to itself
                }
                row.add(c);
            }
            finalMatrix.add(row);
        }
        return finalMatrix;
    }

    // Renders the entire Forbidden Island Game
    public WorldImage makeImage() {

        // Renders the background
        WorldImage background = new RectangleImage(new Posn(
                (int) HALF_ISLAND_SIZE * PIXEL_SIZE, (int) HALF_ISLAND_SIZE
                        * PIXEL_SIZE), ISLAND_SIZE * PIXEL_SIZE, ISLAND_SIZE
                * PIXEL_SIZE, new Color(255, 255, 255)); // white

        // Renders all the cells in the game
        for (ArrayList<Cell> arr : this.board) { // for every arraylist
            for (int j = 0; j < ISLAND_SIZE; j++) { // for every cell
                background = arr.get(j).drawCell(background, this.waterHeight);
            }
        }

        // Renders the player on top of what is drawn so far
        background = this.player.drawPlayer(background);

        // Renders the helicopter on top of what is drawn so far
        background = this.helicopter.drawHelicopter(background);

        // Renders the parts on top of what is drawn so far
        for (Part p : this.parts) {
            background = p.drawPart(background);
        }

        return background;
    }

    // Finds the cell in this World at the given posn
    Cell findCell(Posn p) {
        Cell result = new Cell(0, 0, 0);
        for (int i = 0; i < ForbiddenIslandWorld.ISLAND_SIZE; i++) {
            for (int j = 0; j < ForbiddenIslandWorld.ISLAND_SIZE; j++) {
                Cell c = this.board.get(i).get(j);
                if (c.x == p.x && c.y == p.y) {
                    result = c;
                }
            }
        }
        return result;
    }

    // Returns an updated world on each tick
    public void onTick() {
        ArrayList<Part> parts = this.parts;
        if (this.counter >= 10) {
            this.waterHeight = this.waterHeight + 1;
            this.counter = 0;
        } 
        else {
            this.counter = this.counter + 1;
        }
        for (int i = 0; i < ISLAND_SIZE; i++) {
            for (int j = 0; j < ISLAND_SIZE; j++) {
                Cell thing = this.board.get(i).get(j);
                if (thing.height < this.waterHeight && !thing.landlocked()) {
                    thing.updateToFlooded(thing.height);
                }
            }
        }

        // Removes parts when the player moves over them
        for (int i = 0; i < parts.size(); i++) {
            if (this.player.posn.x == parts.get(i).posn.x
                    && this.player.posn.y == parts.get(i).posn.y) {
                parts.remove(i);
            }
        }
        this.parts = parts;
    }

    // Creates a new world based on the key pressed
    public void onKeyEvent(String ke) {

        // Initializes the perfect mountain
        if (ke.equals("m")) {
            ForbiddenIslandWorld mountainWorld = new ForbiddenIslandWorld();
            this.board = mountainWorld.cellConverter(perfectMountain());
            this.waterHeight = 0;
            this.player = new Player(this);
            this.parts = this.makeParts(NUM_PARTS);
            this.helicopter = new Helicopter(this);
            this.counter = 0;
        }

        // Initializes the random mountain
        else if (ke.equals("r")) {
            ForbiddenIslandWorld randomWorld = new ForbiddenIslandWorld();
            this.board = randomWorld.cellConverter(randomMountain());
            this.waterHeight = 0;
            this.player = new Player(this);
            this.parts = this.makeParts(NUM_PARTS);
            this.helicopter = new Helicopter(this);
            this.counter = 0;
        }

        // Initializes the random terrain
        else if (ke.equals("t")) {
            ForbiddenIslandWorld randomTerrain = new ForbiddenIslandWorld();
            this.board = randomTerrain.cellConverter(randomTerrain());
            this.waterHeight = 0;
            this.player = new Player(this);
            this.parts = this.makeParts(NUM_PARTS);
            this.helicopter = new Helicopter(this);
            this.counter = 0;
        }

        // Moves player left by one cell
        else if (ke.equals("left")) {
            this.player.movePlayer(this, "left");
        }
        // Moves player right by one cell
        else if (ke.equals("right")) {
            this.player.movePlayer(this, "right");
        }
        // Moves player up by one cell
        else if (ke.equals("up")) {
            this.player.movePlayer(this, "up");
        }
        // Moves player down by one cell
        else if (ke.equals("down")) {
            this.player.movePlayer(this, "down");
        }
    }

    // Ends the game
    public WorldEnd worldEnds() {
        return new WorldEnd(this.gameOver(), this.gameOverImage());
    }

    // Determines if the game is over
    boolean gameOver() {
        return this.loseGame() || this.winGame();
    }

    // Returns true if the user has lost the game
    boolean loseGame() {
        return this.findCell(this.player.posn).isFlooded;
    }

    // Returns true if the user has won the game
    boolean winGame() {
        return this.getInHelicopter();
    }

    // Returns true if the user has all the prerequisites needed to win
    boolean getInHelicopter() {
        return this.parts.size() == 0
                && this.player.posn.x == this.helicopter.posn.x
                && this.player.posn.y == this.helicopter.posn.y;
    }

    // Returns the proper image based on the game end conditions
    WorldImage gameOverImage() {
        if (this.loseGame()) {
            return loseImage();
        } else {
            return winImage();
        }
    }

    // Returns the lost game image
    WorldImage loseImage() {
        Posn center = new Posn(ISLAND_SIZE * 5, ISLAND_SIZE * 5);
        return new OverlayImages(new RectangleImage(center,
                ISLAND_SIZE, ISLAND_SIZE, Color.white), new TextImage(center, 
                "Sorry, you lost :(", 30, 1, Color.red));
    }
    
    // Returns the won game image
    WorldImage winImage() {
        Posn center = new Posn(ISLAND_SIZE * 5, ISLAND_SIZE * 5);
        return new OverlayImages(new RectangleImage(center,
                ISLAND_SIZE, ISLAND_SIZE, Color.white), new TextImage(center, 
                "Congrats, you won :)", 30, 1, Color.green));
    }
}