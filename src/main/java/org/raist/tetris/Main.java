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

package org.raist.tetris;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.raist.tetris.control.TetrisGameCommand;
import org.raist.tetris.control.TetrisGameControl;
import org.raist.tetris.control.TetrisGameControlFactory;
import org.raist.tetris.model.TetrisGameModel;
import org.raist.tetris.model.TetrisGameModelFactory;
import org.raist.tetris.view.TetrisGameViewFactory;

/**
 *
 * @author raistlic
 */
public class Main {
  
  public static void main(String[] args) {
    
    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        
        try {
          
          TetrisGameModel model = initGameModel();
          JComponent view = initGameView(model);
          TetrisGameControl control = initGameControl();

          JFrame frame = new JFrame(getGameTitle());
          frame.setContentPane(view);
          frame.addKeyListener(control.getKeyListener());
          frame.pack();
          frame.setMinimumSize(new Dimension(800, 600));
          frame.setLocationRelativeTo(null);
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.setVisible(true);

          GameTick tick = new GameTick(model, view, control);
          new Timer(1, tick).start();
        }
        catch (Exception ex) {
          
          ex.printStackTrace(System.err);
        }
      }
    });
  }
  
  private static TetrisGameModel initGameModel() throws Exception {
    
    @SuppressWarnings("unchecked")
    Class<? extends TetrisGameModelFactory> factoryType = 
            (Class<? extends TetrisGameModelFactory>)Class.forName(LocalConfig.FACTORY_TYPE_MODEL);
    TetrisGameModelFactory factory = factoryType.newInstance();
    return factory.build();
  }
  
  private static JComponent initGameView(TetrisGameModel model) throws Exception {
    
    String fname = LocalConfig.IMAGE_BACKGROUND;
    File file = new File(fname);
    BufferedImage background = ImageIO.read(file);
    
    @SuppressWarnings("unchecked")
    Class<? extends TetrisGameViewFactory> factoryType = 
            (Class<? extends TetrisGameViewFactory>)Class.forName(LocalConfig.FACTORY_TYPE_VIEW);
    TetrisGameViewFactory factory = factoryType.newInstance();
    factory.setModel(model);
    factory.setBackground(background);
    return factory.build();
  }
  
  private static TetrisGameControl initGameControl() throws Exception {
    
    @SuppressWarnings("unchecked")
    Class<? extends TetrisGameControlFactory> factoryType =
            (Class<? extends TetrisGameControlFactory>)Class.forName(LocalConfig.FACTORY_TYPE_CONTROL);
    TetrisGameControlFactory factory = factoryType.newInstance();
    TetrisGameControl control = factory.build();
    
    control.mapCommand(KeyEvent.VK_A,     TetrisGameCommand.Left);
    control.mapCommand(KeyEvent.VK_D,     TetrisGameCommand.Right);
    control.mapCommand(KeyEvent.VK_S,     TetrisGameCommand.Down);
    control.mapCommand(KeyEvent.VK_SPACE, TetrisGameCommand.Drop);
    control.mapCommand(KeyEvent.VK_F1,    TetrisGameCommand.Start);
    control.mapCommand(KeyEvent.VK_ENTER, TetrisGameCommand.Pause);
    control.mapCommand(KeyEvent.VK_J,     TetrisGameCommand.RotateCounterClockwise);
    control.mapCommand(KeyEvent.VK_K,     TetrisGameCommand.RotateClockwise);
    control.mapCommand(KeyEvent.VK_Q,     TetrisGameCommand.Hold);
    
    return control;
  }
  
  private static String getGameTitle() {
    
    return "Tetris";
  }
}
