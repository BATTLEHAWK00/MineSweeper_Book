package cn.battlehawk233.view;

import cn.battlehawk233.model.Difficulty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

public class NextLevelDialog extends JDialog implements ActionListener {
    JButton nextBtn;
    JButton cancelBtn;
    Consumer<Object> callback;

    public NextLevelDialog(Consumer<Object> callback) {
        this.callback = callback;
        setLayout(new GridLayout(2, 1));
        setBounds(100, 100, 240, 100);
        add(new JLabel("恭喜通关!要进行下一关吗?"));
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new GridLayout(1, 2));
        nextBtn = new JButton("确认");
        cancelBtn = new JButton("取消");
        btnPanel.add(nextBtn);
        btnPanel.add(cancelBtn);
        add(btnPanel);
        nextBtn.addActionListener(this);
        cancelBtn.addActionListener(this);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextBtn) {
            callback.accept(null);
        }
        setVisible(false);
        dispose();
    }

}
