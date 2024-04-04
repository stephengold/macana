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

import myworld.obsidian.components.Button;
import myworld.obsidian.components.text.TextDisplay;
import myworld.obsidian.display.ColorRGBA;
import myworld.obsidian.display.skin.StyleClass;
import myworld.obsidian.layout.ComponentLayout;
import myworld.obsidian.layout.Offsets;
import myworld.obsidian.properties.ValueProperty;
import myworld.obsidian.text.TextStyle;

/**
 * An Obsidian Button that displays only text.
 *
 * @author Stephen Gold sgold@sonic.net
 */
class TextButton extends Button {
    // *************************************************************************
    // fields

    /**
     * child component that displays text
     */
    final private TextDisplay textDisplay;
    // *************************************************************************
    // constructors

    /**
     * Instantiate a Button that displays text.
     *
     * @param string the text to display
     * @param style style information (may be null)
     */
    TextButton(String string, StyleClass style) {
        super();

        this.textDisplay = new TextDisplay(string, style);
        super.addChildren(textDisplay);
    }
    // *************************************************************************
    // new methods exposed

    /**
     * Alter the font family of the displayed text.
     *
     * @param size the name of the desired font family
     * @return the (modified) button (for chaining)
     */
    TextButton setFontFamily(String name) {
        ValueProperty<String> property = textDisplay.fontFamily();
        property.set(name);

        return this;
    }

    /**
     * Alter the font size of the displayed text.
     *
     * @param size the desired size
     * @return the (modified) button (for chaining)
     */
    TextButton setFontSize(float size) {
        ValueProperty<Float> property = textDisplay.fontSize();
        property.set(size);

        return this;
    }

    /**
     * Alter the font style of the displayed text.
     *
     * @param style the desired style
     * @return the (modified) button (for chaining)
     */
    TextButton setFontStyle(TextStyle style) {
        ValueProperty<TextStyle> property = textDisplay.fontStyle();
        property.set(style);

        return this;
    }

    /**
     * Alter the internal margin offsets.
     *
     * @param offsets the desired offsets
     * @return the (modified) button (for chaining)
     */
    TextButton setLayoutMargin(Offsets offsets) {
        ComponentLayout textLayout = textDisplay.layout();
        ValueProperty<Offsets> property = textLayout.margin();
        property.set(offsets);

        return this;
    }

    /**
     * Alter the displayed text.
     *
     * @param text the text to display
     * @return the (modified) button (for chaining)
     */
    TextButton setText(String text) {
        ValueProperty<String> property = textDisplay.text();
        property.set(text);

        return this;
    }

    /**
     * Alter the color of the displayed text.
     *
     * @param color the desired color
     * @return the (modified) button (for chaining)
     */
    TextButton setTextColor(ColorRGBA color) {
        ValueProperty<ColorRGBA> property = textDisplay.color();
        property.set(color);

        return this;
    }

    /**
     * Access the internal TextDisplay.
     *
     * @return the pre-existing instance (not null)
     */
    TextDisplay textDisplay() {
        return textDisplay;
    }
}
