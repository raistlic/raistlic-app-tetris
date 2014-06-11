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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.raist.ui.graphics.Pencil;
import org.raist.tetris.model.TetrisGameModel;

import javax.imageio.ImageIO;

/**
 *
 * @author raistlic
 */
class GameModelPencil implements Pencil<TetrisGameModel>{
  
  private static final int INFOR_WIDTH = 400;
  private static final int GAP = 10;
  private static final int FPS_HEIGHT = 20;
  
  private MatrixGameAreaPencil gameArea;
  private MatrixTetrisNextPencil next;
  private MatrixTetrisHoldPencil hold;

  private BufferedImage nextLabelImage;
  private BufferedImage holdLabelImage;
  
  GameModelPencil(Component c) {
    
    gameArea = new MatrixGameAreaPencil(c);
    next = new MatrixTetrisNextPencil(c);
    hold = new MatrixTetrisHoldPencil(c);

    loadImages();
  }

  private void loadImages() {

    try {

      nextLabelImage = ImageIO.read(getClass().getResourceAsStream(NEXT_IMAGE_PATH));
      holdLabelImage = ImageIO.read(getClass().getResourceAsStream(HOLD_IMAGE_PATH));
    }
    catch (Exception ex) {

      ex.printStackTrace();
    }
  }

  void setSize(TetrisGameModel value, int height) {
    
    int big = (height - FPS_HEIGHT - GAP - GAP) / value.getGameArea().getHeight();
    int small = big * 2 / 3;
    
    gameArea.setSize(big);
    next.setSize(small);
    hold.setSize(small);
  }
  
  @Override
  public Dimension getPreferredPaperSize(TetrisGameModel value) {
    
    int width = INFOR_WIDTH + GAP + 
                gameArea.getPreferredPaperSize(value).width + GAP + 
                next.getPreferredPaperSize(value).width;
    int height = 20 + GAP +
                 gameArea.getPreferredPaperSize(value).height + GAP;
    
    return new Dimension(width, height);
  }

  @Override
  public void draw(Graphics2D g, TetrisGameModel value, int width, int height) {
    
    g.setColor(Color.WHITE);
    g.drawString("FPS: " + value.getGameInfor().getFPS(), INFOR_WIDTH + GAP, FPS_HEIGHT);
    
    g.translate(INFOR_WIDTH + GAP, FPS_HEIGHT + GAP);
    gameArea.draw(g, value, width, height);
    Dimension areaSize = gameArea.getPreferredPaperSize(value);
    
    int dx = areaSize.width + GAP;
    int dy = 0;

    g.drawImage(nextLabelImage, dx, dy, null);
    dy += nextLabelImage.getHeight();

    g.translate(dx, dy);
    next.draw(g, value, width, height);
    g.translate(-dx, -dy);
    
    Dimension nextSize = next.getPreferredPaperSize(value);
    
    dy += nextSize.height + 20;

    g.drawImage(holdLabelImage, dx, dy, null);
    dy += holdLabelImage.getHeight();

    g.translate(dx, dy);
    hold.draw(g, value, width, height);
    g.translate(-dx, -dy);
  }

  private static final String HOLD_IMAGE_PATH = "hold.png";
  private static final String NEXT_IMAGE_PATH = "next.png";
}
