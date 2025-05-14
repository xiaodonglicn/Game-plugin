package com.lxd.tool;

import com.intellij.openapi.wm.ToolWindow;
import javax.swing.*;
import java.awt.*;

public class GameToolWindow {
    private JPanel contentPanel;

    public GameToolWindow(ToolWindow toolWindow) {
        contentPanel = new JPanel(new BorderLayout());
        BrickBreakerGame game = new BrickBreakerGame();
        contentPanel.add(game, BorderLayout.CENTER);
    }

    public JPanel getContent() {
        return contentPanel;
    }
}