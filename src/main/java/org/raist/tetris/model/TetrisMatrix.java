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

import org.raist.tetris.model.entity.Block;
import org.raist.tetris.model.entity.BlockState;

/**
 *
 * @author raistlic
 */
class TetrisMatrix implements Matrix<Block> {
  
  private Tetris tetris;
  private Block block;
  
  TetrisMatrix() {
    
    tetris = Tetris.getRandomInstance();
    block = new Block(tetris.getBlockType(), BlockState.Active, 1f);
  }
  
  TetrisMatrix rotateClockwise() {
    
    TetrisMatrix result = new TetrisMatrix();
    result.tetris = this.tetris.rotateClockwise();
    result.block.populate(this.block);
    return result;
  }
  
  TetrisMatrix rotateCounterClockwise() {
    
    TetrisMatrix result = new TetrisMatrix();
    result.tetris = this.tetris.rotateCounterClockwise();
    result.block.populate(this.block);
    return result;
  }

  @Override
  public int getWidth() {
    
    return tetris.getWidth();
  }

  @Override
  public int getHeight() {
    
    return tetris.getHeight();
  }

  @Override
  public Block get(int x, int y) {
    
    block.setType(tetris.get(x, y));
    return block;
  }
}
