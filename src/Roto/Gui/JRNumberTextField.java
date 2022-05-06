package Roto.Gui;

import java.awt.event.KeyEvent;

import javax.swing.JTextField;


public class JRNumberTextField extends JTextField {

    private static final long serialVersionUID = 2729076552810103775L;
    private String badchars = "`~!@#$%^&*()_+=\\|\"':;?/>.<, ";
    private int maxLength = 0;
    private int maxInterVal;
    private int minInterVal;
    private boolean minIntervalUsed;
    private boolean maxIntervalUsed;

    public JRNumberTextField() {
    }

    public JRNumberTextField(int Value) {
        this.setText(String.valueOf(Value));
    }

    public JRNumberTextField(int Value, int Width) {
        this.setText(String.valueOf(Value));
        this.setColumns(Width);
    }

    public JRNumberTextField(int Value, int Width, boolean isDoubleEnabled) {
        this.setText(String.valueOf(Value));
        this.setColumns(Width);
        if (isDoubleEnabled) {
            this.badchars = "`~!@#$%^&*()_+=\\|\"':;?/><, ";
        }
    }

    public void addBadChars(String characters) {
        this.badchars = this.badchars += characters;
    }

    public void setDubleInputEnabled() {
        this.badchars = this.badchars.replaceAll(".", "");
    }

    public int getNumber() {
        if (getText().trim().length() > 0) {
            return Integer.parseInt(getText());
        } else {
            setText("0");
            return 0;
        }
    }

    public double getNumberDouble() {
        if (getText().trim().length() > 0) {
            return Double.parseDouble(getText());
        } else {
            return 0;
        }
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public void processKeyEvent(KeyEvent ev) {

        char c = ev.getKeyChar();
        String text = getText().trim().replaceAll(",", ".");

        if ((Character.isLetter(c) && !ev.isAltDown()) || badchars.indexOf(c) > -1) {
            ev.consume();
            return;
        }

        if (text.indexOf(".") > 0) {
            String firstPart = text.substring(0, text.indexOf("."));
            String lastPart = text.substring(text.indexOf("."));
            if (lastPart.length() > 7) {
                lastPart = lastPart.substring(0, 7);
                setText(firstPart + lastPart);
            }
            if (maxLength > 0 && firstPart.length() > maxLength) {
                setText(firstPart.substring(0, maxLength) + lastPart);
                setCaretPosition(0);
                ev.consume();
                return;
            }
        } else if (maxLength > 0 && text.length() > maxLength) {
            setText(getText().substring(0, maxLength));
            setCaretPosition(0);
            ev.consume();
            return;
        }

        checkInterVal(ev);

        if (c == '-' && getDocument().getLength() > 0) {
            ev.consume();
        } else if (c == '.' && getText().indexOf(".") > -1) {
            ev.consume();
        } else
            super.processKeyEvent(ev);

    }

    private void checkInterVal(KeyEvent ev) {
        try {

            if (maxIntervalUsed && getNumberDouble() > maxInterVal) {
                ev.consume();
                setText(String.valueOf(maxInterVal));
            }

            if (minIntervalUsed && getNumberDouble() < minInterVal) {
                ev.consume();
                setText(String.valueOf(minInterVal));
            }

        } catch (Exception e) {
        }
    }

    public void setInterVal(int min, int max) {
        setMinInterVal(min);
        setMaxInterVal(max);
    }

    public int getMaxInterVal() {
        return maxInterVal;
    }

    public void setMaxInterVal(int maxInterVal) {
        this.maxInterVal = maxInterVal;
        this.maxIntervalUsed = true;
    }

    public int getMinInterVal() {
        return minInterVal;
    }

    public void setMinInterVal(int minInterVal) {
        this.minInterVal = minInterVal;
        this.minIntervalUsed = true;
    }
}


