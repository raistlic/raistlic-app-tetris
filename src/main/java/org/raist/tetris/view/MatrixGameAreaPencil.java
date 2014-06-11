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

package org.raist.tetris.view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import org.raist.tetris.graphics.BasePencils;
import org.raist.tetris.graphics.BorderPencils;
import org.raist.ui.graphics.Pencil;
import org.raist.tetris.model.Matrix;
import org.raist.tetris.model.TetrisGameModel;
import org.raist.tetris.model.entity.Block;
import org.raist.tetris.model.entity.BlockState;
import org.raist.tetris.model.entity.BlockType;
import org.raist.tetris.model.entity.GameState;

/**
 *
 * @author raistlic
 */
class MatrixGameAreaPencil implements Pencil<TetrisGameModel> {
  
  private int size;
  
  private BasePencils pencilsBase;
  private BorderPencils pencilsBorder;
  private BorderPencils pencilsBorderRelic;
  
  private Block block = new Block(BlockType.Empty, BlockState.Active, 1f);
  
  private Component observer;
  
  MatrixGameAreaPencil(Component c) {
    
    this.observer = c;
    setSize(30);
  }
  
  final void setSize(int size) {
    
    this.size = size;
    this.pencilsBase = BasePencils.newInstance(size);
    this.pencilsBorder = BorderPencils.newInstance(size);
    this.pencilsBorderRelic = BorderPencils.newInstance(
            size, new Color(230, 230, 230), Color.BLACK);
  }
  
  @Override
  public Dimension getPreferredPaperSize(TetrisGameModel value) {
    
    int width = value.getGameArea().getWidth() * size;
    int height = value.getGameArea().getHeight() * size;
    return new Dimension(width, height);
  }

  @Override
  public void draw(Graphics2D g, TetrisGameModel value, int width, int height) {
    
    Matrix<Block> area = value.getGameArea();
    
    int areaWidth = area.getWidth();
    int areaHeight = area.getHeight();
    
    boolean gray = GRAY_STATES.contains(value.getGameState());
    
    // base
    Point p = new Point(0, 0);
    for(int y=0; y<areaHeight; y++) {
      
      p.y = size * y;
      
      for(int x=0; x<areaWidth; x++) {
        
        p.x = size * x;
        g.translate(p.x, p.y);
        
        block.populate(area.get(x, y));
        
        BlockType type = block.getType();
        BlockState state = block.getState();
        
        if( state == BlockState.Animating ) {
          
          float alpha = block.getAnimationAlpha();
          Pencil<Component> pencil = gray ? 
                  pencilsBase.getGrayAnimationPencil(type, alpha) :
                  pencilsBase.getColoredAnimationPencil(type, alpha);
          pencil.draw(g, observer, size, size);
        }
        else if( block.getType() == BlockType.Empty ) {
          
          Pencil<Component> base = gray ? 
                  pencilsBase.getGrayPencil(type) : 
                  pencilsBase.getColoredPencil(type);
          Composite orginal = g.getComposite();
          g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, BASE_ALPHA));
          base.draw(g, observer, size, size);
          g.setComposite(orginal);
        }
        else {
          
          Pencil<Component> base = gray ? 
                  pencilsBase.getGrayPencil(type) : 
                  pencilsBase.getColoredPencil(type);
          base.draw(g, observer, size, size);
          
          BorderPencils pencils = state == BlockState.Relic ? pencilsBorderRelic : pencilsBorder;
          pencils.clear();
          if( checkUp(area, block, x, y) ) pencils.up();
          if( checkLeft(area, block, x, y) ) pencils.left();
          if( checkDown(area, block, x, y) ) pencils.down();
          if( checkRight(area, block, x, y) ) pencils.right();
          pencils.build().draw(g, observer, size, size);
        }
        g.translate(-p.x, -p.y);
      }
    }
  }
  
  private boolean checkUp(Matrix<Block> matrix, Block b, int x, int y) {
    
    if( y - 1 < 0 )
      return false;
    Block c = matrix.get(x, y - 1);
    boolean result = c.id() == b.id();
    return result;
  }
  
  private boolean checkLeft(Matrix<Block> matrix, Block b, int x, int y) {
    
    if( x - 1 < 0 )
      return false;
    Block c = matrix.get(x - 1, y);
    boolean result = c.id() == b.id();
    return result;
  }
  
  private boolean checkDown(Matrix<Block> matrix, Block b, int x, int y) {
    
    if( y + 1 >= matrix.getHeight() )
      return false;
    Block c = matrix.get(x, y + 1);
    boolean result = c.id() == b.id();
    return result;
  }
  
  private boolean checkRight(Matrix<Block> matrix, Block b, int x, int y) {
    
    if( x + 1 >= matrix.getWidth() )
      return false;
    Block c = matrix.get(x + 1, y);
    boolean result = c.id() == b.id();
    return result;
  }
  
  private static final Set<GameState> GRAY_STATES = EnumSet.of(
          GameState.GameOver, GameState.Paused, GameState.NotStarted);
  
  private static final Map<BlockType, Color> BASE_COLOR;
  private static final float BASE_ALPHA = 0.5f;
  
  static {
    
    BASE_COLOR = new EnumMap<BlockType, Color>(BlockType.class);
    BASE_COLOR.put(BlockType.Empty, Color.BLACK);
    BASE_COLOR.put(BlockType.O, Color.YELLOW.darker());
    BASE_COLOR.put(BlockType.I, Color.CYAN.darker());
    BASE_COLOR.put(BlockType.T, Color.PINK.darker());
    BASE_COLOR.put(BlockType.J, Color.BLUE.darker());
    BASE_COLOR.put(BlockType.L, Color.ORANGE.darker());
    BASE_COLOR.put(BlockType.S, Color.GREEN.darker());
    BASE_COLOR.put(BlockType.Z, Color.RED.darker());
  }
}
