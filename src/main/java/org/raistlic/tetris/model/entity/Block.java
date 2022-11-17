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

package org.raistlic.tetris.model.entity;

/**
 * 方块。
 *
 * @author raistlic
 */
public class Block {
  
  private static long IDSEQUENCE = 0;
  
  private BlockType type;

  private BlockState state;

  private float animationAlpha;

  private long id;
  
  public Block(BlockType type, BlockState state, float animationAlpha) {
    
    assert type != null;
    assert state != null;
    assert animationAlpha >= 0;
    assert animationAlpha <= 1;

    this.type = type;
    this.state = state;
    this.animationAlpha = animationAlpha;
    this.id = IDSEQUENCE++;
  }

  public BlockType getType() {
    
    return type;
  }

  public void setType(BlockType type) {
    
    this.type = type;
  }

  public BlockState getState() {
    
    return state;
  }

  public void setState(BlockState state) {
    
    this.state = state;
  }

  public float getAnimationAlpha() {
    
    return animationAlpha;
  }

  public void setAnimationAlpha(float animationAlpha) {
    
    this.animationAlpha = animationAlpha;
  }
  
  public long id() { return id; }
  
  public void populate(Block block) {
    
    assert block != null;
    
    this.type = block.type;
    this.state = block.state;
    this.animationAlpha = block.animationAlpha;
    this.id = block.id;
  }
  
  @Override
  public String toString() {
    
    return getClass().getSimpleName() + 
            "[" + type + ", " + state + ", " + id + ", " + animationAlpha + "]";
  }
}
