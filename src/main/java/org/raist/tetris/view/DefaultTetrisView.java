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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import org.raist.tetris.model.TetrisGameModel;

/**
 *
 * @author raistlic
 */
class DefaultTetrisView extends BackgroundLayer {
  
  private static final int MAX_WIDTH;
  private static final int MAX_HEIGHT;
  
  static {
    
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    MAX_HEIGHT = screen.height - 120;
    MAX_WIDTH = screen.width - 100;
  }
  
  private GraphicsLayer gameLayer;
  
  DefaultTetrisView(BufferedImage background, TetrisGameModel model) {
    
    super(background);
    setLayout(new BorderLayout());
    gameLayer = new GraphicsLayer(model);
    add(gameLayer, BorderLayout.CENTER);
  }
  
  @Override
  public Dimension getPreferredSize() {
    
    Dimension result = super.getPreferredSize();
    Dimension game = gameLayer.getPreferredSize();
    
    result.width = Math.max(result.width, game.width);
    result.height = Math.max(result.height, game.height);
    
    result.width = Math.min(result.width, MAX_WIDTH);
    result.height = Math.min(result.width, MAX_HEIGHT);
    
    return result;
  }
}
