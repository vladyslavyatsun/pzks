package utils;

import entities.task.Task;
import entities.task.TaskGraph;

import java.util.*;

/**
 * Created by hadgehog on 09.04.2014.
 */
public class TaskGraphGenerator {
    private Random random = new Random();
    private int maxX;
    private int maxY;
    private WorkManager workManager;

    public TaskGraphGenerator(int maxX, int maxY) {
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public TaskGraphGenerator() {
        this.maxX = 0;
        this.maxY = 0;
    }

    public void setWM(WorkManager wm) {
        this.workManager = wm;
    }

    public TaskGraph generateTaskGraph(int taskNumber, double minTaskWeight, double maxTaskWeight,
                                       double connectivity, double minConnectionWeight, double maxConnectionWeight) {
        TaskGraph taskGraph = new TaskGraph();
        generateTasks(taskGraph, taskNumber, minTaskWeight, maxTaskWeight);
        generateConnections(taskGraph, connectivity, minConnectionWeight, maxConnectionWeight);
//        taskGraph = workManager.openTaskGraphFromFile("D:\\Cloud\\Dropbox\\yozhik\\10 semestr\\PZKS\\labs\\saved_objects\\task1.tsk");
        setTaskCoordinates(taskGraph);
        correctTaskWeights(taskGraph, connectivity);
        return taskGraph;
    }

    private void generateTasks(TaskGraph taskGraph, int taskNumber, double minTaskWeight, double maxTaskWeight) {
        for (int i = 0; i < taskNumber; i++) {
            double weight = random.nextDouble() * (maxTaskWeight - minTaskWeight) + minTaskWeight;
            weight = Math.round(weight);
//            System.out.println(weight);
            taskGraph.addTask(weight);
        }
    }

    private void generateConnections(TaskGraph taskGraph, double connectivity, double minConnectionWeight, double maxConnectionWeight) {
        List<Task> tasks = taskGraph.getTasks();
        double tasksWeights = 0.0;
        for (Task task : tasks) {
            tasksWeights += task.getWeight();
        }
        double connectionWeights = tasksWeights * (1 / connectivity - 1);
        double realConnectionWeight = 0;
        while (realConnectionWeight < connectionWeights) {
            //перевірити ациклічність
            int fromTask = tasks.get(random.nextInt(tasks.size())).getId();
            int toTask;
            do {
                toTask = tasks.get(random.nextInt(tasks.size())).getId();
            } while (toTask == fromTask);
            double connectionWeight = random.nextDouble() * (maxConnectionWeight - minConnectionWeight) + minConnectionWeight;
            connectionWeight = Math.round(connectionWeight);
            double backupConnectionWeight = realConnectionWeight;
            realConnectionWeight += connectionWeight;
            if (realConnectionWeight > connectionWeights)
                connectionWeight = Math.ceil((realConnectionWeight - connectionWeights));
//            if (fromTask == toTask) {
//                realConnectionWeight = backupConnectionWeight;
//                realConnectionWeight = backupConnectionWeight;
//                continue;
//            }
            if (taskGraph.connectionExist(fromTask, toTask)) {
                taskGraph.addDependWeight(fromTask, toTask, connectionWeight);
                continue;
            }
            if (!taskGraph.addDepend(fromTask, toTask, connectionWeight))
                realConnectionWeight = backupConnectionWeight;
        }
    }

    private void setTaskCoordinates(TaskGraph taskGraph) {
        int level = 0;
        List<Task> tasks = taskGraph.getTasks();
        int[][] adjacencyMatrix = taskGraph.getAdjacencyMatrix();
        Map<Integer, List<Task>> levelTasks = new HashMap<>();
        int taskCounter = 0;
        while (taskCounter < tasks.size()) {
            List<Task> thisLevelTasks = new ArrayList<>();
            for (int i = 0; i < adjacencyMatrix.length; i++) {
                boolean thisLevel = true;
                for (int j = 0; j < adjacencyMatrix.length; j++) {
                    if (adjacencyMatrix[j][i] == 1) {
                        thisLevel = false;
                        continue;
                    }
                }
                if (thisLevel) {
                    Task task = taskGraph.getTask(taskGraph.getTaskIdByPos(i));
                    if (!checkTask(levelTasks, task)) {
                        thisLevelTasks.add(task);
                        taskCounter++;
                    }
                }
            }
            levelTasks.put(level++, thisLevelTasks);
            for (Task thisLevelTask : thisLevelTasks) {
                int pos = thisLevelTask.getId();//not always!
                for (int i = 0; i < adjacencyMatrix.length; i++) {
                    adjacencyMatrix[pos][i] = 0;
                }
            }
        }
        int yStep = maxY / (levelTasks.size());
        int y = 30;
        for (List<Task> taskList : levelTasks.values()) {
            int xStep = maxX / (taskList.size());
            int x = 30;
            for (Task task : taskList) {
                task.setY(y);
                task.setX(x);
                x += xStep;
            }
            y += yStep;
        }

        //print level tasks
//        for (Integer key : levelTasks.keySet()) {
//            System.out.println("level #" + key);
//            for (Task task : levelTasks.get(key)) {
//                System.out.println(task);
//            }
//        }
    }

    private void correctTaskWeights(TaskGraph taskGraph, double connectivity) {
        double e = 0.001;
        if (Math.abs(connectivity - taskGraph.getGraphConnectivity()) <= e) return;

        List<Task> tasks = taskGraph.getTasks();
        boolean increment = (connectivity > taskGraph.getGraphConnectivity()) ? true : false;
        while (true) {
            double diff = Math.abs(connectivity - taskGraph.getGraphConnectivity());
            int taskId = tasks.get(random.nextInt(tasks.size())).getId();
            if (increment)
                taskGraph.incTaskWeight(taskId);
            else {
                if (taskGraph.getTask(taskId).getWeight() <= 1.0) continue;
                taskGraph.decTaskWeight(taskId);
            }
            double newDiff = Math.abs(taskGraph.getGraphConnectivity() - connectivity);
            if (newDiff >= diff) {
                if (increment)
                    taskGraph.decTaskWeight(taskId);
                else {
                    taskGraph.incTaskWeight(taskId);
                }
                break;
            }
            if (allWeightIs1(taskGraph)) break;
        }
    }

    private boolean allWeightIs1(TaskGraph taskGraph) {
        for (Task task : taskGraph.getTasks()) {
            if (task.getWeight() > 1.0) return false;
        }
        return true;
    }

    private boolean checkTask(Map<Integer, List<Task>> levelTasks, Task task) {
        for (List<Task> tasks : levelTasks.values()) {
            if (tasks.contains(task)) return true;
        }
        return false;
    }
}
