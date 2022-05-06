package Roto.Gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import javax.swing.TransferHandler;

import Roto.Basic.Actions;

public class ListHandler extends TransferHandler {
  
    private static final long serialVersionUID = 14837277335699567L;


    private String actionCommand;

    public ListHandler(String actionCommand) {
        this.actionCommand = actionCommand;
    }

    public boolean canImport(TransferSupport support) {
        if (!support.isDrop()) {
            return false;
        }

        return support.isDataFlavorSupported(DataFlavor.stringFlavor);
    }

    public boolean importData(TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }

        Transferable transferable = support.getTransferable();
        String line;
        try {
            line = (String) transferable.getTransferData(DataFlavor.stringFlavor);
        } catch (Exception e) {
            return false;
        }

        Actions.getInstance().doAction(actionCommand);
        return true;
    }
}
