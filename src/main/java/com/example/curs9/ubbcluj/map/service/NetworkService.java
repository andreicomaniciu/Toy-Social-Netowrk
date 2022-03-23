package com.example.curs9.ubbcluj.map.service;

import com.example.curs9.ubbcluj.map.MyRepos.FriendshipRepo;
import com.example.curs9.ubbcluj.map.MyRepos.GroupRepo;
import com.example.curs9.ubbcluj.map.MyRepos.MessageRepo;
import com.example.curs9.ubbcluj.map.MyRepos.UserRepo;
import com.example.curs9.ubbcluj.map.domain.MyModels.Friendship;
import com.example.curs9.ubbcluj.map.domain.MyModels.Group;
import com.example.curs9.ubbcluj.map.domain.MyModels.Message;
import com.example.curs9.ubbcluj.map.domain.MyModels.User;
import com.example.curs9.ubbcluj.map.domain.MyUtils.LongestRoad;
import com.example.curs9.ubbcluj.map.domain.MyUtils.MyObserver.Observable;
import com.example.curs9.ubbcluj.map.domain.MyUtils.MyObserver.Observer;
import com.example.curs9.ubbcluj.map.domain.MyValidators.*;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;


public class NetworkService implements Observable {
    private UserValidator userValidator;
    private FriendshipValidator friendshipValidator;
    private UserRepo userRepo;
    private FriendshipRepo friendshipRepo;
    private MessageValidator messageValidator;
    private MessageRepo messageRepo;
    private GroupRepo groupRepo;

    private String idCurent;

    private User currentUser;

    private Integer nrMessages;

    public Integer getNrGroups() {
        return nrGroups;
    }

    private Integer nrGroups;

    public void  setCurrentNrMessages() throws MessageRepoValidator {
        ArrayList<Message> mlist=messageRepo.getAll();
        nrMessages=mlist.size();
    }

    public void  setCurrentNrGroups() throws MessageRepoValidator {
        ArrayList<Group> mlist=groupRepo.getAll();
        nrGroups=mlist.size();
    }

    public void increaseNrGroups(){
        nrGroups=nrGroups+1;
    }

    public void increaseNrMessages(){
        nrMessages=nrMessages+1;
    }

