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

package org.raistlic.tetris.graphics;

import org.raistlic.ui.graphics.Pencil;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import org.raistlic.common.Factory;

/**
 *
 * @author raistlic
 */
public class BorderPencils implements Factory<Pencil<? super Component>> {
  
  private static final Color DEFAULT_FILL = new Color(230, 230, 230);
  private static final Color DEFAULT_LINE = new Color(250, 250, 250);
  
  public static BorderPencils newInstance(int size) {
    
    return newInstance(size, DEFAULT_FILL, DEFAULT_LINE);
  }
  
  public static BorderPencils newInstance(int size, Color fill, Color line) {
    
    assert size > 10;
    assert fill != null;
    assert line != null;
    
    return new BorderPencils(size, fill, line);
  }
  
  private Color fill;
  
  private Color line;
  
  private Map<Integer, Pencil<? super Component>> pencils;
  
  private int code;
  
  private BorderPencils(int size, Color fill, Color line) {
    
    this.fill = fill;
    this.line = line;
    
    pencils = initPencils(size);
    code = 0;
  }
  
  private Map<Integer, Pencil<? super Component>> initPencils(int size) {
    
    Map<Integer, Pencil<? super Component>> result = 
            new HashMap<Integer, Pencil<? super Component>>();
    
    result.put(0, new ImagePencil(initNothing(size)));
    
    // one direction
    BufferedImage image = initUp(size);
    result.put(codeOf(Direction.Up), new ImagePencil(image));
    image = rotate(image, size);
    result.put(codeOf(Direction.Right), new ImagePencil(image));
    image = rotate(image, size);
    result.put(codeOf(Direction.Down), new ImagePencil(image));
    image = rotate(image, size);
    result.put(codeOf(Direction.Left), new ImagePencil(image));
    
    // two directions
    image = initUpLeft(size);
    result.put(codeOf(Direction.Up, Direction.Left), new ImagePencil(image));
    image = rotate(image, size);
    result.put(codeOf(Direction.Up, Direction.Right), new ImagePencil(image));
    image = rotate(image, size);
    result.put(codeOf(Direction.Right, Direction.Down), new ImagePencil(image));
    image = rotate(image, size);
    result.put(codeOf(Direction.Down, Direction.Left), new ImagePencil(image));
    
    image = initUpDown(size);
    result.put(codeOf(Direction.Up, Direction.Down), new ImagePencil(image));
    image = rotate(image, size);
    result.put(codeOf(Direction.Left, Direction.Right), new ImagePencil(image));
    
    // three directions
    image = initLeftUpRight(size);
    result.put(codeOf(Direction.Left, Direction.Up, Direction.Right), new ImagePencil(image));
    image = rotate(image, size);
    result.put(codeOf(Direction.Up, Direction.Right, Direction.Down), new ImagePencil(image));
    image = rotate(image, size);
    result.put(codeOf(Direction.Right, Direction.Down, Direction.Left), new ImagePencil(image));
    image = rotate(image, size);
    result.put(codeOf(Direction.Down, Direction.Left, Direction.Up), new ImagePencil(image));
    
    return result;
  }
  
  public BorderPencils clear() {
    
    code = 0;
    return this;
  }
  
  public BorderPencils up() {
    
    code = Direction.Up.set(code);
    return this;
  }
  
  public BorderPencils left() {
    
    code = Direction.Left.set(code);
    return this;
  }
  
  public BorderPencils down() {
    
    code = Direction.Down.set(code);
    return this;
  }
  
  public BorderPencils right() {
    
    code = Direction.Right.set(code);
    return this;
  }
  
  @Override
  public Pencil<? super Component> build() {
    
    return pencils.get(code);
  }

  @Override
  public boolean isReady() {
    
    return true;
  }
  
