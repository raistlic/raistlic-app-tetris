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

/**
 *
 * @author raistlic
 */
abstract class TetrisGameSetting {
  
  static TetrisGameSetting newInstance() {
    
    return new DefaultTetrisGameSetting(new long[][] {
      
      new long[] {0,    1922 * 1000000L},
      new long[] {10,   1281 * 1000000L},
      new long[] {20,    854 * 1000000L},
      new long[] {50,    569 * 1000000L},
      new long[] {100,   379 * 1000000L},
      new long[] {200,   253 * 1000000L},
      new long[] {500,   168 * 1000000L},
      new long[] {1000,  112 * 1000000L},
      new long[] {2000,   75 * 1000000L},
      new long[] {5000,   50 * 1000000L},
    });
  }
  
  abstract void addScore(int score);
  
  abstract int getScore();
  
  abstract long getDownInterval();
  
  abstract int getLevel();
  
  abstract int getMaxLevel();
  
  abstract void reset();
  
  private static class DefaultTetrisGameSetting extends TetrisGameSetting {
    
    private long[][] levels;
    private int score;
    private int level;
    private DefaultTetrisGameSetting(long[][] levels) {
      
      this.levels = new long[levels.length][];
      for(int i=0; i<levels.length; i++)
        this.levels[i] = (long[])levels[i].clone();
    }
    
    @Override
    final void reset() {
      
      this.score = 0;
      this.level = 0;
    }
    
    @Override
    void addScore(int score) {
      
      this.score += score;
      while( level < levels.length - 1 ) {
        
        if( this.score >= levels[level + 1][0] ) {
          
          level ++;
        }
        else break;
      } 
    }
    
    @Override
    int getScore() {
      
      return score;
    }

    @Override
    long getDownInterval() {
      
      return levels[level][1];
    }

    @Override
    int getLevel() {
      
      return level + 1;
    }

    @Override
    int getMaxLevel() {
      
      return levels.length;
    }
  }
}