    public Integer getNrMessages() {
        return nrMessages;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public String getIdCurent() {
        return idCurent;
    }

    public void setIdCurent(String idCurent) {
        this.idCurent = idCurent;
    }

    /**
     * Constructor NetworkService
     * @param userValidator
     * @param friendshipValidator
     * @param userRepo
     * @param friendshipRepo
     */
    public NetworkService(UserValidator userValidator, FriendshipValidator friendshipValidator, UserRepo userRepo, FriendshipRepo friendshipRepo,MessageValidator messageValidator,MessageRepo messageRepo,GroupRepo groupRepo) throws FriendshipRepoValidator, MessageRepoValidator, UserRepoValidator {
        this.userValidator = userValidator;
        this.friendshipValidator = friendshipValidator;
        this.userRepo = userRepo;
        this.friendshipRepo = friendshipRepo;
        this.messageValidator=messageValidator;
        this.messageRepo=messageRepo;
        this.groupRepo=groupRepo;
    }

    /**
     * adauga un User pe baza parametrilor sai
     * @param ID
     * @param Prenume
     * @param Nume
     * @param Email
     * @throws Exception, daca apar erori la validare sau adaugare
     */
    public void addUser(String ID, String Prenume, String Nume, String Email) throws Exception {

        User user;

        switch (Email) {
            case "":
                user = new User(ID, Prenume, Nume);
                break;
            default:
                user = new User(ID, Prenume, Nume, Email);
        }

        userValidator.validateUser(user);
        userRepo.add(user);
    }

    public void setFlagsForDeletionOfUser(String ID) throws FriendshipRepoValidator {
        for (var x : friendshipRepo.getAll()) {
            if (x.getId1().equals(ID) || x.getId2().equals(ID)) {
                x.setToBeDeletedFlag(1);
            }
        }
    }

    /**
     * sterge un User pe baza ID-ului sau
     * @param ID
     * @throws UserRepoValidator, daca User-ul nu exista
     */
    public void deleteUser(String ID) throws UserRepoValidator, FriendshipRepoValidator {

        //setFlagsForDeletionOfUser(ID);

        friendshipRepo.deleteFlagged(ID);
        userRepo.delete(ID);
    }

    /**
     * modifica un User pe baza parametrilor sai si a ID-ului vechi
     * @param IDvechi
     * @param IDNou
     * @param Prenume
     * @param Nume
     * @param Email
     * @throws Exception, daca apar erori la stergere, validare sau adaugare
     */
    public void modifyUser(String IDvechi, String IDNou, String Prenume, String Nume, String Email) throws Exception {

        User user;

        switch (Email) {
            case "":
                user = new User(IDNou, Prenume, Nume);
                break;
            default:
                user = new User(IDNou, Prenume, Nume, Email);
        }

        userValidator.validateUser(user);
        userRepo.delete(IDvechi);
        userRepo.add(user);
    }

    /**
     * obtine si returneaza toti Userii existenti
     * @return ArrayList<User>
     */
    public ArrayList<User> getUsers () throws UserRepoValidator {
        return userRepo.getAll();
    }

    /**
     * adauga o prietenie pe baza ID-urilor celor doi Useri
     * @param ID1
     * @param ID2
     * @throws Exception, daca apar erori la validare sau adaugare
     */
    public void addFriendship(String ID1, String ID2) throws Exception {

        User user1 = userRepo.getUser(ID1);
        User user2 = userRepo.getUser(ID2);

        Friendship friendship = new Friendship(user1, user2);

        friendshipValidator.validateFriendship(friendship);
        friendshipRepo.add(friendship);

        setCurrentNrGroups();
        increaseNrGroups();
        String nume="";
        if(user1.equals(currentUser))
            nume=user2.getFirstName()+" "+user2.getLastName();
        else
            nume=user1.getFirstName()+" "+user1.getLastName();
        String useri=user1.getId()+" "+ user2.getId();
        AddGroup(String.valueOf(nrGroups),nume,useri);
        //user1.addFriend(user2);
        //user2.addFriend(user1);
    }

    /**
     * sterge prietenie pe baza ID-urilor celor doi Useri
     * @param ID1
     * @param ID2
     * @throws FriendshipRepoValidator
     */
    public void deleteFriendship(String ID1, String ID2) throws FriendshipRepoValidator {
        friendshipRepo.delete(ID1, ID2);

        notifyObservers();

        //User user1 = userRepo.getUser(ID1);
        //User user2 = userRepo.getUser(ID2);

        //user1.deleteFriend(user2);
        //user2.deleteFriend(user1);
    }

    /**
     * modifica o prietenie pe baza parametrilor User-ilor
     * @param ID1vechi
     * @param ID2vechi
     * @param ID1Nou
     * @param ID2Nou
     * @throws Exception, daca apar erori la stergere, validare sau adaugare
     */
    public void modifyFriendship(String ID1vechi, String ID2vechi, String ID1Nou, String ID2Nou) throws Exception {
        friendshipRepo.delete(ID1vechi, ID2vechi);

        User user1 = userRepo.getUser(ID1vechi);
        User user2 = userRepo.getUser(ID2vechi);

        //user1.deleteFriend(user2);
        //user2.deleteFriend(user1);

        User user1N = userRepo.getUser(ID1Nou);
        User user2N = userRepo.getUser(ID2Nou);

        Friendship friendship = new Friendship(user1N, user2N);

        friendshipValidator.validateFriendship(friendship);
        friendshipRepo.add(friendship);

        //user1N.addFriend(user2N);
        //user2N.addFriend(user1N);
    }

    /**
     * obtine si returneaza toate prieteniile returnate
     * @return
     */
    public ArrayList<Friendship> getFriendships () throws FriendshipRepoValidator {
        return friendshipRepo.getAll();
    }

    private Friendship constructFriendshipWithUsers (Friendship friendship) throws UserRepoValidator {

        var users = userRepo.getAll();

        User user1 = users.stream()
                .filter(x -> x.getId().equals(friendship.getId1()))
                .findFirst()
                .get();

        User user2 = users.stream()
                .filter(x -> x.getId().equals(friendship.getId2()))
                .findFirst()
                .get();

        return new Friendship(user1, user2, friendship.getDateOfFriendshipRequest(), friendship.getStatus());
    }

    private ArrayList<Friendship> constructAllFriendshipsWithUsers (ArrayList<Friendship> friendships) throws UserRepoValidator {

        var users = userRepo.getAll();

        var friendshipsWithUsers = friendships.stream()
                .map(x -> {
            try {
                return constructFriendshipWithUsers(x);
            } catch (UserRepoValidator e) {
                e.printStackTrace();
                return null;
            }
        }).toList();

        return new ArrayList<Friendship>(friendshipsWithUsers);

    }

    public ArrayList<Friendship> getFriendshipsOf (String ID) throws FriendshipRepoValidator, UserRepoValidator {

        Predicate<Friendship> hasUserByID = x -> x.hasUserByID(ID);
        Predicate<Friendship> isApproved = x -> x.isStatus_Approved();
        Predicate<Friendship> hasAll =hasUserByID.and(isApproved);

        var friendshipsOfID =  constructAllFriendshipsWithUsers(friendshipRepo.getAll())
                .stream()
                .filter(hasAll)
                .toList();

        return new ArrayList<Friendship>(friendshipsOfID);
    }

    public ArrayList<Friendship> getFriendshipsOfWithDate (String ID, Integer month) throws FriendshipRepoValidator, UserRepoValidator {

        Predicate<Friendship> hasUserByID = x -> x.hasUserByID(ID);
        Predicate<Friendship> isApproved = x -> x.isStatus_Approved();
        Predicate<Friendship> hasSameMonth = x -> x.getMonthOfFriendshipRequest() == month;
        Predicate<Friendship> hasAll =hasUserByID.and(hasSameMonth).and(isApproved);


        var friendshipsOfID =  constructAllFriendshipsWithUsers(friendshipRepo.getAll())
                .stream()
                .filter(hasAll)
                .toList();

        return new ArrayList<Friendship>(friendshipsOfID);
    }

    /**
     * adauga o prietenie pe baza ID-urilor celor doi Useri
     * @param ID1
     * @param ID2
     * @throws Exception, daca apar erori la validare sau adaugare
     */
    public void makeRequest(String ID1, String ID2) throws Exception {

        User user1 = userRepo.getUser(ID1);
        User user2 = userRepo.getUser(ID2);

        Friendship friendship = new Friendship(user1, user2);

        friendshipValidator.validateFriendship(friendship);

        friendship.setStatus_Pending();

        friendshipRepo.add(friendship);

        notifyObservers();
        //user1.addFriend(user2);
        //user2.addFriend(user1);
    }

    /**
     * reaspunde cererii de prietenie dintre doi useri pe baza statusului
     * @param IDtoRespond
     * @param IDwhoSent
     * @throws FriendshipRepoValidator
     */
    public void answerRequest(String IDtoRespond, String IDwhoSent, String status) throws Exception {
        if (!(getPendingRequestsOf(IDtoRespond).contains(new Friendship(IDwhoSent, IDtoRespond))))
        {
            throw new FriendshipRepoValidator("\nCererea nu exista!\n");
        }

        friendshipRepo.delete(IDwhoSent, IDtoRespond);

        if (status.equals("approved")) {
            addFriendship(IDwhoSent, IDtoRespond);
        }

        notifyObservers();
    }

    public ArrayList<Friendship> getPendingRequests() throws FriendshipRepoValidator, UserRepoValidator {
        Predicate<Friendship> isPending = x -> x.isStatus_Pending();

        var pendingRequests = getFriendships().stream()
                .filter(isPending)
                .toList();
        /*
        for(var x: pendingRequests){
            x.numeid1=x.getUser1().getLastName() + " " + x.getUser1().getFirstName();
            x.numeid2=x.getUser2().getLastName() + " " + x.getUser2().getFirstName();
        }
         */


        return new ArrayList<>(pendingRequests);
    }

    public ArrayList<Friendship> getPendingRequestsOf(String ID) throws FriendshipRepoValidator, UserRepoValidator {
        Predicate<Friendship> toUser = x -> x.getId2().equals(ID);

        var pendingRequestsOfUser = getPendingRequests().stream()
                .filter(toUser)
                .toList();

        return new ArrayList<>(pendingRequestsOfUser);
    }

    public ArrayList<Friendship> getSentRequestsOf(String ID) throws FriendshipRepoValidator, UserRepoValidator {
        Predicate<Friendship> toUser = x -> x.getId1().equals(ID);

        var pendingRequestsOfUser = getPendingRequests().stream()
                .filter(toUser)
                .toList();

        return new ArrayList<>(pendingRequestsOfUser);
    }

    public ArrayList<Friendship> transformListReceivedReuqests(String ID) throws UserRepoValidator, FriendshipRepoValidator {
        ArrayList<Friendship> all= this.getPendingRequestsOf(ID);
        all=this.constructAllFriendshipsWithUsers(all);

        for(var x : all){
            x.numeid1=x.getUser1().getLastName() + " " + x.getUser1().getFirstName();
            x.numeid2=x.getUser2().getLastName() + " " + x.getUser2().getFirstName();
        }
        return all;
    }

    public ArrayList<Friendship> transformListSentReuqests(String ID) throws UserRepoValidator, FriendshipRepoValidator {
        ArrayList<Friendship> all= this.getSentRequestsOf(ID);
        all=this.constructAllFriendshipsWithUsers(all);

        for(var x : all){
            x.numeid1=x.getUser1().getLastName() + " " + x.getUser1().getFirstName();
            x.numeid2=x.getUser2().getLastName() + " " + x.getUser2().getFirstName();
        }
        return all;
    }

    /*public ArrayList<ArrayList<User>> getCommunities() {

        var users = userRepo.getAll();

        if (users.size() == 0) {
            return null;
        }

        Map<User, Integer> nodes = new HashMap<>();
        Integer k = 0;

        for (var x : users) {
            nodes.put(x, 0);
        }

        for (var x : users) {
            if (nodes.get(x) == 0) {
                k++;
                DFS(x, nodes, k);
            }
        }

        ArrayList<ArrayList<User>> communities = new ArrayList<>(k);
        for (Integer x = 0 ; x < k ; x++) {
            communities.get(x) = new ArrayList<>();
        }

        for (var x : users) {
            var y = nodes.get(x);
            communities.add(y, );
        }
    }*/

    /**
     * calculeaza si returneaza numarul de comunitati (componente conexe pe baza prieteniilor)
     * @return Integer, nr. de comunitati
     */
    public Integer getNoOfCommunities() throws UserRepoValidator, FriendshipRepoValidator {

        var users = userRepo.getAll();

        if (users.size() < 2) {
            return users.size();
        }

        Map<String, Integer> nodes = new HashMap<>();
        Integer k = 0;

        for (var x : users) {
            nodes.put(x.getId(), 0);
        }

        for (var x : users) {
            if (nodes.get(x.getId()) == 0) {
                k++;
                DFS(x.getId(), nodes, k);
            }
        }

        return k;
    }

    public ArrayList<String> getMostSocialCommunity() throws UserRepoValidator, FriendshipRepoValidator {
        LongestRoad longestRoad = new LongestRoad(userRepo.getAll(), friendshipRepo.getAll());

        return longestRoad.calculate();
    }

    /**
     * functie ajutatoare, parcurge in adancime un graf pe baza relatiilor de prietenie (muchii) intre Useri (noduri)
     * @param userID
     * @param nodes
     * @param k
     */
    private void DFS  (String userID, Map<String, Integer> nodes, Integer k) throws FriendshipRepoValidator {
        nodes.put(userID, k);

        var friendships = friendshipRepo.getAllOfUserID(userID);

        String y;

        for (var x : friendships) {
            if (x.getId1().equals(userID)) {
                y = x.getId2();
            }
            else
            {
                y = x.getId1();
            }

            if (nodes.get(y) == 0) {
                DFS(y, nodes, k);
            }
        }
    }

    /**
     * Functia adauga un mesaj nou
     * @param id
     * @param id_from
     * @param id_to
     * @param mesaj
     * @throws Exception, in cazul in care mesajul creat nu e valid
     * Functia prelueaza data si ora la care a fost creat mesajul si le transmite ca parametru in mesajul creat
     */
    public void addNewMessage(String id,String id_from,String id_to,String mesaj) throws Exception {
        LocalDateTime currentDateTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        String formattedDateTime = currentDateTime.format(formatter);

        Message message=new Message(id,id_from,id_to,mesaj,formattedDateTime,"fara raspuns");

        messageValidator.validateMessage(message);

        messageRepo.add(message);
    }

    /**
     * Functia raspunde la un mesaj existent
     * @param id
     * @param id_from
     * @param id_to
     * @param mesaj
     * @param reply
     * @throws Exception, in cazul in care mesajul nu e valid
     * Functia prelueaza data si ora la care a fost creat mesajul si le transmite ca parametru in mesajul creat
     *
     */
    public void addResponse(String id,String id_from,String id_to,String mesaj,String reply)throws Exception{
        LocalDateTime currentDateTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        String formattedDateTime = currentDateTime.format(formatter);

        String destinatar_vechi=messageRepo.getMessage(reply).getId_To();
        String destinatar_nou="";
        String[] tokens = destinatar_vechi.split(" ");
        for (String t : tokens){
            if(t.equals(id_from))
                destinatar_nou=destinatar_nou+" "+id_to;
            else
                destinatar_nou=destinatar_nou+" "+t;
        }


        Message message=new Message(id,id_from,destinatar_nou,mesaj,formattedDateTime,reply);

        messageValidator.validateMessage(message);

        messageRepo.add(message);
    }

    /**
     * Functia sterge un mesaj
     * @param id
     * @throws MessageRepoValidator daca id -ul nu exista
     */
    public void deleteMessage(String id)throws MessageRepoValidator{
        messageRepo.delete(id);
    }

    /**
     * returneaza un mesaj din lista
     * @param id
     * @return
     * @throws MessageRepoValidator
     */
    public String getMessage(String id) throws MessageRepoValidator {
        return messageRepo.getMessage(id).getId_From();
    }
    /**
     * Returneaza lista intraega de mesaje
     * @return
     * @throws MessageRepoValidator
     */
    public ArrayList<Message> getMessages() throws MessageRepoValidator{
        return messageRepo.getAll();
    }

    /**
     * Returneaza lista mesajelor trimise de 2 useri unul altuia
     * @return
     * @throws MessageRepoValidator
     */
    public ArrayList<Message> getConversation(List<String> ids) throws MessageRepoValidator, UserRepoValidator {


        return constructAllMessagesWithUsers(messageRepo.getConversation(ids));
    }

    /**
     * Returneaza lista de mesaje la care userul cu id-ul dat nu a raspuns
     * @param id
     * @return
     * @throws MessageRepoValidator
     */
    public ArrayList<Message> getMessagesToRespond(String id) throws MessageRepoValidator{
        return messageRepo.getMessagesToRespond(id);
    }

    /**
     * obtine si returneaza toti Userii prieteni cu user
     * @return ArrayList<User>
     */
    public ArrayList<User> getFriendsOf (User user) throws UserRepoValidator, FriendshipRepoValidator {

        ArrayList<Friendship> userFriendships = constructAllFriendshipsWithUsers(getFriendships());

        ArrayList<User> friendsOfUser = new ArrayList<>();

        for (var x : userFriendships) {
            if (x.isStatus_Approved()) {
                if (x.getUser1().equals(user)) {
                    friendsOfUser.add(x.getUser2());
                }
                if (x.getUser2().equals(user)) {
                    friendsOfUser.add(x.getUser1());
                }
            }
        }

        return friendsOfUser;
    }

    private Message constructMessagesWithUsers (Message message) throws UserRepoValidator {

        var users = userRepo.getAll();
        List<User> foundUsers=new ArrayList<>();
            User user1 = users.stream()
                    .filter(x -> x.getId().equals(message.getId_From()))
                    .findFirst()
                    .get();

        List<String> messageusers=new ArrayList<>();
        String useri=message.getId_To();
        String[] s=useri.split(" ");
        //List<String> list = new ArrayList<>();
        for(var x:s){
            User user12= users.stream()
                    .filter(y -> y.getId().equals(x))
                    .findFirst()
                    .get();
            foundUsers.add(user12);
        }

        message.setUser_to(foundUsers);
        message.setUser_from(user1);

        return message;
    }

    private ArrayList<Message> constructAllMessagesWithUsers (ArrayList<Message> messages) throws UserRepoValidator {

        var users = userRepo.getAll();

        var messagesWithUsers = messages.stream()
                .map(x -> {
                    try {
                        return constructMessagesWithUsers(x);
                    } catch (UserRepoValidator e) {
                        e.printStackTrace();
                        return null;
                    }
                }).toList();

        return new ArrayList<Message>(messagesWithUsers);

    }



    /*
    -------------------------------------------------------
    GROUPS
    -------------------------------------------------------
     */

    private Group constructGroupsWithUsers (Group group) throws UserRepoValidator {

        var users = userRepo.getAll();
        List<User> foundUsers=new ArrayList<>();
        if (group.getTip()=="i"){
            User user1 = users.stream()
                    .filter(x -> x.getId().equals(group.getId().get(0)))
                    .findFirst()
                    .get();

            User user2 = users.stream()
                    .filter(x -> x.getId().equals(group.getId().get(1)))
                    .findFirst()
                    .get();
            foundUsers.add(user1);
            foundUsers.add(user2);
        }
        else{
            for(var x:group.getId()){
                User user1 = users.stream()
                        .filter(y -> y.getId().equals(x))
                        .findFirst()
                        .get();
                foundUsers.add(user1);
            }
        }
        group.setUseri(foundUsers);

        return group;
    }

    private ArrayList<Group> constructAllGroupsWithUsers (ArrayList<Group> groups) throws UserRepoValidator {

        var users = userRepo.getAll();

        var groupsWithUsers = groups.stream()
                .map(x -> {
                    try {
                        return constructGroupsWithUsers(x);
                    } catch (UserRepoValidator e) {
                        e.printStackTrace();
                        return null;
                    }
                }).toList();

        return new ArrayList<Group>(groupsWithUsers);

    }

    public void initGroups() throws FriendshipRepoValidator, UserRepoValidator, MessageRepoValidator {


        ArrayList<Friendship> friendships = getFriendships();
        for(var x:friendships){
            increaseNrGroups();
            String nume="";
            if(x.getId1().equals(currentUser.getId()))
                nume=x.getNumeid2();
            else
                nume=x.getNumeid1();
            String useri=x.getId1()+" "+ x.getId2();
            AddGroup(String.valueOf(nrGroups),nume,useri);
        }

    }

    public void AddGroup(String id,String  nume,String useri){
        List<String> list=new ArrayList<>();
        String[] s=useri.split(" ");
        for(var x:s){
            list.add(x);
        }
        Group group=new Group(list,id);
        group.setNume(nume);
        groupRepo.add(group);
    }

    public ArrayList<Group> getAllGroupsFromCurrentUser() throws UserRepoValidator {
        var x=groupRepo.getAllFromCurrent(currentUser.getId());
        x=constructAllGroupsWithUsers(x);

        for (var y:x){
            if(y.getId().size()==2)
            {
                List<User> useri=y.getUseri();
                if(currentUser.equals(useri.get(0)))
                    y.setNume(useri.get(1).getFirstName()+" "+useri.get(1).getLastName());
                else
                    if(currentUser.equals(useri.get(1)))
                        y.setNume(useri.get(0).getFirstName()+" "+useri.get(0).getLastName());
            }
        }

        return x;
    }


    public ArrayList<Group> getAllGroups() throws UserRepoValidator {
        var x=groupRepo.getAll();
        x=constructAllGroupsWithUsers(x);

        return x;
    }


    /*
    --------------------------------------------------------------------------
    --------------------------------------------------------------------------
    AUTH
    --------------------------------------------------------------------------
    --------------------------------------------------------------------------
    */

    public boolean isAuthValid (String currentEmail, String currentPassword) throws UserRepoValidator, NoSuchAlgorithmException, InvalidKeySpecException {

        if (hasUserByEmail(currentEmail) == false) {
            return false;
        }

        /*SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        System.out.println("Salt = " + toHex(salt) + " " + toHex(salt).getClass());
        System.out.println("Salt = " + toHex(salt).getBytes() + " " + toHex(salt).getBytes().getClass() + " " + toHex(salt).getBytes().toString().getBytes());

        KeySpec spec = new PBEKeySpec(currentPassword.toCharArray(), "[B@2e517bd9".getBytes(), 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        byte[] hash = factory.generateSecret(spec).getEncoded();

        System.out.println("Hash = " + toHex(hash));
        System.out.println("\n");
        System.out.println(hash.length);

        return false;
/**/
        String currentStoredSalt = getSaltByEmail(currentEmail);
        //String currentStoredSalt = "[B@7d9f52cd";
        String currentStoredHash = getHashByEmail(currentEmail);

        var generatedHash = generateHashForCurrent(currentPassword, currentStoredSalt.getBytes());

        //System.out.println("Hash = " + generatedHash.toString());
        //var z = generatedHash;

        if (generatedHash.equals(currentStoredHash) == false) {
            return false;
        }

        return true;
    }

    private String generateHashForCurrent(String password, byte[] currentSalt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecureRandom random = new SecureRandom();
//        byte[] salt = new byte[16];
//        random.nextBytes(salt);

        byte[] salt = currentSalt;

//        System.out.println("Salt = " + salt);

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        byte[] hash = factory.generateSecret(spec).getEncoded();

        /*System.out.println(hash);
        System.out.println("\n");
        System.out.println(hash.length);*/

        return toHex(hash);
    }

    private String getSaltByEmail(String email) throws UserRepoValidator {
        return userRepo.getSaltByEmail(email);
    }

    private String getHashByEmail(String email) throws UserRepoValidator {
        return userRepo.getHashByEmail(email);
    }

    /**
     * verifica daca un User exista dupa email-ul sau
     * @return True/ False
     */
    public boolean hasUserByEmail (String email) throws UserRepoValidator {
        Predicate<User> userEmail = x -> x.getEmail().equals(email);

        return userRepo.getAll().
                stream().
                anyMatch(userEmail);
    }

    /**
     * verifica daca un User exista dupa email-ul sau
     * @return User cu email dat
     */
    public User getUserByEmail (String email) throws UserRepoValidator {
        Predicate<User> userEmail = x -> x.getEmail().equals(email);

        return userRepo.getAll().
                stream().
                filter(userEmail).
                toList().
                get(0);


    }

    /**
     * verifica daca un User exista dupa email-ul sau
     * @return User cu id dat
     */
    public User getUserByID (String id) throws UserRepoValidator {
        Predicate<User> userEmail = x -> x.getEmail().equals(id);

        return userRepo.getAll().
                stream().
                filter(userEmail).
                toList().
                get(0);
    }


    /*
    --------------------------------------------------------------------------
    --------------------------------------------------------------------------
    HASH
    --------------------------------------------------------------------------
    --------------------------------------------------------------------------
    */

    private static String generateStorngPasswordHash(String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    public static String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);

        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }

