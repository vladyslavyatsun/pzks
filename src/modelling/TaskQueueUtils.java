package modelling;

import entities.task.TaskGraph;
import entities.task.TaskQueueData;
import utils.CollectionUtils;
import utils.comparators.TaskQueue1Comparator;
import utils.comparators.TaskQueue2Comparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hadgehog on 30.03.2014.
 */
public class TaskQueueUtils {
    private static Map<Integer, Integer> vertexNumberWays;
    private static Map<Integer, Double> timeFromStartWays;
    private static Map<Integer, Double> timeToEndWays;
    public static TaskGraph taskGraph;

    public static int[] getQueue(TaskGraph taskGraph, TaskQueueType taskQueueType) {
        TaskQueueUtils.taskGraph = taskGraph;
        switch (taskQueueType) {
            case CRITICAL_TIME_AND_VERTEX_NUMBER_WAY_2:
                return getQueue1(taskGraph);
            case CONNECTIVITY_CRITICAL_VERTEX_NUMBER_WAY_10:
                return getQueue2(taskGraph);
            case CRITICAL_TIME_WAY_16:
                return getQueue3(taskGraph);
        }
        return null;
    }

    private static int[] getQueue1(TaskGraph taskGraph) {
        List<TaskQueueData> taskQueueDatas = new ArrayList<>();
        int[][] adjacencyMatrix = taskGraph.getAdjacencyMatrix();
        int[][] transposedMatrix = transposeMatrix(adjacencyMatrix);
        //to end times
        timeToEndWays = new HashMap<>();
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            timeToEndWays.put(i, getCriticalWayToEndByTime(i, 0, adjacencyMatrix));
        }
        //from start times
        timeFromStartWays = new HashMap<>();
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            timeFromStartWays.put(i, getCriticalWayFromStartByTime(i, 0, transposedMatrix));
        }
        //graph critical time
        double graphCriticalTime = 0.0;
        for (Double aDouble : timeToEndWays.values()) {
            System.out.println(aDouble);
            if (aDouble > graphCriticalTime) graphCriticalTime = aDouble;
        }
        // Pri
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            //todo first queue
            taskQueueDatas.add(new TaskQueueData(i, Math.abs(timeFromStartWays.get(i) - (graphCriticalTime - timeToEndWays.get(i)))));
            //taskQueueDatas.add(new TaskQueueData(i, - timeFromStartWays.get(i) + timeToEndWays.get(i)));
