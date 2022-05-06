package Roto.Utils;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;

import Roto.Basic.SwingConstans;

import Roto.Gui.Frames;

public class AdminUtil {

    private static AdminUtil instance = null;
    private Frames frames;
    private PropertyUtil propertyUtil;
    private UserUtil userUtil;
    private SwingConstans swc;
    private MessageUtil message;
    private boolean isAdminLoggedIn;
    private boolean isOperatorLoggedIn;
    private char[] adminPassWord;
    private char[] operatorPassWord;

    public static AdminUtil getInstance() {
        if (instance == null) {
            instance = new AdminUtil();
        }
        return instance;
    }

    public AdminUtil() {
        frames = Frames.getInstance();
        propertyUtil = PropertyUtil.getInstance();
        userUtil = UserUtil.getInstance();
        swc = SwingConstans.getInstance();
        adminPassWord = swc.passWordAdmin.toCharArray();
        operatorPassWord = swc.passWordOperator.toCharArray();
        message = MessageUtil.getInstance();
    }

    public void fillLoginCombo() {
        frames.jcbAdminLogin.removeAllItems();
        frames.jcbAdminLogin.addItem("Default");
        Properties profileProperty = propertyUtil.getUserProperty();
        Enumeration enumeration = profileProperty.keys();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement().toString();
            String value = profileProperty.getProperty(key);
            if (key.toLowerCase()
                   .trim()
                   .endsWith(".name")) {
                frames.jcbAdminLogin.addItem(value);
            }
        }
    }

    public void fillProfileCombo(String UserName) {
        frames.jcbAdminProfiles.removeAllItems();
        if (frames.jcbAdminLogin.getSelectedIndex() == 0) {
            frames.jcbAdminProfiles.addItem("Default");
        }
        Properties profileProperty = propertyUtil.getProfileProperty();
        Enumeration enumeration = profileProperty.keys();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement().toString();
            String value = profileProperty.getProperty(key);
            if (value.toLowerCase()
                     .trim()
                     .equalsIgnoreCase(UserName.toLowerCase().trim())) {
                StringTokenizer tokenizer = new StringTokenizer(key);
                tokenizer.nextToken(".");
                frames.jcbAdminProfiles.addItem(tokenizer.nextToken("."));
            }
        }

    }

    public void login(char[] password) {
        if (Arrays.equals(password, adminPassWord)) {
            this.isAdminLoggedIn = true;
            setGUILoggedIn();
        } else if (Arrays.equals(password, operatorPassWord)) {
            this.isOperatorLoggedIn = true;
            setGUILoggedIn();
        } else {
            setGUILoggedOut();
            message.showInformationMessage(propertyUtil.getLangText("Information.Login.Invalid"));
        }


    }

    public void logout() {
        setGUILoggedOut();
    }

    private void setGUILoggedIn() {
        frames.jbAdminLogin.setEnabled(false);
        frames.jbAdminLogout.setEnabled(true);
        frames.jbLoginStatus.setIcon(new ImageIcon(swc.iconPath + "LoginAdmin.jpg"));
        frames.jbLoginStatus.setToolTipText(propertyUtil.getLangText("Frame.Main.LoginStatus") + " : Admin");

        frames.jbGroupAdd.setEnabled(isAdminLoggedIn);
        frames.jbGroupDelete.setEnabled(isAdminLoggedIn);
        frames.jbGroupModify.setEnabled(isAdminLoggedIn);
        frames.jbGroupNameAdd.setEnabled(isAdminLoggedIn);
        frames.jbGroupNameDelete.setEnabled(isAdminLoggedIn);
        frames.jbGroupNameModify.setEnabled(isAdminLoggedIn);
        frames.jbGroupSave.setEnabled(isAdminLoggedIn);

        frames.jmiLang.setEnabled(true);
        frames.jmiReplaceData.setEnabled(true);
        frames.jmiTypes.setEnabled(true);
        frames.jmiAdminLang.setEnabled(true);
        frames.jmiAdminReplaceData.setEnabled(true);
        frames.jmiAdminTypes.setEnabled(true);

        frames.tfPassWord.setText("");
        frames.jriAdminLogin.setVisible(false);
    }

    private void setGUILoggedOut() {
        this.isAdminLoggedIn = false;
        this.isOperatorLoggedIn = false;
        if (userUtil.getUserName() == null || userUtil.getUserName().length() == 0) {
            frames.jbLoginStatus.setIcon(new ImageIcon(swc.iconPath + "LoginDefault.jpg"));
            frames.jbLoginStatus.setToolTipText(propertyUtil.getLangText("Frame.Main.LoginStatus"));
        } else {
            frames.jbLoginStatus.setIcon(new ImageIcon(swc.iconPath + "Profiles.jpg"));
            frames.jbLoginStatus.setToolTipText(propertyUtil.getLangText("Frame.Main.LoginStatus") + " : " +
                                                userUtil.getUserName());
        }

        frames.jbAdminLogin.setEnabled(true);
        frames.jbAdminLogout.setEnabled(false);

        frames.jbGroupAdd.setEnabled(false);
        frames.jbGroupDelete.setEnabled(false);
        frames.jbGroupModify.setEnabled(false);
        frames.jbGroupNameAdd.setEnabled(false);
        frames.jbGroupNameDelete.setEnabled(false);
        frames.jbGroupNameModify.setEnabled(false);
        frames.jbGroupSave.setEnabled(false);

        frames.jmiLang.setEnabled(false);
        frames.jmiReplaceData.setEnabled(false);
        frames.jmiTypes.setEnabled(false);
        frames.jmiAdminLang.setEnabled(false);
        frames.jmiAdminReplaceData.setEnabled(false);
        frames.jmiAdminTypes.setEnabled(false);
    }

    public boolean isAdminLoggedIn() {
        return isAdminLoggedIn;
    }

    public void setAdminLoggedIn(boolean isLoggedIn) {
        this.isAdminLoggedIn = isLoggedIn;
    }

    public boolean isOperatorLoggedIn() {
        return isOperatorLoggedIn;
    }

    public void setOperatorLoggedIn(boolean isOperatorLoggedIn) {
        this.isOperatorLoggedIn = isOperatorLoggedIn;
    }
}
