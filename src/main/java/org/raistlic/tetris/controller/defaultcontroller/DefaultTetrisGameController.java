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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

/**
 * @author raistlic
 */

class DefaultTetrisGameController implements TetrisGameController {

  private Map<Integer, TetrisGameCommand> map;
  private StopWatch[] watches;
  private Map<TetrisGameCommand, Set<Integer>> board;

  private KeyListener keyListener;

  DefaultTetrisGameController() {

    map = new HashMap<Integer, TetrisGameCommand>();
    watches = new StopWatch[TetrisGameCommand.values().length];
    board = new EnumMap<TetrisGameCommand, Set<Integer>>(TetrisGameCommand.class);
    for(TetrisGameCommand cmd : TetrisGameCommand.values()) {

      StopWatch watch = StopWatchFactory.newNanoWatch();
      watch.setTick(cmd.getInterval() * 1000000L);
      watches[cmd.ordinal()] = watch;
      board.put(cmd, new HashSet<Integer>());
    }

    keyListener = this.new KeyEventHandler();
  }

  @Override
  public void mapCommand(int keyCode, TetrisGameCommand command) {

    map.put(keyCode, command);
  }

  @Override
  public void mapCommandAlternitive(int keyCode, TetrisGameCommand command) {

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

      if( board.get(cmd).isEmpty() )
        continue;

      if( watch.getTick() < 0 ) {

        model.perform(cmd, current);
        watch.setTick(cmd.getInterval() * 1000000L);
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

  private class KeyEventHandler implements KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {

      int keyCode = e.getKeyCode();
      TetrisGameCommand cmd = map.get(keyCode);
      if( cmd == null )
        return;

      Set<Integer> set = board.get(cmd);
      if( set.contains(keyCode) )
        return;

      set.add(keyCode);
      watches[cmd.ordinal()].setTick(-1);
    }

    @Override
    public void keyReleased(KeyEvent e) {

      int keyCode = e.getKeyCode();
      TetrisGameCommand cmd = map.get(keyCode);
      if( cmd != null )
        board.get(cmd).remove(keyCode);
    }
  }
}
