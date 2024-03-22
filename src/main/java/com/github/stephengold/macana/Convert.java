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

import myworld.obsidian.input.Key;
import myworld.obsidian.input.MouseButton;
import org.lwjgl.glfw.GLFW;

/**
 * Utility methods for the Macana project.
 *
 * @author Stephen Gold sgold@sonic.net
 */
final class Convert {
    // *************************************************************************
    // constructors

    /**
     * A private constructor to inhibit instantiation of this class.
     */
    private Convert() {
    }
    // *************************************************************************
    // new methods exposed

    /**
     * TODO The Obsidian library should provide this method.
     *
     * @param glfwKey a GLFW key code
     * @return the corresponding Obsidian key
     */
    static Key convertGlfwKey(int glfwKey) {
        Key result = switch (glfwKey) {
            case GLFW.GLFW_KEY_0 -> Key.KEY_0;
            case GLFW.GLFW_KEY_1 -> Key.KEY_1;
            case GLFW.GLFW_KEY_2 -> Key.KEY_2;
            case GLFW.GLFW_KEY_3 -> Key.KEY_3;
            case GLFW.GLFW_KEY_4 -> Key.KEY_4;
            case GLFW.GLFW_KEY_5 -> Key.KEY_5;
            case GLFW.GLFW_KEY_6 -> Key.KEY_6;
            case GLFW.GLFW_KEY_7 -> Key.KEY_7;
            case GLFW.GLFW_KEY_8 -> Key.KEY_8;
            case GLFW.GLFW_KEY_9 -> Key.KEY_9;

            case GLFW.GLFW_KEY_A -> Key.KEY_A;
            case GLFW.GLFW_KEY_APOSTROPHE -> Key.APOSTROPHE;
            case GLFW.GLFW_KEY_B -> Key.KEY_B;
            case GLFW.GLFW_KEY_BACKSLASH -> Key.BACKSLASH;
            case GLFW.GLFW_KEY_BACKSPACE -> Key.BACKSPACE;
            case GLFW.GLFW_KEY_C -> Key.KEY_C;
            case GLFW.GLFW_KEY_CAPS_LOCK -> Key.CAPS_LOCK;
            case GLFW.GLFW_KEY_COMMA -> Key.COMMA;
            case GLFW.GLFW_KEY_D -> Key.KEY_D;
            case GLFW.GLFW_KEY_DELETE -> Key.DELETE;
            case GLFW.GLFW_KEY_DOWN -> Key.DOWN;
            case GLFW.GLFW_KEY_E -> Key.KEY_E;
            case GLFW.GLFW_KEY_EQUAL -> Key.EQUAL;
            case GLFW.GLFW_KEY_END -> Key.END;
            case GLFW.GLFW_KEY_ENTER -> Key.ENTER;
            case GLFW.GLFW_KEY_ESCAPE -> Key.ESCAPE;
            case GLFW.GLFW_KEY_F -> Key.KEY_F;

            case GLFW.GLFW_KEY_F1 -> Key.F1;
            case GLFW.GLFW_KEY_F10 -> Key.F10;
            case GLFW.GLFW_KEY_F11 -> Key.F11;
            case GLFW.GLFW_KEY_F12 -> Key.F12;
            case GLFW.GLFW_KEY_F13 -> Key.F13;
            case GLFW.GLFW_KEY_F14 -> Key.F14;
            case GLFW.GLFW_KEY_F15 -> Key.F15;
            case GLFW.GLFW_KEY_F16 -> Key.F16;
            case GLFW.GLFW_KEY_F17 -> Key.F17;
            case GLFW.GLFW_KEY_F18 -> Key.F18;
            case GLFW.GLFW_KEY_F19 -> Key.F19;
            case GLFW.GLFW_KEY_F2 -> Key.F2;
            case GLFW.GLFW_KEY_F20 -> Key.F20;
            case GLFW.GLFW_KEY_F21 -> Key.F21;
            case GLFW.GLFW_KEY_F22 -> Key.F22;
            case GLFW.GLFW_KEY_F23 -> Key.F23;
            case GLFW.GLFW_KEY_F24 -> Key.F24;
            case GLFW.GLFW_KEY_F25 -> Key.F25;
            case GLFW.GLFW_KEY_F3 -> Key.F3;
            case GLFW.GLFW_KEY_F4 -> Key.F4;
            case GLFW.GLFW_KEY_F5 -> Key.F5;
            case GLFW.GLFW_KEY_F6 -> Key.F6;
            case GLFW.GLFW_KEY_F7 -> Key.F7;
            case GLFW.GLFW_KEY_F8 -> Key.F8;
            case GLFW.GLFW_KEY_F9 -> Key.F9;

            case GLFW.GLFW_KEY_G -> Key.KEY_G;
            case GLFW.GLFW_KEY_GRAVE_ACCENT -> Key.GRAVE_ACCENT;
            case GLFW.GLFW_KEY_H -> Key.KEY_H;
            case GLFW.GLFW_KEY_HOME -> Key.HOME;
            case GLFW.GLFW_KEY_I -> Key.KEY_I;
            case GLFW.GLFW_KEY_INSERT -> Key.INSERT;
            case GLFW.GLFW_KEY_J -> Key.KEY_J;
            case GLFW.GLFW_KEY_K -> Key.KEY_K;

            case GLFW.GLFW_KEY_KP_0 -> Key.KP_0;
            case GLFW.GLFW_KEY_KP_1 -> Key.KP_1;
            case GLFW.GLFW_KEY_KP_2 -> Key.KP_2;
            case GLFW.GLFW_KEY_KP_3 -> Key.KP_3;
            case GLFW.GLFW_KEY_KP_4 -> Key.KP_4;
            case GLFW.GLFW_KEY_KP_5 -> Key.KP_5;
            case GLFW.GLFW_KEY_KP_6 -> Key.KP_6;
            case GLFW.GLFW_KEY_KP_7 -> Key.KP_7;
            case GLFW.GLFW_KEY_KP_8 -> Key.KP_8;
            case GLFW.GLFW_KEY_KP_9 -> Key.KP_9;
            case GLFW.GLFW_KEY_KP_ADD -> Key.KP_ADD;
            case GLFW.GLFW_KEY_KP_DECIMAL -> Key.KP_DECIMAL;
            case GLFW.GLFW_KEY_KP_DIVIDE -> Key.KP_DIVIDE;
            case GLFW.GLFW_KEY_KP_ENTER -> Key.KP_ENTER;
            case GLFW.GLFW_KEY_KP_EQUAL -> Key.KP_EQUAL;
            case GLFW.GLFW_KEY_KP_MULTIPLY -> Key.KP_MULTIPLY;
            case GLFW.GLFW_KEY_KP_SUBTRACT -> Key.KP_SUBTRACT;

            case GLFW.GLFW_KEY_L -> Key.KEY_L;

            case GLFW.GLFW_KEY_LEFT -> Key.LEFT;
            case GLFW.GLFW_KEY_LEFT_ALT -> Key.LEFT_ALT;
            case GLFW.GLFW_KEY_LEFT_BRACKET -> Key.LEFT_BRACKET;
            case GLFW.GLFW_KEY_LEFT_CONTROL -> Key.LEFT_CONTROL;
            case GLFW.GLFW_KEY_LEFT_SHIFT -> Key.LEFT_SHIFT;
            case GLFW.GLFW_KEY_LEFT_SUPER -> Key.LEFT_META;

            case GLFW.GLFW_KEY_M -> Key.KEY_M;
            case GLFW.GLFW_KEY_MENU -> Key.MENU;
            case GLFW.GLFW_KEY_MINUS -> Key.MINUS;
            case GLFW.GLFW_KEY_N -> Key.KEY_N;
            case GLFW.GLFW_KEY_NUM_LOCK -> Key.NUM_LOCK;
            case GLFW.GLFW_KEY_O -> Key.KEY_O;
            case GLFW.GLFW_KEY_P -> Key.KEY_P;
            case GLFW.GLFW_KEY_PAGE_DOWN -> Key.PAGE_DOWN;
            case GLFW.GLFW_KEY_PAGE_UP -> Key.PAGE_UP;
            case GLFW.GLFW_KEY_PAUSE -> Key.PAUSE;
            case GLFW.GLFW_KEY_PERIOD -> Key.PERIOD;
            case GLFW.GLFW_KEY_PRINT_SCREEN -> Key.PRINT_SCREEN;
            case GLFW.GLFW_KEY_Q -> Key.KEY_Q;
            case GLFW.GLFW_KEY_R -> Key.KEY_R;

            case GLFW.GLFW_KEY_RIGHT -> Key.RIGHT;
            case GLFW.GLFW_KEY_RIGHT_ALT -> Key.RIGHT_ALT;
            case GLFW.GLFW_KEY_RIGHT_BRACKET -> Key.RIGHT_BRACKET;
            case GLFW.GLFW_KEY_RIGHT_CONTROL -> Key.RIGHT_CONTROL;
            case GLFW.GLFW_KEY_RIGHT_SHIFT -> Key.RIGHT_SHIFT;
            case GLFW.GLFW_KEY_RIGHT_SUPER -> Key.RIGHT_META;

            case GLFW.GLFW_KEY_S -> Key.KEY_S;
            case GLFW.GLFW_KEY_SCROLL_LOCK -> Key.SCROLL_LOCK;
            case GLFW.GLFW_KEY_SEMICOLON -> Key.SEMICOLON;
            case GLFW.GLFW_KEY_SLASH -> Key.SLASH;
            case GLFW.GLFW_KEY_SPACE -> Key.SPACE;
            case GLFW.GLFW_KEY_T -> Key.KEY_T;
            case GLFW.GLFW_KEY_TAB -> Key.TAB;
            case GLFW.GLFW_KEY_U -> Key.KEY_U;
            case GLFW.GLFW_KEY_UP -> Key.UP;
            case GLFW.GLFW_KEY_V -> Key.KEY_V;
            case GLFW.GLFW_KEY_W -> Key.KEY_W;
            case GLFW.GLFW_KEY_WORLD_1 -> Key.WORLD_1;
            case GLFW.GLFW_KEY_WORLD_2 -> Key.WORLD_2;
            case GLFW.GLFW_KEY_X -> Key.KEY_X;
            case GLFW.GLFW_KEY_Y -> Key.KEY_Y;
            case GLFW.GLFW_KEY_Z -> Key.KEY_Z;

            default -> Key.UNKNOWN;
        };

        return result;
    }

    /**
     * TODO The Obsidian library should provide this method.
     *
     * @param glfwButton a GLFW mouse button code
     * @return the corresponding Obsidian mouse button
     */
    static MouseButton convertGlfwMouseButton(int glfwButton) {
        MouseButton result = switch (glfwButton) {
            case GLFW.GLFW_MOUSE_BUTTON_LEFT -> MouseButton.PRIMARY;
            case GLFW.GLFW_MOUSE_BUTTON_MIDDLE -> MouseButton.MIDDLE;
            case GLFW.GLFW_MOUSE_BUTTON_RIGHT -> MouseButton.SECONDARY;

            default -> MouseButton.NONE;
        };

        return result;
    }
}