    /*
    -------------------------------------
    STATISTICS
    -------------------------------------
     */

    public static int stringCompare(String str1, String str2)
    {

        int l1 = str1.length();
        int l2 = str2.length();
        int lmin = Math.min(l1, l2);

        for (int i = 0; i < lmin; i++) {
            int str1_ch = (int)str1.charAt(i);
            int str2_ch = (int)str2.charAt(i);

            if (str1_ch != str2_ch) {
                return str1_ch - str2_ch;
            }
        }

        // Edge case for strings like
        // String 1="Geeks" and String 2="Geeksforgeeks"
        if (l1 != l2) {
            return l1 - l2;
        }

        else {
            return 0;
        }
    }

    public ArrayList<Message> getStatisticsAllMessages(String datainceput,String datafinal) throws UserRepoValidator, MessageRepoValidator {
        ArrayList<Group> grupuri=getAllGroupsFromCurrentUser();
        ArrayList<Message> mesaje=new ArrayList<>();
        for(var x:grupuri){
            for(var y:getConversation(x.getId())){
                int i1=stringCompare(y.getData(),datainceput);
                int i2=stringCompare(y.getData(),datafinal);
                boolean b = i2 <= 0;
                if (i1>=0 && b){
                    mesaje.add(y);
                }
            }
        }
        mesaje=constructAllMessagesWithUsers(mesaje);
        return mesaje;
    }

