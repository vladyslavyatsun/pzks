package gui.modeling;

import gui.modeling.gant.GantGraphFrame;
import utils.ModellingManager;
import utils.WorkManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by hadgehog on 16.02.14.
 */
public class ModellingMenu {

    public static JMenu getMenu(WorkManager workManager) {
        //todo modeling actions
        JMenu modellingMenu = new JMenu("Моделювання");

        JMenuItem PEParameters = new JMenuItem("Параметри процесорів");
        PEParameters.addActionListener(e -> workManager.showPEParametersFrame());

        JMenuItem gantDiagram = new JMenuItem("Діаграма Ганта");
        gantDiagram.addActionListener(actionEvent -> {
            if (workManager.getComputerSystem().getSystemConnectivity())
                if (workManager.getComputerSystem().getProcessors().size() > 0)
                    new GantGraphFrame(workManager);
                else
                    JOptionPane.showMessageDialog(null, "У системі відсутні процесорні елементи!!");
            else
                JOptionPane.showMessageDialog(null, "Система не зв’язна!");
        });
        JMenu statistic = new JMenu("Статистика");

        JMenuItem statisticParameters = new JMenuItem("Параметри");
//        statisticParameters.addActionListener(e -> workManager.showStatisticParametersFrame());
        JMenuItem statisticResults = new JMenuItem("Результати");
//        statisticResults.addActionListener(e -> workManager.showStatisticFrame());
        statistic.add(statisticParameters);
        statistic.add(statisticResults);

        JMenuItem queueModelling = new JMenuItem("Сформувати черги задач");
        queueModelling.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //new GantGraphFrame(workManager);
                JFrame frame = new JFrame();
                frame.setTitle("Моделювання");
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int leftCornerH = (screenSize.height - 200) / 2;
                int leftCornerW = 250;
                frame.setBounds(leftCornerW, leftCornerH, 450, 200);
                ArrayList<int[]> list = (ArrayList<int[]>) ModellingManager.createQueue(workManager.getTaskGraph());
                frame.add(new QueuePanelResult(list));
                frame.setVisible(true);
              //
            }
        });

        modellingMenu.add(PEParameters);
        modellingMenu.add(gantDiagram);
        modellingMenu.add(statistic);
        modellingMenu.add(queueModelling);

        return modellingMenu;
    }
}
