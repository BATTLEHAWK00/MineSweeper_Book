package cn.battlehawk233.view;

import cn.battlehawk233.dao.RecordDAO;
import cn.battlehawk233.model.Difficulty;
import cn.battlehawk233.model.IDifficulty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RecordWritingDialog extends JDialog implements ActionListener {
    private int time = 0;
    private IDifficulty difficulty;
    private final String key = null;
    private final String message = null;
    private final JTextField textName;
    private JLabel label;
    private final JButton confirm;
    private final JButton cancel;

    public RecordWritingDialog() {
        setTitle("记录你的成绩");
        setBounds(100, 100, 240, 160);
        setResizable(false);
        setModal(true);
        confirm = new JButton("确定");
        cancel = new JButton("取消");
        textName = new JTextField(8);
        confirm.addActionListener(this);
        cancel.addActionListener(this);
        setLayout(new GridLayout(2, 1));
        label = new JLabel("输入您的大名看是否可以上榜");
        add(label);
        JPanel p = new JPanel();
        p.add(textName);
        p.add(confirm);
        p.add(cancel);
        add(p);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    public void setDifficulty(IDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == confirm) {
            String name = textName.getText();
            writeRecord(name, time, difficulty);
            setVisible(false);
        }
        if (e.getSource() == cancel) {
            setVisible(false);
        }
    }

    public void writeRecord(String name, int time, IDifficulty difficulty) {
        if (RecordDAO.getInstance().addRecord(name, time, difficulty)) {
            JOptionPane.showMessageDialog(null, "恭喜您，上榜了！", "消息框", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "成绩未能上榜", "消息框", JOptionPane.WARNING_MESSAGE);
        }
    }
}
