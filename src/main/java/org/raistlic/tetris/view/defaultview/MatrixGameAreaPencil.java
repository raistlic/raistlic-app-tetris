package org.raistlic.tetris.view.defaultview;

import org.raistlic.tetris.graphics.BasePencils;
import org.raistlic.tetris.graphics.BorderPencils;
import org.raistlic.tetris.model.Matrix;
import org.raistlic.tetris.model.TetrisGameModel;
import org.raistlic.tetris.model.entity.Block;
import org.raistlic.tetris.model.entity.BlockState;
import org.raistlic.tetris.model.entity.BlockType;
import org.raistlic.tetris.model.entity.GameState;
import org.raistlic.ui.graphics.Pencil;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.EnumSet;
import java.util.Set;

/**
 * @author Lei.C (2014-06-12)
 */
class MatrixGameAreaPencil implements Pencil<TetrisGameModel> {

  private int size;

  private BasePencils pencilsBase;
  private BorderPencils pencilsBorder;
  private BorderPencils pencilsBorderRelic;

  private final Block block = new Block(BlockType.Empty, BlockState.Active, 1f);

  private final Component observer;

  MatrixGameAreaPencil(Component c) {

    this.observer = c;
    setSize(30);
  }

  final void setSize(int size) {

    if (this.size != size) {

      this.size = size;
      this.pencilsBase = BasePencils.newInstance(size);
      this.pencilsBorder = BorderPencils.newInstance(size);
      this.pencilsBorderRelic = BorderPencils.newInstance(
        size, new Color(230, 230, 230), Color.BLACK);
    }
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
    for (int y = 0; y < areaHeight; y++) {

      p.y = size * y;

      for (int x = 0; x < areaWidth; x++) {

        p.x = size * x;
        g.translate(p.x, p.y);

        block.populate(area.get(x, y));

        BlockType type = block.getType();
        BlockState state = block.getState();

        if (state == BlockState.Animating) {

          float alpha = block.getAnimationAlpha();
          Pencil<Component> pencil = gray ?
            pencilsBase.getGrayAnimationPencil(type, alpha) :
            pencilsBase.getColoredAnimationPencil(type, alpha);
          pencil.draw(g, observer, size, size);
        } else if (block.getType() == BlockType.Empty) {

          Pencil<Component> base = gray ?
            pencilsBase.getGrayPencil(type) :
            pencilsBase.getColoredPencil(type);
          Composite orginal = g.getComposite();
          g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, BASE_ALPHA));
          base.draw(g, observer, size, size);
          g.setComposite(orginal);
        } else {

          Pencil<Component> base = gray ?
            pencilsBase.getGrayPencil(type) :
            pencilsBase.getColoredPencil(type);
          base.draw(g, observer, size, size);

          BorderPencils pencils = state == BlockState.Relic ? pencilsBorderRelic : pencilsBorder;
          pencils = pencils.clear();
          if (checkUp(area, block, x, y)) {
            pencils = pencils.up();
          }
          if (checkLeft(area, block, x, y)) {
            pencils = pencils.left();
          }
          if (checkDown(area, block, x, y)) {
            pencils = pencils.down();
          }
          if (checkRight(area, block, x, y)) {
            pencils = pencils.right();
          }
          pencils.build().draw(g, observer, size, size);
        }
        g.translate(-p.x, -p.y);
      }
    }
  }

  private boolean checkUp(Matrix<Block> matrix, Block b, int x, int y) {

    if (y - 1 < 0) {
      return false;
    }
    Block c = matrix.get(x, y - 1);
    return c.id() == b.id();
  }

  private boolean checkLeft(Matrix<Block> matrix, Block b, int x, int y) {

    if (x - 1 < 0) {
      return false;
    }
    Block c = matrix.get(x - 1, y);
    return c.id() == b.id();
  }

  private boolean checkDown(Matrix<Block> matrix, Block b, int x, int y) {

    if (y + 1 >= matrix.getHeight()) {
      return false;
    }
    Block c = matrix.get(x, y + 1);
    return c.id() == b.id();
  }

  private boolean checkRight(Matrix<Block> matrix, Block b, int x, int y) {

    if (x + 1 >= matrix.getWidth()) {
      return false;
    }
    Block c = matrix.get(x + 1, y);
    return c.id() == b.id();
  }

  private static final Set<GameState> GRAY_STATES = EnumSet.of(
    GameState.GameOver, GameState.Paused, GameState.NotStarted);

  private static final float BASE_ALPHA = 0.5f;
}

