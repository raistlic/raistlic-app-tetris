package org.raist.tetris.view.defaultview;

import org.raist.tetris.model.TetrisGameModel;
import org.raist.tetris.model.entity.GameState;
import org.raist.ui.graphics.Pencil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.EnumSet;
import java.util.Set;

/**
 * @author Lei.C (2014-06-12)
 */
class ScorePencil implements Pencil<TetrisGameModel> {

  private int score;

  private boolean gray;

  private int width;

  private int height;

  private BufferedImage buffer;

  private Ruler ruler;

  private double angle;

  private Color boardColor;

  private Color grayColor;

  private Color activeColor;

  ScorePencil() {

    ruler = new Ruler();
    angle = 0.1;

    boardColor = Color.GRAY.darker();
    grayColor = Color.WHITE;
    activeColor = Color.ORANGE.brighter();
  }

  @Override
  public Dimension getPreferredPaperSize(TetrisGameModel value) {

    return new Dimension(120, 20);
  }

  @Override
  public void draw(Graphics2D g, TetrisGameModel value, int width, int height) {

    if (value.getGameInfor().getScore() != score ||
            gray != GRAY_STATES.contains(value.getGameState()) ||
            width != this.width ||
            height != this.height ||
            buffer == null) {

      prepareImage(value, width, height);
    }

    g.drawImage(buffer, 0, 0, null);
  }

  private void prepareImage(TetrisGameModel value, int width, int height) {

    buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

    this.score = value.getGameInfor().getScore();
    this.gray = GRAY_STATES.contains(value.getGameState());
    this.width = width;
    this.height = height;

    String str = String.format("%06d", score);
    Graphics2D g = buffer.createGraphics();
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    g.setColor(boardColor);
    g.fillRoundRect(0, 0, width, height, 5, 5);

    Rectangle area = new Rectangle(0, 0, 20, 20);
    g.setColor(gray ? grayColor : activeColor);
    for (int i = 0; i < 6; i++) {

      area.x = i * 20;
      prepareRuler(area);
      int ordinal = str.charAt(i) - '0';
      Digit digit = Digit.values()[ordinal];
      for (Led led : digit.leds) {

        led.draw(g, ruler);
      }
    }
    g.dispose();
  }

