package com.lxd.tool;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;

public class OpenBrickBreakerAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        // 获取当前项目实例
        Project project = e.getProject();
        if (project == null) {
            return;
        }
        // 显示名为 "BrickBreakerGame" 的工具窗口
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("BrickBreakerGame");
        if (toolWindow != null) {
            toolWindow.show(null);
        }
    }
}
