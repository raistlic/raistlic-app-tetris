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

package org.raistlic.tetris.model.defaultmodel;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.raistlic.tetris.controller.TetrisGameCommand;
import static org.raistlic.tetris.model.defaultmodel.LocalConfig.*;

import org.raistlic.tetris.model.Matrix;
import org.raistlic.tetris.model.TetrisGameInfor;
import org.raistlic.tetris.model.TetrisGameModel;
import org.raistlic.tetris.model.entity.Block;
import org.raistlic.tetris.model.entity.BlockState;
import org.raistlic.tetris.model.entity.BlockType;
import org.raistlic.tetris.model.entity.GameState;
import org.raistlic.common.stopwatch.StopWatch;
import org.raistlic.common.stopwatch.StopWatchFactory;

/**
 *
 * @author raistlic
 */
class DefaultTetrisGameModel implements TetrisGameModel, Matrix<Block> {
  
  private final Block[][] map;
  
  private final Block empty;
  
  private final Block query; // for defensive copy
  
  private GameState state;
  
  private final Map<GameState, Moment[]> moments;
  
  private final TetrisManager tetrisManager;
  
  private int cx;
  
  private int cy;
  
  private final StopWatch watchDown;
  
  private final StopWatch watchAnimation;
  
  private final TetrisGameInfor infor;
  
  private final TetrisGameSetting setting;
  
  private final FPSHelper fps;
  
  DefaultTetrisGameModel(TetrisGameSetting setting) {
    
    this.setting = setting;
    
    map = new Block[GAME_HEIGHT][GAME_WIDTH];
    for(int y=0; y<GAME_HEIGHT; y++) {
      
      for(int x=0; x<GAME_WIDTH; x++)
        map[y][x] = new Block(BlockType.Empty, BlockState.Relic, 0);
    }
    empty = new Block(BlockType.Empty, BlockState.Relic, 0f);
    query = new Block(BlockType.Empty, BlockState.Relic, 0f);
    
    watchDown = StopWatchFactory.createStopWatch(this.setting.getDownInterval(), TimeUnit.NANOSECONDS);
    watchAnimation = StopWatchFactory.createStopWatch(ANIMATION_LENGTH, TimeUnit.NANOSECONDS);
    moments = initMoments();
    tetrisManager = new TetrisManager();
    state = GameState.NotStarted;
    infor = this.new GameInfor();
    fps = new FPSHelper();
  }
  
  private void reset(long current) {
    
    tetrisManager.reset();
    for(int y=0; y<GAME_HEIGHT; y++) {
      
      for(int x=0; x<GAME_WIDTH; x++)
        map[y][x] = new Block(BlockType.Empty, BlockState.Relic, 0);
    }
    
    watchDown.reset(current);
    watchDown.resume(current);
    setting.reset();
  }

  @Override
  public Matrix<Block> getGameArea() {
    
    return this;
  }

  @Override
  public int getNextTetrisCount() {
    
    return tetrisManager.nextCount();
  }

  @Override
  public Matrix<Block> getNextTetris(int index) {
    
    return tetrisManager.next(index);
  }

  @Override
  public Matrix<Block> getCurrentTetris() {
    
    return tetrisManager.current();
  }

  @Override
  public Matrix<Block> getHoldTetris() {
    
    Matrix<Block> result =  tetrisManager.hold();
    return result == null ? EmptyTetrisMatrix.INSTANCE : result;
  }

  @Override
  public GameState getGameState() {
    
    return state;
  }
  
  @Override
  public TetrisGameInfor getGameInfor() {
    
    return infor;
  }
  
  @Override
  public void perform(TetrisGameCommand command, long current) {
    
    assert command != null;
    
    Moment moment = moments.get(state)[1 + command.ordinal()];
    state = moment.pass(current);
  }

  @Override
  public void tick(long current) {

    Moment moment = moments.get(state)[0];
    state = moment.pass(current);
  }

  @Override
  public int getWidth() {
    
    return GAME_WIDTH;
  }

  @Override
  public int getHeight() {
    
    return GAME_HEIGHT;
  }

  @Override
  public Block get(int x, int y) {
    
    query.populate(map[y][x]);
    return query;
  }
  
  @Override
  public void refreshFPS(long current) {
    
    fps.refresh(current);
  }
  
  private class GameInfor implements TetrisGameInfor {

    @Override
    public int getScore() {
      
      return setting.getScore();
    }

