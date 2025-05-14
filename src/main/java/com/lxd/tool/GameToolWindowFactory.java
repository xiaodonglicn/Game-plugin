package com.lxd.tool;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class GameToolWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull com.intellij.openapi.project.Project project, @NotNull ToolWindow toolWindow) {
        GameToolWindow gameToolWindow = new GameToolWindow(toolWindow);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(gameToolWindow.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}