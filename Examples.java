import javalib.worldimages.*;
import tester.*;

import java.awt.Color;

// to represent examples for the Forbidden Island game
class Examples {

    ForbiddenIslandWorld world1 = new ForbiddenIslandWorld();
    
    // Examples of Cells
    Cell c1 = new Cell(3, 3, 3);
    Cell c2 = new Cell(-2, 2, 2);
    Cell c3 = new Cell(0, 1, 1);
    OceanCell o1 = new OceanCell(5, 5);
    OceanCell o2 = new OceanCell(4, 4);

    // Examples of Players
    Player player1 = new Player(new Posn(10, 10));
    Player player2 = new Player(new Posn(2, 2));

    // Examples of Parts
    Part part1 = new Part(new Posn(1, 1));
    Part part2 = new Part(new Posn(3, 3));

    // Example of Helicopter
    Helicopter helicopter1 = new Helicopter(new Posn(3, 3));

    // Renders the background for the game
    WorldImage background = new RectangleImage(new Posn(
            (int) ForbiddenIslandWorld.HALF_ISLAND_SIZE
                    * ForbiddenIslandWorld.PIXEL_SIZE,
            (int) ForbiddenIslandWorld.HALF_ISLAND_SIZE
                    * ForbiddenIslandWorld.PIXEL_SIZE),
            ForbiddenIslandWorld.ISLAND_SIZE * ForbiddenIslandWorld.PIXEL_SIZE,
            ForbiddenIslandWorld.ISLAND_SIZE * ForbiddenIslandWorld.PIXEL_SIZE,
            new Color(255, 255, 255)); // white

    // EFFECT: initializes the data
    void initData() {
        c1 = new Cell(3, 3, 3);
        c2 = new Cell(-2, 2, 2);
        c3 = new Cell(0, 1, 1);
        o1 = new OceanCell(5, 5);
        o2 = new OceanCell(4, 4);
        player1 = new Player(new Posn(10, 10));
        player2 = new Player(new Posn(2, 2));
/*
        mtCell = new MtList<Cell>();
        cellList1 = new ConsList<Cell>(this.c1, this.mtCell);
        cellList2 = new ConsList<Cell>(this.c3, new ConsList<Cell>(this.c2,
                this.cellList1));*/
    }

    // ALL TESTS FOR METHODS OF THE CLASS CELL

    // EFFECT: tests the method updateLeft for the class Cell
    void testUpdateLeft(Tester t) {
        this.initData();

        // first test
        t.checkExpect(this.c1.left, null);
        this.c1.updateLeft(this.c2);
        t.checkExpect(this.c1.left, this.c2);

        // second test
        t.checkExpect(this.c2.left, null);
        this.c2.updateLeft(this.c3);
        t.checkExpect(this.c2.left, this.c3);
    }

    // EFFECT: tests the method updateTop for the class Cell
    void testUpdateTop(Tester t) {
        this.initData();

        // first test
        t.checkExpect(this.c1.top, null);
        this.c1.updateTop(this.c2);
        t.checkExpect(this.c1.top, this.c2);

        // second test
        t.checkExpect(this.c2.top, null);
        this.c2.updateTop(this.c3);
        t.checkExpect(this.c2.top, this.c3);
    }

    // EFFECT: tests the method updateRight for the class Cell
    void testUpdateRight(Tester t) {
        this.initData();

        // first test
        t.checkExpect(this.c1.right, null);
        this.c1.updateRight(this.c2);
        t.checkExpect(this.c1.right, this.c2);

        // second test
        t.checkExpect(this.c2.right, null);
        this.c2.updateRight(this.c3);
        t.checkExpect(this.c2.right, this.c3);
    }

    // EFFECT: tests the method updateBottom for the class Cell
    void testUpdateBottom(Tester t) {
        this.initData();

        // first test
        t.checkExpect(this.c1.bottom, null);
        this.c1.updateBottom(this.c2);
        t.checkExpect(this.c1.bottom, this.c2);

        // second test
        t.checkExpect(this.c2.bottom, null);
        this.c2.updateBottom(this.c3);
        t.checkExpect(this.c2.bottom, this.c3);
    }

    // EFFECT: tests the method updateLeftRight for the class Cell
    void testUpdateLeftRight(Tester t) {
        this.initData();

        // first test
        t.checkExpect(this.c1.left, null);
        t.checkExpect(this.c2.right, null);
        this.c1.updateLeftRight(this.c2);
        t.checkExpect(this.c1.left, this.c2);
        t.checkExpect(this.c2.right, this.c1);

        // second test
        t.checkExpect(this.c2.left, null);
        t.checkExpect(this.c3.right, null);
        this.c2.updateLeftRight(this.c3);
        t.checkExpect(this.c2.left, this.c3);
        t.checkExpect(this.c3.right, this.c2);
    }

