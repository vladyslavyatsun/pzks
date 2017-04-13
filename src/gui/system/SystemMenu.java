package gui.system;

import gui.system.listeners.OpenSystemFromFileListener;
import gui.system.listeners.SaveSystemToFileListener;
import utils.WorkManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by hadgehog on 16.02.14.
 */
public class SystemMenu {
    public static JMenu getMenu(WorkManager workManager) {
        JMenu systemMenu = new JMenu("Граф КС");
        JMenuItem newSystem = new JMenuItem("Нова");
        newSystem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                workManager.removeAllProcessors();
            }
        });

        JMenuItem openSystemFromFile = new JMenuItem("Файл");
        openSystemFromFile.addActionListener(new OpenSystemFromFileListener(workManager));

        JMenuItem saveSystemToFileTask = new JMenuItem("Запис");
        saveSystemToFileTask.addActionListener(new SaveSystemToFileListener(workManager));

        systemMenu.add(newSystem);
        systemMenu.add(openSystemFromFile);
        systemMenu.add(saveSystemToFileTask);
        return systemMenu;
    }
}
