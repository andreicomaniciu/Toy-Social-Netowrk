package com.example.curs9.ubbcluj.map.domain.MyUtils;

import com.example.curs9.ubbcluj.map.domain.MyModels.Friendship;
import com.example.curs9.ubbcluj.map.domain.MyModels.Message;
import com.example.curs9.ubbcluj.map.domain.MyValidators.FriendshipRepoValidator;
import com.example.curs9.ubbcluj.map.domain.MyValidators.MessageRepoValidator;
import com.example.curs9.ubbcluj.map.domain.MyValidators.UserRepoValidator;
import com.example.curs9.ubbcluj.map.service.NetworkService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfCreator {
    NetworkService networkService;

    public PdfCreator(NetworkService networkService) {
        this.networkService = networkService;
    }

    public void createPDFStats(String datainceput,String datafinal) throws MessageRepoValidator, UserRepoValidator, FriendshipRepoValidator, IOException {
        ArrayList<Message> mesaje=networkService.getStatisticsAllMessages(datainceput,datafinal);
        ArrayList<Friendship> prietenii=networkService.getStatisticsAllFriendships(datainceput,datafinal);

        ArrayList<String> mesajeString=new ArrayList<>();
        for(var x:mesaje){
            String usersString="";
            for(var y:x.getUser_to()){
                usersString=usersString+" "+y.getFirstName()+" "+y.getLastName()+" | ";
            }
            mesajeString.add(usersString+x.getMesaj()+" | "+x.getData());
        }

        ArrayList<String> prieteniiString=new ArrayList<>();
        for(var x:prietenii){
            String usersString="";
            if(networkService.getCurrentUser().getId().equals(x.getId1()))
                prieteniiString.add(x.getName2()+" | "+x.getDateOfFriendshipRequest());
            else
                prieteniiString.add(x.getName1()+" | "+x.getDateOfFriendshipRequest());
        }
        float currenty=720;

        PDDocument document=new PDDocument();
        PDPage newPage=new PDPage();
        document.addPage(newPage);

        PDPageContentStream  content=new PDPageContentStream(document,newPage);

        content.beginText();
        content.setFont(PDType1Font.HELVETICA,20);
        content.moveTextPositionByAmount(60,750);
        content.drawString("Mesajele utilizatorului "+networkService.getCurrentUser().getFirstName()+" "+networkService.getCurrentUser().getLastName());
        content.endText();

        for(var x:mesajeString){
            if(currenty<50){
                content.close();
                newPage=new PDPage();
                document.addPage(newPage);
                content=new PDPageContentStream(document,newPage);
                currenty=750;
            }

            content.beginText();
            content.setFont(PDType1Font.HELVETICA,10);
            content.moveTextPositionByAmount(20,currenty);
            content.drawString(x);
            content.endText();

            currenty-=20;
        }
        currenty-=10;

        content.beginText();
        content.setFont(PDType1Font.HELVETICA,20);
        content.moveTextPositionByAmount(60,currenty);
        content.drawString("Prieteniile utilizatorului "+networkService.getCurrentUser().getFirstName()+" "+networkService.getCurrentUser().getLastName());
        content.endText();

        currenty-=20;

        for(var x:prieteniiString){
            if(currenty<50){
                content.close();
                newPage=new PDPage();
                document.addPage(newPage);
                content=new PDPageContentStream(document,newPage);
                currenty=750;
            }

            content.beginText();
            content.setFont(PDType1Font.HELVETICA,10);
            content.moveTextPositionByAmount(20,currenty);
            content.drawString(x);
            content.endText();

            currenty-=20;
        }

        content.close();

        document.save("C:\\Users\\Andrei\\mypdf.pdf");
        System.out.println("pdf created");

        document.close();

    }

    public  void createPDFConversation(String datainceput, String datafinal, List<String> useri) throws MessageRepoValidator, UserRepoValidator, IOException {
        ArrayList<Message> mesaje=networkService.getStatisticsMessagesreceivedfromUser(datainceput,datafinal,useri);
        ArrayList<String> mesajeString=new ArrayList<>();
        for(var x:mesaje){
            String usersString="";
            for(var y:x.getUser_to()){
                usersString=usersString+" "+y.getFirstName()+" "+y.getLastName()+" | ";
            }
            mesajeString.add(usersString+x.getMesaj()+" | "+x.getData());
        }

        float currenty=720;

        PDDocument document=new PDDocument();
        PDPage newPage=new PDPage();
        document.addPage(newPage);

        PDPageContentStream  content=new PDPageContentStream(document,newPage);

        content.beginText();
        content.setFont(PDType1Font.HELVETICA,20);
        content.moveTextPositionByAmount(60,750);
        content.drawString("Mesajele utilizatorului "+networkService.getCurrentUser().getFirstName()+" "+networkService.getCurrentUser().getLastName());
        content.endText();

        for(var x:mesajeString){
            if(currenty<50){
                content.close();
                newPage=new PDPage();
                document.addPage(newPage);
                content=new PDPageContentStream(document,newPage);
                currenty=750;
            }

            content.beginText();
            content.setFont(PDType1Font.HELVETICA,10);
            content.moveTextPositionByAmount(20,currenty);
            content.drawString(x);
            content.endText();

            currenty-=20;
        }
        content.close();
        document.save("C:\\Users\\Andrei\\mypdf.pdf");
        System.out.println("pdf created");

        document.close();
    }
}