    // EFFECT: tests the method updateTopBottom for the class Cell
    void testUpdateTopBottom(Tester t) {
        this.initData();

        // first test
        t.checkExpect(this.c1.top, null);
        t.checkExpect(this.c2.bottom, null);
        this.c1.updateTopBottom(this.c2);
        t.checkExpect(this.c1.top, this.c2);
        t.checkExpect(this.c2.bottom, this.c1);

        // second test
        t.checkExpect(this.c2.top, null);
        t.checkExpect(this.c3.bottom, null);
        this.c2.updateTopBottom(this.c3);
        t.checkExpect(this.c2.top, this.c3);
        t.checkExpect(this.c3.bottom, this.c2);
    }

    // Tests the method landlocked for the class Cell
    void testLandlocked(Tester t) {
        this.initData();

        // first test
        this.c1.updateLeft(this.c1);
        this.c1.updateRight(this.c1);
        this.c1.updateTop(this.c1);
        this.c1.updateBottom(this.c1);
        t.checkExpect(this.c1.landlocked(), true);

        // second test
        this.o1.updateLeft(this.c2);
        this.o1.updateRight(this.c2);
        this.o1.updateTop(this.c2);
        this.o1.updateBottom(this.c2);
        t.checkExpect(this.o1.landlocked(), false);

        // third test
        this.c3.updateLeft(this.o1);
        this.c3.updateRight(this.o1);
        this.c3.updateTop(this.o1);
        this.c3.updateBottom(this.c3);
        t.checkExpect(this.c3.landlocked(), false);
    }

    // Tests the method drawCell for the class Cell
    void testDrawCell(Tester t) {
        this.initData();
        t.checkExpect(this.c1.drawCell(this.background, 0),
                this.background
                        .overlayImages(new RectangleImage(new Posn(3
                                * ForbiddenIslandWorld.PIXEL_SIZE
                                + ForbiddenIslandWorld.PIXEL_SIZE / 2, 3
                                * ForbiddenIslandWorld.PIXEL_SIZE
                                + ForbiddenIslandWorld.PIXEL_SIZE / 2),
                                ForbiddenIslandWorld.PIXEL_SIZE,
                                ForbiddenIslandWorld.PIXEL_SIZE, new Color(0,
                                        127, 50))));
        t.checkExpect(this.c2.drawCell(this.background, 0),
                this.background
                        .overlayImages(new RectangleImage(new Posn(2
                                * ForbiddenIslandWorld.PIXEL_SIZE
                                + ForbiddenIslandWorld.PIXEL_SIZE / 2, 2
                                * ForbiddenIslandWorld.PIXEL_SIZE
                                + ForbiddenIslandWorld.PIXEL_SIZE / 2),
                                ForbiddenIslandWorld.PIXEL_SIZE,
                                ForbiddenIslandWorld.PIXEL_SIZE, new Color(30,
                                        30, 229))));
        t.checkExpect(this.c3.drawCell(this.background, 0),
                this.background
                        .overlayImages(new RectangleImage(new Posn(1
                                * ForbiddenIslandWorld.PIXEL_SIZE
                                + ForbiddenIslandWorld.PIXEL_SIZE / 2, 1
                                * ForbiddenIslandWorld.PIXEL_SIZE
                                + ForbiddenIslandWorld.PIXEL_SIZE / 2),
                                ForbiddenIslandWorld.PIXEL_SIZE,
                                ForbiddenIslandWorld.PIXEL_SIZE, new Color(30,
                                        30, 255))));
        t.checkExpect(this.o1.drawCell(this.background, 0), this.background
                .overlayImages(new RectangleImage(new Posn(5
                        * ForbiddenIslandWorld.PIXEL_SIZE
                        + ForbiddenIslandWorld.PIXEL_SIZE / 2, 5
                        * ForbiddenIslandWorld.PIXEL_SIZE
                        + ForbiddenIslandWorld.PIXEL_SIZE / 2),
                        ForbiddenIslandWorld.PIXEL_SIZE,
                        ForbiddenIslandWorld.PIXEL_SIZE, new Color(0, 0, 125))));
        t.checkExpect(this.o2.drawCell(this.background, 0), this.background
                .overlayImages(new RectangleImage(new Posn(4
                        * ForbiddenIslandWorld.PIXEL_SIZE
                        + ForbiddenIslandWorld.PIXEL_SIZE / 2, 4
                        * ForbiddenIslandWorld.PIXEL_SIZE
                        + ForbiddenIslandWorld.PIXEL_SIZE / 2),
                        ForbiddenIslandWorld.PIXEL_SIZE,
                        ForbiddenIslandWorld.PIXEL_SIZE, new Color(0, 0, 125))));
    }

