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

import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import org.raist.tetris.model.TetrisGameModel;
import org.raist.tetris.view.TetrisGameViewFactory;

/**
 *
 * @author raistlic
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
    
    return new DefaultTetrisView(background, model);
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