  private void prepareRuler(Rectangle area) {

    if( area == null )
      return;
    int dwidthHalf = Math.abs((int) (area.height * angle)) / 2;
    int dwidth = dwidthHalf * 2;

    int penxHalf = 1;
    int penx = penxHalf * 2;
    int penyHalf = 1;
    int peny = penyHalf * 2;

    int dpenx = Math.abs((int) (penx * angle));
    int dx = (dwidth - dpenx) / 2;
    int dy = (area.height - penx) / 2;
    int dw = area.width - dwidth - peny;

    ruler.fa.tl.x = angle > 0
            ? area.x + dwidth
            : area.x;
    ruler.fa.tl.y = area.y;
    ruler.fa.tr.x = ruler.fa.tl.x + peny;
    ruler.fa.tr.y = ruler.fa.tl.y;
    ruler.fa.bl.x = angle > 0
            ? ruler.fa.tl.x - dpenx
            : ruler.fa.tl.x + dpenx;
    ruler.fa.bl.y = ruler.fa.tl.y + penx;
    ruler.fa.br.x = ruler.fa.bl.x + peny;
    ruler.fa.br.y = ruler.fa.bl.y;
    ruler.fa.center.x = ruler.fa.bl.x + ((ruler.fa.tr.x - ruler.fa.bl.x) / 2);
    ruler.fa.center.y = ruler.fa.tl.y + penyHalf;

    ruler.ef.tl.x = ruler.fa.tl.x - dx;
    ruler.ef.tl.y = ruler.fa.tl.y + dy;
    ruler.ef.tr.x = ruler.fa.tr.x - dx;
    ruler.ef.tr.y = ruler.fa.tl.y + dy;
    ruler.ef.bl.x = ruler.fa.bl.x - dx;
    ruler.ef.bl.y = ruler.fa.bl.y + dy;
    ruler.ef.br.x = ruler.fa.br.x - dx;
    ruler.ef.br.y = ruler.fa.br.y + dy;
    ruler.ef.center.x = ruler.fa.center.x - dx;
    ruler.ef.center.y = ruler.fa.center.y + dy;

    ruler.de.tl.x = ruler.ef.tl.x - dx;
    ruler.de.tl.y = ruler.ef.tl.y + dy;
    ruler.de.tr.x = ruler.ef.tr.x - dx;
    ruler.de.tr.y = ruler.ef.tl.y + dy;
    ruler.de.bl.x = ruler.ef.bl.x - dx;
    ruler.de.bl.y = ruler.ef.bl.y + dy;
    ruler.de.br.x = ruler.ef.br.x - dx;
    ruler.de.br.y = ruler.ef.br.y + dy;
    ruler.de.center.x = ruler.ef.center.x - dx;
    ruler.de.center.y = ruler.ef.center.y + dy;

    ruler.ab.tl.x = ruler.fa.tl.x + dw;
    ruler.ab.tl.y = ruler.fa.tl.y;
    ruler.ab.tr.x = ruler.fa.tr.x + dw;
    ruler.ab.tr.y = ruler.fa.tl.y;
    ruler.ab.bl.x = ruler.fa.bl.x + dw;
    ruler.ab.bl.y = ruler.fa.bl.y;
    ruler.ab.br.x = ruler.fa.br.x + dw;
    ruler.ab.br.y = ruler.fa.br.y;
    ruler.ab.center.x = ruler.fa.center.x + dw;
    ruler.ab.center.y = ruler.fa.center.y;

    ruler.bc.tl.x = ruler.ab.tl.x - dx;
    ruler.bc.tl.y = ruler.ab.tl.y + dy;
    ruler.bc.tr.x = ruler.ab.tr.x - dx;
    ruler.bc.tr.y = ruler.ab.tl.y + dy;
    ruler.bc.bl.x = ruler.ab.bl.x - dx;
    ruler.bc.bl.y = ruler.ab.bl.y + dy;
    ruler.bc.br.x = ruler.ab.br.x - dx;
    ruler.bc.br.y = ruler.ab.br.y + dy;
    ruler.bc.center.x = ruler.ab.center.x - dx;
    ruler.bc.center.y = ruler.ab.center.y + dy;

    ruler.cd.tl.x = ruler.bc.tl.x - dx;
    ruler.cd.tl.y = ruler.bc.tl.y + dy;
    ruler.cd.tr.x = ruler.bc.tr.x - dx;
    ruler.cd.tr.y = ruler.bc.tl.y + dy;
    ruler.cd.bl.x = ruler.bc.bl.x - dx;
    ruler.cd.bl.y = ruler.bc.bl.y + dy;
    ruler.cd.br.x = ruler.bc.br.x - dx;
    ruler.cd.br.y = ruler.bc.br.y + dy;
    ruler.cd.center.x = ruler.bc.center.x - dx;
    ruler.cd.center.y = ruler.bc.center.y + dy;
  }

  private static final Set<GameState> GRAY_STATES = EnumSet.of(
          GameState.GameOver, GameState.Paused, GameState.NotStarted);

  private static enum Led {

    A {

      @Override
      void draw(Graphics2D g, Ruler ruler) {

        int[] x = {
                ruler.fa.tl.x + 1, ruler.ab.tr.x - 1, ruler.ab.bl.x - 1,
                ruler.fa.br.x + 1, ruler.fa.tl.x + 1
        };
        int[] y = {
                ruler.fa.tl.y, ruler.ab.tr.y, ruler.ab.bl.y,
                ruler.fa.br.y, ruler.fa.tl.y
        };
        g.fillPolygon(x, y, Math.min(x.length, y.length));
      }
    },

    B {

      @Override
      void draw(Graphics2D g, Ruler ruler) {

        int[] x = {
                ruler.ab.tr.x, ruler.bc.tr.x, ruler.bc.center.x,
                ruler.bc.tl.x, ruler.ab.bl.x, ruler.ab.tr.x
        };
        int[] y = {
                ruler.ab.tr.y + 1, ruler.bc.tr.y - 1, ruler.bc.center.y - 1,
                ruler.bc.tl.y - 1, ruler.ab.bl.y + 1, ruler.ab.tr.y + 1
        };
        g.fillPolygon(x, y, Math.min(x.length, y.length));
      }
    },

    C {

      @Override
      void draw(Graphics2D g, Ruler ruler) {

        int[] x = {
                ruler.bc.center.x, ruler.bc.br.x, ruler.cd.br.x,
                ruler.cd.tl.x, ruler.bc.bl.x, ruler.bc.center.x
        };
        int[] y = {
                ruler.bc.center.y + 1, ruler.bc.br.y + 1, ruler.cd.br.y - 1,
                ruler.cd.tl.y - 1, ruler.bc.bl.y + 1, ruler.bc.center.y + 1
        };
        g.fillPolygon(x, y, Math.min(x.length, y.length));
      }
    },

