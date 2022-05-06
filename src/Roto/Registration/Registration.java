package Roto.Registration;

//REGISZRÁCIÓS PROGRAM KÁDÁR VIKTOR
import javax.swing.*;

import Roto.Basic.SwingConstans;
import Roto.DataBase.SerializedRegistration;
import Roto.Utils.IOUtil;
import Roto.Utils.MessageUtil;
import Roto.Utils.RegisterUtil;

import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.Arrays;

class RegistTextFilter extends javax.swing.filechooser.FileFilter {
	public boolean accept(File f) {
	  String name = f.getName().toUpperCase();
	  return f.isDirectory() ||  name.endsWith(".LIC");
	}

	public String getDescription() {
	  return "Registration forms";
	}
}

public class Registration extends JFrame implements ActionListener {

private JFrame R,E;
private JTextField Rk1,Rk2,Rk3,Rd1,Rd2,Rd3,jtComment;
private JPasswordField jtPassword;
private JButton BtRg,BtRk,btEok,btEm,BtRm,BtRny;
private JTextArea jtContent;
private JFileChooser fc = new JFileChooser("Registry");
private IOUtil ioUtil = IOUtil.getInstance();
private RegisterUtil registerUtil = RegisterUtil.getInstance();
private SerializedRegistration registration;
private MessageUtil message = MessageUtil.getInstance();
private SwingConstans swc = SwingConstans.getInstance();

public void Password(){
   E= new JFrame("Enter password.");
   E.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
   E.setLocation(310,370);
   E.setSize(360,70);
   E.setResizable(false);
   
   JPanel pnPassword= new JPanel();
   pnPassword.add(new JLabel("Key: "));
   pnPassword.add(jtPassword=new JPasswordField("",20));
   pnPassword.add(btEok=new JButton("Ok"));
   pnPassword.add(btEm=new JButton("Cancel"));
   pnPassword.setBorder(BorderFactory.createRaisedBevelBorder());
   
   btEok.addActionListener(this);
   btEm.addActionListener(this);
   jtPassword.addActionListener(this);
   
   E.getContentPane().add(pnPassword);
   E.setVisible(true);
}

void login(){
	//BtRg.setEnabled(true);E.setVisible(false);
	char[] correctPassword =("reg2010roto").toCharArray();
	if (Arrays.equals (jtPassword.getPassword() , correctPassword)){
		BtRg.setEnabled(true);E.setVisible(false);
	}
	else {
		JOptionPane.showMessageDialog(this,"Acces denied !","",JOptionPane.ERROR_MESSAGE);
		jtPassword.setText("");
		jtPassword.requestFocus();
	}
}

void generateKey(){
	String code = registerUtil.getCodeByKey((Rk1.getText()+"     ").substring(0,5),
											(Rk2.getText()+"     ").substring(0,5),
											(Rk3.getText()+"     ").substring(0,5));
	Rd1.setText((code.substring(0,5)).trim());
	Rd2.setText((code.substring(5,10)).trim());
	Rd3.setText((code.substring(10,15)).trim());
}

void saveRegistryFile() {
	SerializedRegistration registrationSave = new SerializedRegistration();
	if (this.registration!=null) {
		registrationSave = registration;
		
		registrationSave.setCode1(Rd1.getText());
		registrationSave.setCode2(Rd2.getText());
		registrationSave.setCode3(Rd3.getText());
		registrationSave.setComment(jtComment.getText().trim());
		registrationSave.setLicenceFile(false);
		
		fc.setAcceptAllFileFilterUsed(false);
		fc.setDialogTitle("Open registry form");
		fc.setCurrentDirectory(new File(swc.registrationPath));
		fc.setSelectedFile(new File(""));
		fc.setFileFilter(new RegistTextFilter());
		if (fc.showSaveDialog(this)==fc.APPROVE_OPTION) {
			File out = fc.getSelectedFile();
			if (!out.getAbsolutePath().toString().toLowerCase().endsWith(".lic")){
				out = new File(out.getAbsolutePath() + ".lic");
			}
			
			if (!out.exists() || (out.exists() && message.showConfirmDialog("This file is exists ! Are you sure you want to overwrite it?"))){
				ioUtil.saveObject(registrationSave, out);
				message.showInformationMessage("Registration form saved !");
			}
		}
	} else {
		message.showInformationMessage("Please open a registration form firstly !");
	}
}

void openRegistryFile() {
  fc.setAcceptAllFileFilterUsed(false);
  fc.setDialogTitle("Open registry form");
  fc.setCurrentDirectory(new File(swc.registrationPath));
  fc.setSelectedFile(new File(""));
  fc.setFileFilter(new RegistTextFilter());
  if (fc.showOpenDialog(this)==fc.APPROVE_OPTION) {
	this.registration = (SerializedRegistration) ioUtil.openObject(fc.getSelectedFile());    
    
    Rk1.setText(registration.getKey1());
    Rk2.setText(registration.getKey2());
    Rk3.setText(registration.getKey3());
    
    jtContent.setText("");
    jtContent.append("Reistration ID: " + registration.getRegisterID()+"\n");
    jtContent.append("Company name: " + registration.getCompanyName()+"\n");
    jtContent.append("Company address: " + registration.getCompanyAddress()+"\n");
    jtContent.append("Operator name: " + registration.getOperator()+"\n");
    jtContent.append("E-mail: " + registration.getEmail()+"\n");
    jtContent.append("Roto salesman: " + registration.getSalesMan()+"\n");
    
    jtComment.setText(registration.getComment());
  }
}

public Registration() {
 R= new JFrame("Program registry");
 R.setLocation(300,200);
 R.setSize(400,350);
 R.setResizable(false);
 R.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
 JPanel pnReg=new JPanel();
 JPanel pnReg2=new JPanel();
 JPanel pnReg24=new JPanel();
 JPanel pnReg3=new JPanel();
 pnReg.add(new JLabel("Key:               "));
 pnReg.add(Rk1=new JTextField("",6));pnReg.add(new JLabel("-"));
 pnReg.add(Rk2=new JTextField("",6));pnReg.add(new JLabel("-"));
 pnReg.add(Rk3=new JTextField("",6));
 pnReg2.add(new JLabel("Password:  "));
 pnReg2.add(Rd1=new JTextField("",6));pnReg2.add(new JLabel("-"));
 pnReg2.add(Rd2=new JTextField("",6));pnReg2.add(new JLabel("-"));
 pnReg2.add(Rd3=new JTextField("",6));
 pnReg24.setLayout(new BorderLayout());
 pnReg24.add(pnReg2,"North");
 pnReg24.add(jtContent=new JTextArea(),"Center");
 pnReg24.add(jtComment=new JTextField(),"South");
 jtComment.setBorder(BorderFactory.createTitledBorder("Remark"));
 jtContent.setEditable(false);
 jtContent.setBorder(BorderFactory.createTitledBorder("Registration file"));
 Rd1.setEditable(false);Rd2.setEditable(false);Rd3.setEditable(false);
 Rk1.setFont(new Font("Courier New",Font.BOLD,17));
 Rk2.setFont(new Font("Courier New",Font.BOLD,17));
 Rk3.setFont(new Font("Courier New",Font.BOLD,17));
 Rd1.setFont(new Font("Courier New",Font.BOLD,17));
 Rd2.setFont(new Font("Courier New",Font.BOLD,17));
 Rd3.setFont(new Font("Courier New",Font.BOLD,17));
 Rd1.setBackground(Color.WHITE);
 Rd2.setBackground(Color.WHITE);
 Rd3.setBackground(Color.WHITE);
 Rd1.setForeground(Color.BLACK);
 Rd2.setForeground(Color.BLACK);
 Rd3.setForeground(Color.BLACK);
 pnReg3.add(BtRny=new JButton("Open"));
 pnReg3.add(BtRg=new JButton("Generate"));
 pnReg3.add(BtRm=new JButton("Save"));
 pnReg3.add(BtRk=new JButton("Exit"));
 BtRm.addActionListener(this);
 BtRny.addActionListener(this);
 BtRg.addActionListener(this);
 BtRk.addActionListener(this);
 BtRk.setPreferredSize(new Dimension(90,25));
 BtRm.setPreferredSize(new Dimension(90,25));
 BtRny.setPreferredSize(new Dimension(90,25));
 BtRg.setPreferredSize(new Dimension(90,25));
 R.getContentPane().add(pnReg,"North");
 R.getContentPane().add(pnReg24,"Center");
 R.getContentPane().add(pnReg3,"South");
 Toolkit tx=Toolkit.getDefaultToolkit();
 R.setIconImage(tx.createImage("iko.jpg"));
 pnReg.setBackground(Color.WHITE);
 pnReg2.setBackground(Color.WHITE);
 pnReg3.setBackground(Color.WHITE);
 BtRg.setEnabled(false);
 R.setVisible(true);
 //engedelyez();
 Password();
}

public void actionPerformed(ActionEvent ev) {
if (ev.getSource()==BtRk||ev.getSource()==btEm){System.exit(0);}
if (ev.getSource()==BtRm){saveRegistryFile();}
if (ev.getSource()==BtRny){openRegistryFile();}
if (ev.getSource()==BtRg){generateKey();}
if (ev.getSource()==btEok||ev.getSource()==jtPassword){login();}
}

public static void main (String args[]) {
  new Registration();
}
}
