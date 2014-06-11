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

package org.raist.tetris.controller;

/**
 * 俄罗斯方块这个游戏中，所有可能的按键命令。
 *
 * @author raistlic
 */
public enum TetrisGameCommand implements GameCommand {
  
  Start                   (false, 1), // 当 continuous 是 false 的时候，后面的 interval 没有意义。
  Pause                   (false, 1),
  
  Left                    (true, 1000 / 10),
  Right                   (true, 1000 / 10),
  Down                    (true, 1000 / 10),
  Drop                    (true, 1000 / 5),
  RotateClockwise         (true, 1000 / 3),
  RotateCounterClockwise  (true, 1000 / 3),
  Hold                    (false, 1),
  ;
  
  private final boolean continuous;
  private final long interval;
  
  TetrisGameCommand(boolean continuous, long interval) {
    
    this.continuous = continuous;
    this.interval = interval;
  }

  @Override
  public boolean isContinuous() {
    
    return continuous;
  }

  @Override
  public long getInterval() {
    
    return interval;
  }
}
