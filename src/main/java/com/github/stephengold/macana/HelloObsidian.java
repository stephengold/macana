/*
 Copyright (c) 2020-2024 Stephen Gold

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice, this
    list of conditions and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions and the following disclaimer in the documentation
    and/or other materials provided with the distribution.

 3. Neither the name of the copyright holder nor the names of its
    contributors may be used to endorse or promote products derived from
    this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.stephengold.macana;

import com.github.stephengold.sport.Constants;
import com.github.stephengold.sport.input.InputManager;
import com.github.stephengold.sport.input.InputProcessor;
import com.github.stephengold.sport.physics.BasePhysicsApp;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.PlaneCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.objects.PhysicsBody;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Plane;
import com.jme3.math.Vector3f;
import example.ExampleComponentSkin;
import example.ObsidianContext;
import myworld.obsidian.ObsidianUI;
import myworld.obsidian.components.Button;
import myworld.obsidian.components.text.TextDisplay;
import myworld.obsidian.display.Colors;
import myworld.obsidian.display.skin.UISkin;
import myworld.obsidian.display.skin.obsidian.ObsidianSkin;
import myworld.obsidian.events.scene.ButtonEvent;
import myworld.obsidian.geometry.Dimension2D;
import myworld.obsidian.input.Key;
import myworld.obsidian.input.MouseButton;
import myworld.obsidian.input.MouseWheelAxis;
import myworld.obsidian.text.Text;
import org.lwjgl.glfw.GLFW;

/**
 * Overlay an Obsidian button onto SPORT graphics.
 *
 * @author Stephen Gold sgold@sonic.net
 */
public class HelloObsidian extends BasePhysicsApp<PhysicsSpace> {
    // *************************************************************************
    // fields

    /**
     * GUI height
     */
    private static int guiHeight;
    /**
     * GUI width
     */
    private static int guiWidth;
    /**
     * system time as of the previous GUI update (or null if no previous render)
     */
    private static Long lastUpdate;
    /**
     * separate OpenGL context for Obsidian
     */
    private static ObsidianContext ctx;
    /**
     * Obsidian graphical user-interface layer
     */
    private static ObsidianUI ui;
    // *************************************************************************
    // constructors

    /**
     * Explicit no-arg constructor to avoid javadoc warnings from JDK 18+.
     */
    public HelloObsidian() {
        // do nothing
    }
    // *************************************************************************
    // new methods exposed

    /**
     * Main entry point for the HelloObsidian application.
     *
     * @param arguments array of command-line arguments (not null)
     */
    public static void main(String[] arguments) {
        setDebuggingEnabled(true);

        HelloObsidian application = new HelloObsidian();
        application.start();
    }
    // *************************************************************************
    // BasePhysicsApp methods

    /**
     * Callback invoked by SPORT after the main update loop terminates.
     */
    @Override
    public void cleanUp() {
        if (ctx != null) {
            ctx.close();
        }
        if (ui != null) {
            ui.cleanup();
        }

        super.cleanUp(); // clean up the physics
    }

    /**
     * Create the PhysicsSpace. Invoked once during initialization.
     *
     * @return a new instance
     */
    @Override
    public PhysicsSpace createSpace() {
        PhysicsSpace.BroadphaseType bPhase = PhysicsSpace.BroadphaseType.DBVT;
        return new PhysicsSpace(bPhase);
    }

    /**
     * Callback invoked by SPORT before the main update loop begins.
     */
    @Override
    protected void initialize() {
        setBackgroundColor(Constants.SKY_BLUE);

        updateGuiSurface(); // This creates the GUI.
        ui.clearColor().set(Colors.TRANSPARENT);

        UISkin skin = ObsidianSkin.create();
        skin.addComponentSkin(ExampleComponentSkin.create());
        ui.useSkin(skin);

        // Create an initialize the Obsidian rendering context:
        ctx = new ObsidianContext(ui);

        Dimension2D size = new Dimension2D(guiWidth, guiHeight);
        InputManager inputManager = getInputManager();
        long windowHandle = inputManager.getGlfwWindowHandle();
        ctx.init(size, 4, windowHandle);

        addGuiInput();
        addGuiLayout();

        super.initialize(); // initialize the physics
    }

    /**
     * Populate the PhysicsSpace. Invoked once during initialization.
     */
    @Override
    public void populateSpace() {
        // Add a static horizontal plane at y=-1.
        float planeY = -1f;
        Plane plane = new Plane(Vector3f.UNIT_Y, planeY);
        CollisionShape planeShape = new PlaneCollisionShape(plane);
        float mass = PhysicsBody.massForStatic;
        PhysicsRigidBody floor = new PhysicsRigidBody(planeShape, mass);
        physicsSpace.addCollisionObject(floor);

        // Add a sphere-shaped, dynamic, rigid body at the origin.
        float radius = 0.3f;
        CollisionShape ballShape = new SphereCollisionShape(radius);
        mass = 1f;
        PhysicsRigidBody ball = new PhysicsRigidBody(ballShape, mass);
        physicsSpace.addCollisionObject(ball);

        // Visualize the collision objects.
        visualizeShape(floor);
        visualizeShape(ball);
    }

