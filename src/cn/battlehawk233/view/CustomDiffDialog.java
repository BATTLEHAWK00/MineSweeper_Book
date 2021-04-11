package cn.battlehawk233.view;

import cn.battlehawk233.model.CustomDifficulty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

public class CustomDiffDialog extends JDialog implements ActionListener {
    JButton confirmBtn;
    JButton cancelBtn;
    JTextArea rowInput;
    JTextArea columnInput;
    JTextArea mineCountInput;
    Consumer<CustomDifficulty> callback;

    public CustomDiffDialog(Consumer<CustomDifficulty> callback) {
        this.callback = callback;
        setTitle("自定义难度");
        setResizable(false);
        setLayout(new GridLayout(4, 2));
        setBounds(100, 100, 240, 160);

        confirmBtn = new JButton("开始");
        cancelBtn = new JButton("取消");
        rowInput = new JTextArea(1, 2);
        columnInput = new JTextArea(1, 2);
        mineCountInput = new JTextArea(1, 2);

        add(new JLabel("行数:"));
        add(rowInput);
        add(new JLabel("列数:"));
        add(columnInput);
        add(new JLabel("雷数:"));
        add(mineCountInput);
        add(confirmBtn);
        add(cancelBtn);

        confirmBtn.addActionListener(this);
        cancelBtn.addActionListener(this);
        setVisible(true);
    }

    private boolean CheckInput() {
        try {
            double row = Integer.parseInt(columnInput.getText());
            double column = Integer.parseInt(rowInput.getText());
            int mineCount = Integer.parseInt(mineCountInput.getText());
            double rate = row > column ? row / column : column / row;
            if (rate >= 1.5f || mineCount > row * column || row > 15 || column > 15)
                throw new Exception();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "输入的格式有误！");
            return false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelBtn) {
            setVisible(false);
            dispose();
        } else if (e.getSource() == confirmBtn) {
            if (CheckInput()) {
                CustomDifficulty difficulty =
                        new CustomDifficulty(
                                Integer.parseInt(columnInput.getText()),
                                Integer.parseInt(rowInput.getText()),
                                Integer.parseInt(mineCountInput.getText())
                        );
                setVisible(false);
                callback.accept(difficulty);
            }
        }
    }
}
