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
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.PlaneCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.objects.PhysicsBody;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Plane;
import com.jme3.math.Vector3f;
import java.util.logging.Level;
import java.util.logging.Logger;
import myworld.obsidian.display.ColorRGBA;
import myworld.obsidian.display.skin.StyleClass;
import myworld.obsidian.events.scene.ButtonEvent;
import myworld.obsidian.geometry.Distance;
import myworld.obsidian.layout.Offsets;
import myworld.obsidian.text.TextStyle;

/**
 * Overlay an Obsidian button onto SPORT graphics.
 *
 * @author Stephen Gold sgold@sonic.net
 */
public class HelloObsidian extends MacanaApp<PhysicsSpace> {
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
        Logger.getLogger("").setLevel(Level.WARNING);
        //setDebuggingEnabled(true);

        HelloObsidian application = new HelloObsidian();
        application.start();
    }
    // *************************************************************************
    // BasePhysicsApp methods

    /**
     * Create the PhysicsSpace. Invoked once during initialization.
     *
     * @return a new instance
     */
    @Override
    protected PhysicsSpace createSpace() {
        PhysicsSpace.BroadphaseType bPhase = PhysicsSpace.BroadphaseType.DBVT;
        return new PhysicsSpace(bPhase);
    }

    /**
     * Callback invoked by SPORT before the main update loop begins.
     */
    @Override
    protected void initialize() {
        setBackgroundColor(Constants.SKY_BLUE);
        cam.setLocation(new Vector3f(0f, 0f, 4f));

        super.initialize();
    }

    /**
     * Add a layout and a button to the Obsidian GUI. Invoked once during
     * initialization.
     */
    @Override
    protected void populateGui() {
        SimpleLayout layout = new SimpleLayout();
        gui.getRoot().addChild(layout);

        StyleClass style = gui.getStyle("ExampleText");

        TextButton restartButton = new TextButton("Restart", style);
        layout.addToColumn(restartButton);

        restartButton.addButtonListener(
                ButtonEvent::isClicked, event -> restartSimulation());
        restartButton
                .setFontFamily("Clear Sans")
                .setFontSize(24f)
                .setFontStyle(TextStyle.BOLD)
                .setLayoutMargin(new Offsets(Distance.pixels(5f)))
                .setTextColor(ColorRGBA.of(0, 170, 0));
        gui.requestFocus(restartButton);
    }

    /**
     * Populate the PhysicsSpace. Invoked once during initialization.
     */
    @Override
    protected void populateSpace() {
        // Add a static horizontal plane at y=-1.
        float planeY = -1f;
        Plane plane = new Plane(Vector3f.UNIT_Y, planeY);
        CollisionShape planeShape = new PlaneCollisionShape(plane);
        float mass = PhysicsBody.massForStatic;
        PhysicsRigidBody floor = new PhysicsRigidBody(planeShape, mass);
        physicsSpace.addCollisionObject(floor);

        // Add a sphere-shaped, dynamic, rigid body at y=1.
        float radius = 0.3f;
        CollisionShape ballShape = new SphereCollisionShape(radius);
        mass = 1f;
        PhysicsRigidBody ball = new PhysicsRigidBody(ballShape, mass);
        physicsSpace.addCollisionObject(ball);
        ball.setPhysicsLocation(new Vector3f(0f, 1f, 0f));

        // Visualize the collision objects.
        visualizeShape(floor);
        visualizeShape(ball);
    }
    // *************************************************************************
    // private methods

    /**
     * Restart the simulation.
     */
    private void restartSimulation() {
        physicsSpace.destroy();
        populateSpace();
    }
}
