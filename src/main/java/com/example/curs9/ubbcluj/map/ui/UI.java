package com.example.curs9.ubbcluj.map.ui;

import com.example.curs9.ubbcluj.map.MyRepos.*;
import com.example.curs9.ubbcluj.map.domain.MyModels.Message;
import com.example.curs9.ubbcluj.map.domain.MyValidators.*;
import com.example.curs9.ubbcluj.map.service.NetworkService;

import java.util.ArrayList;
import java.util.Scanner;

public class UI {

    private Scanner in;
    private NetworkService networkService;

    public UI(UserValidator userValidator, FriendshipValidator friendshipValidator, UserRepo userRepo, FriendshipRepo friendshipRepo, MessageValidator messageValidator, MessageRepo messageRepo, GroupRepo groupRepo) throws FriendshipRepoValidator, MessageRepoValidator, UserRepoValidator {
        this.in = new Scanner(System.in);
        this.networkService = new NetworkService(userValidator, friendshipValidator, userRepo, friendshipRepo, messageValidator,messageRepo,groupRepo);
    }

    private String optiuni() {
        ArrayList<String> optiuni = new ArrayList<>();
        optiuni.add(new String("1. Adaugati utilizator - tasta 1"));
        optiuni.add(new String("2. Modificati utilizator - tasta 2"));
        optiuni.add(new String("3. Stergeti utilizator - tasta 3"));
        optiuni.add(new String("4. Afisati utilizatori - tasta 4"));
        optiuni.add(new String("5. Adaugati prietenie - tasta 5"));
        optiuni.add(new String("6. Modificati prietenie - tasta 6"));
        optiuni.add(new String("7. Stergeti prietenie - tasta 7"));
        optiuni.add(new String("8. Afisati prietenii - tasta 8"));
        optiuni.add(new String("9. Afisati numarul de comunitati - tasta 9"));
        optiuni.add(new String("10. Afisati cea mai sociabila comunitate - tasta 10"));
        optiuni.add(new String("11. Afisati prieteniile unui anumit utilizator - tasta 11"));
        optiuni.add(new String("12. Afisati prieteniile unui anumit utilizator, dintr-o anumita luna - tasta 12"));
        optiuni.add(new String("13. Faceti o cerere de prietenie - tasta 13"));
        optiuni.add(new String("14. Afisare cereri de prietenii - tasta 14"));
        optiuni.add(new String("15. Raspundeti unei cereri de prietenii - tasta 15"));
        optiuni.add(new String("16. Trimiteti un mesaj nou - tasta 16"));
        optiuni.add(new String("17. Afisati toate mesajele - tasta 17"));
        optiuni.add(new String("18. Afisati o conversatie - tasta 18"));

        optiuni.add(new String("0. Exit - tasta 0"));

        String meniu = new String("");

        for (var x : optiuni) {
            meniu = meniu + x + "\n";
        }

        return meniu;
    }

    public void meniu() throws Exception {
        String s = "";

        while (!s.equals("0")) {
            System.out.println("Doriti sa:");
            System.out.println(optiuni());
            System.out.println("Optiunea este: ");

            s = in.nextLine();

            switch (s) {
                case "1":
                    addUser();
                    break;
                case "2":
                    modifyUser();
                    break;
                case "3":
                    deleteUser();
                    break;
                case "4":
                    showUsers();
                    break;
                case "5":
                    addFriendship();
                    break;
                case "6":
                    modifyFriendship();
                    break;
                case "7":
                    deleteFriendship();
                    break;
                case "8":
                    showFriendships();
                    break;
                case "9":
                    showCommunities();
                    break;
                case "10":
                    showMostSociableCommunity();
                    break;

                case "16":
                    SendMessage();
                    break;
                case "17":
                    showAllMessages();
                    break;
                case "18":
                    showConversation();
                    break;
                case "11":
                    showFriendshipsOfUser();
                    break;
                case "12":
                    showFriendshipsOfUserAndDate();
                    break;
                case "13":
                    makeRequest();
                    break;
                case "14":
                    showRequests();
                    break;
                case "15":
                    answerRequests();
                    break;


                case "0":
                    System.out.println("\nSunteti sigur ca doriti sa iesiti?\n1.Da - tasta 1\n2.Nu - tasta 0 ");
                    s = in.nextLine();
                    switch (s) {
                        case "1":
                            //System.out.println("Final");
                            return;
                        default:
                            break;
                    }
                    break;
                default:
                    System.out.println("Optiunea data nu este valida!\n");
                    break;
            }
        }
    }

