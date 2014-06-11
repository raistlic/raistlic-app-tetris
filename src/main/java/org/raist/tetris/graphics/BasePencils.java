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
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import org.raist.tetris.model.entity.BlockType;

/**
 *
 * @author raistlic
 */
public class BasePencils {
  
  public static BasePencils newInstance(int size) {
    
    assert size > 0;
    
    return new BasePencils(size);
  }
  
  private Map<BlockType, Pencil<Component>> colored;
  private Map<BlockType, Pencil<Component>[]> coloredAnimation;
  private Map<BlockType, Pencil<Component>> gray;
  private Map<BlockType, Pencil<Component>[]> grayAnimation;
  
  private BasePencils(int size) {
    
    colored = new EnumMap<BlockType, Pencil<Component>>(BlockType.class);
    coloredAnimation = new EnumMap<BlockType, Pencil<Component>[]>(BlockType.class);
    gray = new EnumMap<BlockType, Pencil<Component>>(BlockType.class);
    grayAnimation = new EnumMap<BlockType, Pencil<Component>[]>(BlockType.class);
    
    for(BlockType type : BlockType.values()) {
      
      colored.put(type, initPencil(BASE_COLORS.get(type), size));
      gray.put(type, initPencil(grayOf(BASE_COLORS.get(type)), size));
      coloredAnimation.put(type, initAnimationPencils(BASE_COLORS.get(type), size));
      grayAnimation.put(type, initAnimationPencils(grayOf(BASE_COLORS.get(type)), size));
    }
  }
  
  public Pencil<Component> getColoredPencil(BlockType type) {
    
    return colored.get(type);
  }
  
  public Pencil<Component> getGrayPencil(BlockType type) {
    
    return gray.get(type);
  }
  
  public Pencil<Component> getColoredAnimationPencil(BlockType type, float alpha) {
    
    Pencil<Component>[] pencils = coloredAnimation.get(type);
    int index = Math.round(alpha * ANIMATION_LEVELS);
    return pencils[index];
  }
  
  public Pencil<Component> getGrayAnimationPencil(BlockType type, float alpha) {
    
    Pencil<Component>[] pencils = grayAnimation.get(type);
    int index = Math.round(alpha * ANIMATION_LEVELS);
    return pencils[index];
  }
  
  private static Pencil<Component> initPencil(Color c, int size) {
    
    BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = image.createGraphics();
    g.setColor(c);
    g.fillRect(0, 0, size - 1, size - 1);
    g.setColor(c.brighter());
    g.drawRect(0, 0, size - 1, size - 1);
    return new ImagePencil(image);
  }
  
  @SuppressWarnings({"unchecked", "rawtypes"})
  private static Pencil<Component>[] initAnimationPencils(Color c, int size) {
    
    Pencil<Component>[] pencils = new Pencil[ANIMATION_LEVELS + 1];
    
    for(int i=0, dh=size/(ANIMATION_LEVELS+2); i<ANIMATION_LEVELS + 1; i++) {
      
      int height = size - (dh * (ANIMATION_LEVELS + 2 - i));
      int y = (size - height) / 2;
      
      BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = image.createGraphics();
      g.setColor(c);
      g.fillRect(0, y, size - 1, height);
      g.setColor(c.brighter());
      g.drawRect(0, y, size - 1, height);
      g.dispose();
      pencils[i] = new ImagePencil(image);
    }
    return pencils;
  }
  
  private static Color grayOf(Color c) {
    
    int red = (int)(0.21f * c.getRed());
    int green = (int)(0.71f * c.getGreen());
    int blue = (int)(0.07f * c.getBlue());
    int gray = red + green + blue;
    return new Color(gray, gray, gray);
  }
  
  private static final int ANIMATION_LEVELS = 5;
  private static final Map<BlockType, Color> BASE_COLORS;
  static {
    
    Map<BlockType, Color> map = new EnumMap<BlockType, Color>(BlockType.class);
    map.put(BlockType.Empty, Color.GRAY);
    map.put(BlockType.O, Color.YELLOW.darker());
    map.put(BlockType.I, Color.CYAN.darker());
    map.put(BlockType.T, Color.PINK.darker());
    map.put(BlockType.J, Color.BLUE.darker());
    map.put(BlockType.L, Color.ORANGE.darker());
    map.put(BlockType.S, Color.GREEN.darker());
    map.put(BlockType.Z, Color.RED.darker());
    BASE_COLORS = Collections.unmodifiableMap(map);
  }
}
