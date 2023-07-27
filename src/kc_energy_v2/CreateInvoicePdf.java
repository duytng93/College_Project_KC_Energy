package kc_energy_v2;
import java.awt.Color;
import java.io.IOException;
 
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
 
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName;
public class CreateInvoicePdf {
	//private String accountName, phoneNumber, plan, address;
	//private int accountNumber;
	//private double lastPayment, remainingDue, currentBill, totalDue, currentUsage, cost1, cost2, cost3;
	private double currentBill, totalDue, cost1, cost2, cost3;
	private Accounts account;
//    public CreateInvoicePdf(String accountName, String phoneNumber, String address, int accountNumber, double lastPayment, double remainingDue,
//			double currentBill, double totalDue, double currentUsage, double cost1, double cost2, double cost3, String plan) {
//		this.accountName = accountName;
//		this.phoneNumber = phoneNumber;
//		this.accountNumber = accountNumber;
//		this.address = address;
//		this.lastPayment = lastPayment;
//		this.remainingDue = remainingDue;
//		this.currentBill = currentBill;
//		this.totalDue = totalDue;
//		this.currentUsage = currentUsage;
//		this.cost1 = cost1;
//		this.cost2 = cost2;
//		this.cost3 = cost3;
//		this.plan = plan;
//	}
	 public CreateInvoicePdf(Accounts acc, double currentBill, double totalDue, double cost1, double cost2, double cost3) {
		this.account = acc;
		this.currentBill = currentBill;
		this.totalDue = totalDue;
		this.cost1 = cost1;
		this.cost2 = cost2;
		this.cost3 = cost3;
	 }
	
    
    public void createPdf(String location, String date) throws IOException {
    	
    	PDDocument document = new PDDocument();
    	PDPage page = new PDPage(PDRectangle.A4);
    	document.addPage(page);
    	
    	int pageWidth = (int) page.getMediaBox().getWidth();
    	int pageHeight = (int) page.getMediaBox().getHeight();
    	
    	PDPageContentStream contentStream = new PDPageContentStream(document, page);
    	WriteText write = new WriteText(document,contentStream);
    	
    	PDFont font = new PDType1Font(FontName.COURIER);
    	PDFont italicFont = new PDType1Font(FontName.COURIER_OBLIQUE);
    	
    	int pageMargin = 40;
    	write.addSingleLineText("KC ENERGY", pageMargin, pageHeight - 100, font, 40, Color.BLUE);
    									// formula for RIGHT AGLIMENT xPosition: pageWidth - font.getStringWidth("string")/1000*fontSize - pageMargin
    	write.addSingleLineText("Contact: 816-222-333", (int)(pageWidth-font.getStringWidth("Contact: 816-222-333")/1000*12-pageMargin), pageHeight - 100, font, 12, Color.red);
    									// formula for CENTER AGLIGMENT xPosition: pageWidth/2 - (font.getStringWidth("string")/1000*fontSize)/2
    	write.addSingleLineText("Invoice", (int)(pageWidth/2-(font.getStringWidth("Invoice")/1000*30)/2), pageHeight - 180, font, 30, Color.black);
    	
    	int bodyWidth = (int)(font.getStringWidth("__________________________________________")/1000*12);
    	int bodyXPosition = pageWidth/2 - bodyWidth/2;
    	
    	write.addSingleLineText("Full Name     :  "+ account.getName(), bodyXPosition, pageHeight-220, font, 12, Color.black);
    	write.addSingleLineText("Account#      :  "+ account.getAccountNumber(), bodyXPosition, pageHeight-240, font, 12, Color.black);
    	write.addSingleLineText("Phone#        :  "+ account.getPhoneNumber(), bodyXPosition, pageHeight-260, font, 12, Color.black);
    	write.addSingleLineText("Invoice date  :  "+ date, bodyXPosition, pageHeight-280, font, 12, Color.black);
    	write.addSingleLineText("__________________________________________", bodyXPosition, pageHeight-290, font, 12, Color.black);
    	write.addSingleLineText("              Account Summary             ", bodyXPosition, pageHeight-310, font, 12, Color.black);
    	write.addSingleLineText("Last Payment Recieved...............$"+account.getLastPayment(), bodyXPosition, pageHeight-330, font, 12, Color.black);
    	write.addSingleLineText("Remaining Balance...................$"+account.getRemainingDue(), bodyXPosition, pageHeight-350, font, 12, Color.black);
    	write.addSingleLineText("Current Charges (details below).....$"+currentBill, bodyXPosition, pageHeight-370, font, 12, Color.black);
    	write.addSingleLineText("Total Due...........................$"+totalDue, bodyXPosition, pageHeight-390, font, 12, Color.black);
    	write.addSingleLineText("__________________________________________", bodyXPosition, pageHeight-400, font, 12, Color.black);
    	write.addSingleLineText("              Billing Details             ", bodyXPosition, pageHeight-420, font, 12, Color.black);
    	write.addSingleLineText("For service from 02/01/2023 to 02/28/2023", bodyXPosition, pageHeight-440, font, 12, Color.black);
    	write.addSingleLineText("Total usage:....................."+account.getCurrentUsage()+" kwh", bodyXPosition, pageHeight-460, font, 12, Color.black);
    	
    	double price1, price2,price3;
    	if(account.getPlan().equals("resident")) {
			price1 = 0.25;
			price2 = 0.31;
			price3 = 0.35;
		}
		else { // business plan
			price1 = 0.35;
			price2 = 0.41;
			price3 = 0.45;
		}
    	
    	write.addSingleLineText("First 600 kwh @ " + price1 +"................$"+cost1, bodyXPosition, pageHeight-480, font, 12, Color.black);
    	write.addSingleLineText("Next  400 kwh @ " + price2 +"................$"+cost2, bodyXPosition, pageHeight-500, font, 12, Color.black);
    	write.addSingleLineText("Over 1000 kwh @ " + price3 +"................$"+cost3, bodyXPosition, pageHeight-520, font, 12, Color.black);
    	write.addSingleLineText("Total:..............................$"+currentBill, bodyXPosition, pageHeight-540, font, 12, Color.black);
    	
    	write.addSingleLineText("Mailing Information", 30, pageHeight- 600, italicFont, 12, Color.gray);
    	write.addSingleLineText("--------------------------------------------------------------------------", 30, pageHeight - 610, font, 12, Color.black);
    	
    	String address = account.getAddress();
    	
    	String addressPart1 = address.substring(0, address.indexOf(',')).toUpperCase();
    	String addressPart2 = address.substring(address.indexOf(',')+2).replaceAll(",", "").toUpperCase();
    	
    	write.addSingleLineText(account.getName().toUpperCase(), 80, pageHeight- 650, font, 12, Color.black);
    	write.addSingleLineText(addressPart1, 80, pageHeight- 670, font, 12, Color.black);
    	write.addSingleLineText(addressPart2, 80, pageHeight- 690, font, 12, Color.black);
    	
    	write.addSingleLineText("KC ENERGY", 370, pageHeight- 710, font, 12, Color.black);
    	write.addSingleLineText("PO BOX 123456", 370, pageHeight- 730, font, 12, Color.black);
    	write.addSingleLineText("KANSAS CITY MO 64999", 370, pageHeight- 750, font, 12, Color.black);
    	
    	
    	contentStream.close();
    	document.save(location);
    	document.close();
    	System.out.print("pdf created");
    }
	
	
	private class WriteText{
    	PDDocument document;
    	PDPageContentStream contentStream;
    	
		public WriteText(PDDocument document, PDPageContentStream contentStream) {
			this.document = document;
			this.contentStream = contentStream;
		}
		
    	void addSingleLineText(String text, int xPosition, int yPosition, PDFont font
    							, float fontSize, Color color) throws IOException {
    		contentStream.beginText();
    		contentStream.setFont(font, fontSize);
    		contentStream.setNonStrokingColor(color);
    		contentStream.newLineAtOffset(xPosition, yPosition);
    		contentStream.showText(text);
    		contentStream.endText();
    		contentStream.moveTo(0, 0);
    	}
    }
	
}