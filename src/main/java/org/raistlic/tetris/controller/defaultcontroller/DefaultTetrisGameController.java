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

package org.raistlic.tetris.controller.defaultcontroller;

import org.raistlic.common.stopwatch.StopWatch;
import org.raistlic.common.stopwatch.StopWatchFactory;
import org.raistlic.tetris.controller.TetrisGameCommand;
import org.raistlic.tetris.controller.TetrisGameController;
import org.raistlic.tetris.model.TetrisGameModel;

import javax.swing.JComponent;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author raistlic
 */

class DefaultTetrisGameController implements TetrisGameController {

  private static final long ACTIVE_TICK = 123L;

  private final Map<Integer, TetrisGameCommand> map;

  private final StopWatch[] watches;

  private final Map<TetrisGameCommand, Set<Integer>> board;

  private final KeyListener keyListener;

  DefaultTetrisGameController() {

    map = new HashMap<>();
    watches = new StopWatch[TetrisGameCommand.values().length];
    board = new EnumMap<>(TetrisGameCommand.class);
    for(TetrisGameCommand cmd : TetrisGameCommand.values()) {

      StopWatch watch = StopWatchFactory.createStopWatch(cmd.getIntervalMillis(), TimeUnit.MILLISECONDS);
      watches[cmd.ordinal()] = watch;
      board.put(cmd, new HashSet<>());
    }

    keyListener = this.new KeyEventHandler();
  }

  @Override
  public void mapCommand(int keyCode, TetrisGameCommand command) {

    map.put(keyCode, command);
  }

  @Override
  public void mapCommandAlternative(int keyCode, TetrisGameCommand command) {

    map.put(keyCode, command);
  }

  @Override
  public KeyListener getKeyListener() {

    return keyListener;
  }

  @Override
  public void tick(TetrisGameModel model, long current) {

    for(TetrisGameCommand cmd : TetrisGameCommand.values()) {

      int index = cmd.ordinal();
      StopWatch watch = watches[index];

      if( board.get(cmd).isEmpty() ) {
        continue;
      }

      if( watch.getTick() == ACTIVE_TICK ) {

        model.perform(cmd, current);
        watch.setTick(cmd.getIntervalMillis() * 1000000L);
        watch.reset(current);
        continue;
      }

      if( cmd.isContinuous() && watch.expired(current) ) {

        model.perform(cmd, current);
        watch.reset(current);
      }
    }
    model.tick(current);
  }

//  @Override
//  public void accept(JComponent source) {
//
//    InputMap inputMap = source.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//    ActionMap actionMap = source.getActionMap();
//    map.keySet().forEach((Integer keyCode) -> {
//      inputMap.put(KeyStroke.getKeyStroke(keyCode, 0), "action" + keyCode);
//      actionMap.put("action" + keyCode, new AbstractAction() {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//
//          keyPressed(keyCode);
//        }
//      });
//    });
//  }
  
  @Override
  public void accept(JComponent source) {

    System.out.println("installing key dispatcher ... ");
    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEvent -> {
        if ((keyEvent.getID() & KeyEvent.KEY_PRESSED) != 0) {
          keyPressed(keyEvent.getKeyCode());
        } else if ((keyEvent.getID() & KeyEvent.KEY_RELEASED) != 0) {
          keyReleased(keyEvent.getKeyCode());
        }
        return false;
    });

//    Keymap keymap = JTextComponent.addKeymap("tetrisComponent", null);
//
//    map.keySet().forEach((Integer keyCode) -> 
//        keymap.addActionForKeyStroke(KeyStroke.getKeyStroke(keyCode, 0), new AbstractAction() {
//      @Override
//      public void actionPerformed(ActionEvent e) {
//
//        keyPressed(keyCode);
//      }
//    }));
//    ((JTextComponent) source).setKeymap(keymap);
  }
  
  private void keyPressed(int keyCode) {
    
    TetrisGameCommand cmd = map.get(keyCode);
    if( cmd == null )
      return;

    Set<Integer> set = board.get(cmd);
    if( set.contains(keyCode) )
      return;

    set.add(keyCode);
    watches[cmd.ordinal()].setTick(ACTIVE_TICK);
  }
  
  private void keyReleased(int keyCode) {
    TetrisGameCommand cmd = map.get(keyCode);
    if( cmd != null )
      board.get(cmd).remove(keyCode);
  }

  private class KeyEventHandler implements KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {

      DefaultTetrisGameController.this.keyPressed(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {

      DefaultTetrisGameController.this.keyReleased(e.getKeyCode());
    }
  }
}
