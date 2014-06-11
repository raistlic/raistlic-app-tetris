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

import org.raist.tetris.model.entity.GameState;

/**
 * 这个类名真是高端大气上档次有木有。
 *
 * @author raistlic
 */
abstract class Moment {
  
  abstract GameState pass(long current);
  
  static Moment doNothingMoment(GameState state) {
    
    assert state != null;
    
    return new DoNothingMoment(state);
  }
  
  private static class DoNothingMoment extends Moment {
    
    private final GameState state;
    private DoNothingMoment(GameState state) {
      
      this.state = state;
    }

    @Override
    GameState pass(long current) {
      
      return state;
    }
  }
}