    @Override
    public int getLevel() {
      
      return setting.getLevel();
    }

    @Override
    public int getMaxLevel() {
      
      return setting.getMaxLevel();
    }

    @Override
    public int getFPS() {
      
      return fps.get();
    }
  }
  
  private Map<GameState, Moment[]> initMoments() {
    
    Map<GameState, Moment[]> result = new EnumMap<>(GameState.class);
    
    result.put(GameState.NotStarted, initMomentsNotStarted());
    result.put(GameState.OnGoing, initMomentsOnGoing());
    result.put(GameState.Animating, initMomentsAnimating());
    result.put(GameState.Paused, initMomentsPaused());
    result.put(GameState.GameOver, initMomentsNotStarted());
    
    return result;
  }
  
  private Moment[] initMomentsNotStarted() {
    
    Moment[] result = new Moment[1 + TetrisGameCommand.values().length];
    Moment doNothing = Moment.doNothingMoment(GameState.NotStarted);
    
    result[0] = doNothing;
    result[1 + TetrisGameCommand.Pause.ordinal()] = doNothing;
    result[1 + TetrisGameCommand.Left.ordinal()] = doNothing;
    result[1 + TetrisGameCommand.Right.ordinal()] = doNothing;
    result[1 + TetrisGameCommand.Down.ordinal()] = doNothing;
    result[1 + TetrisGameCommand.Drop.ordinal()] = doNothing;
    result[1 + TetrisGameCommand.RotateClockwise.ordinal()] = doNothing;
    result[1 + TetrisGameCommand.RotateCounterClockwise.ordinal()] = doNothing;
    result[1 + TetrisGameCommand.Hold.ordinal()] = doNothing;
    result[1 + TetrisGameCommand.Start.ordinal()] = momentStart;
    return result;
  }
  
  private Moment[] initMomentsOnGoing() {
    
    Moment[] result = new Moment[1 + TetrisGameCommand.values().length];
    result[0] = new Moment() {

      @Override
      GameState pass(long current) {

        if( watchDown.expired(current) ) {
          watchDown.tickForward(current);
          return down(current);
        }
        else return GameState.OnGoing;
      }
    };
    result[1 + TetrisGameCommand.Pause.ordinal()] = new Moment() {

      @Override
      GameState pass(long current) {
        
        watchDown.pause(current);
        return GameState.Paused;
      }
    };
    result[1 + TetrisGameCommand.Left.ordinal()] = new Moment() {

      @Override
      GameState pass(long current) {
        
        TetrisMatrix curr = tetrisManager.current();
        erase(curr, cx, cy);
        
        if( check(curr, cx - 1, cy) ) {
          
          cx --;
          if( !check(curr, cx, cy + 1) )
            giveReactionTime(current);
        }
        print(curr, cx, cy, BlockState.Active);
        return GameState.OnGoing;
      }
    };
    result[1 + TetrisGameCommand.Right.ordinal()] = new Moment() {

      @Override
      GameState pass(long current) {
        
        TetrisMatrix curr = tetrisManager.current();
        erase(curr, cx, cy);

        if( check(curr, cx + 1, cy) ) {

          cx++;
          if( !check(curr, cx, cy + 1) )
            giveReactionTime(current);
        }
        print(curr, cx, cy, BlockState.Active);
        return GameState.OnGoing;
      }
    };
    result[1 + TetrisGameCommand.Down.ordinal()] = new Moment() {

      @Override
      GameState pass(long current) {
        
        return down(current);
      }
    };
    result[1 + TetrisGameCommand.Drop.ordinal()] = new Moment() {

      @Override
      GameState pass(long current) {
        
        return drop(current);
      }
    };
    result[1 + TetrisGameCommand.RotateClockwise.ordinal()] = new Moment() {

      @Override
      GameState pass(long current) {
        
        TetrisMatrix curr = tetrisManager.current();
        erase(curr, cx, cy);
        TetrisMatrix rotated = curr.rotateClockwise();
        
        Integer dx = checkWithHorizontalMove(rotated, cx, cy);
        if( dx != null ) {
          
          cx += dx;
          tetrisManager.doReplace(rotated);
          if( !check(curr, cx, cy + 1) )
            giveReactionTime(current);
        }
        print(tetrisManager.current(), cx, cy, BlockState.Active);
        return GameState.OnGoing;
      }
    };
    result[1 + TetrisGameCommand.RotateCounterClockwise.ordinal()] = new Moment() {

      @Override
      GameState pass(long current) {
        
        TetrisMatrix curr = tetrisManager.current();
        erase(curr, cx, cy);
        TetrisMatrix rotated = curr.rotateCounterClockwise();
        
        Integer dx = checkWithHorizontalMove(rotated, cx, cy);
        if( dx != null ) {
          
          cx += dx;
          tetrisManager.doReplace(rotated);
          if( !check(curr, cx, cy + 1) )
            giveReactionTime(current);
        }
        print(tetrisManager.current(), cx, cy, BlockState.Active);
        return GameState.OnGoing;
      }
    };
    result[1 + TetrisGameCommand.Hold.ordinal()] = new Moment() {

      @Override
      GameState pass(long current) {

        TetrisMatrix curr = tetrisManager.current();
        erase(curr, cx, cy);

        TetrisMatrix replaced = tetrisManager.expectedCurrentIfHold();
        Integer dx = checkWithHorizontalMove(replaced, cx, cy);
        if (dx != null) {

          cx += dx;
          tetrisManager.doHold();
          if( !check(tetrisManager.current(), cx, cy + 1) )
            giveReactionTime(current);
        }
        print(tetrisManager.current(), cx, cy, BlockState.Active);
        return GameState.OnGoing;
      }
    };
    result[1 + TetrisGameCommand.Start.ordinal()] = momentStart;
    return result;
  }
  
