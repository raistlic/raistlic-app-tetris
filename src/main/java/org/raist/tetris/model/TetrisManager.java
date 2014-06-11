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

/**
 *
 * @author raistlic
 */
public class TetrisManager {
  
  private TetrisMatrix current;
  private TetrisMatrix hold;
  private TetrisMatrix[] queue;
  private int next;
  
  TetrisManager() {
    
    reset();
  }
  
  final void reset() {
    
    current = new TetrisMatrix();
    hold = null;
    queue = new TetrisMatrix[NEXT_TETRIS_COUNT];
    
    for(int i=0; i<NEXT_TETRIS_COUNT; i++)
      queue[i] = new TetrisMatrix();
    next = 0;
  }
  
  int nextCount() {
    
    return NEXT_TETRIS_COUNT;
  }
  
  TetrisMatrix current() {
    
    return current;
  }
  
  TetrisMatrix hold() {
    
    return hold;
  }
  
  TetrisMatrix next(int index) {
    
    return queue[(index + next) % queue.length];
  }
  
  TetrisMatrix expectedCurrentIfHold() {
    
    return hold == null ? queue[next] : hold;
  }
  
  void doHold() {

    TetrisMatrix temp = hold;
    hold = current;
    current = temp;
    if (current == null)
      doNext();
  }
  
  void doNext() {
    
    current = queue[next];
    queue[next] = new TetrisMatrix();
    next = (next + 1) % queue.length;
  }
  
  void doReplace(TetrisMatrix tetris) {
    
    current = tetris;
  }
  
  static final int NEXT_TETRIS_COUNT = 3;
}
