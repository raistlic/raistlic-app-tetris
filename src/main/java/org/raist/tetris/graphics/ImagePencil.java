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

package org.raist.tetris.graphics;

import org.raist.ui.graphics.Pencil;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 *
 * @author raistlic
 */
public class ImagePencil implements Pencil<Component> {
  
  private Image image;
  
  public ImagePencil(Image image) {
    
    if( image == null )
      throw new NullPointerException("image is null.");
    
    this.image = image;
  }

  @Override
  public Dimension getPreferredPaperSize(Component value) {
    
    return new Dimension(image.getWidth(null), image.getHeight(null));
  }

  @Override
  public void draw(Graphics2D g, Component value, int width, int height) {
    
    assert g != null;
    assert value != null;
    
    g.drawImage(image, 0, 0, value);
  }
}