  private Moment[] initMomentsAnimating() {
    
    Moment[] result = new Moment[1 + TetrisGameCommand.values().length];
    result[0] = new Moment() {

      @Override
      GameState pass(long current) {
        
        if( watchAnimation.expired(current) ) {
          
          int erased = 0;
          for(int y = 0, h = getHeight(); y < h; y++) {
            
            if( map[y][0].getState() == BlockState.Animating ) {
              
              erase(y);
              erased++;
            }
          }
          setting.addScore(erased);
          watchDown.setTick(setting.getDownInterval());
          return next();
        }
        else {
          
          long total = watchAnimation.getTick();
          long passed = watchAnimation.readElapsed(current);
          float alpha = 1.0f - Math.min((float)passed / total, 1.0f);
          for(Block[] row : map)
            for(Block b : row)
              if( b.getState() == BlockState.Animating )
                b.setAnimationAlpha(alpha);
          return GameState.Animating;
        }
      }
    };
    result[1 + TetrisGameCommand.Pause.ordinal()] = new Moment() {

      @Override
      GameState pass(long current) {
        
        watchAnimation.pause(current);
        return GameState.Paused;
      }
    };
    
    Moment doNothing = Moment.doNothingMoment(GameState.Animating);
    result[1 + TetrisGameCommand.Left.ordinal()] = doNothing;
    result[1 + TetrisGameCommand.Right.ordinal()] = doNothing;
    result[1 + TetrisGameCommand.Down.ordinal()] = doNothing;
    result[1 + TetrisGameCommand.Drop.ordinal()] = doNothing;
    result[1 + TetrisGameCommand.RotateClockwise.ordinal()] = doNothing;
    result[1 + TetrisGameCommand.RotateCounterClockwise.ordinal()] = doNothing;
    result[1 + TetrisGameCommand.Hold.ordinal()] = doNothing;
    result[1 + TetrisGameCommand.Start.ordinal()] = momentStart;
    return result;
  }
  
  private Moment[] initMomentsPaused() {
    
    Moment[] result = new Moment[1 + TetrisGameCommand.values().length];
    
    Moment doNothing = Moment.doNothingMoment(GameState.Paused);
    result[0] = doNothing;
    result[1 + TetrisGameCommand.Left.ordinal()] = doNothing;
    result[1 + TetrisGameCommand.Right.ordinal()] = doNothing;
    result[1 + TetrisGameCommand.Down.ordinal()] = doNothing;
    result[1 + TetrisGameCommand.Drop.ordinal()] = doNothing;
    result[1 + TetrisGameCommand.RotateClockwise.ordinal()] = doNothing;
    result[1 + TetrisGameCommand.RotateCounterClockwise.ordinal()] = doNothing;
    result[1 + TetrisGameCommand.Pause.ordinal()] = new Moment() {

      @Override
      GameState pass(long current) {
        
        watchAnimation.resume(current);
        watchDown.resume(current);
        return GameState.OnGoing;
      }
    };
    result[1 + TetrisGameCommand.Start.ordinal()] = momentStart;
    return result;
  }
  
