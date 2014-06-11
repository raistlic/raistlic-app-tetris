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
 * 矩阵。只读。
 * 
 * 矩阵本身未必是只读，不过这里定义的接口是只读，换句话说，这个接口定义一个矩阵的“只读”的 aspect。
 *
 * @author raistlic
 */
public interface Matrix<E> {
  
  /**
   * 矩阵有宽。
   * 
   * @return 
   */
  public int getWidth();
  
  /**
   * 矩阵有高。
   * 
   * @return 
   */
  public int getHeight();
  
  /**
   * 给定一个坐标，可以查询这个位置的值。
   * 
   * 值，可以是任何类型。
   * 
   * @param x
   * @param y
   * @return 
   */
  public E get(int x, int y);
}
