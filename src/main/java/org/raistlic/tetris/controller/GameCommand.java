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

package org.raistlic.tetris.controller;

/**
 *
 * @author raistlic
 */
public interface GameCommand {
  
  /**
   * 这个 command 是不是持续性的，就是说，按键一直按下的时候，它是不是一直重复
   */
  public boolean isContinuous();
  
  /**
   * 两次重复之间的间隔，比如频率是 10次每秒，那么 interval 是 1000 / 10 毫秒。
   */
  public long getInterval(); // milli seconds
}
