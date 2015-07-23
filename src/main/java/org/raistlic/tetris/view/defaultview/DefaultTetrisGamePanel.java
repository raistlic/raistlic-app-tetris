package org.raistlic.tetris.view.defaultview;

import org.raistlic.tetris.model.TetrisGameModel;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Lei.C (2014-06-12)
 */
class DefaultTetrisGamePanel extends JPanel {

  private BufferedImage background;

  private TetrisGameModel model;

  private GameAreaPanel gameAreaPanel;

  private GameInforPanel gameInforPanel;

  DefaultTetrisGamePanel(BufferedImage background, TetrisGameModel model) {

    this.model = model;
    this.background = background;
    if( this.background != null )
      setBackground(new Color(this.background.getRGB(0, 0)));

    gameAreaPanel = new GameAreaPanel(model);
    gameInforPanel = new GameInforPanel(model);

    super.add(gameAreaPanel);
    super.add(gameInforPanel);

    super.setLayout(new TetrisGameLayout());
  }

  @Override
  public Dimension getPreferredSize() {

    return new Dimension(background.getWidth(this), background.getHeight(this));
  }

  @Override
  protected void paintComponent(Graphics g) {

    super.paintComponent(g);

    Image bg = background;
    if( bg == null )
      return;

    int width = getWidth();
    int height = getHeight();

    int imgWidth = background.getWidth(this);
    int imgHeight = background.getHeight(this);

    g.drawImage(bg, (width - imgWidth) / 2, (height - imgHeight) / 2, this);
    g.setColor(Color.WHITE);

    g.drawString("FPS: " + model.getGameInfor().getFPS(), 10, 10);
  }

  private static class TetrisGameLayout implements LayoutManager {

    private static final int GAP = 20;

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {

      @SuppressWarnings("unchecked")
      DefaultTetrisGamePanel gamePanel = (DefaultTetrisGamePanel) parent;

      Dimension gameAreaSize = gamePanel.gameAreaPanel.getPreferredSize();
      Dimension gameInforSize = gamePanel.gameInforPanel.getPreferredSize();

      Dimension size = new Dimension(0, 0);
      size.width = gameAreaSize.width + gameInforSize.width + (GAP * 3);
      size.height = Math.max(gameAreaSize.height, gameInforSize.height) + (GAP * 2);
      return size;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {

      return preferredLayoutSize(parent);
    }

    @Override
    public void layoutContainer(Container parent) {

      @SuppressWarnings("unchecked")
      DefaultTetrisGamePanel gamePanel = (DefaultTetrisGamePanel) parent;

      Dimension gameInforSize = gamePanel.gameInforPanel.getPreferredSize();

      int height = gamePanel.getHeight();
      int width = gamePanel.getWidth();

      int availableHeight = height - (GAP * 2);
      int availableWidth = width - (GAP * 3) - gameInforSize.width;

      int indent = 0;
      if (availableWidth * 2 > availableHeight) {

        indent = (availableWidth - (availableHeight / 2)) / 2;
        availableWidth -= indent * 2;
      }

      gamePanel.gameAreaPanel.setBounds(indent, GAP, availableWidth, availableHeight);
      gamePanel.gameInforPanel.setBounds(indent + availableWidth + GAP, GAP, gameInforSize.width, availableHeight);
    }
  }
}
