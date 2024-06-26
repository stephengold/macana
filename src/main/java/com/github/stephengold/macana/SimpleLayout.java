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

import myworld.obsidian.components.layout.Pane;
import myworld.obsidian.geometry.Distance;
import myworld.obsidian.layout.ComponentLayout;
import myworld.obsidian.layout.ItemJustification;
import myworld.obsidian.layout.Layout;
import myworld.obsidian.scene.Component;
import myworld.obsidian.scene.layout.Column;
import myworld.obsidian.scene.layout.Row;

/**
 * A simple window layout for Obsidian.
 *
 * @author Stephen Gold sgold@sonic.net
 */
class SimpleLayout extends Pane {
    // *************************************************************************
    // fields

    /**
     * the Column
     */
    final private Column column;
    /**
     * the Row that contains the Column
     */
    final private Row row;
    // *************************************************************************
    // constructors

    /**
     * Instantiate a layout that starts 10px inward from the upper-left corner
     * and proceeds downward.
     */
    SimpleLayout() {
        layoutOnly.set(true);

        this.row = new Row();
        addChild(row);
        ComponentLayout rowLayout = row.layout();
        rowLayout.clampedSize(Layout.FULL_SIZE, Layout.AUTO);
        rowLayout.justifyContent().set(ItemJustification.FLEX_END);

        this.column = new Column();
        row.addChild(column);

        column.withPadding(Distance.pixels(10f));
        ComponentLayout columnLayout = column.layout();
        columnLayout.clampedSize(Layout.AUTO, Layout.FULL_SIZE);
        columnLayout.justifyContent().set(ItemJustification.FLEX_START);
    }
    // *************************************************************************
    // new methods exposed

    /**
     * Add a component to the Column.
     *
     * @param child the child to add (not null)
     */
    void addToColumn(Component child) {
        column.addChild(child);
    }
}