  /*---------------------------------------------------------------------------
   * utilities
   ---------------------------------------------------------------------------*/
  
  private void print(TetrisMatrix t, int x, int y, BlockState state) {
    
    for(int i=0, h=t.getHeight(); i<h; i++) {
      
      int dy = y + i;
      for(int j=0, w=t.getWidth(); j<w; j++) {
        
        int dx = x + j;
        Block b = t.get(j, i);
        if( b.getType() != BlockType.Empty ) {
          
          map[dy][dx].populate(b);
          map[dy][dx].setState(state);
        }
      }
    }
  }
  
  private void erase(TetrisMatrix t, int x, int y) {
    
    for (int i = 0, h = t.getHeight(); i < h; i++) {

      int dy = y + i;
      for (int j = 0, w = t.getWidth(); j < w; j++) {

        int dx = x + j;
        BlockType b = t.get(j, i).getType();
        if (b != BlockType.Empty)
          map[dy][dx].populate(empty);
      }
    }
  }
  
  private boolean check(TetrisMatrix t, int x, int y) {
    
    for (int i = 0, h = t.getHeight(); i < h; i++) {

      int dy = y + i;
      for (int j = 0, w = t.getWidth(); j < w; j++) {

        int dx = x + j;
        if( t.get(j, i).getType() == BlockType.Empty )
          continue;
        
        if( dy < 0 || dy >= GAME_HEIGHT )
          return false;
        if( dx < 0 || dx >= GAME_WIDTH )
          return false;
        if( map[dy][dx].getType() != BlockType.Empty )
          return false;
      }
    }
    return true;
  }
  
  private Integer checkWithHorizontalMove(TetrisMatrix t, int x, int y) {
    
    for(Integer dx : HORIZONTAL_CHECKS)
      if( check(t, x + dx, y) )
        return dx;
    return null;
  }
  
  private GameState down(long current) {
    
    TetrisMatrix curr = tetrisManager.current();
    erase(curr, cx, cy);
    
    if( check(curr, cx, cy + 1) ) {
      
      cy++;
      print(curr, cx, cy, BlockState.Active);
      return GameState.OnGoing;
    }
    else return commit(current);
  }
  
  private GameState drop(long current) {
    
    TetrisMatrix curr = tetrisManager.current();
    erase(curr, cx, cy);
    
    int y = cy;
    while( check(curr, cx, y+1) )
      y++;
    cy = y;
    return commit(current);
  }
  
  private GameState commit(long current) {
    
    TetrisMatrix curr = tetrisManager.current();
    print(curr, cx, cy, BlockState.Relic);
    
    boolean hasErase = false;
    for(int y=GAME_HEIGHT-1; y>=0; y--) {
      
      if( full(y) ) {
        
        for(Block b : map[y]) {
          
          b.setState(BlockState.Animating);
          b.setAnimationAlpha(1.0f);
        }
        hasErase = true;
      }
    }
    
    if( hasErase ) {
      
      watchAnimation.reset(current);
      return GameState.Animating;
    }
    else return next();
  }
  
  private boolean full(int y) {
    
    for(Block b : map[y])
      if( b.getType() == BlockType.Empty )
        return false;
    return true;
  }
  
  private void erase(int y) {
    
    for(; y > 0; y--) {
      
      for(int x = 0, w = getWidth(); x < w; x++)
        map[y][x].populate(map[y-1][x]);
    }
  }
  
  private GameState next() {
    
    tetrisManager.doNext();
    TetrisMatrix current = tetrisManager.current();
    cx = (GAME_WIDTH - current.getWidth()) / 2;
    cy = 0;
    
    if( check(current, cx, cy) ) {
      
      print(current, cx, cy, BlockState.Active);
      return GameState.OnGoing;
    }
    else return GameState.GameOver;
  }
  
  private void giveReactionTime(long current) {
    
    long read = watchDown.readElapsed(current);
    long diff = watchDown.getTick() - read;
    if( diff < REACTION_TIME )
      watchDown.set(REACTION_TIME, current);
  }
  
  private final MomentStart momentStart = this.new MomentStart();
  private class MomentStart extends Moment {
    
    @Override
    GameState pass(long current) {
      
      reset(current);
      return next();
    }
  }
  
  private static final List<Integer> HORIZONTAL_CHECKS = 
          Arrays.asList(0, -1, 1, -2, 2);
}
