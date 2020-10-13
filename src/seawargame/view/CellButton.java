package seawargame.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import seawargame.model.Cell;
import seawargame.model.IShotableUnit;

public class CellButton extends UnitButton {

    private Cell cell;

    public CellButton(Cell cell, UnitButtonState state) {
        super(state);
        this.cell = cell;
        this.cell.addButtonUnitListener(unitButtonObserver);

    }

    /**
     * Передать выстрел клетке.
     */
    @Override
    public void shoot() {
        this.cell.shootAction();
    }

    public Cell getCell() {
        return cell;
    }

    /**
     * Рисуем клетку на кнопке
     */
    @Override
    protected void paintComponentOpened(Graphics g) {

        // Отрисовка фона
        int width = getWidth();
        int height = getHeight();

        // границы для клетки
        Rectangle rectForCell = new Rectangle();
        rectForCell.x = 0;
        rectForCell.y = 0;
        rectForCell.width = width;
        rectForCell.height = height;

        g.setColor(new Color(167, 226, 242));
        g.fillRect(0, 0, width, height);

        g.setColor(Color.BLACK);   // восстанавливаем цвет пера

        // рисуем клетку в отведённой области
        if (cell.getState() != IShotableUnit.ShotableUnitState.Ok) {
            paintBroken(g, rectForCell);
            repaint();

        }
    }

    protected void paintBroken(Graphics g, Rectangle rectForCell){
        int r = rectForCell.width / 4; // radius
        int cx = rectForCell.x + rectForCell.width / 2, cy = rectForCell.y + rectForCell.height / 2;
        g.setColor(Color.BLUE);
        g.fillOval(cx - r, cy - r, r + r, r + r);
    }

}