    /**
     * Callback invoked by SPORT during each iteration of the main update loop.
     */
    @Override
    protected void render() {
        super.render();

        long nanoTime = System.nanoTime();
        float seconds
                = (lastUpdate == null) ? 0f : 1e-9f * (nanoTime - lastUpdate);
        lastUpdate = nanoTime;
        ui.update(seconds);

        // TODO detect resize and react to it
        ctx.render();
    }

    /**
     * Advance the physics simulation by the specified amount. Invoked during
     * each update.
     *
     * @param wallClockSeconds the elapsed wall-clock time since the previous
     * invocation of {@code updatePhysics} (in seconds, &ge;0)
     */
    @Override
    public void updatePhysics(float wallClockSeconds) {
        physicsSpace.update(wallClockSeconds);
    }
    // *************************************************************************
    // private methods

    /**
     * Add an InputProcessor for the Obsidian GUI.
     */
    private static void addGuiInput() {
        InputManager inputManager = getInputManager();
        InputProcessor processor = new InputProcessor() {
            @Override
            public void onCharacter(int codePoint) {
                char[] characters = Character.toChars(codePoint);
                ui.getInput().fireCharacterEvent(characters);
            }

            @Override
            public void onKeyboard(int glfwKey, boolean isPressed) {
                Key obsidianId = Convert.convertGlfwKey(glfwKey);
                ui.getInput().fireKeyEvent(obsidianId, isPressed);
            }

            @Override
            public void onMouseButton(int glfwButton, boolean isPressed) {
                MouseButton obsidianId
                        = Convert.convertGlfwMouseButton(glfwButton);
                int x = (int) inputManager.glfwCursorX();
                int y = (int) inputManager.glfwCursorY();
                ui.getInput().fireMouseButtonEvent(obsidianId, isPressed, x, y);
            }

            @Override
            public void onMouseMotion(double rightFraction, double upFraction) {
                int x = (int) inputManager.glfwCursorX();
                int y = (int) inputManager.glfwCursorY();
                ui.getInput().fireMouseMoveEvent(x, y);
            }

            @Override
            public void onScrollMotion(double xScroll, double yScroll) {
                int x = (int) inputManager.glfwCursorX();
                int y = (int) inputManager.glfwCursorY();

                if (xScroll != 0.) {
                    ui.getInput().fireMouseWheelEvent(
                            MouseWheelAxis.HORIZONTAL, x, y, (float) xScroll);
                }

                if (yScroll != 0.) {
                    ui.getInput().fireMouseWheelEvent(
                            MouseWheelAxis.VERTICAL, x, y, (float) yScroll);
                }
            }
        };
        inputManager.add(processor);
    }

    /**
     * Add a layout and a single text button to the GUI.
     */
    private static void addGuiLayout() {
        Text text = Text.styled("Restart", ui.getStyle("ExampleText"));
        Button button = Button.textButton(text);
        button.addButtonListener(ButtonEvent::isClicked,
                event -> System.out.println("Clicked restart!"));
        // TODO Clicking should restart the physics simulation.

        TextDisplay buttonText
                = (TextDisplay) button.children().get(0); // TODO ugly
        buttonText.fontFamily().set("Clear Sans");
        buttonText.fontSize().set(24f);

        SimpleLayout layout = new SimpleLayout();
        layout.addToColumn(button);

        ui.getRoot().addChild(layout);
        ui.requestFocus(button);
    }

    /**
     * Create or resize the GUI surface, as appropriate.
     */
    private static void updateGuiSurface() {
        InputManager inputManager = getInputManager();
        long windowHandle = inputManager.getGlfwWindowHandle();

        int[] widthArray = new int[1];
        int[] heightArray = new int[1];
        GLFW.glfwGetWindowSize(windowHandle, widthArray, heightArray);

        float[] xsArray = new float[1];
        float[] ysArray = new float[1];
        GLFW.glfwGetWindowContentScale(windowHandle, xsArray, ysArray);

        int renderWidth = widthArray[0];
        int renderHeight = (int) (heightArray[0] / ysArray[0] * xsArray[0]);

        if (ui == null) {
            System.out.println("ObsidianUI.createHeadless()");
            ui = ObsidianUI.createHeadless();
            assert ui != null;

        } else if (renderWidth != guiWidth || renderHeight != guiHeight) {
            System.out.println(
                    "resize(" + renderWidth + ", " + renderHeight + ")");
            ui.display().ifSet(d -> d.resize(renderWidth, renderHeight));
        }

        guiWidth = renderWidth;
        guiHeight = renderHeight;
    }
}
