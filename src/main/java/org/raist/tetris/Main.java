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

import org.raist.tetris.controller.TetrisGameCommand;
import org.raist.tetris.controller.TetrisGameController;
import org.raist.tetris.controller.TetrisGameControllerFactory;
import org.raist.tetris.model.TetrisGameModel;
import org.raist.tetris.model.TetrisGameModelFactory;
import org.raist.tetris.view.TetrisGameViewFactory;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;

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

          Properties config = loadConfig();

          TetrisGameModelFactory modelFactory = loadGameModelFactory(config);
          TetrisGameModel model = modelFactory.build();

          TetrisGameControllerFactory controllerFactory = loadGameControllerFactory(config);
          TetrisGameController controller = controllerFactory.build();
          loadKeyBindings(controller, config);

          TetrisGameViewFactory viewFactory = loadGameViewFactory(config);
          viewFactory.setModel(model);
          viewFactory.setBackground(loadBackgroundImage(config));
          JComponent view = viewFactory.build();

          JFrame frame = new JFrame(config.getProperty(GameConfig.GameTitle.key));
          frame.setContentPane(view);
          frame.addKeyListener(controller.getKeyListener());
          frame.pack();
          frame.setMinimumSize(new Dimension(800, 600));
          frame.setLocationRelativeTo(null);
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.setVisible(true);

          int fps = getFPS(config);
          GameTick tick = new GameTick(model, view, controller, fps);
          new Timer(1, tick).start();
        }
        catch (Exception ex) {
          
          ex.printStackTrace(System.err);
        }
      }
    });
  }

  private static int getFPS(Properties config) {

    int fps = Integer.parseInt(GameConfig.FPS.defaultValue);
    try {

      fps = Integer.parseInt(config.getProperty(GameConfig.FPS.key));
    }
    catch (Exception ex) {

      System.out.println("Failed to get fps value from config: " + config.getProperty(GameConfig.FPS.key));
      System.out.println("Use default fps instead: " + GameConfig.FPS.defaultValue);
    }
    return fps;
  }

  private static TetrisGameModelFactory loadGameModelFactory(Properties config)
          throws Exception {

    String className = config.getProperty(GameConfig.ClassModelFactory.key);
    @SuppressWarnings("unchecked")
    Class<? extends TetrisGameModelFactory> factoryType =
            (Class<? extends TetrisGameModelFactory>) Class.forName(className);
    return factoryType.newInstance();
  }

  private static TetrisGameControllerFactory loadGameControllerFactory(Properties config)
          throws Exception {

    String className = config.getProperty(GameConfig.ClassControllerFactory.key);
    @SuppressWarnings("unchecked")
    Class<? extends TetrisGameControllerFactory> factoryType =
            (Class<? extends TetrisGameControllerFactory>) Class.forName(className);
    return factoryType.newInstance();
  }

  private static TetrisGameViewFactory loadGameViewFactory(Properties config)
          throws Exception {

    String className = config.getProperty(GameConfig.ClassViewFactory.key);
    @SuppressWarnings("unchecked")
    Class<? extends TetrisGameViewFactory> factoryType =
            (Class<? extends TetrisGameViewFactory>) Class.forName(className);
    return factoryType.newInstance();
  }

  private static BufferedImage loadBackgroundImage(Properties config) throws IOException {

    String path = config.getProperty(GameConfig.ImageBackground.key);
    InputStream in = Main.class.getResourceAsStream(path);
    BufferedImage image = ImageIO.read(in);
    in.close();
    return image;
  }

  private static void loadKeyBindings(TetrisGameController controller,
                                      Properties config) {

    Map<GameConfig, TetrisGameCommand> keyCodeConfigs =
            new EnumMap<GameConfig, TetrisGameCommand>(GameConfig.class);

    keyCodeConfigs.put(GameConfig.KeyCodeLeft, TetrisGameCommand.Left);
    keyCodeConfigs.put(GameConfig.KeyCodeRight, TetrisGameCommand.Right);
    keyCodeConfigs.put(GameConfig.KeyCodeDown, TetrisGameCommand.Down);
    keyCodeConfigs.put(GameConfig.KeyCodeDrop, TetrisGameCommand.Drop);
    keyCodeConfigs.put(GameConfig.KeyCodeStart, TetrisGameCommand.Start);
    keyCodeConfigs.put(GameConfig.KeyCodePause, TetrisGameCommand.Pause);
    keyCodeConfigs.put(GameConfig.KeyCodeRotateCounterClockwise, TetrisGameCommand.RotateCounterClockwise);
    keyCodeConfigs.put(GameConfig.KeyCodeRotateClockwise, TetrisGameCommand.RotateClockwise);
    keyCodeConfigs.put(GameConfig.KeyCodeHold, TetrisGameCommand.Hold);

    for (Map.Entry<GameConfig, TetrisGameCommand> entry : keyCodeConfigs.entrySet()) {

      int keyCode = Integer.parseInt(config.getProperty(entry.getKey().key));
      controller.mapCommand(keyCode, entry.getValue());
    }
  }

  private static Properties loadConfig() throws IOException {

    Properties config = new Properties();
    for (GameConfig gc : GameConfig.values()) {

      config.setProperty(gc.key, gc.defaultValue);
    }

    File configFile = new File(CONFIG_FILE);
    if (configFile.exists())
      loadConfigFile(config, configFile);
    return config;
  }

  private static void loadConfigFile(Properties config, File configFile) throws IOException {

    InputStream in = null;
    try {

      in = new FileInputStream(configFile);
      config.load(in);
    }
    finally {

      if (in != null) try {

        in.close();
      }
      catch (IOException ex) {

        // close exception is not important and ignored.
      }
    }
  }

  private static final String CONFIG_FILE = "config.properties";
}
