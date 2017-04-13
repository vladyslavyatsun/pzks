package gui.task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by hadgehog on 16.02.14.
 */
public class EditTaskTools extends JToolBar {
    private final int ICON_WIDTH = 20;
    private final int ICON_HEIGHT = 20;

    private EditTaskGraphPanel editTaskGraphPanel;

    private Toolkit toolkit = Toolkit.getDefaultToolkit();

    public EditTaskTools(EditTaskGraphPanel editTaskGraphPanel) {
        this.editTaskGraphPanel = editTaskGraphPanel;
        setOrientation(SwingConstants.VERTICAL);
        setFloatable(false);

        JButton mouseArrowButton = new JButton();
        Image mouseArrowIcon = (new ImageIcon(getClass().getResource("/gui/icons/task/cursor_arrow_icon.png"))).getImage();;
//        ImageIcon mouseArrowIcon =new ImageIcon("gui\\icons\\task\\cursor_arrow_icon.png");
        mouseArrowButton.setIcon(scaledImageIcon(mouseArrowIcon));
        mouseArrowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                editTaskGraphPanel.setActiveTaskToolType(EditTaskGraphPanel.TaskToolType.MOUSE);
                editTaskGraphPanel.getTaskGraphPanel().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
        add(mouseArrowButton);

        JButton newVertexButton = new JButton();
        Image newVertexIcon = (new ImageIcon(getClass().getResource("/gui/icons/task/new_vertex_icon.png"))).getImage();
//        ImageIcon newVertexIcon = new ImageIcon("src\\gui\\icons\\task\\new_vertex_icon.png");
        newVertexButton.setIcon(scaledImageIcon(newVertexIcon));
        newVertexButton.setToolTipText("Нова вершина графа");
        newVertexButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                editTaskGraphPanel.setActiveTaskToolType(EditTaskGraphPanel.TaskToolType.NEW_VERTEX);
                Image image = (new ImageIcon(getClass().getResource("/gui/icons/task/new_vertex_icon_pointer.png"))).getImage();
//                Image image = toolkit.getImage("src\\gui\\icons\\task\\new_vertex_icon_pointer.png");
                Cursor newVertexPointer = toolkit.createCustomCursor(image, new Point(editTaskGraphPanel.getX(),
                        editTaskGraphPanel.getY()), "new vertex");
                         editTaskGraphPanel.getTaskGraphPanel().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
//                editTaskGraphPanel.getTaskGraphPanel().setCursor(newVertexPointer);
            }
        });
        add(newVertexButton);

        JButton newLinkButton = new JButton();
        Image linkIcon = (new ImageIcon(getClass().getResource("/gui/icons/task/link_icon.png"))).getImage();
//        ImageIcon linkIcon = new ImageIcon("src\\gui\\icons\\task\\link_icon.png");
        newLinkButton.setIcon(scaledImageIcon(linkIcon));
        newLinkButton.setToolTipText("Нове ребро графа");
        newLinkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                editTaskGraphPanel.setActiveTaskToolType(EditTaskGraphPanel.TaskToolType.CONNECTION);
                Image image = (new ImageIcon(getClass().getResource("/gui/icons/task/link_icon_pointer.png"))).getImage();
//                Image image = toolkit.getImage("src\\gui\\icons\\task\\link_icon_pointer.png");
                Cursor connectionPointer = toolkit.createCustomCursor(image, new Point(editTaskGraphPanel.getX(),
                        editTaskGraphPanel.getY()), "connection");
                //editTaskGraphPanel.getTaskGraphPanel().setCursor(connectionPointer);
                editTaskGraphPanel.getTaskGraphPanel().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
        add(newLinkButton);

        JButton deleteButton = new JButton();
        Image deleteIcon = (new ImageIcon(getClass().getResource("/gui/icons/task/delete_icon.png"))).getImage();
//        ImageIcon deleteIcon = new ImageIcon("src\\gui\\icons\\task\\delete_icon.png");
        deleteButton.setIcon(scaledImageIcon(deleteIcon));
        deleteButton.setToolTipText("Видалити(вершину/ребро)");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                editTaskGraphPanel.setActiveTaskToolType(EditTaskGraphPanel.TaskToolType.REMOVE);
                Image image = (new ImageIcon(getClass().getResource("/gui/icons/task/delete_icon_pointer.png"))).getImage();
//                Image image = toolkit.getImage("src\\gui\\icons\\task\\delete_icon_pointer.png");
                Cursor removePointer = toolkit.createCustomCursor(image, new Point(editTaskGraphPanel.getX(),
                        editTaskGraphPanel.getY()), "remove");
              //  editTaskGraphPanel.getTaskGraphPanel().setCursor(removePointer);
                editTaskGraphPanel.getTaskGraphPanel().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
        add(deleteButton);
    }

    private ImageIcon scaledImageIcon(Image image) {
        Image newimg = image.getScaledInstance(ICON_WIDTH, ICON_HEIGHT,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(newimg);
        return newIcon;
    }
}
