package gui.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by hadgehog on 16.02.14.
 */
public class EditSystemTools extends JToolBar {
    private final int ICON_WIDTH = 20;
    private final int ICON_HEIGHT = 20;

    private EditSystemGraphPanel editSystemGraphPanel;
    private Toolkit toolkit = Toolkit.getDefaultToolkit();

    public EditSystemTools(EditSystemGraphPanel editSystemGraphPanel) {
        this.editSystemGraphPanel = editSystemGraphPanel;
        setOrientation(SwingConstants.VERTICAL);
        setFloatable(false);

        JButton mouseArrowButton = new JButton();
        Image mouseArrowIcon = (new ImageIcon(getClass().getResource("/gui/icons/system/cursor_arrow_icon.png"))).getImage();
//        ImageIcon mouseArrowIcon = new ImageIcon("src\\gui\\icons\\system\\cursor_arrow_icon.png");
        mouseArrowButton.setIcon(scaledImageIcon(mouseArrowIcon));
        mouseArrowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                editSystemGraphPanel.setActiveSystemToolType(EditSystemGraphPanel.SystemToolType.MOUSE);
                editSystemGraphPanel.getSystemGraphPanel().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
        add(mouseArrowButton);

        JButton newVertexButton = new JButton();
        Image newVertexIcon = (new ImageIcon(getClass().getResource("/gui/icons/task/new_vertex_icon.png"))).getImage();
//        ImageIcon newVertexIcon = new ImageIcon("src\\gui\\icons\\system\\new_vertex_icon.png");
        newVertexButton.setIcon(scaledImageIcon(newVertexIcon));
        newVertexButton.setToolTipText("Нова вершина графа");
        newVertexButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                editSystemGraphPanel.setActiveSystemToolType(EditSystemGraphPanel.SystemToolType.NEW_VERTEX);
                Image image = (new ImageIcon(getClass().getResource("/gui/icons/system/new_vertex_icon_pointer.png"))).getImage();
//                Image image = toolkit.getImage("src\\gui\\icons\\system\\new_vertex_icon_pointer.png");
                Cursor newVertexPointer = toolkit.createCustomCursor(image, new Point(0, 0), "new vertex");
               // editSystemGraphPanel.getSystemGraphPanel().setCursor(newVertexPointer);
            }
        });
        add(newVertexButton);

        JButton newLinkButton = new JButton();
        Image linkIcon = (new ImageIcon(getClass().getResource("/gui/icons/task/link_icon.png"))).getImage();
//        ImageIcon linkIcon = new ImageIcon("src\\gui\\icons\\system\\link_icon.png");
        newLinkButton.setIcon(scaledImageIcon(linkIcon));
        newLinkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                editSystemGraphPanel.setActiveSystemToolType(EditSystemGraphPanel.SystemToolType.CONNECTION);
                Image image = (new ImageIcon(getClass().getResource("/gui/icons/system/link_icon_pointer.png"))).getImage();
//                Image image = toolkit.getImage("src\\gui\\icons\\system\\link_icon_pointer.png");
                Cursor connectionPointer = toolkit.createCustomCursor(image, new Point(0, 0), "connection");
               // editSystemGraphPanel.getSystemGraphPanel().setCursor(connectionPointer);
            }
        });
        newLinkButton.setToolTipText("Нове ребро графа");
        add(newLinkButton);

        JButton deleteButton = new JButton();
        Image deleteIcon = (new ImageIcon(getClass().getResource("/gui/icons/system/delete_icon.png"))).getImage();
//        ImageIcon deleteIcon = new ImageIcon("src\\gui\\icons\\system\\delete_icon.png");
        deleteButton.setIcon(scaledImageIcon(deleteIcon));
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                editSystemGraphPanel.setActiveSystemToolType(EditSystemGraphPanel.SystemToolType.REMOVE);
                Image image = (new ImageIcon(getClass().getResource("/gui/icons/system/delete_icon_pointer.png"))).getImage();
//                Image image = toolkit.getImage("src\\gui\\icons\\system\\delete_icon_pointer.png");
                Cursor removePointer = toolkit.createCustomCursor(image, new Point(0, 0), "remove");
                //editSystemGraphPanel.getSystemGraphPanel().setCursor(removePointer);
            }
        });
        deleteButton.setToolTipText("Видалити(вершину/ребро)");
        add(deleteButton);
    }

    private ImageIcon scaledImageIcon(Image image) {
        Image newimg = image.getScaledInstance(ICON_WIDTH, ICON_HEIGHT,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(newimg);
        return newIcon;
    }
}
