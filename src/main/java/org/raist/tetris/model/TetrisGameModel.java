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

package org.raist.tetris.model;

import org.raist.tetris.controller.TetrisGameCommand;
import org.raist.tetris.model.entity.Block;
import org.raist.tetris.model.entity.GameState;

/**
 * 除了一些附加的游戏信息，这个model基本就成了。
 * 
 * 附加信息包括当前等级，分数，速度，fps 等等等等。等会再说。
 *
 * @author raistlic
 */
public interface TetrisGameModel {
  
  /*---------------------------------------------------------------------------
   * queries
   ---------------------------------------------------------------------------*/
  
  public Matrix<Block> getGameArea();
  
  public int getNextTetrisCount();
  
  public Matrix<Block> getNextTetris(int index);
  
  public Matrix<Block> getCurrentTetris();
  
  public Matrix<Block> getHoldTetris();
  
  public GameState getGameState();
  
  public TetrisGameInfor getGameInfor();
  
  /*---------------------------------------------------------------------------
   * modifications
   ---------------------------------------------------------------------------*/
  
  public void perform(TetrisGameCommand command, long current); // current 是纳秒
  
  public void tick(long current);
  
  public void refreshFPS(long current);
}
