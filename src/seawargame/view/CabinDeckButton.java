package seawargame.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import seawargame.model.Deck;

public class CabinDeckButton extends DeckButton{

    public CabinDeckButton(Deck deck, UnitButtonState state) {
        super(deck, state);
    }

    @Override
    protected void paintComponentOpened(Graphics g){
        // Отрисовка фона
        int width  = getWidth();
        int height = getHeight();

        // границы для клетки
        Rectangle rectForCell = new Rectangle();
        rectForCell.x = 0;
        rectForCell.y = 0;
        rectForCell.width = width;
        rectForCell.height = height;


        g.setColor(Color.WHITE);
        int radius = rectForCell.width/4; // radius
        int cx = rectForCell.x + rectForCell.width/2
                ,cy = rectForCell.y + rectForCell.height/2;

        for(int i=0 ; i<2 /*width of circle`s line*/; i++) {
            int r = radius - i;
            g.drawOval(cx - r, cy - r, r + r, r + r);
        }
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);


        g.setColor(Color.BLACK);   // восстанавливаем цвет пера

        // рисуем клетку в отведённой области
        // cell.paint(g, rectForCell /*viewRole);

        if (deck.getState() == Deck.ShotableUnitState.Broken){
            paintDamaged(g, rectForCell);
        }
    }
}
