package org.raist.tetris.view.defaultview;

import org.raist.tetris.model.TetrisGameModel;
import org.raist.tetris.view.TetrisGameViewFactory;

import javax.swing.JComponent;
import java.awt.image.BufferedImage;

/**
 * @author Lei.C (2014-06-12)
 */
public class DefaultTetrisGameViewFactory implements TetrisGameViewFactory {

  private TetrisGameModel model;

  private BufferedImage background;

  @Override
  public void setModel(TetrisGameModel model) {

    this.model = model;
  }

  @Override
  public JComponent build() {

    return new DefaultTetrisGamePanel(background, model);
  }

  @Override
  public boolean isReady() {

    return model != null;
  }

  @Override
  public void setBackground(BufferedImage image) {

    this.background = image;
  }
}
