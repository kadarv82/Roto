package Roto.Utils;

import java.io.File;
import java.io.FileOutputStream;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.swing.ImageIcon;

import Roto.Basic.SwingConstans;

import Roto.Gui.Frames;
import Roto.Gui.Tree.CheckNode;


public class ExportThread extends Thread {
    private IOUtil ioUtil;
    private File locationDirectory;
    private MessageUtil message;
    private PropertyUtil propertyUtil;
    private SwingConstans swc;
    private UserUtil userUtil;
    private Frames frames;

    public ExportThread(File locationDirectory) {
        ioUtil = IOUtil.getInstance();
        message = MessageUtil.getInstance();
        propertyUtil = PropertyUtil.getInstance();
        swc = SwingConstans.getInstance();
        frames = Frames.getInstance();
        userUtil = UserUtil.getInstance();
        this.locationDirectory = locationDirectory;
    }

    public void run() {
        frames.jtExportInfo.setText("");
        exportProgram();
    }

    private void createDirectories() {
        String exportRoot = locationDirectory.getAbsolutePath() + "\\";
        frames.jtExportInfo.append(propertyUtil.getLangText("InternalFrame.Profiles.ExportCreateDirectory") + " ..." +
                                   "\n");

        //Create main directory
        new File(exportRoot).mkdir();
        new File(exportRoot + "\\Bin").mkdir();
        new File(exportRoot + "\\Bin\\Etc").mkdir();
        new File(exportRoot + "\\Bin\\Etc\\Images").mkdir();
        new File(exportRoot + "\\Bin\\Etc\\Images\\Flags").mkdir();
        new File(exportRoot + "\\Bin\\Etc\\Images\\Icon").mkdir();
        new File(exportRoot + "\\Bin\\Etc\\Images\\Viewer").mkdir();
        new File(exportRoot + "\\Bin\\Properties").mkdir();
        new File(exportRoot + "\\Bin\\Properties\\Basic").mkdir();
        new File(exportRoot + "\\Bin\\Properties\\Profiles").mkdir();
        new File(exportRoot + "\\Dir").mkdir();
        new File(exportRoot + "\\Dir\\Database").mkdir();
        new File(exportRoot + "\\Dir\\Excel").mkdir();
        new File(exportRoot + "\\Dir\\List").mkdir();
        new File(exportRoot + "\\Dir\\Pricelist").mkdir();
        new File(exportRoot + "\\Dir\\Rebate").mkdir();
        new File(exportRoot + "\\Dir\\Registration").mkdir();
        new File(exportRoot + "\\Dir\\StockList").mkdir();
        new File(exportRoot + "\\Dir\\Xml").mkdir();
        new File(exportRoot + "\\Setup").mkdir();

        frames.jtExportInfo.append("\n" + propertyUtil.getLangText("InternalFrame.Profiles.CopyDirectory") + " ..." +
                                   "\n\n");

        //Copy full directories - Images/...
        frames.jtExportInfo.append(exportRoot + swc.iconPath + "\n");
        ioUtil.copyDirectory(new File(swc.iconPath), new File(exportRoot + swc.iconPath));
        frames.jtExportInfo.append(exportRoot + swc.flagPath + "\n");
        ioUtil.copyDirectory(new File(swc.flagPath), new File(exportRoot + swc.flagPath));
        frames.jtExportInfo.append(exportRoot + swc.viewerPath + "\n");
        ioUtil.copyDirectory(new File(swc.viewerPath), new File(exportRoot + swc.viewerPath));
        frames.jtExportInfo.append(exportRoot + swc.setupPath + "\n");
        ioUtil.copyDirectory(new File(swc.setupPath), new File(exportRoot + swc.setupPath));

        //Copy full directories - dir/...
        frames.jtExportInfo.append(exportRoot + swc.RebatePath + "\n");
        ioUtil.copyDirectory(new File(swc.RebatePath), new File(exportRoot + swc.RebatePath));
        frames.jtExportInfo.append(exportRoot + swc.dataBasePath + "\n");
        ioUtil.copyDirectory(new File(swc.dataBasePath), new File(exportRoot + swc.dataBasePath));
        frames.jtExportInfo.append(exportRoot + swc.priceListPath + "\n");
        ioUtil.copyDirectory(new File(swc.priceListPath), new File(exportRoot + swc.priceListPath));
        frames.jtExportInfo.append(exportRoot + swc.xmlPath + "\n");
        ioUtil.copyDirectory(new File(swc.xmlPath), new File(exportRoot + swc.xmlPath));


        frames.jtExportInfo.append("\n\n" + propertyUtil.getLangText("InternalFrame.Profiles.CopyFiles") + " ..." +
                                   "\n\n");
        //Copy files - Basic
        List<String> fileList = new ArrayList<String>();
        fileList.add("Database.properties");
        fileList.add("DatabaseList.properties");
        fileList.add("Pricelist.properties");
        fileList.add("Profiles.properties");
        fileList.add("Users.properties");
        fileList.add("Roto.properties");

        for (int i = 0; i < swc.langCodes.length; i++) {
            fileList.add("Language_" + swc.langCodes[i] + ".properties");
        }

        for (String fileName : fileList) {
            File source = new File(swc.basicPath + "\\" + fileName);
            File target = new File(exportRoot + "\\" + swc.basicPath + "\\" + fileName);
            if (source.exists()) {
                //System.out.println(source.getName());
                ioUtil.copyFile(source, target);
                frames.jtExportInfo.append(target.getAbsolutePath() + "\n");
                frames.jtExportInfo.setCaretPosition(frames.jtExportInfo
                                                           .getText()
                                                           .length());
            }
        }

        //Copy files - Root
        fileList = new ArrayList<String>();
        fileList.add("forms-1.2.1.jar");
        fileList.add("jxl.jar");
        fileList.add("jxl.jar");
        fileList.add("Roto.jar");
        //fileList.add("Registration.bat");
        //fileList.add("RemoveDemo.bat");
        fileList.add("Roto.bat");
        fileList.add("Roto.exe");
        fileList.add("Setup.bat");

        for (String fileName : fileList) {
            File source = new File(fileName);
            File target = new File(exportRoot + "\\" + fileName);
            if (source.exists()) {
                //System.out.println(source.getName());
                ioUtil.copyFile(source, target);
                frames.jtExportInfo.append(target.getAbsolutePath() + "\n");
                frames.jtExportInfo.setCaretPosition(frames.jtExportInfo
                                                           .getText()
                                                           .length());
            }
        }

        frames.jtExportInfo.append("\n\n" + propertyUtil.getLangText("InternalFrame.Profiles.CreateProfiles") + " ..." +
                                   "\n\n");
        //Create users and profiles
        List<String> selectedUsers = new ArrayList<String>();
        List<String> selectedProfiles = new ArrayList<String>();
        List<String> allUsers = userUtil.getUserList();

        //Fill selected profiles and users to list
        CheckNode root = frames.nodeRootExport;
        Enumeration e = root.children();
        while (e.hasMoreElements()) {
            //Users
            CheckNode profileUserNode = (CheckNode) e.nextElement();
            //Add users
            if (!selectedUsers.contains(profileUserNode.getTitle()))
                selectedUsers.add(profileUserNode.getTitle());

            Enumeration en = profileUserNode.children();
            while (en.hasMoreElements()) {
                //Profiles
                CheckNode profileProfileNode = (CheckNode) en.nextElement();
                //Add profiles
                if (!selectedProfiles.contains(profileProfileNode.getTitle()))
                    selectedProfiles.add(profileProfileNode.getTitle());
            }
        }

        //Remove not existing profiles
        File targetProfileProperty = new File(exportRoot + "\\" + swc.basicPath + "\\" + "Profiles.properties");
        File targetUserProperty = new File(exportRoot + "\\" + swc.basicPath + "\\" + "Users.properties");

        if (targetProfileProperty.exists() && targetUserProperty.exists()) {

            Properties targetProfileProperties = propertyUtil.loadProperty(targetProfileProperty.getAbsolutePath());
            Properties targetUserProperties = propertyUtil.loadProperty(targetUserProperty.getAbsolutePath());

            for (String user : allUsers) {

                List<String> allProfiles = userUtil.getProfileList(user);
                //Remove not existing profiles:
                for (String profile : allProfiles) {
                    String profileDirectoryKey = "Profiles." + profile + ".Directory";
                    String profileRebateDirectory = "Profiles." + profile + ".RebateFile";
                    String profileUserKey = "Profiles." + profile + ".User";

                    if (!selectedProfiles.contains(profile)) {
                        //System.out.println("Removing profile : " + profile);
                        targetProfileProperties.remove(profileDirectoryKey);
                        targetProfileProperties.remove(profileRebateDirectory);
                        targetProfileProperties.remove(profileUserKey);

                    }
                    //Copy directory, if profile exists
                    else {
                        File sourceDirectory =
                            new File(targetProfileProperties.getProperty(profileDirectoryKey).toString());
                        File targetDirectory =
                            new File(exportRoot + "\\" +
                                     targetProfileProperties.getProperty(profileDirectoryKey).toString());
                        //System.out.println("copy source: " + sourceDirectory.getAbsolutePath());
                        //System.out.println("copy target: " + targetDirectory.getAbsolutePath());
                        ioUtil.copyDirectory(sourceDirectory, targetDirectory);
                    }

                }

                //Remove not existing users
                if (!selectedUsers.contains(user)) {
                    //System.out.println("Removing user : " + user);
                    targetUserProperties.remove("Users." + user + ".Name");
                    targetUserProperties.remove("Users.LastLogin");
                }

            }

            //Save modified target properties
            try {
                targetProfileProperties.store(new FileOutputStream(targetProfileProperty.getAbsolutePath()), null);
                targetUserProperties.store(new FileOutputStream(targetUserProperty.getAbsolutePath()), null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            frames.jbExportDirectory.setIcon(new ImageIcon(swc.iconPath + "Open.jpg"));
            frames.jbProfileExport.setEnabled(true);
            frames.jtExportInfo.append("\n\n" + propertyUtil.getLangText("InternalFrame.Profiles.Expport.Ready") + "." +
                                       "\n\n");
            frames.jtExportInfo.setCaretPosition(frames.jtExportInfo
                                                       .getText()
                                                       .length());
        }


    }


    public void exportProgram() {
        try {
            //Create and copy directories
            createDirectories();

        } catch (Exception e) {
            message.showErrorMessage(propertyUtil.getLangText("Error.Export") + " " + e.getMessage(), "");
        }
    }

}
