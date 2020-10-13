package seawargame.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import seawargame.model.GameModel;
import seawargame.model.ShootResult;
import seawargame.model.HumanPlayer;
import seawargame.model.Player;
import seawargame.model.events.IGameListener;

public class GamePanel extends JFrame {

    private GameModel game;
    private JLabel welcomeLabel;
    private JLabel statusBarLabel;
    private JLabel statusBarLeftLabel;
    private Box mainBoxWithStatusBar;
    private static final int[] CELL_FRAME = {80, 100, 200};
    private final int gameMode;
    private final int fieldSize;
    boolean firstTimeMakeGameWindow;

    private final GameObserver observer;

    private JMenuBar menu = null;
    private static final String fileItems[] = new String[]{"Новая игра", "Выход"};

    private final GameFieldPanel fields[] = {null, null};

    public GameModel getGame() {
        return this.game;
    }

    public GamePanel(int gameMode) {
        super();

        //create a Game
        this.game = new GameModel();
        this.observer = new GameObserver();
        this.game.addGameListener(observer);
        this.fieldSize = 10;
        this.gameMode = gameMode;

        for (int i = 0; i < 2; i++) {
            fields[i] = new GameFieldPanel();
        }

        // Первоначальная настройка окна
        setupWindow();

        this.setMinimumSize(new Dimension(1000, 500));

        startNewGame(true);

        setLocationRelativeTo(null);
    }

    /**
     * Первоначальная настройка окна
     */
    private void setupWindow() {

        this.firstTimeMakeGameWindow = true;

        this.setTitle("Морской бой");

        this.welcomeLabel = new JLabel();
        this.welcomeLabel.setFont(new Font("Verdana", Font.BOLD, 28));
        this.welcomeLabel.setToolTipText("(Меню -> Новая игра)");

        this.statusBarLabel = new JLabel();
        this.statusBarLabel.setFont(new Font("Courier", Font.PLAIN, 11));
        this.statusBarLabel.setText("Запустить игру : Меню -> Новая игра");

        this.statusBarLeftLabel = new JLabel("---");

        this.mainBoxWithStatusBar = Box.createVerticalBox();
        this.mainBoxWithStatusBar.add(Box.createVerticalStrut(80));
        this.mainBoxWithStatusBar.add(welcomeLabel);
        this.mainBoxWithStatusBar.add(Box.createVerticalStrut(110));
        this.mainBoxWithStatusBar.add(statusBarLabel);

        setContentPane(mainBoxWithStatusBar);

        // Меню
        createMenu();
        setJMenuBar(menu);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pack();

        setResizable(false);
    }

    /**
     * Начать новую игру
     *
     * @param force прервать текущую игру, если true
     * @return false, если игра ещё идёт и force == false
     */
    private boolean startNewGame(boolean force) {

        // остановить игру, если запущена
        if (!stopCurrentGame(force)) {
            return false;
        }

        // тут создаем и инициализируем поля, клетки
        game.startNewGame(true, gameMode, fieldSize);

        // turn repaint off
        boolean ignoreRepaint = true;
        this.setIgnoreRepaint(ignoreRepaint);
        for (int i = 0; i < 2; i++) {
            fields[i].setIgnoreRepaint(ignoreRepaint);
        }

        this.pack();

        fields[0].init(game.getFields()[0], UnitButtonState.LOCKED);
        fields[1].init(game.getFields()[1], UnitButtonState.OPENED);

        // инициализация окна
        initWindowForGame();

        // turn repaint on
        ignoreRepaint = false;
        this.setIgnoreRepaint(ignoreRepaint);
        for (int i = 0; i < 2; i++) {
            fields[i].setIgnoreRepaint(ignoreRepaint);
        }

        this.setTitle("Морской бой");

        // старт игры (первый ход)
        game.runGame();

        return true;
    }

    private boolean stopCurrentGame(boolean force) {
        if (!game.stopGame(force)) {
            return false;
        }

        // заблокировать и открыть поля
        for (int i = 0; i < 2; i++) {
            fields[i].setInputEnabled(false);
            fields[i].highlight(false);
            fields[i].repaint();
        }

        if (force) {
            this.setStatusText("Игра остановлена.");
        }

        return true;
    }

    private void setStatusText(String text) {
        this.statusBarLabel.setText(text);
    }