    D {

      @Override
      void draw(Graphics2D g, Ruler ruler) {

        int[] x = {
                ruler.de.tr.x + 1, ruler.cd.tl.x - 1, ruler.cd.br.x - 1,
                ruler.de.bl.x + 1, ruler.de.tr.x + 1
        };
        int[] y = {
                ruler.de.tr.y, ruler.cd.tl.y, ruler.cd.br.y,
                ruler.de.bl.y, ruler.de.tr.y
        };
        g.fillPolygon(x, y, Math.min(x.length, y.length));
      }
    },

    E {

      @Override
      void draw(Graphics2D g, Ruler ruler) {

        int[] x = {
                ruler.ef.center.x, ruler.ef.br.x, ruler.de.tr.x,
                ruler.de.bl.x, ruler.ef.bl.x, ruler.ef.center.x
        };
        int[] y = {
                ruler.ef.center.y + 1, ruler.ef.br.y + 1, ruler.de.tr.y - 1,
                ruler.de.bl.y - 1, ruler.ef.bl.y + 1, ruler.ef.center.y + 1
        };
        g.fillPolygon(x, y, Math.min(x.length, y.length));
      }
    },

    F {

      @Override
      void draw(Graphics2D g, Ruler ruler) {

        int[] x = {
                ruler.fa.br.x, ruler.ef.tr.x, ruler.ef.center.x,
                ruler.ef.tl.x, ruler.fa.tl.x, ruler.fa.br.x
        };
        int[] y = {
                ruler.fa.br.y + 1, ruler.ef.tr.y - 1, ruler.ef.center.y - 1,
                ruler.ef.tl.y - 1, ruler.fa.tl.y + 1, ruler.fa.br.y + 1
        };
        g.fillPolygon(x, y, Math.min(x.length, y.length));
      }
    },

    G {

      @Override
      void draw(Graphics2D g, Ruler ruler) {

        int[] x = {
                ruler.ef.center.x + 1, ruler.ef.tr.x + 1, ruler.bc.tl.x - 1,
                ruler.bc.center.x - 1, ruler.bc.bl.x - 1, ruler.ef.br.x + 1,
                ruler.ef.center.x + 1
        };
        int[] y = {
                ruler.ef.center.y, ruler.ef.tr.y, ruler.bc.tl.y,
                ruler.bc.center.y, ruler.bc.bl.y, ruler.ef.br.y,
                ruler.ef.center.y
        };
        g.fillPolygon(x, y, Math.min(x.length, y.length));
      }
    };

    abstract void draw(Graphics2D g, Ruler ruler);
  }

  private static enum Digit {

    _0(Led.A, Led.B, Led.C, Led.D, Led.E, Led.F),
    _1(Led.B, Led.C),
    _2(Led.A, Led.B, Led.D, Led.E, Led.G),
    _3(Led.A, Led.B, Led.C, Led.D, Led.G),
    _4(Led.B, Led.C, Led.F, Led.G),
    _5(Led.A, Led.C, Led.D, Led.F, Led.G),
    _6(Led.A, Led.C, Led.D, Led.E, Led.F, Led.G),
    _7(Led.A, Led.B, Led.C),
    _8(Led.A, Led.B, Led.C, Led.D, Led.E, Led.F, Led.G),
    _9(Led.A, Led.B, Led.C, Led.D, Led.F, Led.G),
    ;

    private final Set<Led> leds;

    Digit(Led... leds) {

      this.leds = EnumSet.noneOf(Led.class);
      for (Led led : leds)
        this.leds.add(led);
    }
  }

  private static enum Pen {

    Thick(3, 5),
    Normal(4, 7),
    Thin(8, 11),;
    private final int widthH, widthV;

    Pen(int widthH, int widthV) {
      this.widthH = widthH;
      this.widthV = widthV;
    }
  }

  private static class Ruler {

    private Nail ab = new Nail();
    private Nail bc = new Nail();
    private Nail cd = new Nail();
    private Nail de = new Nail();
    private Nail ef = new Nail();
    private Nail fa = new Nail();
  }

  private static class Nail {

    private Point center = new Point();
    private Point tl = new Point();
    private Point tr = new Point();
    private Point bl = new Point();
    private Point br = new Point();
  }
}
