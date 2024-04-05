/*
 Copyright (c) 2024 Stephen Gold

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

import com.github.stephengold.sport.blend.BlendOp;
import com.github.stephengold.sport.blend.OverOp;
import com.github.stephengold.sport.input.InputManager;
import com.github.stephengold.sport.input.InputProcessor;
import com.github.stephengold.sport.physics.BasePhysicsApp;
import com.jme3.bullet.PhysicsSpace;
import example.ExampleComponentSkin;
import example.ObsidianContext;
import myworld.obsidian.ObsidianUI;
import myworld.obsidian.display.Colors;
import myworld.obsidian.display.skin.ComponentSkin;
import myworld.obsidian.display.skin.UISkin;
import myworld.obsidian.display.skin.obsidian.ObsidianSkin;
import myworld.obsidian.geometry.Dimension2D;
import myworld.obsidian.input.Key;
import myworld.obsidian.input.MouseButton;
import myworld.obsidian.input.MouseWheelAxis;
import org.lwjgl.glfw.GLFW;

/**
 * Overlay an Obsidian GUI onto SPORT graphics.
 *
 * @author Stephen Gold sgold@sonic.net
 */
abstract class MacanaApp<T extends PhysicsSpace> extends BasePhysicsApp<T> {
    // *************************************************************************
    // constants

    /**
     * blending op for the GUI overlay
     */
    final private static BlendOp guiBlendOp = new OverOp();
    // *************************************************************************
    // fields

    /**
     * temporary storage for GLFW.glfwGetWindowContentScale() results
     */
    final private static float[] xsArray = new float[1];
    final private static float[] ysArray = new float[1];
    /**
     * GUI height
     */
    private static int guiHeight;
    /**
     * temporary storage for GLFW.glfwGetWindowSize() results
     */
    final private static int[] heightArray = new int[1];
    final private static int[] widthArray = new int[1];
    /**
     * GUI width
     */
    private static int guiWidth;
    /**
     * system time as of the previous GUI update (or null if no previous update)
     */
    private static Long lastUpdate;
    /**
     * separate OpenGL context for Obsidian
     */
    private static ObsidianContext context;
    /**
     * Obsidian graphical user-interface layer
     */
    protected static ObsidianUI gui;
    // *************************************************************************
    // constructors

    /**
     * Explicit no-arg constructor to avoid javadoc warnings from JDK 18+.
     */
    protected MacanaApp() {
        // do nothing
    }
    // *************************************************************************
    // new protected methods

    /**
     * Add a components to the Obsidian GUI during initialization.
     */
    abstract protected void populateGui();
    // *************************************************************************
    // BasePhysicsApp methods

    /**
     * Callback invoked by SPORT after the main update loop terminates.
     */
    @Override
    protected void cleanUp() {
        if (context != null) {
            context.close();
        }
        if (gui != null) {
            gui.cleanup();
        }

        super.cleanUp(); // clean up the physics
    }

    /**
     * Callback invoked by SPORT before the main update loop begins.
     */
    @Override
    protected void initialize() {
        updateGuiSurface(); // This creates the GUI.
        gui.clearColor().set(Colors.TRANSPARENT); // default=BLACK

        UISkin skin = ObsidianSkin.create();
        ComponentSkin componentSkin = ExampleComponentSkin.create();
        skin.addComponentSkin(componentSkin);
        gui.useSkin(skin);

        // Create an initialize the Obsidian rendering context:
        context = new ObsidianContext(gui);

        Dimension2D size = new Dimension2D(guiWidth, guiHeight);
        Integer msaaSamples = msaaSamples();
        int msaa = msaaSamples == null ? 0 : msaaSamples;
        InputManager inputManager = getInputManager();
        long windowHandle = inputManager.getGlfwWindowHandle();
        context.init(size, msaa, windowHandle);

        addGuiInput();
        populateGui();

        super.initialize(); // initialize the physics
    }

    /**
     * Callback invoked by SPORT during each iteration of the main update loop.
     */
    @Override
    protected void render() {
        super.render();

        updateGuiSurface();

        long nanoTime = System.nanoTime();
        float seconds
                = (lastUpdate == null) ? 0f : 1e-9f * (nanoTime - lastUpdate);
        lastUpdate = nanoTime;
        gui.update(seconds);

        context.render();

        int textureName = context.getTextureHandle();
        blendTexture(textureName, guiBlendOp);
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
                gui.getInput().fireCharacterEvent(characters);
            }

            @Override
            public void onKeyboard(int glfwKey, boolean isPressed) {
                Key obsidianId = Convert.convertGlfwKey(glfwKey);
                gui.getInput().fireKeyEvent(obsidianId, isPressed);
                /*
                 * BaseApplication has already added processors
                 * for KEY_ESCAPE and KEY_C:
                 */
                super.onKeyboard(glfwKey, isPressed);
            }

            @Override
            public void onMouseButton(int glfwButton, boolean isPressed) {
                MouseButton obsidianId
                        = Convert.convertGlfwMouseButton(glfwButton);
                int x = (int) inputManager.glfwCursorX();
                int y = (int) inputManager.glfwCursorY();
                gui.getInput().fireMouseButtonEvent(obsidianId, isPressed, x, y);
            }

            @Override
            public void onMouseMotion(double rightFraction, double upFraction) {
                int x = (int) inputManager.glfwCursorX();
                int y = (int) inputManager.glfwCursorY();
                gui.getInput().fireMouseMoveEvent(x, y);
            }

            @Override
            public void onScrollMotion(double xScroll, double yScroll) {
                int x = (int) inputManager.glfwCursorX();
                int y = (int) inputManager.glfwCursorY();

                if (xScroll != 0.) {
                    gui.getInput().fireMouseWheelEvent(
                            MouseWheelAxis.HORIZONTAL, x, y, (float) xScroll);
                }

                if (yScroll != 0.) {
                    gui.getInput().fireMouseWheelEvent(
                            MouseWheelAxis.VERTICAL, x, y, (float) yScroll);
                }
            }
        };
        inputManager.add(processor);
    }

    /**
     * Create or resize the GUI surface, as appropriate.
     */
    private static void updateGuiSurface() {
        InputManager inputManager = getInputManager();
        long windowHandle = inputManager.getGlfwWindowHandle();
        GLFW.glfwGetWindowContentScale(windowHandle, xsArray, ysArray);
        GLFW.glfwGetWindowSize(windowHandle, widthArray, heightArray);

        int renderWidth = widthArray[0];
        int renderHeight = (int) (heightArray[0] / ysArray[0] * xsArray[0]);

        if (gui == null) {
            gui = ObsidianUI.createHeadless();
            assert gui != null;

        } else if (renderWidth != guiWidth || renderHeight != guiHeight) {
            Dimension2D size = new Dimension2D(renderWidth, renderHeight);
            context.resize(size);
        }

        guiWidth = renderWidth;
        guiHeight = renderHeight;
    }
}
