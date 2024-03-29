/*
 *  Copyright 2013 raistlic (raistlic@gmail.com)
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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

import org.raistlic.tetris.controller.TetrisGameController;
import org.raistlic.tetris.model.TetrisGameModel;
import org.raistlic.common.stopwatch.StopWatch;
import org.raistlic.common.stopwatch.StopWatchFactory;

/**
 *
 * @author raistlic
 */
class GameTick implements ActionListener {
  
  private final TetrisGameModel model;

  private final TetrisGameController control;

  private final Component view;
  
  private final StopWatch watchRepaint;
  
  GameTick(TetrisGameModel model, Component view, TetrisGameController control, int fps) {
    
    this.model = model;
    this.view = view;
    this.control = control;
    this.watchRepaint = StopWatchFactory.createStopWatch(1000 * 1000 * 1000L / fps, TimeUnit.NANOSECONDS);
    this.watchRepaint.resume(System.nanoTime());
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    
    long current = System.nanoTime();
    
    control.tick(model, current);
    
    long read = watchRepaint.readElapsed(current);
    
    int expired = (int)(read / watchRepaint.getTick());
    
    if( expired > 0 ) {
      
      if( expired > 6 )
        watchRepaint.reset(current);
      else
        watchRepaint.tickForward(current);
      
      model.refreshFPS(current);
      view.repaint();
    }
  }
}
