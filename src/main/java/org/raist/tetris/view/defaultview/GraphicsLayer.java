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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JComponent;
import org.raist.tetris.model.TetrisGameModel;

/**
 *
 * @author raistlic
 */
class GraphicsLayer extends JComponent {
  
  private TetrisGameModel model;
  private GameModelPencil pencil;
  
  GraphicsLayer(TetrisGameModel model) {
    
    this.model = model;
    this.pencil = new GameModelPencil(this);
    
    addComponentListener(new ComponentListener() {

      @Override
      public void componentResized(ComponentEvent e) {
        
        pencil.setSize(GraphicsLayer.this.model, getHeight());
      }

      @Override
      public void componentMoved(ComponentEvent e) {}

      @Override
      public void componentShown(ComponentEvent e) {}

      @Override
      public void componentHidden(ComponentEvent e) {}
    });
  }
  
  @Override
  public Dimension getPreferredSize() {
    
    return pencil.getPreferredPaperSize(model);
  }
  
  @Override
  protected void paintComponent(Graphics g) {
    
    @SuppressWarnings("unchecked")
    Graphics2D g2d = (Graphics2D)g;
    pencil.draw(g2d, model, getWidth(), getHeight());
  }
}