    private void initWindowForGame() {

        if (this.firstTimeMakeGameWindow) {

            this.firstTimeMakeGameWindow = false;

            // Очистить всё
            System.out.print("Clearing view...");

            this.mainBoxWithStatusBar.removeAll();

            System.out.println(" Finished.");

            // Создать GUI-компоненты для новой игры...
            Box mainBox = Box.createHorizontalBox();

            // колонки для игроков
            Box box[] = {Box.createVerticalBox(), Box.createVerticalBox()};

            for (int i = 0; i < 2; i++) {

                System.out.println("playerPanel added");

                // Игровые поля: чужое на другой стороне, своё на своей
                box[i].add(fields[1 - i]);

                mainBox.add(box[i]);
                if (i < 1) {
                    // вертикальный зазор между полями
                    mainBox.add(Box.createVerticalStrut(10));
                }

                System.out.println("opponentsFieldPanel added");
            }

            mainBoxWithStatusBar.add(mainBox);

            // Статусная панель
            Box horBox = Box.createHorizontalBox();
            horBox.add(statusBarLeftLabel);
            horBox.add(Box.createHorizontalStrut(50));
            horBox.add(statusBarLabel);

            mainBoxWithStatusBar.add(horBox);
            setContentPane(mainBoxWithStatusBar);
        }

        // turn repaint on
        boolean ignoreRepaint = false;
        this.setIgnoreRepaint(ignoreRepaint);
        for (int i = 0; i < 2; i++) {
            fields[i].setIgnoreRepaint(ignoreRepaint);
        }

        pack();
        setResizable(false);
    }

    // ----------------------------- Создаем меню ----------------------------------
    private void createMenu() {

        menu = new JMenuBar();
        JMenu fileMenu = new JMenu("Меню");

        for (int i = 0; i < fileItems.length; i++) {

            JMenuItem item = new JMenuItem(fileItems[i]);
            item.setActionCommand(fileItems[i].toLowerCase());
            item.addActionListener(new NewMenuListener(this));
            fileMenu.add(item);
        }
        fileMenu.insertSeparator(fileItems.length - 1);

        menu.add(fileMenu);
    }

    public class NewMenuListener implements ActionListener {

        private GamePanel gamePanel;

        public NewMenuListener(GamePanel gamePanel) {
            this.gamePanel = gamePanel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (GamePanel.fileItems[1].equalsIgnoreCase(command)) {
                // Выход
                boolean stopOk = gamePanel.stopCurrentGame(false);

                if (!stopOk) {
                    int answer = JOptionPane.showOptionDialog(
                            null, "Текущая игра не закончена. Прервать её и выйти?", "Внимание", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                            new Object[]{"Да!", "Нет"}, "Нет");

                    if (answer == 0) {
                        gamePanel.stopCurrentGame(true);
                    } else {
                        return;
                    }
                }
                System.exit(0);
            }
            if (GamePanel.fileItems[0].equalsIgnoreCase(command)) {
                System.out.println("New GAME !");

                boolean startOk = gamePanel.startNewGame(false);

                if (!startOk) {
                    int answer = JOptionPane.showOptionDialog(
                            null, "Текущая игра не закончена. Прервать её и начать новую игру?", "Внимание", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                            new Object[]{"Да!", "Нет"}, "Нет");

                    if (answer == 0) {
                        gamePanel.setVisible(false);
                        new Settings().setVisible(true);
                    }
                }
                // debug
                System.out.println("New GAME started.");
            }
        }
    }

    private class GameObserver implements IGameListener {

        @Override
        public void shootPerformed(ShootResult shootResult) {
            //рисовка
            String humanReadableResult = " ";
            switch (shootResult) {
                case MISS:
                    humanReadableResult += "Промах!";
                    break;

                case SHIP_DAMAGED:
                    humanReadableResult += "Ранил!";
                    break;

                case SHIP_DROWNED:
                    humanReadableResult += "Убил!";
                    break;
            }
            setStatusText(humanReadableResult);

            repaint();
        }

        @Override
        public void gameFinished(GameModel.GameResult result) {
            stopCurrentGame(false);
            for (GameFieldPanel field: fields){
                field.setInputEnabled(false);
            }
            JOptionPane.showMessageDialog(null, result, "Игра завершена", JOptionPane.INFORMATION_MESSAGE);
        }

        @Override
        public void exchangePlayer(Player activePlayer) {
            if (activePlayer instanceof HumanPlayer) {
                fields[0].highlight(true);
                fields[1].highlight(false);
                fields[0].setInputEnabled(true);
            } else {
                fields[1].highlight(true);
                fields[0].highlight(false);
                fields[0].setInputEnabled(false);
            }
            repaint();
        }
    }
}
