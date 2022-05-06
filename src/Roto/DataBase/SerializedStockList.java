package Roto.DataBase;

import java.io.Serializable;

import java.util.HashMap;

import javax.swing.DefaultListModel;

public class SerializedStockList implements Serializable {

    private static final long serialVersionUID = 7349660826558627104L;

    private HashMap<String, Integer> selection;
    private DefaultListModel fileList;

    public HashMap<String, Integer> getSelection() {
        return selection;
    }

    public void setSelection(HashMap<String, Integer> selection) {
        this.selection = selection;
    }

    public DefaultListModel getFileList() {
        return fileList;
    }

    public void setFileList(DefaultListModel fileList) {
        this.fileList = fileList;
    }


}