    private void showFriendshipsOfUser() throws FriendshipRepoValidator, UserRepoValidator {
        System.out.println("\nAfisarea prieteniilor unui anumit utilizator\n");

        System.out.println("Dati ID-ul utilizatorului: ");
        System.out.println("ID:");
        String ID = in.nextLine();

        if (networkService.getFriendshipsOf(ID).size() == 0) {
            System.out.println("\nNu exista prietenii ale utilizatorului " + ID + "!\n");
            return;
        }

        System.out.println("");

        for (var x : networkService.getFriendshipsOf(ID)) {
            if (x.getId1().equals(ID)) {
                System.out.println("Nume: " + x.getUser2().getLastName() + " | Prenume: " + x.getUser2().getFirstName() + " | Data: " + x.getDateOfFriendshipRequest());
            }
            else
            {
                System.out.println("Nume: " + x.getUser1().getLastName() + " | Prenume: " + x.getUser1().getFirstName() + " | Data: " + x.getDateOfFriendshipRequest());

            }
        }

        System.out.println("\n");
    }

    private void showFriendshipsOfUserAndDate() throws FriendshipRepoValidator, UserRepoValidator {
        System.out.println("\nAfisarea prieteniilor unui anumit utilizator, dintr-o anumita luna\n");

        System.out.println("Dati ID-ul utilizatorului: ");
        System.out.println("ID:");
        String ID = in.nextLine();

        System.out.println("Dati luna: ");
        System.out.println("Luna:");
        Integer month;
        try {
            month = Integer.parseInt(in.nextLine());
        } catch (Exception e) {
            System.out.println("\nFormatul lunii (1-12) neindeplinit. Eroare!\n");
            return;
        }


        if (networkService.getFriendshipsOfWithDate(ID, month).size() == 0) {
            System.out.println("\nNu exista prietenii ale utilizatorului " + ID + " din luna " + month + " !\n");
            return;
        }

        System.out.println("");

        for (var x : networkService.getFriendshipsOfWithDate(ID, month)) {
            if (x.getId1().equals(ID)) {
                System.out.println("Nume: " + x.getUser2().getLastName() + " | Prenume: " + x.getUser2().getFirstName() + " | Data: " + x.getDateOfFriendshipRequest());
            }
            else
            {
                System.out.println("Nume: " + x.getUser1().getLastName() + " | Prenume: " + x.getUser1().getFirstName() + " | Data: " + x.getDateOfFriendshipRequest());
            }
        }

        System.out.println("\n");
    }

    private void makeRequest() {
        System.out.println("\nCerere prietenie\n");

        String ID1, ID2;

        var atribute = readingCerere();

        ID1 = atribute.get(0);
        ID2 = atribute.get(1);

        try {
            networkService.makeRequest(ID1, ID2);
            System.out.println("\nCererea de la " + ID1 + " catre " + ID2 + " a fost trimisa cu succes!\n");
        } catch (FriendshipRepoValidator e) {
            System.out.println("\nPrietenia sau cererea de prietenie " + ID1 + "-" + ID2 + " exista deja!\n");
        } catch (Exception e) {
            System.out.println("\n" + e.getMessage());
            System.out.println("Cererea nu a fost trimisa!\n");
        }
    }

