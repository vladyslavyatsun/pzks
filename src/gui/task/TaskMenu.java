package gui.task;

import gui.task.listeners.OpenTaskFromFileListener;
import gui.task.listeners.SaveTaskToFileListener;
import utils.WorkManager;

import javax.swing.*;

/**
 * Created by hadgehog on 16.02.14.
 */
public class TaskMenu {

    //todo task actions
    public static JMenu getMenu(WorkManager workManager) {
        JMenu taskMenu = new JMenu("Граф задачі");
        JMenu newTask = new JMenu("Новий");

        JMenuItem taskEditor = new JMenuItem("Редактор графу");
        taskEditor.addActionListener(actionEvent -> {
            workManager.removeAllTask();
        });
        newTask.add(taskEditor);
        JMenuItem taskGenerator = new JMenuItem("Генерування графу");
        taskGenerator.addActionListener(actionEvent -> {
            workManager.showGeneratingTasksFrame();
        });
        newTask.add(taskGenerator);

        JMenuItem openTaskFromFile = new JMenuItem("Файл");
        openTaskFromFile.addActionListener(new OpenTaskFromFileListener(workManager));

        JMenuItem saveTaskToFile = new JMenuItem("Запис");
        saveTaskToFile.addActionListener(new SaveTaskToFileListener(workManager));

        taskMenu.add(newTask);
        taskMenu.add(openTaskFromFile);
        taskMenu.add(saveTaskToFile);
        return taskMenu;
    }
}
