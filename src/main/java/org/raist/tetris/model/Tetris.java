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

import java.util.Random;

import org.raist.tetris.model.entity.BlockType;

/**
 *
 * @author raistlic
 */
public final class Tetris implements Matrix<BlockType> {
  
  public static Tetris getInstance(BlockType type, int orientation) {
    
    if( type == null )
      throw new NullPointerException("BlockType is null.");
    
    if( type.ordinal() >= POOL.length )
      throw new IllegalArgumentException("Invalid BlockType: " + type);
    
    while(orientation < 0)
      orientation += 4;
    return POOL[type.ordinal()][orientation % 4];
  }
  
  public static Tetris getRandomInstance() {
    
    int index1 = RAND.nextInt(7);
    int index2 = RAND.nextInt(4);
    return POOL[index1][index2];
  }
  
  private final BlockType type;
  private final int map; // 一个 int 怎么保存一个 4 x 4 的地图？
  
  /*
   * 比如 L ：
   * 
   * 0 1 0 0
   * 0 1 0 0
   * 0 1 1 0
   * 0 0 0 0
   * 
   * 总共使用 16 个 bit 就够了， int 32 个bit，不算符号位还有31 个 bit，足够了。
   */
  
  private final int orientation; // 这个成员，更多是表示一个“序号”
  
  /*
   * 因为俄罗斯方块可以转动，同样是 L， 转动可以产生 4 种情况：
   * 
   * 0100 0000 0110 0000
   * 0100 0010 0010 1110
   * 0110 1110 0010 1000
   * 0000 0000 0000 0000
   * 
   * 0    1    2    3       <- orientation
   */
  
  /**
   * private 的 constructor，这里是为了做实例控制。
   * 
   * 实例控制让 immutable 变得更有意义。
   */
  private Tetris(BlockType type, int map, int orientation) {
    
    this.type = type;
    this.map = map;
    this.orientation = orientation;
  }
  
  public Tetris rotateClockwise() {
    
    return getInstance(type, orientation + 1);
  }
  
  public BlockType getBlockType() {
    
    return type;
  }
  
  public Tetris rotateCounterClockwise() {
    
    return getInstance(type, orientation - 1);
  }
  
  /*
   * 宽高这里都是常量。
   */
  @Override
  public int getWidth() {
    
    return WIDTH;
  }

  @Override
  public int getHeight() {
    
    return HEIGHT;
  }

  @Override
  public BlockType get(int x, int y) {
    
    int mask = 1 << (y * WIDTH  + x);
    return (map & mask) == 0 ? BlockType.Empty : type;
  }
  
  private static final int WIDTH = 4;
  private static final int HEIGHT = 4;
  
  // 实例控制，需要一个常量池。
  
  private static Tetris[][] POOL;
  
