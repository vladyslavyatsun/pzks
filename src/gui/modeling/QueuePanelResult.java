package gui.modeling;

import modelling.TaskQueueUtils;
import utils.WorkManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.util.List;

/**
 * Created by dog on 3/26/17.
 */
public class QueuePanelResult extends JPanel{
    private JLabel queueLabel;
    private List<int []> queue;
    private WorkManager workManager;

    public QueuePanelResult(List<int []> qeueue) {
        this.queue = qeueue;
        setLayoutsParameters();
        queueLabel = new JLabel("");
        add(queueLabel);
        queueToString();

    }

    private void setLayoutsParameters() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new TitledBorder("Черги задач"));
    }

    public void setQueue(TaskQueueUtils.TaskQueueType taskQueueType) {
        int[] queue = TaskQueueUtils.getQueue(workManager.getTaskGraph(), taskQueueType);
        queueLabel.setText(getQueueToString(queue));
    }

    private String getQueueToString(int[] queue) {
        String resultString = "";
        for (int i = 0; i < queue.length; i++) {
            resultString += queue[i];
            if (i != queue.length - 1) resultString += ", ";
        }
        return resultString;
    }

    private void queueToString(){
        StringBuffer result = new StringBuffer("<html>");
        for (int i = 0; i < this.queue.size(); i++) {
            result.append("<h4>Черга № " + (i + 1) + ":</h4>");
            for (int j = 0; j < queue.get(i).length; j++) {
                result.append(queue.get(i)[j]);
                if (j != queue.get(i).length - 1) {
                    result.append(" -> ");
                }
                else {
                    result.append(";");
                }
            }
            result.append(" <br>");
        }
        result.append("</html>");
        this.queueLabel.setText(result.toString());
    }
}