//            System.out.println("t" + i + "(" + timeFromStartWays.get(i) + "-(" + graphCriticalTime + "-" + timeToEndWays.get(i) + ")");
        }
        //////
        taskQueueDatas.sort(new TaskQueue1Comparator());
        int[] queue = new int[adjacencyMatrix.length];
        for (int i = 0; i < taskQueueDatas.size(); i++) {
            queue[i] = taskGraph.getTaskIdByPos(taskQueueDatas.get(i).getId());
        }

        System.out.println("Queue 1");
        for (int i = 0; i < taskQueueDatas.size(); i++) {
            int pos = 0;
            for (Integer integer : timeFromStartWays.keySet()) {
                if (integer == taskQueueDatas.get(i).getId()) {
                    pos = integer;
                    break;
                }
            }

            System.out.println("t" + taskGraph.getTaskIdByPos(taskQueueDatas.get(i).getId()) + "(" +
                    taskQueueDatas.get(i).getDeltaTime()  + ")" + "   | " + timeFromStartWays.get(pos) +
                    " - ("  +graphCriticalTime  + " - " + timeToEndWays.get(pos) + ")");
        }
        System.out.println();
        return queue;
    }

    private static int[] getQueue2(TaskGraph taskGraph) {
        List<TaskQueueData> taskQueueDatas = new ArrayList<>();
        int[][] adjacencyMatrix = taskGraph.getAdjacencyMatrix();
        //id, critical way
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            TaskQueueData taskQueueData = new TaskQueueData(i, getCriticalWayToEnd(i, 1, adjacencyMatrix));
            taskQueueDatas.add(taskQueueData);
        }
        //connectivity
        for (int i = 0; i < taskQueueDatas.size(); i++) {
            int connectivity = 0;
            for (int j = 0; j < adjacencyMatrix.length; j++) {
                if (adjacencyMatrix[i][j] == 1 || adjacencyMatrix[j][i] == 1) connectivity++;
            }
            taskQueueDatas.get(i).setConnectivity(connectivity);
        }
        taskQueueDatas.sort(new TaskQueue2Comparator());
        //sort vertexNumberWays
        int[] queue = new int[adjacencyMatrix.length];
        for (int i = 0; i < taskQueueDatas.size(); i++) {
            queue[i] = taskGraph.getTaskIdByPos(taskQueueDatas.get(i).getId());
        }
        //print
        System.out.println("Queue 2");
        for (int i = 0; i < taskQueueDatas.size(); i++) {
            System.out.println("t" + taskGraph.getTaskIdByPos(taskQueueDatas.get(i).getId()) + "(" +
                    taskQueueDatas.get(i).getCriticalVertexWay() + "," + taskQueueDatas.get(i).getConnectivity() + ")");
        }
        System.out.println();
        return queue;
    }

    private static int[] getQueue3(TaskGraph taskGraph) {
        timeFromStartWays = new HashMap<>();
        int[][] adjacencyMatrix = taskGraph.getAdjacencyMatrix();
        int[][] transposedMatrix = transposeMatrix(adjacencyMatrix);
        //
//        for (int i = 0; i < adjacencyMatrix.length; i++) {
//            for (int j = 0; j < transposedMatrix[i].length; j++) {
//                System.out.print(transposedMatrix[i][j] + " ");
//            }
//            System.out.println();
//        }
//        System.out.println();
        //
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            timeFromStartWays.put(i, getCriticalWayFromStartByTime(i, 0, transposedMatrix));
        }
        timeFromStartWays = CollectionUtils.sortByValuesFromMinToMax(timeFromStartWays);
        //sort vertexNumberWays
        int[] queue = new int[adjacencyMatrix.length];
        int count = 0;
        for (Integer integer : timeFromStartWays.keySet()) {
            queue[count++] = taskGraph.getTaskIdByPos(integer);
        }
        //print
        System.out.println("Queue 3");
        for (Integer integer : timeFromStartWays.keySet()) {
            System.out.println("t" + taskGraph.getTaskIdByPos(integer) + "(" + timeFromStartWays.get(integer) + ")");
        }
        System.out.println();
        return queue;
    }

    private static int getCriticalWayToEnd(int from, int value, int[][] adjacencyMatrix) {
        boolean isEndVertex = true;
        List<Integer> criticalWaysVariants = new ArrayList<>();
        for (int i = 0; i < adjacencyMatrix[from].length; i++) {
            if (adjacencyMatrix[from][i] == 1) {
                isEndVertex = false;
                return getCriticalWayToEnd(i, ++value, adjacencyMatrix);
            }
        }
        if (isEndVertex) {
            return value;
        } else {
//            int max = 0;
//            for (Integer criticalWaysVariant : criticalWaysVariants) {
//
//            }
        }
        return 0;
    }

    private static double getCriticalWayFromStartByTime(int from, double value, int[][] adjacencyMatrix) {
        boolean isEndVertex = true;
        List<Double> criticalWaysVariants = new ArrayList<>();
        for (int i = 0; i < adjacencyMatrix[from].length; i++) {
            if (adjacencyMatrix[from][i] == 1) {
                isEndVertex = false;
//                value += taskGraph.getTaskWeightByPos(from);
                criticalWaysVariants.add(getCriticalWayFromStartByTime(i, value, adjacencyMatrix) + taskGraph.getTaskWeightByPos(i));
//                return getCriticalWayFromStartByTime(i, value, adjacencyMatrix) + taskGraph.getTaskWeightByPos(i);
            }
        }
        if (isEndVertex) {
            return 0;//taskGraph.getTaskWeightByPos(from);
        } else {
            double max = 0.0;
            for (Double criticalWaysVariant : criticalWaysVariants) {
                if (criticalWaysVariant > max) max = criticalWaysVariant;
            }
            return max;
        }
    }

