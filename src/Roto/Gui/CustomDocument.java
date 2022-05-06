package Roto.Gui;

import java.util.Enumeration;
import java.util.StringTokenizer;

import javax.swing.JTextArea;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class CustomDocument extends PlainDocument {
	  private int MAX_WIDTH = 90;
	  private JTextArea textArea;
	 
	  CustomDocument(JTextArea textArea) {
	    this.textArea = textArea;
	  }

	  public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {

		  if (str.trim().length()>1){
			  StringTokenizer tokenizer = new StringTokenizer (str);
			  while (tokenizer.hasMoreTokens()){
				 String nextToken = tokenizer.nextToken() + " ";
				 offs = textArea.getText().length();
				 addWord(offs, nextToken, a);
			  }
		  } else {
			  addWord(offs, str, a);  
		  }

	  }
	 
	  private int getActualRowLength(int offset) throws BadLocationException{
		  int actRow = textArea.getLineOfOffset( offset );   
		  int rowBeginn = textArea.getLineStartOffset( actRow );   
		  int rowEnd = textArea.getLineEndOffset( actRow );
		  return  rowEnd - rowBeginn;  
	  }
	  
	  private int getActualRowStart(int offset) throws BadLocationException{
		  int actRow = textArea.getLineOfOffset( offset );   
		  return textArea.getLineStartOffset( actRow );   
	  }
	  
	  private int getActualRowEnd(int offset) throws BadLocationException{
		  int actRow = textArea.getLineOfOffset( offset );   
		  return textArea.getLineEndOffset( actRow );   
	  }
	  
	  private void addWord(int offs, String str, AttributeSet a) throws BadLocationException{
			if (str.equals("\t")){
				str = " ";
			}
			
			if(getActualRowLength(offs) > MAX_WIDTH) {
			
				int rowStart = getActualRowStart(offs);
				int rowEnd = offs;
				String line =  textArea.getText().substring(rowStart, rowEnd);
				int lastSpaceIndex = line.lastIndexOf(" ");
				
				if (lastSpaceIndex > 0 && str.length() == 1){
					
					//int rowEnd = getActualRowEnd(offs);
	
					String lastWord = line.substring(lastSpaceIndex, line.length())+str;
					String cropText = line.substring(0, lastSpaceIndex);
					//String textToInsert = fullText.substring(0, lastSpaceIndex) + "\n" +lastWord;
					//System.out.println(line);
					//System.out.println(cropText);
					//System.out.println(lastWord);

					//super.insertString(rowStart + rowEnd,  "\n" + lastWord, a);
					//super.replace(rowStart, rowStart+cropText.length(), cropText, a);
					//remove(rowStart + cropText.length(), getActualRowEnd(offs));
					//str = "\n" +lastWord;
					//return;
				} 
				str ="\n" + str;
				
			}
			
			super.insertString(offs, str, a);	  

	  }
}
