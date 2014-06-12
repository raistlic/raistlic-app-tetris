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

package org.raist.tetris.view.defaultview;

import org.raist.tetris.graphics.BasePencils;
import org.raist.tetris.graphics.BorderPencils;
import org.raist.tetris.model.Matrix;
import org.raist.tetris.model.TetrisGameModel;
import org.raist.tetris.model.entity.Block;
import org.raist.tetris.model.entity.BlockState;
import org.raist.tetris.model.entity.BlockType;
import org.raist.tetris.model.entity.GameState;
import org.raist.ui.graphics.Pencil;

import java.awt.*;
import java.util.EnumSet;
import java.util.Set;

/**
 *
 * @author raistlic
 */
class MatrixTetrisHoldPencil implements Pencil<TetrisGameModel> {

  private BasePencils pencilsBase;

  private BorderPencils pencilsBorder;

  private int size;

  private Block block = new Block(BlockType.Empty, BlockState.Active, 1f);

  private Component observer;

  MatrixTetrisHoldPencil(Component c) {

    this.observer = c;
    setSize(30);
  }

  final void setSize(int size) {

    this.size = size;
    this.pencilsBase = BasePencils.newInstance(size);
    this.pencilsBorder = BorderPencils.newInstance(
            size, new Color(230, 230, 230), Color.BLACK);
  }

  @Override
  public Dimension getPreferredPaperSize(TetrisGameModel value) {

    int width = size * value.getHoldTetris().getWidth();
    int height = size * value.getHoldTetris().getHeight();

    return new Dimension(width, height);
  }

  @Override
  public void draw(Graphics2D g, TetrisGameModel value, int width, int height) {

    Matrix<Block> tetris = value.getHoldTetris();
    GameState state = value.getGameState();
    drawMatrix(g, tetris, state);
  }

  private void drawMatrix(Graphics2D g, Matrix<Block> matrix, GameState state) {

    Point p = new Point(0, 0);
    boolean gray = GRAY_STATES.contains(state);
    for (int y = 0, h = matrix.getHeight(); y < h; y++) {

      p.y = y * size;
      for (int x = 0, w = matrix.getWidth(); x < w; x++) {

        p.x = x * size;
        g.translate(p.x, p.y);

        block.populate(matrix.get(x, y));
        BlockType type = block.getType();
        if (block.getType() == BlockType.Empty) {

          Pencil<Component> base = gray
                  ? pencilsBase.getGrayPencil(type)
                  : pencilsBase.getColoredPencil(type);
          Composite orginal = g.getComposite();
          g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
          base.draw(g, observer, size, size);
          g.setComposite(orginal);
        }
        else {

          Pencil<Component> base = gray
                  ? pencilsBase.getGrayPencil(type)
                  : pencilsBase.getColoredPencil(type);
          base.draw(g, observer, size, size);

          pencilsBorder.clear();
          if (check(matrix, x, y - 1))
            pencilsBorder.up();
          if (check(matrix, x - 1, y))
            pencilsBorder.left();
          if (check(matrix, x, y + 1))
            pencilsBorder.down();
          if (check(matrix, x + 1, y))
            pencilsBorder.right();
          pencilsBorder.build().draw(g, observer, size, size);
        }
        g.translate(-p.x, -p.y);
      }
    }
  }

  private boolean check(Matrix<Block> matrix, int x, int y) {

    if (x < 0 || x >= matrix.getWidth())
      return false;
    if (y < 0 || y >= matrix.getHeight())
      return false;
    Block c = matrix.get(x, y);
    return c.getType() != BlockType.Empty;
  }

  private static final Set<GameState> GRAY_STATES = EnumSet.of(
          GameState.GameOver, GameState.Paused, GameState.NotStarted);
}
