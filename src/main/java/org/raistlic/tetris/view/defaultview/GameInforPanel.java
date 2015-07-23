package org.raistlic.tetris.view.defaultview;

import org.raistlic.tetris.model.TetrisGameModel;
import org.raistlic.ui.graphics.Pencil;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * @author Lei.C (2014-06-12)
 */
class GameInforPanel extends JPanel {

  private TetrisGameModel model;

  private Pencil<TetrisGameModel> pencilNext;

  private Pencil<TetrisGameModel> pencilHold;

  private Pencil<TetrisGameModel> pencilScore;

  private JLabel labelNext;

  private JLabel labelHold;

  private JLabel labelScore;

  private Dimension nextSize;

  private Dimension holdSize;

  private Dimension scoreSize;

  private Dimension labelNextSize;

  private Dimension labelHoldSize;

  private Dimension labelScoreSize;

  GameInforPanel(TetrisGameModel model) {

    this.model = model;

    pencilNext = new MatrixTetrisNextPencil(this);
    pencilHold = new MatrixTetrisHoldPencil(this);
    pencilScore = new ScorePencil();

    try {

      BufferedImage next = ImageIO.read(getClass().getResourceAsStream("next.png"));
      BufferedImage hold = ImageIO.read(getClass().getResourceAsStream("hold.png"));
      BufferedImage score = ImageIO.read(getClass().getResourceAsStream("score.png"));
      labelNext = new JLabel(new ImageIcon(next));
      labelHold = new JLabel(new ImageIcon(hold));
      labelScore = new JLabel(new ImageIcon(score));
    }
    catch (Exception ex) {

      ex.printStackTrace();
      labelNext = new JLabel("NEXT");
      labelHold = new JLabel("HOLD");
      labelScore = new JLabel("SCORE");
    }

    nextSize = pencilNext.getPreferredPaperSize(model);
    holdSize = pencilHold.getPreferredPaperSize(model);
    scoreSize = pencilScore.getPreferredPaperSize(model);
    labelNextSize = labelNext.getPreferredSize();
    labelHoldSize = labelHold.getPreferredSize();
    labelScoreSize = labelScore.getPreferredSize();
  }

  @Override
  public Dimension getPreferredSize() {

    Dimension size = new Dimension(0, 0);

    size.width = Math.max(size.width, labelNextSize.width);
    size.width = Math.max(size.width, nextSize.width);
    size.width = Math.max(size.width, labelHoldSize.width);
    size.width = Math.max(size.width, holdSize.width);
    size.width = Math.max(size.width, labelScoreSize.width);
    size.width = Math.max(size.width, scoreSize.width);

    size.height += labelNextSize.height;
    size.height += nextSize.height;
    size.height += labelHoldSize.height;
    size.height += holdSize.height;
    size.height += labelScoreSize.height;
    size.height += scoreSize.height;
    size.height += GAP;

    return size;
  }

  @Override
  protected void paintComponent(Graphics g) {

    @SuppressWarnings("unchecked")
    Graphics2D gg = (Graphics2D) g;

    int dy = 0;

    gg.translate(0, dy);
    labelNext.setSize(labelNextSize.width, labelNextSize.height);
    labelNext.paint(g);
    gg.translate(0, -dy);

    dy += labelNext.getHeight();
    gg.translate(0, dy);
    pencilNext.draw(gg, model, nextSize.width, nextSize.height);
    gg.translate(0, -dy);

    dy += nextSize.height + GAP;
    gg.translate(0, dy);
    labelHold.setSize(labelHoldSize.width, labelHoldSize.height);
    labelHold.paint(gg);
    gg.translate(0, -dy);

    dy += labelHoldSize.height;
    gg.translate(0, dy);
    pencilHold.draw(gg, model, holdSize.width, holdSize.height);
    gg.translate(0, -dy);

    dy += holdSize.height + GAP;
    gg.translate(0, dy);
    labelScore.setSize(labelScoreSize.width, labelScoreSize.height);
    labelScore.paint(gg);
    gg.translate(0, -dy);

    dy += labelScoreSize.height;
    gg.translate(0, dy);
    pencilScore.draw(gg, model, scoreSize.width, scoreSize.height);
    gg.translate(0, -dy);
  }

  private static final int GAP = 10;
}
