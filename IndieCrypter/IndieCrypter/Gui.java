package IndieCrypter;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.SystemColor;

public class Gui {
	// Swing components for building
	// TextPanel=text Panel | PPanel=Popup Panel |StaPanel=staganography panel
	
	JFrame frame;
	JPanel mainPanel, TextPanel, Stapanel, PPanel;
	JOptionPane Popup;
	JTextField passwd;		  
	JTabbedPane tP;			 
	
	// For Text
	JTextArea string, result; // JTExtArea Support multiple line system
	JButton enc,dec,cop,pas,exp,imp;
	JLabel stringL,resultL;
	JScrollPane sPane, rPane;
	
	// For stega
	JTextField source;
	JButton select, enc1, dec1, imp1;
	JScrollPane mPane;
	JTextArea message;
	private JLabel label_1;

	//Design of Gui feature
	public Gui(){
		frame = new JFrame("IndieCrypter");
		frame.setBackground(SystemColor.activeCaption);
		mainPanel = new JPanel();
		mainPanel.setBackground(Color.WHITE);
		tP= new JTabbedPane();
		tP.setBackground(Color.WHITE);

		PPanel= new JPanel(new GridLayout(0,1));		
		PPanel.add(new JLabel("Type password!:"));
		passwd = new JTextField();
		PPanel.add(passwd);
		
		stegaPenal();
		textPenal();
		
		mainPanel.setOpaque(true);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
		mainPanel.setLayout(new BorderLayout(10,10));
		mainPanel.add(tP);
		
		frame.setSize(750, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.getContentPane().add(mainPanel);
		
	
		
	}
	public void textPenal(){
		TextPanel= new JPanel();
		TextPanel.setBackground(Color.WHITE);
		TextPanel.setLayout(null);
		
			string = new JTextArea("String to be Encrypted or Decrypted");
			result = new JTextArea("Result");
			enc = new JButton("Encryption");
			dec = new JButton("Decryption");
			cop = new JButton("Copy");
			pas = new JButton("Paste");
			exp = new JButton("Export");
			imp = new JButton("Import");
			stringL = new JLabel("Input");
			resultL = new JLabel("En&De coded Text");
			sPane = new JScrollPane(string);
			rPane = new JScrollPane(result);
			
			result.setEditable(false);
			resultL.setHorizontalAlignment(SwingConstants.CENTER);
			stringL.setHorizontalAlignment(SwingConstants.CENTER);
			resultL.setFont(new Font("Cambria", Font.BOLD | Font.ITALIC, 17));
			stringL.setFont(new Font("Cambria", Font.BOLD | Font.ITALIC, 17));
			
			sPane.setBounds(12, 36, 460, 141);
			rPane.setBounds(12, 222, 460, 141);
			enc.setBounds(12, 373, 330, 30);
			dec.setBounds(367, 373, 330, 30);	
			cop.setBounds(542, 36, 120, 30);
			pas.setBounds(542, 74, 120, 30);
			exp.setBounds(542, 154, 120, 30);
			imp.setBounds(542, 114, 120, 30);
			stringL.setBounds(170,10,152,23);
			resultL.setBounds(157, 187, 173, 34);
			string.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			result.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			
			TextPanel.add(sPane);
			TextPanel.add(rPane);
			TextPanel.add(enc);
			TextPanel.add(dec);
			TextPanel.add(cop);
			TextPanel.add(pas);
			TextPanel.add(exp);
			TextPanel.add(imp);
			TextPanel.add(stringL);
			TextPanel.add(resultL);
			
			label_1 = new JLabel("");
			Image img = new ImageIcon(this.getClass().getResource("/nsa_logo.jpg")).getImage();
			label_1.setIcon(new ImageIcon(img));
			label_1.setBounds(529, 206, 155, 157);
			TextPanel.add(label_1);

			tP.addTab("Text", TextPanel);
	}
	public void stegaPenal(){
		Stapanel= new JPanel();
		Stapanel.setBackground(Color.WHITE);
		Stapanel.setLayout(null);
		source = new JTextField();
		source.setEditable(false);
		message = new JTextArea("Input message or output hidden message");
		message.setBackground(SystemColor.inactiveCaptionBorder);
		mPane = new JScrollPane(message);
		enc1 = new JButton("Encryption");
		dec1 = new JButton("Decryption");
		imp1 = new JButton("Import");
		select = new JButton("Select Img...");
		
		source.setBounds(12, 36, 417, 30);
		mPane.setBounds(12,92,417,250);
		enc1.setBounds(12, 373, 330, 30);
		dec1.setBounds(367,373,330,30);
		imp1.setBounds(496,87,128,30);
		select.setBounds(496,35,128,30);
		message.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		Stapanel.add(source);
		Stapanel.add(mPane);
		Stapanel.add(enc1);
		Stapanel.add(dec1);
		Stapanel.add(imp1);
		Stapanel.add(select);
		
		JLabel label = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("/nsa_logo.jpg")).getImage();
		label.setIcon(new ImageIcon(img));
		label.setBounds(529, 136, 156, 174);
		Stapanel.add(label);
				
		tP.addTab("Image", Stapanel);
	}
	public int Show(){
		int buffer = JOptionPane.showConfirmDialog(null, PPanel, "PASSWARD",
				JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
		if(passwd.getText().length()!=0){
			return buffer;
		}else{
			JOptionPane.showMessageDialog(null, "Please Type password");
			return JOptionPane.CANCEL_OPTION;
		}
							
	}
}
