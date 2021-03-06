package cn.battlehawk233.view;

import cn.battlehawk233.model.Block;
import cn.battlehawk233.util.MyUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BlockViewPanel extends JPanel implements ViewForBlock, MouseListener {
    private final JLabel blockNameOrIcon;
    private final JButton blockCover;
    private final CardLayout card;
    private Block block;
    public static final String markIcon = "/cn/battlehawk233/res/img/flag.png";
    public static final String mineIcon = "/cn/battlehawk233/res/img/mine.png";
    public static final String markSound = "/cn/battlehawk233/res/sounds/flag.wav";
    public static final String normalSound = "/cn/battlehawk233/res/sounds/normal.wav";
    public static final String mineSound = "/cn/battlehawk233/res/sounds/mine.wav";

    public BlockViewPanel() {
        card = new CardLayout();
        setLayout(card);
        blockNameOrIcon = new JLabel("", JLabel.CENTER);
        blockNameOrIcon.setHorizontalTextPosition(AbstractButton.CENTER);
        blockNameOrIcon.setVerticalTextPosition(AbstractButton.CENTER);
        blockCover = new JButton();
        blockCover.setEnabled(true);
        blockCover.setIcon(null);
        blockCover.addMouseListener(this);
        add("cover", blockCover);
        add("view", blockNameOrIcon);
    }

    @Override
    public void acceptBlock(Block block) {
        this.block = block;
    }

    @Override
    public void setDataOnView() {
        if (block.isMine())
            blockNameOrIcon.setIcon(MyUtil.getInstance().getIcon(BlockViewPanel.class, BlockViewPanel.mineIcon));
        else {
            int n = block.getAroundMineNumber();
            if (n >= 1)
                blockNameOrIcon.setText("" + n);
            else
                blockNameOrIcon.setText(" ");
        }
    }

    @Override
    public void seeBlockNameOrIcon() {
        card.show(this, "view");
        validate();
    }

    @Override
    public void seeBlockCover() {
        card.show(this, "cover");
        validate();
    }

    @Override
    public void seeBlockMark() {
        if (block.isMark())
            blockCover.setIcon(MyUtil.getInstance().getIcon(BlockViewPanel.class, BlockViewPanel.markIcon));
        else
            blockCover.setIcon(null);
        validate();
    }

    @Override
    public JButton getBlockCover() {
        return blockCover;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 1) {
            block.getController().onMineScout(block);
        } else if (e.getButton() == 3) {
            block.getController().onMineMark(block);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
