package seawargame.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JPanel;
import seawargame.model.GameField;
import seawargame.model.Cell;
import seawargame.model.Location;

public class GameFieldPanel extends JPanel {

    private GameField _gamefield;
    private Set<UnitButton> _buttons;

    private boolean highlighted;

    // ------------------------------ Размеры ---------------------------------
    private static final int CELL_SIZE = 36; // PREFERRED
    private static final int MAX_SIZE  = 25;
    private static final int GAP = 8;
    private static final int MAX_WIDTH  = 400;
    private static final int MAX_HEIGHT = 450;
    private static final Color ACTIVE_BORDER_COLOR = Color.BLUE;
    private static final Color INACTIVE_BORDER_COLOR = Color.GRAY;

    public GameFieldPanel() {
        super();
        my_constructor();
    }

    public GameFieldPanel(GameField field) {
        my_constructor();
        this._gamefield = field;
    }

    private void my_constructor(){
        this._buttons = new HashSet<>();
        this._gamefield = null;
    }

    public void init(GameField field, UnitButtonState state){
        if(field != null) {
            this._gamefield = field;
        }

        // Проверить, что состояние допустимо
        assert this._gamefield != null;

        this.setVisible(false);

        this.removeAll();

        createField(state);

        this.setVisible(true);

        this.repaint();
    }

    public Set<UnitButton> getButtons() {
        return _buttons;
    }

    public void setInputEnabled(boolean isEnabled) {
        for(JButton cb : _buttons) {
            cb.setEnabled(isEnabled);
        }
    }

    public void highlight(boolean isOn) {
        this.highlighted = isOn;
        this.repaint();
    }

    private void createField(UnitButtonState state) {
        int size = this._gamefield.size();
        int actualSize = size + 1;

        JPanel buttonsPanel = new JPanel(true);
        buttonsPanel.setLayout(new GridLayout(actualSize, actualSize));

        Dimension fieldDimension = new Dimension(CELL_SIZE*actualSize, CELL_SIZE*actualSize);

        buttonsPanel.setPreferredSize(fieldDimension);
        buttonsPanel.setMinimumSize(fieldDimension);
        buttonsPanel.setMaximumSize(fieldDimension);

        for (int row = 0; row < size; row++)
        {
            if( row == 0) { // ADD labels
                Label l = new Label(""); // empty
                buttonsPanel.add(l);
                for (int col = 0; col < size; col++) { // letter
                    String label_text = "    " + ((char)('A' + col));
                    l = new Label(label_text);
                    buttonsPanel.add(l);
                }
            }
            for (int col = 0; col < size; col++)
            {
                if (col == 0) { // ADD labels
                    Label l = new Label("    " + (row + 1)); // digit
                    buttonsPanel.add(l);
                }
                Cell cell = _gamefield.cellAt(new Location(col, row));
                UnitButton button;
                if (cell.getDeck() != null){
                    button = new DeckButton( cell.getDeck(), state);
                }
                else{
                    button = new CellButton( cell, state);
                }

                button.setEnabled( false );

                buttonsPanel.add(button);
                _buttons.add(button);

                // ожидаем клика
                ActionListener actionListener = new UnitButtonActionListener();
                button.addActionListener(actionListener);
            }
        }

        this.add(buttonsPanel);
        this.validate();

        int width = 2*GAP + CELL_SIZE*actualSize;
        int height = 2*GAP + CELL_SIZE*actualSize;
        Dimension panelDimension = new Dimension(width, height);

        this.setPreferredSize(panelDimension);
        this.setMinimumSize(panelDimension);
        this.setMaximumSize(panelDimension);
    }

    /** Рисуем поле */
    @Override
    public void paintComponent(Graphics g) {

        // Отрисовка фона
        int width  = getWidth();
        int height = getHeight();

        g.setColor(highlighted? ACTIVE_BORDER_COLOR : INACTIVE_BORDER_COLOR);
        g.fillRect(GAP-3, GAP-5, width-GAP-2, height-GAP-4);
        g.setColor(Color.BLACK);   // восстанавливаем цвет пера
    }
}
