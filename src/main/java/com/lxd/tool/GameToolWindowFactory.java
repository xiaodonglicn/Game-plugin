package com.lxd.tool;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class GameToolWindowFactory implements ToolWindowFactory {

    private ContentFactory contentFactory;

    @Override
    public void createToolWindowContent(@NotNull com.intellij.openapi.project.Project project, @NotNull ToolWindow toolWindow) {
        GameToolWindow gameToolWindow = new GameToolWindow(toolWindow);
        if (contentFactory == null) {
            contentFactory = ApplicationManager.getApplication().getService(ContentFactory.class);
        }
        Content content = contentFactory.createContent(gameToolWindow.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}