  private BufferedImage initNothing(int size) {
    
    BufferedImage result = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = result.createGraphics();
    Rectangle rec = new Rectangle(0, 0, size - 1, size - 1);
    Area area = new Area(rec);
    rec = new Rectangle(5, 5, size - 11,  size - 11);
    area.subtract(new Area(rec));
    g.setColor(fill);
    g.fill(area);
    g.setColor(line);
    g.draw(area);
    g.dispose();
    return result;
  }
  
  private BufferedImage initUp(int size) {
    
    BufferedImage result = new BufferedImage(size, size * 2, BufferedImage.TYPE_INT_ARGB);
    Rectangle rec = new Rectangle(0, 0, size - 1, size * 2 - 1);
    Area area = new Area(rec);
    area.subtract(new Area(new Rectangle(5, 5, size - 11, size * 2 - 11)));
    Graphics2D g = result.createGraphics();
    g.setColor(fill);
    g.fill(area);
    g.setColor(line);
    g.draw(area);
    g.dispose();
    return result.getSubimage(0, size, size, size);
  }
  
  private BufferedImage initUpLeft(int size) {
    
    BufferedImage result = new BufferedImage(size * 2, size * 2, BufferedImage.TYPE_INT_ARGB);
    Rectangle rec = new Rectangle(5, 5, size * 2 - 11, size * 2 - 11);
    Area inner = new Area(rec);
    rec = new Rectangle(size - 5, size - 5, 10, 10);
    inner.subtract(new Area(rec));
    rec = new Rectangle(0, 0, size * 2 - 1, size * 2 - 1);
    Area area = new Area(rec);
    area.subtract(inner);
    Graphics2D g = result.createGraphics();
    g.setColor(fill);
    g.fill(area);
    g.setColor(line);
    g.draw(area);
    g.dispose();
    return result.getSubimage(size, size, size, size);
  }
  
  private BufferedImage initUpDown(int size) {
    
    BufferedImage result = new BufferedImage(size, size * 3, BufferedImage.TYPE_INT_ARGB);
    Rectangle rec = new Rectangle(0, 0, size - 1, size * 3 - 1);
    Area area = new Area(rec);
    rec = new Rectangle(5, 5, size - 11, size * 3 - 11);
    area.subtract(new Area(rec));
    Graphics2D g = result.createGraphics();
    g.setColor(fill);
    g.fill(area);
    g.setColor(line);
    g.draw(area);
    g.dispose();
    return result.getSubimage(0, size, size, size);
  }
  
  private BufferedImage initLeftUpRight(int size) {
    
    BufferedImage result = new BufferedImage(size * 3, size * 2, BufferedImage.TYPE_INT_ARGB);
    Rectangle rec = new Rectangle(5, 5, size * 3 - 11, size * 2 - 11);
    Area inner = new Area(rec);
    rec = new Rectangle(size - 5, size - 5, 10, 10);
    inner.subtract(new Area(rec));
    rec = new Rectangle(size * 2 - 6, size - 5, 10, 10);
    inner.subtract(new Area(rec));
    rec = new Rectangle(0, 0, size * 3 - 1, size * 2 - 1);
    Area area = new Area(rec);
    area.subtract(inner);
    
    Graphics2D g = result.createGraphics();
    g.setColor(fill);
    g.fill(area);
    g.setColor(line);
    g.draw(area);
    g.dispose();
    
    return result.getSubimage(size, size, size, size);
  }
  
  private static BufferedImage rotate(BufferedImage image, int size) {
    
    BufferedImage result = new BufferedImage(size, size, image.getType());
    Graphics2D g = result.createGraphics();
    g.setTransform(AffineTransform.getQuadrantRotateInstance(1));
    g.drawImage(image, 0, -size, null);
    g.dispose();
    return result;
  }
  
  private static int codeOf(Direction... directions) {

    int code = 0;
    for(Direction d : directions)
      code = d.set(code);
    return code;
  }
  
  private static enum Direction {
    
    Up, Left, Down, Right;
    
    private final int mask = 1 << ordinal();
    
    int set(int code) { return mask | code; }
    boolean isSet(int code) { return (mask & code) != 0; }
    
  }
}
