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

package org.raist.tetris.model.entity;

/**
 * 方块的类型。
 *
 * @author raistlic
 */
public enum BlockType {
  
  O,
  I,
  T,
  S,
  Z,
  L,
  J, // 共有 7 种方块，对 i 《= 0 ~ 6， BlockType.values()[i] 为俄罗斯方块的方块类型。
  Empty, // 用 Empty 而不用 null，单纯为了更好的可读性。
  ;
}
