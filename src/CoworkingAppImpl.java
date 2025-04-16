import entities.CoworkingSpace;
import entities.Reservation;
import entities.User;
import entities.enums.Roles;
import exceptions.CoworkingSpaceNotFoundException;
import exceptions.ReservationNotFoundException;
import service.FileService;

import java.util.*;

public class CoworkingAppImpl implements CoworkingApp{
    private final String SPACES_FILE = "spaces.dat";
    private final String RESERVATIONS_FILE = "reservations.dat";

    private List<CoworkingSpace> coworkingSpaces = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();
    private User currentUser;
    private static int reservationIdCounter;
    private static int coworkingSpaceCounter;
    private Scanner in = new Scanner(System.in);

    private int findMaxReservationId(List<Reservation> reservations){
        return reservations.stream().mapToInt(Reservation::getId).max().orElse(0);
    }

    private int findMaxCoworkingSpaceId(List<CoworkingSpace> coworkingSpaces){
        return coworkingSpaces.stream().mapToInt(CoworkingSpace::getId).max().orElse(0);
    }


    @Override
    public void addSpace() {
        System.out.println("Enter space type: (ex: Open,Private,Room)");
        String spaceType = in.nextLine();
        System.out.println("Enter space price: ");
        int price = in.nextInt();
        coworkingSpaces.add(new CoworkingSpace(coworkingSpaceCounter++, spaceType,price));
        FileService.saveToFile(coworkingSpaces, SPACES_FILE);
        System.out.println("New coworking space was successfully added");

        getAllAvailableSpaces();
    }

    @Override
    public void removeSpace() {
        getAllAvailableSpaces();
        System.out.println("Enter coworking space id to remove");
        try {
            int id = in.nextInt();
            coworkingSpaces.remove(coworkingSpaces.get(id));
            FileService.saveToFile(coworkingSpaces, SPACES_FILE);
            System.out.println("Coworking space was removed successfully");
        }catch (CoworkingSpaceNotFoundException exception){
            throw new CoworkingSpaceNotFoundException("Coworking space with given Id not found");

        }
    }

    @Override
    public void viewAllReservations() {
        reservations = FileService.readFromFile(RESERVATIONS_FILE);
        if (reservations.isEmpty()){
            System.out.println("No reservations");
        }
        System.out.println("Printing all active reservations");
        reservations.forEach(System.out::println);
        System.out.println();
    }



    @Override
    public void cancelReservation() {

        viewMyReservations();
        System.out.println("Enter reservation id to remove");
        int id = in.nextInt();
        try {
        if (reservations.get(id).getCustomerId() == currentUser.getId()) {
            reservations.remove(id);
            System.out.println("Reservation with id " + id + " was successfully deleted");
            FileService.saveToFile(reservations, RESERVATIONS_FILE);
        }else {
            System.out.println("You can`t delete not your reservation");
        }}
        catch (ReservationNotFoundException exception){
            throw new ReservationNotFoundException("Reservation with given Id not found");
        }
    }

    @Override
    public void getAllAvailableSpaces() {
        coworkingSpaces = FileService.readFromFile(SPACES_FILE);
        coworkingSpaces.stream().forEach(System.out::println);
        System.out.println();
    }

    @Override
    public void makeReservation() {
        getAllAvailableSpaces();
        System.out.print("Enter space ID to reserve: ");
        int spaceId = in.nextInt();
        Optional<CoworkingSpace> selectedSpace = coworkingSpaces.stream().filter(space -> space.getId() == spaceId).findFirst();


        if (selectedSpace.isPresent()) {
            in.nextLine();
            System.out.print("Enter date (YYYY-MM-DD): ");
            String date = in.nextLine();
            System.out.print("Enter start time (HH:MM): ");
            String startTime = in.nextLine();
            System.out.print("Enter end time (HH:MM): ");
            String endTime = in.nextLine();
            reservations.add(new Reservation(reservationIdCounter++, currentUser.getId(), spaceId, date, startTime, endTime));
            FileService.saveToFile(reservations, RESERVATIONS_FILE);
            System.out.println("Reservation made successfully!\n");
        }else {
            System.out.println("Invalid space ID! Please try again.");
        }
    }