    // Tests the method drawPlayer for the class Player
    void testDrawPlayer(Tester t) {
        this.initData();
        t.checkExpect(
                this.player1.drawPlayer(this.background),
                background.overlayImages(new FromFileImage(new Posn(10
                        * ForbiddenIslandWorld.PIXEL_SIZE
                        + ForbiddenIslandWorld.PIXEL_SIZE / 2, 10
                        * ForbiddenIslandWorld.PIXEL_SIZE
                        + ForbiddenIslandWorld.PIXEL_SIZE / 2), "pilot.png")));
        t.checkExpect(
                this.player2.drawPlayer(this.background),
                background.overlayImages(new FromFileImage(new Posn(2
                        * ForbiddenIslandWorld.PIXEL_SIZE
                        + ForbiddenIslandWorld.PIXEL_SIZE / 2, 2
                        * ForbiddenIslandWorld.PIXEL_SIZE
                        + ForbiddenIslandWorld.PIXEL_SIZE / 2), "pilot.png")));
    }

    // Tests the method drawHelicopter for the class Helicopter
    void testDrawHelicopter(Tester t) {
        this.initData();
        t.checkExpect(this.helicopter1.drawHelicopter(this.background),
                background.overlayImages(new FromFileImage(new Posn(3
                        * ForbiddenIslandWorld.PIXEL_SIZE
                        + ForbiddenIslandWorld.PIXEL_SIZE / 2, 3
                        * ForbiddenIslandWorld.PIXEL_SIZE
                        + ForbiddenIslandWorld.PIXEL_SIZE / 2),
                        "helicopter.png")));
    }
    
    // Tests the method drawPart for the class Part
    void testDrawPart(Tester t) {
        this.initData();
        t.checkExpect(this.part1.drawPart(this.background),
                background.overlayImages(new CircleImage(new Posn(1
                        * ForbiddenIslandWorld.PIXEL_SIZE
                        + ForbiddenIslandWorld.PIXEL_SIZE / 2, 1
                        * ForbiddenIslandWorld.PIXEL_SIZE
                        + ForbiddenIslandWorld.PIXEL_SIZE / 2), 
                        ForbiddenIslandWorld.PIXEL_SIZE / 2, new Color(255, 0, 0))));
        t.checkExpect(this.part2.drawPart(this.background),
                background.overlayImages(new CircleImage(new Posn(3
                        * ForbiddenIslandWorld.PIXEL_SIZE
                        + ForbiddenIslandWorld.PIXEL_SIZE / 2, 3
                        * ForbiddenIslandWorld.PIXEL_SIZE
                        + ForbiddenIslandWorld.PIXEL_SIZE / 2), 
                        ForbiddenIslandWorld.PIXEL_SIZE / 2, new Color(255, 0, 0))));
    }
    
    // runs the mountain game
    /*void testGame(Tester t) {
        ForbiddenIslandWorld world2 = new ForbiddenIslandWorld();
        world2.onKeyEvent("m");
        world2.onTick();
        world2.bigBang(ForbiddenIslandWorld.ISLAND_SIZE
                * ForbiddenIslandWorld.PIXEL_SIZE,
                ForbiddenIslandWorld.ISLAND_SIZE
                        * ForbiddenIslandWorld.PIXEL_SIZE, 2);

    }
    
    // runs the random mountain game
    void testGame2(Tester t) {
        ForbiddenIslandWorld world3 = new ForbiddenIslandWorld();
        world3.onKeyEvent("r");
        world3.onTick();
        world3.bigBang(ForbiddenIslandWorld.ISLAND_SIZE
                * ForbiddenIslandWorld.PIXEL_SIZE,
                ForbiddenIslandWorld.ISLAND_SIZE
                        * ForbiddenIslandWorld.PIXEL_SIZE, .1);
    }
    
    // runs the random terrain game
    void testGame3(Tester t) {
        ForbiddenIslandWorld world4 = new ForbiddenIslandWorld();
        world4.onKeyEvent("t");
        world4.onTick();
        world4.bigBang(ForbiddenIslandWorld.ISLAND_SIZE
                * ForbiddenIslandWorld.PIXEL_SIZE,
                ForbiddenIslandWorld.ISLAND_SIZE
                        * ForbiddenIslandWorld.PIXEL_SIZE, .1);
    }*/
    
}
