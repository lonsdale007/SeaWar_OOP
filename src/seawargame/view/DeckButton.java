package seawargame.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import seawargame.model.Deck;

public class DeckButton extends UnitButton{

    protected Deck deck;
    protected Color deckColor;

    public DeckButton(Deck deck, UnitButtonState state){
        super(state);
        this.deck = deck;
        this.deck.addButtonUnitListener(unitButtonObserver);
        this.deckColor = Color.BLACK;
    }

    /**
     * Передать выстрел палубе.
     */
    @Override
    public void shoot(){
        this.deck.shootAction();
    }


    /** Отрисовать подбитую палубу.
     * Для изменения стандартного поведения переопределить метод в подклассах DeckButton
     * @param g
     * @param area
     */
    public void paintDamaged(Graphics g, Rectangle area) {

        int b = area.width/5; // border - отступ
        // крестик: 2 линии по точкам в абсолютных координатах с доп. утолщением
        for(int i=-1 ; i<=1; i++) {
            g.drawLine(area.x+b-i, area.y+b+i, area.width-b+i, area.height-b-i);
            g.drawLine(area.x+b-i, area.height-b-i, area.width-b+i, area.y+b+i);
        }
        repaint();
    }

    /** Отрисовать детали палубы.
     * Для изменения стандартного поведения переопределить метод в подклассах DeckButton
     * @param g
     */
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

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);

        g.setColor(Color.BLACK);   // восстанавливаем цвет пера

        if (deck.getState() == Deck.ShotableUnitState.Broken){
            g.setColor(Color.RED);
            paintDamaged(g, rectForCell);
        }
    }
}
