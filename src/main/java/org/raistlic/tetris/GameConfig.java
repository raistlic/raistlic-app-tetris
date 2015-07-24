/*
 *  Copyright 2014 raistlic (raistlic@gmail.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.raistlic.tetris;

import java.awt.event.KeyEvent;

/**
 *
 * @author raistlic
 */
enum GameConfig {

  GameTitle       ("game.title",       "Tetris"),

  ImageBackground ("image.background", "/images/background.jpg"),

  ClassModelFactory        ("class.model.factory",      "org.raistlic.tetris.model.defaultmodel.DefaultTetrisGameModelFactory"),
  ClassControllerFactory   ("class.controller.factory", "org.raistlic.tetris.controller.defaultcontroller.DefaultTetrisGameControllerFactory"),
  ClassViewFactory         ("class.view.factory",       "org.raistlic.tetris.view.defaultview.DefaultTetrisGameViewFactory"),

  FPS  ("fps", "100"),

  KeyCodeLeft                    ("keycode.left",                    String.valueOf(KeyEvent.VK_A)),
  KeyCodeRight                   ("keycode.right",                   String.valueOf(KeyEvent.VK_D)),
  KeyCodeDown                    ("keycode.down",                    String.valueOf(KeyEvent.VK_S)),
  KeyCodeDrop                    ("keycode.drop",                    String.valueOf(KeyEvent.VK_SPACE)),
  KeyCodeStart                   ("keycode.start",                   String.valueOf(KeyEvent.VK_F1)),
  KeyCodePause                   ("keycode.pause",                   String.valueOf(KeyEvent.VK_W)),
  KeyCodeRotateClockwise         ("keycode.rotate.clockwise",        String.valueOf(KeyEvent.VK_H)),
  KeyCodeRotateCounterClockwise  ("keycode.rotate.counterclockwise", String.valueOf(KeyEvent.VK_J)),
  KeyCodeHold                    ("keycode.hold",                    String.valueOf(KeyEvent.VK_Q)),
  ;

  final String key;
  final String defaultValue;

  GameConfig(String key, String defaultValue) {

    this.key = key;
    this.defaultValue = defaultValue;
  }
}
