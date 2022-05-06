package Roto.Gui.Tree;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class NodeSelectionListener extends MouseAdapter {

	JTree tree;

	public NodeSelectionListener(JTree tree) {
		this.tree = tree;
	}

	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		int row = tree.getRowForLocation(x, y);
		TreePath path = tree.getPathForRow(row);
		
		if (path != null && e.getClickCount() == 1) {
			CheckNode node = (CheckNode) path.getLastPathComponent();
			boolean isSelected = !(node.isSelected());
	
			node.setSelected(isSelected);

			tree.revalidate();
			tree.repaint();
		}
	} 

}