    public ArrayList<Friendship> getStatisticsAllFriendships(String datainceput,String datafinal) throws UserRepoValidator, MessageRepoValidator, FriendshipRepoValidator {
        ArrayList<Friendship> prietenii=getFriendshipsOf(currentUser.getId());
        ArrayList<Friendship> prietenii1=new ArrayList<>();
        for(var x:prietenii){
            LocalDateTime date=x.getLocalDateTimeOfFriendshipRequest();
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            String formattedDateTime = date.format(formatter);
            int i1=stringCompare(formattedDateTime,datainceput);
            int i2=stringCompare(formattedDateTime,datafinal);
            boolean b = i2 <= 0;
            if (i1>=0 && b){
                prietenii1.add(x);
            }
        }
        return prietenii1;
    }

    public ArrayList<Message> getStatisticsMessagesreceivedfromUser(String datainceput,String datafinal,List<String> useri) throws UserRepoValidator, MessageRepoValidator {
        ArrayList<Group> grupuri=getAllGroupsFromCurrentUser();
        ArrayList<Message> mesaje=new ArrayList<>();
        String user="";
        if(useri.get(0).equals(currentUser.getId()))
            user=useri.get(1);
        else
            user=useri.get(0);

        for(var x:grupuri){
            if(x.getId().contains(user) && x.getId().size()==2){
                for(var y:getConversation(x.getId())){
                    int i1=stringCompare(y.getData(),datainceput);
                    int i2=stringCompare(y.getData(),datafinal);
                    boolean b = i2 <= 0;
                    if (i1>=0 && b){
                        mesaje.add(y);
                    }
                }
            }

        }
        return mesaje;
    }

    /*
    --------------------------------------------------------------------------
    --------------------------------------------------------------------------
    OBSERVER
    --------------------------------------------------------------------------
    --------------------------------------------------------------------------
    */

    ArrayList<Observer> observerArrayList = new ArrayList<>();

    @Override
    public void addObserver(Observer e) {
        observerArrayList.add(e);
    }

    @Override
    public void removeObserver(Observer e) {
        observerArrayList.remove(e);
    }

    @Override
    public void notifyObservers() {
        observerArrayList.stream().forEach(x -> x.update());
    }
}
