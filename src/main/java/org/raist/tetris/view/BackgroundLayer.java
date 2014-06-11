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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author raistlic
 */
class BackgroundLayer extends JPanel {
  
  private BufferedImage background;
  
  BackgroundLayer(BufferedImage background) {
    
    this.background = background;
    if( this.background != null )
      setBackground(new Color(this.background.getRGB(0, 0)));
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
  }
}