    @Override
    public void viewMyReservations() {
        reservations = FileService.readFromFile(RESERVATIONS_FILE);
        System.out.println("Printing your reservations");

        Map<Integer, List<Reservation>> reservationsByUser = new HashMap<>();
        for (Reservation r : reservations) {
            reservationsByUser
                    .computeIfAbsent(r.getCustomerId(), k -> new ArrayList<>())
                    .add(r);
        }

        List<Reservation> myReservations = reservationsByUser.get(currentUser.getId());
        if (myReservations == null || myReservations.isEmpty()) {
            System.out.println("You have no reservations.");
        } else {
            myReservations.forEach(System.out::println);
        }
    }


    @Override
    public void mainMenu() {
        while (true){
            System.out.println("Welcome to Coworking Space Booking Application");
            System.out.println("1- Admin Login \n 2- Customer Login \n 3-Exit");
            System.out.println("Write number for your choice:");
            int choice = in.nextInt();
            in.nextLine();

            if (choice == 1){
                adminMenu();
            }else if (choice == 2){
                customerMenu();
            }else if (choice == 3){
                break;
            }else {
                System.out.println("Number is wrong");
            }
        }
    }

    @Override
    public void customerMenu() {
        System.out.println("Enter your name");
        String name = in.nextLine();
        System.out.println("Enter your password");
        String password = in.nextLine();
        boolean authenticated = false;
        for (User user : users){
            if (user.getName().equals(name) && user.getPassword().equals(password)){
                authenticated = true;
                currentUser = user;
                break;
            }
        }
           if (!authenticated){
               System.out.println("Invalid name or password");
               System.out.println();
           }
        while (authenticated){
            System.out.println("1. View All Spaces\n2. Make Reservation\n3. View My Reservations\n4. Cancel Reservation\n5. Logout");
            System.out.println("Write number for your choice:");
            int choice = in.nextInt();
            in.nextLine();

            if (choice==1) getAllAvailableSpaces();
            else if (choice==2) makeReservation();
            else if (choice==3) viewMyReservations();
            else if (choice==4) cancelReservation();
            else if (choice ==5) {
                authenticated=false;

                break;
            }
            else {
                System.out.println("Invalid choice");
            }

        }
    }

    @Override
    public void adminMenu() {
        System.out.println("Enter your name");
        String name = in.nextLine();
        System.out.println("Enter your password");
        String password = in.nextLine();
        boolean authenticated = false;

        for (User user : users){
            if (user.getName().equals(name) && user.getPassword().equals(password) && user.getRole().equals(Roles.ADMIN)){
                authenticated = true;
                currentUser = user;
                break;
            }
        }
        if (!authenticated){
                System.out.println("Invalid name or password");
        }
        while (authenticated){
            System.out.println("1. Add Space\n2. Remove Space\n3. View Reservations\n4. Logout");
            System.out.println("Write number for your choice:");
            System.out.print("Enter choice: ");
            int choice = in.nextInt();
            in.nextLine();

            if (choice==1) addSpace();
            else if (choice==2) removeSpace();
            else if (choice==3) viewAllReservations();
            else if (choice==4) {
                authenticated = false;
                break;
            }
            else {
                System.out.println("Invalid choice");
            }
        }
    }

    public CoworkingAppImpl() {
        coworkingSpaces = FileService.readFromFile(SPACES_FILE);
        reservations = FileService.readFromFile(RESERVATIONS_FILE);


        users.add(new User(1, "Rifat", "Aisabakiev", "123", Roles.CUSTOMER, 5000));
        users.add(new User(2, "Alex", "Grant", "1234567890", Roles.ADMIN, 0));
        users.add(new User(3, "Martin", "Targaryen", "qwerty", Roles.CUSTOMER, 5000));

        if (coworkingSpaces.isEmpty()) {
            coworkingSpaces.add(new CoworkingSpace(1, "Room1", 2000));
            coworkingSpaces.add(new CoworkingSpace(2, "Private", 5000));
            coworkingSpaces.add(new CoworkingSpace(3, "Open", 1000));
            FileService.saveToFile(coworkingSpaces,SPACES_FILE);
        }
        reservationIdCounter = findMaxReservationId(reservations) + 1;
        coworkingSpaceCounter = findMaxCoworkingSpaceId(coworkingSpaces) + 1;

    }
}
