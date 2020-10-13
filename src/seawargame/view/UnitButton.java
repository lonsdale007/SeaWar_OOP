package seawargame.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JButton;
import seawargame.model.IShotableUnit;
import seawargame.model.events.IShotableUnitListener;

public abstract class UnitButton extends JButton {

    protected UnitButtonObserver unitButtonObserver;

    protected UnitButtonState unitButtonState;

    public UnitButton(UnitButtonState state) {
        super();

        this.unitButtonObserver = new UnitButtonObserver();

        this.unitButtonState = state;
    }

    public abstract void shoot();

    /**
     * Рисуем клетку на кнопке
     */
    @Override
    public void paintComponent(Graphics g) {
        if (this.unitButtonState == UnitButtonState.LOCKED) {
            paintComponentLocked(g);
        } else {
            paintComponentOpened(g);
        }
    }

    protected void paintComponentLocked(Graphics g) {
        // Отрисовка фона
        int width = getWidth();
        int height = getHeight();

        // границы для клетки
        Rectangle rectForCell = new Rectangle();
        rectForCell.x = 0;
        rectForCell.y = 0;
        rectForCell.width = width;
        rectForCell.height = height;

        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, width, height);

        g.setColor(Color.BLACK);   // восстанавливаем цвет пера
    }

    protected abstract void paintComponentOpened(Graphics g);

    private class UnitButtonObserver implements IShotableUnitListener {

        @Override
        public void unitDamaged(IShotableUnit unit) {
            if(unit.getState() == IShotableUnit.ShotableUnitState.Broken){
                unitButtonState = UnitButtonState.OPENED;
            }
        }
    }
}
