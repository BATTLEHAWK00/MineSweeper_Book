package cn.battlehawk233.view;

import cn.battlehawk233.dao.RecordDAO;
import cn.battlehawk233.model.Difficulty;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ShowRecordDialog extends JDialog {
    private List<Map<String, Object>> record;
    private final JTextArea showMess;

    public ShowRecordDialog() {
        showMess = new JTextArea();
        showMess.setFont(new Font("楷体", Font.BOLD, 15));
        add(new JScrollPane(showMess));
        setTitle("显示英雄榜");
        setBounds(400, 200, 400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
    }

    public void showRecord(Difficulty difficulty) {
        showMess.setText(null);
        record = RecordDAO.getInstance().queryRecord(difficulty);
        if (record.size() == 0) {
            JOptionPane.showMessageDialog(null, "目前无人上榜", "消息框", JOptionPane.WARNING_MESSAGE);
        } else {
            for (int i = 0; i < record.size(); i++) {
                showMess.append(String.format("\n英雄%d:%s 成绩:%d", i + 1, record.get(i).get("p_name"), record.get(i).get("p_time")));
                showMess.append("\n------------------------------------");
            }
            setVisible(true);
        }
    }
}