//    private static double getCriticalWayToEndByTime(int from, double value, int[][] adjacencyMatrix) {
//        boolean isEndVertex = true;
//        //todo
//        ArrayList<Double> list = new ArrayList();
//        for (int i = 0; i < adjacencyMatrix[from].length; i++) {
//            if (adjacencyMatrix[from][i] == 1) {
//                isEndVertex = false;
//                value += taskGraph.getTaskWeightByPos(from);
//                return getCriticalWayToEndByTime(i, value, adjacencyMatrix);
//            }
//        }
//        if (isEndVertex) {
//            return value+=taskGraph.getTaskWeightByPos(from);
//        }
//        return 0;
//    }

    private static double getCriticalWayToEndByTime(int from, double value, int[][] adjacencyMatrix) {
        boolean isEndVertex = true;
        ArrayList<Double> list = new ArrayList();
        for (int i = 0; i < adjacencyMatrix[from].length; i++) {
            if (adjacencyMatrix[from][i] == 1) {
                isEndVertex = false;
                list.add(getCriticalWayToEndByTime(i, value + taskGraph.getTaskWeightByPos(from), adjacencyMatrix));
            }
        }
        if (isEndVertex) {
            list.add(value + taskGraph.getTaskWeightByPos(from));
        }

        Double result = 0.0;
        for (Double aDouble : list) {
            if (aDouble > result) {
                result = aDouble;
            }
        }
        return result;
    }

    public static int[][] transposeMatrix(int[][] matrix) {
        int[][] transposeMatrix = new int[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                transposeMatrix[j][i] = matrix[i][j];
        return transposeMatrix;
    }

    //    private static List<Integer> getReadyToExecuteTasks(int[][] adjacencyMatrix) {
//        List<Integer> readyToExecuteTasks = new ArrayList<>();
//        if (adjacencyMatrix.length == 0) return readyToExecuteTasks;
//        for (int i = 0; i < adjacencyMatrix[0].length; i++) {
//            boolean ready = true;
//            for (int j = 0; j < adjacencyMatrix.length; j++) {
//                if (adjacencyMatrix[j][i] == 1) {
//                    ready = false;
//                    break;
//                }
//            }
//            if (ready) readyToExecuteTasks.add(i);
//        }
//        return readyToExecuteTasks;
//    }
    public static void printQueue(int[] queue, TaskQueueUtils.TaskQueueType taskQueueType) {
        System.out.println("Queue: " + taskQueueType);
        for (int i = 0; i < queue.length; i++) {
            System.out.print(queue[i] + (i == queue.length - 1 ? "" : ", "));
        }
        System.out.println();
    }

    public enum TaskQueueType {
        CRITICAL_TIME_AND_VERTEX_NUMBER_WAY_2, CONNECTIVITY_CRITICAL_VERTEX_NUMBER_WAY_10, CRITICAL_TIME_WAY_16
    }

    public static double getCriticalWayFromStartByTime(TaskGraph taskGraph, int from, double value, int[][] adjacencyMatrix) {
        boolean isEndVertex = true;
        List<Double> criticalWaysVariants = new ArrayList<>();
        for (int i = 0; i < adjacencyMatrix[from].length; i++) {
            if (adjacencyMatrix[from][i] == 1) {
                isEndVertex = false;
//                value += taskGraph.getTaskWeightByPos(from);
                criticalWaysVariants.add(getCriticalWayFromStartByTime(i, value, adjacencyMatrix) + taskGraph.getTaskWeightByPos(i));
//                return getCriticalWayFromStartByTime(i, value, adjacencyMatrix) + taskGraph.getTaskWeightByPos(i);
            }
        }
        if (isEndVertex) {
            return 0;//taskGraph.getTaskWeightByPos(from);
        } else {
            double max = 0.0;
            for (Double criticalWaysVariant : criticalWaysVariants) {
                if (criticalWaysVariant > max) max = criticalWaysVariant;
            }
            return max;
        }
    }
}
