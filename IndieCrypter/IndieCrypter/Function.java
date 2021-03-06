package IndieCrypter;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import IndieCrypter.AES;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Function {
	//JfileChooser will be used when to pop up selecting file or creating new files
	JFileChooser fC = new JFileChooser();
	JFileChooser fC1= new JFileChooser();

	//FileNameExtensionFilter allows to limit the types of file one can select
	FileNameExtensionFilter textfilter = new FileNameExtensionFilter("TEXT files (*.txt)", "txt");
	FileNameExtensionFilter ImagefilterPng = new FileNameExtensionFilter("png files (*.png)", "png");

	File file;				//for general importing and exporting files
	BufferedImage image;	//for modifying pixels of an image
	AES AES = new AES();	// Wanna use AES?

	public Function(){
		fC.setFileFilter(textfilter);
		fC1.setFileFilter(ImagefilterPng);
	}
	
	//Text Encryption is Simple! set password and return what is encrypted!
	public String Encryption(String input, String passwd){
		AES.setKey(passwd);				
		return AES.Encrypt(input);
	}
	public String Decryption(String input, String passwd){
		AES.setKey(passwd);
		return AES.Decrypt(input);
	}
	
	// Import from a text file to be chosen
	public String Import(){
		StringBuilder sB = new StringBuilder();
		String temp = "";
		if(fC.showOpenDialog(null)== JFileChooser.APPROVE_OPTION){
			try {
				FileInputStream fIS = new FileInputStream(fC.getSelectedFile());
				InputStreamReader iSR = new InputStreamReader(fIS, "UTF-8");
				BufferedReader bR = new BufferedReader(iSR);
				
				try{
					while((temp=bR.readLine())!=null) {
						sB.append(temp);
						sB.append(System.lineSeparator());
					}bR.close();
					return sB.toString();
				}catch(Exception e){
					JOptionPane.showMessageDialog(null, "There is nothing in the text");
				}
				
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "file not FOUND");
			}
			
				/*try {
					file = fC.getSelectedFile();
					Scanner input = new Scanner(file, "UTF-8");		//If not UTF-8, it will not read certain alian characters!
					while(input.hasNext()){
						sB.append(input.nextLine());
						sB.append(System.lineSeparator());
					}
					input.close();
					return sB.toString();
				}catch (FileNotFoundException e) {
					JOptionPane.showMessageDialog(null, "File cannot be READ! Try other file");
					Import();
				}*/
		}else{
			JOptionPane.showMessageDialog(null, "Select Canceled");
			return "String to be Encrypted or Decrypted";
		}
		return "";
	}
	
	
	public void Export(String result){
			if(fC.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
				try{
					file = new File(fC.getSelectedFile()+".txt");
					BufferedWriter br= new BufferedWriter(new 
					OutputStreamWriter(new FileOutputStream(file,true), "UTF-8"));
					/*FileOutputStream fIS = new FileOutputStream(fC.getSelectedFile());
					OutputStreamWriter oSR = new OutputStreamWriter(fIS, "UTF-8");
					BufferedWriter bR = new BufferedWriter(oSR);*/
					br.write(result);
					br.flush();
					br.close();
				}catch(Exception e){
					JOptionPane.showMessageDialog(null, "ERRRROORRRR");
				}
			}
	}
	
	public String Select(){
		int buffer = fC1.showOpenDialog(null);
		file = fC1.getSelectedFile();
		if(fC1.getSelectedFile()!=null && (buffer==fC1.APPROVE_OPTION)){
			try{
				image=ImageIO.read(file);
			}catch(IOException e){
				JOptionPane.showMessageDialog(null, "Image Loading failed!");
				return "";
			}
			return file.getPath();
		}else{
			return "";
		}				
	}
	// If use Jpeg format, when the image is exported, it will encoded with its own algorithm, causing lost of data stored in the image
	// Thus, This will use png format, which is lossless format.
	public void StegaEnc(String input, String passwd){
		
		AES.setKey(passwd);
		String encData = AES.Encrypt(input);	//After encrypt the given data, 
		encData = StringtoBinary(encData);		//convert it to string of binary.
		
		//Code below is used for total height for hiding place.
		//Note that size of encData increases 2^n
		String encDataSize = String.format("%32s", Integer.toBinaryString(encData.length()/32)); 
		
		/*  width is set to 32!
		 *  set first column to save the total height of hiding place
		 * 	Storing information on pixel is done by Modifying LSB of the pixel
		 *  (which is LSB of blue value)
		 */
		for(int i = 0; i < 32; i++){ 			// first 32bits of first column is saving info of data size
			String temp = intToBinary(image.getRGB(i, 0));
			if(encDataSize.charAt(i)=='1'){
				temp = temp.substring(0,31)+'1';
			}else{
				temp = temp.substring(0,31)+'0';
			}
			image.setRGB(i, 0, binaryToInt(temp));	
		}
		/*There could be two cases, whether inputsize is greater than the height of the image, or not
		 * In the first case, once it reaches to the height, it will continue to save to data in next row
		 * If datasize is greater than h * w of the image, it will throw exception.
		 */
		try{
			int numOfCol = encData.length()/32;
			if(image.getHeight() < numOfCol){
				int numOfRow = (numOfCol/image.getHeight());
				int iTemp = 0;
				while(iTemp < numOfRow+1){
					if(iTemp < numOfRow){
						for(int i = 1; i < image.getHeight(); i++){
							for(int j = iTemp*32; j<32+(32*iTemp); j++){
								String temp = intToBinary(image.getRGB(j, i));
								temp = temp.substring(0,31)+ encData.charAt(j+((i-1)*32));
								image.setRGB(j, i, binaryToInt(temp));
							}
						}
					}else{
						for(int i = 1; i <(numOfCol-(numOfRow*image.getHeight())+1); i++){
							for(int j = iTemp*32; j<32+(32*iTemp); j++){
								String temp = intToBinary(image.getRGB(j, i));
								temp = temp.substring(0,31)+ encData.charAt(j+((i-1)*32));
								image.setRGB(j, i, binaryToInt(temp));
							}
						}
					}
					iTemp++;
				}
			}else{
				for(int i = 1; i <numOfCol+1; i++){
					for(int j = 0; j<32; j++){
						String temp = intToBinary(image.getRGB(j, i));
						temp = temp.substring(0,31)+ encData.charAt(((i-1)*32)+j);
						image.setRGB(j, i, binaryToInt(temp));
					}
				}
			}
		}catch(Exception e){
				JOptionPane.showMessageDialog(null, "Resolution of the image is too small! Try with other picture!");
		}
	//Exporting image as png.
		if(fC1.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
			try{
				File file = new File(fC1.getSelectedFile(),"");
				ImageIO.write(image, "png", file);
			}catch(Exception e){
				JOptionPane.showMessageDialog(null, "ERRRROORRRR");
			}
		}
	}
	
	public String StegaDec(){
		//There is no asking for the password to decipher.
		//The name of the file will be the password.
		AES.setKey(file.getName().substring(0,file.getName().length()-4));
		String length = "",data="";
		
		//reading total height
		for(int i = 0; i<32; i++){
			length = length + intToBinary(image.getRGB(i, 0)).charAt(31);
		}
		//retriving the data from the pixels
		// same algorithm from StagaEnc(), except exception statement is not needed
		int dataSize = binaryToInt(length);
		if(dataSize > image.getHeight()){
			int numOfRow = dataSize/image.getHeight();
			int iTemp = 0;
			while(iTemp < numOfRow+1){
				if(iTemp < numOfRow){
					for(int i = 1; i<image.getHeight(); i++){
						for(int j =iTemp*32; j < 32+(iTemp*32); j++){
							String temp = intToBinary(image.getRGB(j,i));
							data = data + temp.charAt(31);
						}
					}
				}else{
					for(int i = 1; i<(dataSize-(numOfRow*image.getHeight())+1); i++){
						for(int j = iTemp*32; j < 32+(iTemp*32); j++){
							String temp = intToBinary(image.getRGB(j,i));
							data = data + temp.charAt(31);
						}
					}
				}
				iTemp++;
			}
		}else{
			for(int i = 1; i<dataSize; i++){
				for(int j = 0; j < 32; j++){
					String temp = intToBinary(image.getRGB(j,i));
					data = data + temp.charAt(31);
				}
			}
		}
		data = BinarytoString(data);
		return AES.Decrypt(data);
	}
	
	//_____________________________________________UTILITY_________________________________________________________//
	
	public String StringtoBinary(String input){
		String temp = "";
		for(int i = 0; i < input.length(); i++){
			temp = temp + String.format("%8s", Integer.toBinaryString(input.charAt(i) & 0xFF)).replace(' ','0');
		}
		return temp;
	}
	
	public String BinarytoString(String input){
		String[] temp = new String[input.length()/8];
		StringBuilder sB= new StringBuilder();
			for(int i = 0; i < input.length()/8; i++){	
					temp[i] = input.substring(i*8, (i+1)*8);
			}
			for(int i=0; i < temp.length; i++){
				sB.append((char)Integer.parseInt(temp[i],2));
			}
		return sB.toString();
	}
	
	public String intToBinary(int input){
		return  String.format("%32s", Integer.toBinaryString(input).replace(' ', '0'));
	}
	
	public int binaryToInt(String input){
		return ((int)Long.parseLong(input, 2));
	}
}
	

