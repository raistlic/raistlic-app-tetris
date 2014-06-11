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

package org.raist.tetris.model.defaultmodel;

import org.raist.tetris.model.Matrix;
import org.raist.tetris.model.entity.Block;
import org.raist.tetris.model.entity.BlockState;
import org.raist.tetris.model.entity.BlockType;

/**
 *
 * @author raistlic
 */
enum EmptyTetrisMatrix implements Matrix<Block> {

  INSTANCE;
  
  private final Block empty = new Block(BlockType.Empty, BlockState.Relic, 0f);
  private final Block query = new Block(BlockType.Empty, BlockState.Relic, 0f);

  @Override
  public int getWidth() {
    
    return 4;
  }

  @Override
  public int getHeight() {
    
    return 4;
  }

  @Override
  public Block get(int x, int y) {
    
    query.populate(empty);
    return query;
  }
}
