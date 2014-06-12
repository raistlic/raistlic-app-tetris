package org.raist.tetris.view.defaultview;

import org.raist.tetris.model.TetrisGameModel;
import org.raist.ui.graphics.Pencil;

import javax.swing.JComponent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * @author Lei.C (2014-06-12)
 */
class GameAreaPanel extends JComponent {

  private TetrisGameModel model;

  private MatrixGameAreaPencil pencil;

  private double ratio;

  GameAreaPanel(TetrisGameModel model) {

    this.model = model;
    this.pencil = new MatrixGameAreaPencil(this);

    Dimension preferredSize = pencil.getPreferredPaperSize(model);
    ratio = preferredSize.getWidth() / preferredSize.getHeight();
    this.setPreferredSize(preferredSize);
  }

  @Override
  protected void paintComponent(Graphics g) {

    int dx = 0;
    int dy = 0;
    int width = getWidth();
    int height = getHeight();

    if ((double) width / height > ratio) {

      int newWidth = (int) Math.round(height * ratio);
      dx = width - newWidth;
      width = newWidth;
    }
    else {

      height = (int) (width / ratio);
    }

    pencil.setSize(width / 10);

    Graphics2D gg = (Graphics2D) g;
    gg.translate(dx, dy);
    pencil.draw(gg, model, width, height);
    gg.translate(-dx, -dy);
  }
}