    private void showRequests() throws FriendshipRepoValidator, UserRepoValidator {
        System.out.println("\nAfisare cereri\n");

        String ID1, ID2;

        System.out.println("Dati utilizatorul pentru a afisa cererile catre sine: ");
        ID1 = in.nextLine();

        if (networkService.getPendingRequestsOf(ID1).size() == 0) {
            System.out.println("\nNu exista cereri de prietenie catre utilizatorul " + ID1 + "!\n");
            return;
        }

        System.out.println("\n");

        for (var x : networkService.getPendingRequestsOf(ID1)) {
            System.out.println("ID1: " + x.getId1() + " | ID2: " + x.getId2());
        }

        System.out.println("\n");
    }

    private void answerRequests() throws FriendshipRepoValidator {
        System.out.println("\nRaspundere cereri\n");

        String IDtoRespond, IDwhoSent, status;

        System.out.println("Dati utilizatorul pentru a raspunde cererilor catre sine: ");
        IDtoRespond = in.nextLine();

        System.out.println("Dati utilizatorul initial: ");
        IDwhoSent = in.nextLine();

        System.out.println("Dati raspunsul (Approve/ Reject): ");
        status = in.nextLine();

        switch (status) {
            case "Approve":
                try {
                    networkService.answerRequest(IDtoRespond, IDwhoSent, "approved");
                    System.out.println("\nAdaugarea prieteniei " + IDtoRespond + "-" + IDwhoSent + " s-a realizat cu succes!\n");
                } catch (FriendshipRepoValidator e) {
                    System.out.println(e.getMessage());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                break;
            case "Reject":
                try {
                    networkService.answerRequest(IDtoRespond, IDwhoSent, "rejected");
                    System.out.println("\nStergerea cererei de prietenie " + IDtoRespond + "-" + IDwhoSent + " s-a realizat cu succes!\n");
                } catch (FriendshipRepoValidator e) {
                    System.out.println(e.getMessage());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                break;
            default:
                System.out.println("\nCerere invalida!\n");
                return;
        }


    }

    private void showMostSociableCommunity() throws UserRepoValidator, FriendshipRepoValidator {
        System.out.println("\nAfisare cea mai sociabila comunitate\n");

        var res = networkService.getMostSocialCommunity();

        System.out.println("\nCea mai sociabila comunitate este formata din: ");
        for (var x : res) {
            System.out.println(x);
        }
        System.out.println("\n");
    }

    private void showCommunities() throws UserRepoValidator, FriendshipRepoValidator {
        System.out.println("\nAfisare comunitati\n");

        System.out.println("\nNumarul de comunitati este: " + networkService.getNoOfCommunities());
        System.out.println("\n");
    }

    private void addFriendship() {
        System.out.println("\nAdaugare prietenie\n");

        String ID1, ID2;

        var atribute = readingPrietenie();

        ID1 = atribute.get(0);
        ID2 = atribute.get(1);

        try {
            networkService.addFriendship(ID1, ID2);
            System.out.println("\nPrietenia " + ID1 + "-" + ID2 + " a fost adaugat cu succes!\n");
        } catch (FriendshipRepoValidator e) {
            System.out.println(e.getMessage());
            System.out.println("\nPrietenia " + ID1 + "-" + ID2 + " exista deja!\n");
        } catch (Exception e) {
            System.out.println("\n" + e.getMessage());
            System.out.println("Prietenia nu a fost adaugata!\n");
        }
    }

    private void modifyFriendship() {
        System.out.println("\nModificare prietenie\n");

        String ID1V, ID2V, ID1N, ID2N;

        System.out.println("Dati prietenia ce urmeaza sa fie modificata: ");
        System.out.println("ID1:");
        ID1V = in.nextLine();
        System.out.println("ID2:");
        ID2V = in.nextLine();

        System.out.println("\n");
        var atribute = readingPrietenie();

        ID1N = atribute.get(0);
        ID2N = atribute.get(1);

        try {
            networkService.modifyFriendship(ID1V, ID2V, ID1N, ID2N);
            System.out.println("\nPrietenia " + ID1N + "-" + ID2N + " a fost modificata cu succes!\n");
        } catch (FriendshipRepoValidator e) {
            System.out.println("\nPrietenia " + ID1V + "-" + ID2V + " nu exista!\n");
        } catch (Exception e) {
            System.out.println("\n" + e.getMessage());
        }
    }

    private void deleteFriendship() {
        System.out.println("\nStergere prietenie\n");

        String ID1, ID2;

        System.out.println("Dati atributele pentru stergere: ");
        System.out.println("ID1:");
        ID1 = in.nextLine();
        System.out.println("ID2:");
        ID2 = in.nextLine();

        try {
            networkService.deleteFriendship(ID1, ID2);
            System.out.println("\nStergerea prieteniei " + ID1 + "-" + ID2 + " s-a realizat cu succes!\n");
        } catch (FriendshipRepoValidator e) {
            System.out.println(e.getMessage());
        }
    }

    private void showFriendships() throws FriendshipRepoValidator {
        System.out.println("\nAfisarea prieteniilor\n");

        if (networkService.getFriendships().size() == 0) {
            System.out.println("Nu exista prietenii!\n");
            return;
        }

        for (var x : networkService.getFriendships()) {
            System.out.println("ID1: " + x.getId1() + " | ID2: " + x.getId2() + " | status: " + x.getStatus());
        }

        System.out.println("\n");
    }

    private ArrayList<String> reading() {
        ArrayList<String> atribute = new ArrayList<>();

        System.out.println("Dati atributele utilizatorului:");
        System.out.println("ID:");
        atribute.add(in.nextLine());

        System.out.println("Prenume:");
        atribute.add(in.nextLine());

        System.out.println("Nume:");
        atribute.add(in.nextLine());

        System.out.println("Email?\n1. Automat - tasta 1\n2. Explicit - tasta 2");
        String s = in.nextLine();

        switch (s) {
            case "2":
                System.out.println("Email explicit:");
                atribute.add(in.nextLine());
            default:
                atribute.add("");
        }

        return atribute;
    }

    private ArrayList<String> readingPrietenie() {
        ArrayList<String> atribute = new ArrayList<>();

        System.out.println("Dati atributele prieteniei:");
        System.out.println("ID User #1:");
        atribute.add(in.nextLine());

        System.out.println("ID User #2:");
        atribute.add(in.nextLine());

        return atribute;
    }

    private ArrayList<String> readingCerere() {
        ArrayList<String> atribute = new ArrayList<>();

        System.out.println("Dati atributele cererii de prietenie:");
        System.out.println("Din partea - ID User:");
        atribute.add(in.nextLine());

        System.out.println("Catre - ID User:");
        atribute.add(in.nextLine());

        return atribute;
    }

    private void addUser() {
        System.out.println("\nAdaugare utilizator\n");

        String ID, Prenume, Nume;
        String Email = "";

        var atribute = reading();

        ID = atribute.get(0);
        Prenume = atribute.get(1);
        Nume = atribute.get(2);
        Email = atribute.get(3);

        try {
            networkService.addUser(ID, Prenume, Nume, Email);
            System.out.println("\nUtilizatorul " + ID + " a fost adaugat cu succes!\n");
        } catch (UserRepoValidator e) {
            System.out.println("\nUtilizatorul cu ID-ul " + ID + " exista deja!\n");
        } catch (Exception e) {
            System.out.println("\n" + e.getMessage());
            System.out.println("Utilizatorul nu a fost adaugat!\n");
        }

    }

    private void modifyUser() {
        System.out.println("\nModificare utilizator\n");

        String IDV;

        System.out.println("Dati utilizatorul ce urmeaza sa fie modificat: ");
        System.out.println("ID:");

        IDV = in.nextLine();

        String ID, Prenume, Nume;
        String Email = "";

        System.out.println("\n");
        var atribute = reading();

        ID = atribute.get(0);
        Prenume = atribute.get(1);
        Nume = atribute.get(2);
        Email = atribute.get(3);

        try {
            networkService.modifyUser(IDV, ID, Prenume, Nume, Email);
            System.out.println("\nUtilizatorul " + ID + " a fost modificat cu succes!\n");
        } catch (UserRepoValidator e) {
            System.out.println("\nUtilizatorul " + IDV + " nu exista!\n");
        } catch (Exception e) {
            System.out.println("\n" + e.getMessage());
        }
    }

    private void deleteUser() {
        System.out.println("\nStergere utilizator\n");

        String ID;

        System.out.println("Dati atributele pentru stergere: ");
        System.out.println("ID:");

        ID = in.nextLine();

        try {
            networkService.deleteUser(ID);
            System.out.println("\nStergerea utilizatorului " + ID + " s-a realizat cu succes!\n");
        } catch (UserRepoValidator | FriendshipRepoValidator e) {
            System.out.println(e.getMessage());
        }
    }

    private void showUsers() throws UserRepoValidator {
        System.out.println("\nAfisarea utilizatorilor\n");

        if (networkService.getUsers().size() == 0) {
            System.out.println("Nu exista utilizatori!\n");
            return;
        }

        for (var x : networkService.getUsers()) {
            System.out.println("ID: " + x.getId() + " | Prenume: " + x.getFirstName() + " | Nume: " + x.getLastName() + " | Email: " + x.getEmail());
        }

        System.out.println("\n");
    }

    private void SendMessage() throws Exception {
        System.out.println("\nDati id-ul utilizatorului: ");
        String id=in.nextLine();
        System.out.println("\nDoriti sa creati un mesaj nou sau sa raspundeti unui mesaj?");
        System.out.println("\n1-Creare mesaj nou\n2-Raspunde la un mesaj vechi");
        String opt=in.nextLine();
        if(opt.equals("1")){
            System.out.println("\nDati mesajul: ");
            String mesaj=in.nextLine();
            System.out.println("\nDati id-ul mesajului: ");
            String id1=in.nextLine();
            System.out.println("\nDati destinatarii: ");
            String dest=in.nextLine();
            try{
                networkService.addNewMessage(id1,id,dest,mesaj);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }else
            if(opt.equals("2")){
                if(networkService.getMessagesToRespond(id).size()==0)
                    System.out.println("Nu aveti mesaje pentru care sa raspundeti!");
                else{
                    for(var x: networkService.getMessagesToRespond(id))
                    {
                        System.out.println("\nID: "+ x.getId_mesaj()+"| Expeditor: "+x.getId_From()+"| Mesaj: "+ x.getMesaj()+"| Data: "+x.getData());
                    }
                    System.out.println("\nDati id-ul mesajului la care doriti sa raspundeti: ");
                    String id_mesaj=in.nextLine();
                    System.out.println("\nDati id-ul raspunsului: ");
                    String id_nou=in.nextLine();
                    System.out.println("\nDati mesajul: ");
                    String mesaj=in.nextLine();
                    try{
                        networkService.addResponse(id_nou,id,networkService.getMessage(id_mesaj),mesaj,id_mesaj);
                    }catch (Exception e){
                        System.out.println("\nRaspunsul nu este valid!");
                    }

                }
            }
    }

    private void showAllMessages(){
        try {
            ArrayList<Message> list=networkService.getMessages();
            if(list.size()==0)
                System.out.println("Nu exista mesaje!");
            else
                for(var x: list){
                    System.out.println("\nDe la"+x.getId_From()+"| Pentru "+x.getId_To()+"| Data: "+x.getData()+"| Mesaj: "+x.getMesaj());
                }
        } catch (MessageRepoValidator messageRepoValidator) {
            messageRepoValidator.printStackTrace();
        }

    }

    private void showConversation(){
        System.out.println("\nDati id-ul primului utilizator: ");
        String id1=in.nextLine();
        System.out.println("\nDati id-ul celui de al doilea utilizator: ");
        String id2=in.nextLine();
        try {
            ArrayList<Message> list = null;
            if (list.size() == 0)
                System.out.println("Nu exista nici o conversatie intre cei 2!");
            else {
                for (var x : list) {
                    System.out.println("\nDe la" + x.getId_From() + "| Pentru " + x.getId_To() + "| Data: " + x.getData() + "| Mesaj: " + x.getMesaj());

                }
            }


        } finally {

        }
        ;
    }
}
