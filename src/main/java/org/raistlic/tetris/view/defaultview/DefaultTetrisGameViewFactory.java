package org.raistlic.tetris.view.defaultview;

import org.raistlic.common.precondition.Precondition;
import org.raistlic.tetris.model.TetrisGameModel;
import org.raistlic.tetris.view.TetrisGameViewFactory;

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
    Precondition.assertContext(model != null, "model cannot be null");
    return new DefaultTetrisGamePanel(background, model);
  }

  @Override
  public void setBackground(BufferedImage image) {
    this.background = image;
  }
}