  static {
    
    POOL = new Tetris[7][4]; // 4 ， 上下左右 共有四个方向。
    
    // O
    POOL[BlockType.O.ordinal()] = new Tetris[] {
      
      /*
       *x 0123 0123 0123 0123
       *  0000 0000 0000 0000  <- y == 0
       *  0110 0110 0110 0110          1
       *  0110 0110 0110 0110          2
       *  0000 0000 0000 0000          3
       */
      
      new Tetris(BlockType.O, Integer.parseInt("0000011001100000", 2), 0),
      new Tetris(BlockType.O, Integer.parseInt("0000011001100000", 2), 1),
      new Tetris(BlockType.O, Integer.parseInt("0000011001100000", 2), 2),
      new Tetris(BlockType.O, Integer.parseInt("0000011001100000", 2), 3),
    };
    
    POOL[BlockType.I.ordinal()] = new Tetris[] {
      
      /*
       *x 0123 0123 0123 0123
       *  0100 0000 0100 0000  <- y == 0
       *  0100 1111 0100 1111          1
       *  0100 0000 0100 0000          2
       *  0100 0000 0100 0000          3
       */
      
      new Tetris(BlockType.I, Integer.parseInt("0100010001000100", 2), 0),
      new Tetris(BlockType.I, Integer.parseInt("0000000011110000", 2), 1),
      new Tetris(BlockType.I, Integer.parseInt("0100010001000100", 2), 2),
      new Tetris(BlockType.I, Integer.parseInt("0000000011110000", 2), 3),
    };
    
    POOL[BlockType.T.ordinal()] = new Tetris[] {
      
      /*
       *x 0123 0123 0123 0123
       *  0100 0100 0000 0100  <- y == 0
       *  1110 0110 1110 1100          1
       *  0000 0100 0100 0100          2
       *  0000 0000 0000 0000          3
       */
      
      new Tetris(BlockType.T, Integer.parseInt("0000000001110010", 2), 0),
      new Tetris(BlockType.T, Integer.parseInt("0000001001100010", 2), 1),
      new Tetris(BlockType.T, Integer.parseInt("0000001001110000", 2), 2),
      new Tetris(BlockType.T, Integer.parseInt("0000001000110010", 2), 3),
    };
    
    POOL[BlockType.J.ordinal()] = new Tetris[] {
      
      /*
       *x 0123 0123 0123 0123
       *  0010 1000 0110 0000  <- y == 0
       *  0010 1110 0100 1110          1
       *  0110 0000 0100 0010          2
       *  0000 0000 0000 0000          3
       */
      
      new Tetris(BlockType.J, Integer.parseInt("0000011001000100", 2), 0),
      new Tetris(BlockType.J, Integer.parseInt("0000000001110001", 2), 1),
      new Tetris(BlockType.J, Integer.parseInt("0000001000100110", 2), 2),
      new Tetris(BlockType.J, Integer.parseInt("0000010001110000", 2), 3),
    };
    
    POOL[BlockType.L.ordinal()] = new Tetris[] {
      
      /*
       *x 0123 0123 0123 0123
       *  0100 0010 0110 0000  <- y == 0
       *  0100 1110 0010 1110          1
       *  0110 0000 0010 1000          2
       *  0000 0000 0000 0000          3
       */
      
      new Tetris(BlockType.L, Integer.parseInt("0000011000100010", 2), 0),
      new Tetris(BlockType.L, Integer.parseInt("0000000001110100", 2), 1),
      new Tetris(BlockType.L, Integer.parseInt("0000010001000110", 2), 2),
      new Tetris(BlockType.L, Integer.parseInt("0000000101110000", 2), 3),
    };
    
    POOL[BlockType.S.ordinal()] = new Tetris[] {
      
      /*
       *x 0123 0123 0123 0123
       *  0100 0110 0100 0110  <- y == 0
       *  0110 1100 0110 1100          1
       *  0010 0000 0010 0000          2
       *  0000 0000 0000 0000          3
       */
      
      new Tetris(BlockType.S, Integer.parseInt("0000010001100010", 2), 0),
      new Tetris(BlockType.S, Integer.parseInt("0000000000110110", 2), 1),
      new Tetris(BlockType.S, Integer.parseInt("0000010001100010", 2), 2),
      new Tetris(BlockType.S, Integer.parseInt("0000000000110110", 2), 3),
    };
    
    POOL[BlockType.Z.ordinal()] = new Tetris[] {
      
      /*
       *x 0123 0123 0123 0123
       *  0010 1100 0010 1100  <- y == 0
       *  0110 0110 0110 0110          1
       *  0100 0000 0100 0000          2
       *  0000 0000 0000 0000          3
       */
      
      new Tetris(BlockType.Z, Integer.parseInt("0000001001100100", 2), 0),
      new Tetris(BlockType.Z, Integer.parseInt("0000000001100011", 2), 1),
      new Tetris(BlockType.Z, Integer.parseInt("0000001001100100", 2), 2),
      new Tetris(BlockType.Z, Integer.parseInt("0000000001100011", 2), 3),
    };
  }
  
  private static final Random RAND = new Random();
}
