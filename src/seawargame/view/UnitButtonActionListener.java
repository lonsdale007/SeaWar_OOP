package seawargame.view;

import java.awt.event.ActionListener;

public class UnitButtonActionListener implements  ActionListener {

    @Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        UnitButton b = (UnitButton) evt.getSource();
        b.shoot();
    }
}
