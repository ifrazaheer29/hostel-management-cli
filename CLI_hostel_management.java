import java.util.Scanner;

public class coursework {

    static Scanner input = new Scanner(System.in);
    static String[][] rooms = new String[0][6];
    static String[][] students = new String[0][5];
    static String[][] allocations = new String[0][5];
    static String[][] occupancy = new String[0][0]; 
    static String[][] pastAllocations = new String[0][6];

    public static void initializeOccupancy() {
        occupancy = new String[rooms.length][];
        for (int i = 0; i < rooms.length; i++) {
            int capacity = Integer.parseInt(rooms[i][3]); // room capacity
            occupancy[i] = new String[capacity];
            for (int j = 0; j < capacity; j++) {
                occupancy[i][j] = "EMPTY";
            }
        }
    }

    public static void main(String[] args) {
        login();
        mainMenu();
    }

    // === login page ===
    public static void login() { 
        final String USERNAME = "warden"; 
        final String PASSWORD = "1234"; 
        boolean loggedIn = false; 
        clearScreen(); 
        System.out.println("\n=== HostelMate Login ==="); 
        while (!loggedIn) { 
            System.out.print("\nUsername: "); 
            String user = input.nextLine(); 
            System.out.print("\nPassword: "); 
            String pass = input.nextLine(); 
            if (user.equals(USERNAME) && pass.equals(PASSWORD)) { 
                System.out.println("\nLogin successful. Welcome, warden!"); 
                loggedIn = true; 
            } else { 
                System.out.println("\nIncorrect credentials. Try again."); 
            } 
        } try { 
            Thread.sleep(3000); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        } 
        clearScreen(); 
    }

    // === main menu ===
    public static void mainMenu() {
        while (true) {
            System.out.println("\n=== HostelMate ===");
            System.out.println("\n1) Manage Rooms");
            System.out.println("\n2) Manage Students");
            System.out.println("\n3) Allocate Bed");
            System.out.println("\n4) Vacate Bed");
            System.out.println("\n5) Transfers");
            System.out.println("\n6) View Reports");
            System.out.println("\n7) Exit");
            System.out.print("\nChoose: ");

            String choice = input.nextLine();

            switch (choice) {
                case "1": manageRooms(); break;
                case "2": manageStudents(); break;
                case "3": allocateBed(); break;
                case "4": vacateBed(); break;
                case "5": transfers(); break;
                case "6": viewReports(); break;
                case "7": exit(); return;
                default: System.out.println("Invalid choice. Try again."); break;
            }

            pauseAndClear();
        }
    }

    // === manage rooms page ===
    public static void manageRooms() {
        while (true) {
            clearScreen();
            System.out.println("\n=== Manage Rooms ===");
            System.out.println("\na) Add Room");
            System.out.println("\nb) Update Room");
            System.out.println("\nc) Delete Room");
            System.out.println("\nd) Search Room");
            System.out.println("\ne) View All Rooms");
            System.out.println("\nf) Back");
            System.out.print("\nChoose: ");

            String choice = input.nextLine().toLowerCase();

            switch (choice) {
                case "a": addRoom(); break;
                case "b": updateRoom(); break;
                case "c": deleteRoom(); break;
                case "d": searchRoom(); break;
                case "e": viewRooms(); break;
                case "f": return; // back to main menu
                default: System.out.println("Invalid choice."); break;
            }

            pauseAndClear(); // pause before redisplaying sub-menu
        }
    }

    // === add room page ===
    public static void addRoom() {
        clearScreen();
        System.out.println("Add Room");

        // duplicate check for room ID
        System.out.printf("%-12s: ", "\nRoom ID");
        String roomID = input.nextLine();
            
        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i][0].equals(roomID)) {
                System.out.println("\nError! Room ID already exists.");
                return;
            }
        }
    
        System.out.printf("%-12s: ", "\nFloor");
        int floor = Integer.parseInt(input.nextLine());

        System.out.printf("%-12s: ", "\nRoom No");
        int roomNo = Integer.parseInt(input.nextLine());

        int capacity;
        while (true) {
            System.out.printf("%-12s: ", "\nCapacity");
            capacity = Integer.parseInt(input.nextLine());
            if (capacity <= 0) {
                System.out.println("\nError: Capacity must be greater than 0.");
            } else {
                break;
            }
        }

        double feePerDay;
        while (true) {
            System.out.printf("%-12s: ", "\nFee/Day");
            feePerDay = Double.parseDouble(input.nextLine());
            if (feePerDay < 0) {
                System.out.println("\nError: Fee cannot be negative.");
            } else {
                break;
            }
        }

        String[] newRoom = {
            roomID,
            Integer.toString(floor),
            Integer.toString(roomNo),
            Integer.toString(capacity),
            Double.toString(feePerDay),
            Integer.toString(capacity) // available beds = capacity
        };

        String[][] newRooms = new String[rooms.length + 1][6];
        for (int i = 0; i < rooms.length; i++) {
            newRooms[i] = rooms[i];
        }
        newRooms[newRooms.length - 1] = newRoom;
        rooms = newRooms; // replace old array

        System.out.println("\nRoom Added. Available beds: "+capacity);
        initializeOccupancy();
    }

    public static void updateRoom() {
        clearScreen();
        System.out.println("Update Room");

        System.out.print("\nRoom ID: ");
        String roomID = input.nextLine();

        // find the room by ID
        int index = -1;
        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i][0].equals(roomID)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            System.out.println("\nRoom not found!");
            return;
        }

        // extract existing data
        String oldFloor = rooms[index][1];
        String oldRoomNo = rooms[index][2];
        int oldCapacity = Integer.parseInt(rooms[index][3]);
        double oldFee = Double.parseDouble(rooms[index][4]);
        int oldAvailable = Integer.parseInt(rooms[index][5]);

        int occupiedBeds = oldCapacity - oldAvailable;

        // ask for updates
        System.out.print("\nNew Capacity (or - to skip): ");
        String capacityInput = input.nextLine();
        if (!capacityInput.equals("-")) {
            int newCapacity = Integer.parseInt(capacityInput);
            if (newCapacity < occupiedBeds) {
                System.out.println("\nError: New capacity cannot be less than occupied beds (" + occupiedBeds + ").");
                return;
            }
            int newAvailable = newCapacity - occupiedBeds;
            rooms[index][3] = Integer.toString(newCapacity);
            rooms[index][5] = Integer.toString(newAvailable);
        }

        System.out.print("\nNew Fee/Day (or - to skip): ");
        String feeInput = input.nextLine();
        if (!feeInput.equals("-")) {
            double newFee = Double.parseDouble(feeInput);
            if (newFee < 0) {
                System.out.println("\nError: Fee cannot be negative.");
                return;
            }
            rooms[index][4] = Double.toString(newFee);
        }

        // display updated details
        System.out.printf(
            "\nUpdated: %s | Floor=%s | RoomNo=%s | Capacity=%s | Fee/Day=%.2f | Avail=%s%n",
            rooms[index][0],
            rooms[index][1],
            rooms[index][2],
            rooms[index][3],
            Double.parseDouble(rooms[index][4]),
            rooms[index][5]
        );
    }

    public static void deleteRoom() {
        clearScreen();
        System.out.println("Delete Room");

        System.out.print("\nRoom ID: ");
        String roomID = input.nextLine();

        int index = -1;
        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i][0].equals(roomID)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            System.out.println("\nRoom not found!");
            return;
        }

        int capacity = Integer.parseInt(rooms[index][3]);
        int available = Integer.parseInt(rooms[index][5]);
        int occupied = capacity - available;

        if (occupied > 0) {
            System.out.println("\nCannot delete room. There are " + occupied + " occupied bed(s).");
            return;
        }

        // create new array (size - 1)
        String[][] newRooms = new String[rooms.length - 1][6];
        int newIndex = 0;

        for (int i = 0; i < rooms.length; i++) {
            if (i != index) {
                newRooms[newIndex] = rooms[i];
                newIndex++;
            }
        }

        rooms = newRooms; // compacted array

        System.out.println("\nDeleted Successfully.");
    }

    public static void searchRoom() {
        clearScreen();
        System.out.println("Search Room");
        System.out.print("\nRoom ID: ");
        String roomID = input.nextLine();
        boolean found = false;

        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i][0].equalsIgnoreCase(roomID)) {
                found = true;
                System.out.println("\nFound");
                System.out.println("ID     Floor  No   Cap  Avail  Fee/Day");
                System.out.println("--------------------------------------");
                System.out.printf("%-6s %-6s %-4s %-4s %-6s %.2f%n",
                        rooms[i][0], rooms[i][1], rooms[i][2],
                        rooms[i][3], rooms[i][5],
                        Double.parseDouble(rooms[i][4]));
                break;
            }
        }

        if (!found) {
            System.out.println("Room not found!");
        }
    }

    public static void viewRooms() {
        clearScreen();
        System.out.println("All Rooms\n");

        sortRoomsByAvailableBeds();
        
        System.out.println("ID     Floor  No   Cap  Avail  Fee/Day");
        System.out.println("--------------------------------------");

        if (rooms.length == 0) {
            System.out.println("No rooms available.");
            return;
        }

        for (int i = 0; i < rooms.length; i++) {
            System.out.printf("%-6s %-6s %-4s %-4s %-6s %.2f%n",
                    rooms[i][0], rooms[i][1], rooms[i][2],
                    rooms[i][3], rooms[i][4],
                    Double.parseDouble(rooms[i][5]));
        }
    }

    public static void manageStudents() { 
        while (true) {
            clearScreen();
            System.out.println("=== Manage Students ===");
            System.out.println("a) Add Student");
            System.out.println("b) Update Student");
            System.out.println("c) Delete Student");
            System.out.println("d) Search Student");
            System.out.println("e) View All Students");
            System.out.println("f) Back");
            System.out.print("Choose: ");

            String choice = input.nextLine().toLowerCase();

            switch (choice) {
                case "a": addStudent(); break;
                case "b": updateStudent(); break;
                case "c": deleteStudent(); break;
                case "d": searchStudent(); break;
                case "e": viewStudents(); break;
                case "f": return; // back to main menu
                default: System.out.println("Invalid choice."); break;
            }

            pauseAndClear(); // pause before redisplaying sub-menu
        }
    }

    public static void addStudent() {
        clearScreen();
        System.out.println("Add Student");

        System.out.printf("%-12s: ", "Student ID");
        String stuID = input.nextLine();

        // check for unique ID
        for (int i = 0; i < students.length; i++) {
            if (students[i][0].equals(stuID)) {
                System.out.println("Student ID already exists!");
                return;
            }
        }

        System.out.printf("%-12s: ", "Name");
        String stuName = input.nextLine();

        // contact validation loop
        String stuContact;
        while (true) {
            System.out.printf("%-12s: ", "Contact");
            stuContact = input.nextLine();
            if (stuContact.matches("\\d{10}")) break;
            System.out.println("Invalid contact number! Must be 10 digits.");
        }

        // email validation loop
        String stuEmail;
        while (true) {
            System.out.printf("%-12s: ", "Email");
            stuEmail = input.nextLine();
            if (stuEmail.contains("@") && stuEmail.contains(".")) break;
            System.out.println("Invalid email! Must contain '@' and '.'");
        }

        String stuStatus = "ACTIVE";
        System.out.printf("%-12s: ", "Status"); 
        System.out.print(stuStatus);

        // create new student record
        String[] newStudent = {
            stuID,
            stuName,
            stuContact,
            stuEmail,
            stuStatus
        };

        // expand array to add new student
        String[][] newStudents = new String[students.length + 1][5];
        for (int i = 0; i < students.length; i++) {
            newStudents[i] = students[i];
        }
        newStudents[newStudents.length - 1] = newStudent;
        students = newStudents; // replace old array

        System.out.println();
        System.out.println("Student added.");
    }

    public static void updateStudent() { 
        clearScreen(); 
        System.out.println("Update Student"); 

        System.out.print("Student ID to update: ");
        String stuID = input.nextLine();

        // find student details by ID
        int index = -1;
        for (int i = 0; i < students.length; i++) {
            if (students[i][0].equals(stuID)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            System.out.println("Student not found!");
            return;
        }

        // extract existing data
        String oldStuContact = students[index][2];
        String oldStuEmail = students[index][3];

        // ask for updates
        System.out.print("New Contact (or -): ");
        String stuContactInput = input.nextLine();
        if (!stuContactInput.equals("-")) {
            students[index][2] = stuContactInput;
        }

        System.out.print("New Email (or -): ");
        String stuEmailInput = input.nextLine();
        if (!stuEmailInput.equals("-")) {
            students[index][3] = stuEmailInput;
        }

        // display updated details
        System.out.printf(
            "Updated: %s | %s | %s | %s | %s ",
            students[index][0],
            students[index][1],
            students[index][2],
            students[index][3],
            students[index][4]
        );
    }

    public static void deleteStudent() { 
        clearScreen(); 
        System.out.println("Delete Student"); 

        System.out.print("Student ID: ");
        String stuID = input.nextLine();

        int index = -1;
        for (int i = 0; i < students.length; i++) {
            if (students[i][0].equals(stuID)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            System.out.println("Student not found!");
            return;
        }

        // create new array (size - 1)
        String[][] newStudents = new String[students.length - 1][5];
        int newIndex = 0;

        for (int i = 0; i < students.length; i++) {
            if (i != index) {
                newStudents[newIndex] = students[i];
                newIndex++;
            }
        }

        students = newStudents; // compacted array

        System.out.println("Deleted Successfully.");
    }

    public static void searchStudent() { 
        clearScreen(); 
        System.out.println("Search Student"); 

        System.out.print("     Student ID: ");
        String stuID = input.nextLine();
        boolean found = false;

        for (int i = 0; i < students.length; i++) {
            if (students[i][0].equalsIgnoreCase(stuID)) {
                found = true;
                System.out.println("     Found");
                System.out.println();
                System.out.printf("     %-6s %-15s %-12s %-20s %-8s%n",
                        "ID", "Name", "Contact", "Email", "Status");
                System.out.println("     ----------------------------------------------------------------------------");
                System.out.printf("     %-6s %-15s %-12s %-20s %-8s%n",
                        students[i][0], students[i][1], students[i][2],
                        students[i][3], students[i][4]);
                break;
            }
        }

        if (!found) {
            System.out.println("Student not found!");
        }
    }

    public static void viewStudents() { 
        clearScreen(); 
        System.out.println("All Students\n"); 

        if (students.length == 0) {
            System.out.println("No students found.");
            return;
        }

        System.out.printf("     %-6s %-15s %-12s %-20s %-8s%n",
                "ID", "Name", "Contact", "Email", "Status");
        System.out.println("     ---------------------------------------------------------------------------");

        for (int i = 0; i < students.length; i++) {
            System.out.printf("     %-6s %-15s %-12s %-20s %-8s%n",
                    students[i][0], students[i][1], students[i][2],
                    students[i][3], students[i][4]);
        }
    }

    public static void allocateBed() { 
        clearScreen(); 
        System.out.println("Allocate Bed"); 

        System.out.printf("%-12s:", "     Student ID");
        String stuID = input.nextLine();

        int stuIndex = -1;
        for (int i = 0; i < students.length; i++) {
            if (students[i][0].equals(stuID) && students[i][4].equals("ACTIVE")) {
                stuIndex = i;
                break;
            }
        }

        if (stuIndex == -1) {
            System.out.println("Invalid student ID or not ACTIVE. Can not allocate bed");
            return;
        }

        // Check if student already has active allocation
        for (int i = 0; i < allocations.length; i++) {
            if (allocations[i][0].equals(stuID)) {
                System.out.println("Student already has a bed allocated.");
                return;
            }
        }

        System.out.printf("%-12s:", "     Room ID");
        String roomID = input.nextLine();

        int roomIndex = -1;
        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i][0].equals(roomID) && Integer.parseInt(rooms[i][5])>0) {
                roomIndex = i;
                break;
            }
        }

        if (roomIndex == -1) {
            System.out.println("Invalid room ID or no available beds.");
            return;
        }

        System.out.printf("%-12s:", "     Due Date");
        String dueDate = input.nextLine();

        // use current date as check in date
        String checkInDate = java.time.LocalDate.now().toString();

        // validate due date >= check in date
        if (dueDate.compareTo(checkInDate) < 0) {
            System.out.println("Due date cannot be before check-in date.");
            return;
        }

        int capacity = Integer.parseInt(rooms[roomIndex][3]); 
        int availableBeds = Integer.parseInt(rooms[roomIndex][5]); 
        int bedIndex = capacity - availableBeds; // lowest available bed

        // update available beds
        rooms[roomIndex][5] = Integer.toString(availableBeds - 1);

        // add allocation
        String[][] newAllocations = new String[allocations.length + 1][5];
        for (int i = 0; i < allocations.length; i++) {
            newAllocations[i] = allocations[i];
        }
        newAllocations[allocations.length] = new String[]{stuID, roomID, Integer.toString(bedIndex), checkInDate, dueDate};
        allocations = newAllocations;

        occupancy[roomIndex][bedIndex] = stuID;

        System.out.println("     Allocated: " + stuID + " -> Room " + roomID + " Bed " + bedIndex);
        System.out.println("Available beds (" + roomID + ") : " + rooms[roomIndex][5]);
    }

    public static void vacateBed() { 
        clearScreen(); 
        System.out.println("Vacate Bed"); 

        System.out.printf("%-12s:", "     Student ID");
        String stuID = input.nextLine();

        System.out.printf("%-12s:", "     Room ID");
        String roomID = input.nextLine();

        int allocIndex = -1;
        for (int i = 0; i < allocations.length; i++) {
            if (allocations[i][0].equals(stuID) && allocations[i][1].equals(roomID)) {
                allocIndex = i;
                break;
            }
        }

        if (allocIndex == -1) {
            System.out.println("No allocation.");
            return;
        }

        int roomIndex = getRoomIndex(roomID);

        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalDate dueDate = java.time.LocalDate.parse(allocations[allocIndex][4]);

        long overdueDays;
        double fine = 0.0;
        String status;

        if (today.isBefore(dueDate)) {  // Early checkout → Overdue
            overdueDays = java.time.temporal.ChronoUnit.DAYS.between(today, dueDate); 
            fine = overdueDays * Double.parseDouble(rooms[roomIndex][4]);
            status = "Overdue";

            System.out.println("     Overdue days: " + overdueDays + " | Fee/Day: "+rooms[roomIndex][4]+ " | Fine: "+fine);
        } else {  // On time / Late
            overdueDays = 0;
            status = "Completed";
            System.out.println("     Checkout completed with no overdue.");
        }

        // Increase bed availability
        int availableBeds = Integer.parseInt(rooms[roomIndex][5]);
        rooms[roomIndex][5] = Integer.toString(availableBeds + 1);
        System.out.println("     Bed freed. Available beds ("+roomID+"): "+(availableBeds + 1));

        // Store in history
        pastAllocations = addRow(pastAllocations, new String[]{
            allocations[allocIndex][0],
            allocations[allocIndex][1],
            allocations[allocIndex][2],
            today.toString(),
            Double.toString(fine),
            status
        });

        // Clear bed and remove from current list
        int bedIndex = Integer.parseInt(allocations[allocIndex][2]);
        occupancy[roomIndex][bedIndex] = "EMPTY";

        String[][] newAllocations = new String[allocations.length - 1][5];
        int newIndex = 0;
        for (int i = 0; i < allocations.length; i++) {
            if (i != allocIndex) {
                newAllocations[newIndex++] = allocations[i];
            }
        }
        allocations = newAllocations;
    }

    public static String[][] addRow(String[][] existingArray, String[] newRecord) {
        String[][] updatedArray = new String[existingArray.length + 1][newRecord.length];
        
        for (int i = 0; i < existingArray.length; i++) {
            updatedArray[i] = existingArray[i];
        }
        
        updatedArray[existingArray.length] = newRecord;
        return updatedArray;
    }

    public static void transfers() { 
        clearScreen(); 
        System.out.println("Transfer"); 

        System.out.printf("%-15s: ", "     Student ID");
        String stuID = input.nextLine();

        System.out.printf("%-15s: ", "     From Room");
        String fromRoom = input.nextLine();

        System.out.printf("%-15s: ", "     To Room");
        String toRoom = input.nextLine();

        // find allocation
        int allocIndex = -1;
        for (int i = 0; i < allocations.length; i++) {
            if (allocations[i][0].equals(stuID) && allocations[i][1].equals(fromRoom)) {
                allocIndex = i;
                break;
            }
        }

        if (allocIndex == -1) {
            System.out.println("     No allocation found for that student in the given room.");
            return;
        }

        // find fromRoom and toRoom indices
        int fromRoomIndex = -1, toRoomIndex = -1;
        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i][0].equals(fromRoom)) fromRoomIndex = i;
            if (rooms[i][0].equals(toRoom)) toRoomIndex = i;
        }

        if (toRoomIndex == -1) {
            System.out.println("     Target room not found.");
            return;
        }

        if (fromRoomIndex == -1) {
            System.out.println("     Source room not found.");
            return;
        }

        // check availability
        int availTo = Integer.parseInt(rooms[toRoomIndex][5]);
        if (availTo <= 0) {
            System.out.println("     Target room has no available beds.");
            return;
        }

        // find lowest available bed in target room
        int newBedIndex = -1;
        for (int j = 0; j < occupancy[toRoomIndex].length; j++) {
            if (occupancy[toRoomIndex][j].equals("EMPTY")) {
                newBedIndex = j;
                break;
            }
        }

        if (newBedIndex == -1) {
            System.out.println("     No empty bed found in target room.");
            return;
        }

        // get old bed index
        int oldBedIndex = Integer.parseInt(allocations[allocIndex][2]);

        // update occupancy
        occupancy[fromRoomIndex][oldBedIndex] = "EMPTY";
        occupancy[toRoomIndex][newBedIndex] = stuID;

        // update allocations
        allocations[allocIndex][1] = toRoom;
        allocations[allocIndex][2] = Integer.toString(newBedIndex);

        // update available beds
        int availFrom = Integer.parseInt(rooms[fromRoomIndex][5]);
        rooms[fromRoomIndex][5] = Integer.toString(availFrom + 1);

        rooms[toRoomIndex][5] = Integer.toString(availTo - 1);

        // log transfer date (not stored, just for display)
        String transferDate = java.time.LocalDate.now().toString();

        System.out.println("     Transferred to " + toRoom + " Bed " + newBedIndex);
        System.out.println("     Avail (" + fromRoom + ") : " + (availFrom + 1) +
                        " | Avail (" + toRoom + ") : " + (availTo - 1));
    }

    // manual bubble sorting for rooms by available beds
    public static void sortRoomsByAvailableBeds() {
        for (int i = 0; i < rooms.length - 1; i++) {
            for (int j = 0; j < rooms.length - i - 1; j++) {
                int availJ = Integer.parseInt(rooms[j][5]);
                int availJ1 = Integer.parseInt(rooms[j + 1][5]);
                
                if (availJ < availJ1) {
                    // swap rooms[j] and rooms[j+1]
                    String[] temp = rooms[j];
                    rooms[j] = rooms[j + 1];
                    rooms[j + 1] = temp;
                }
            }
        }
    }

    public static void viewReports() { 
        while (true) {
            clearScreen();
            System.out.println("=== View Reports ===");
            System.out.println("a) Occupancy Map");
            System.out.println("b) Vacant Beds by Floor");
            System.out.println("c) Students per Room");
            System.out.println("d) Overdue Dues");
            System.out.println("e) Revenue Projection (Daily)");
            System.out.println("f) Back");
            System.out.print("Choose: ");

            String choice = input.nextLine().toLowerCase();

            switch (choice) {
                case "a": occupancyMap(); break;
                case "b": vacantBedsFloor(); break;
                case "c": studentsPerRoom(); break;
                case "d": overdueDues(); break;
                case "e": revenueProjection(); break;
                case "f": return; // back to main menu
                default: System.out.println("Invalid choice."); break;
            }

            pauseAndClear(); // pause before redisplaying sub-menu
        }
    }

    public static void occupancyMap() {
        clearScreen(); 
        System.out.println("Occupancy Grid (rooms x beds)");

        if (occupancy.length == 0) {
            System.out.println("No rooms available.");
            return;
        }

        // print header
        System.out.printf("%-8s %s%n", "     RoomRow", "Beds");
        System.out.println("     -------------------------------");

        // print each row manually
        for (int i = 0; i < occupancy.length; i++) {
            System.out.print("     "+i + "        [");
            for (int j = 0; j < occupancy[i].length; j++) {
                System.out.print(occupancy[i][j]);
                if (j < occupancy[i].length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");
        }
    }

    public static void vacantBedsFloor() {
        clearScreen(); 
        System.out.println("Vacant Beds by Floor");

        // print header
        System.out.printf("%-6s %-10s %-10s %-10s %-10s%n", "Floor", "TotalRooms", "TotalBeds", "Occupied", "Vacant");
        System.out.println("-------------------------------------------------------------");

        // find distinct floors
        for (int i = 0; i < rooms.length; i++) {
            String floor = rooms[i][1]; // floor of current room

            // check if this floor was already processed
            boolean alreadyProcessed = false;
            for (int j = 0; j < i; j++) {
                if (rooms[j][1].equals(floor)) {
                    alreadyProcessed = true;
                    break;
                }
            }
            if (alreadyProcessed) continue;

            // aggregate stats for this floor
            int totalRooms = 0;
            int totalBeds = 0;
            int occupiedBeds = 0;

            for (int r = 0; r < rooms.length; r++) {
                if (rooms[r][1].equals(floor)) {
                    totalRooms++;
                    int capacity = Integer.parseInt(rooms[r][3]);
                    totalBeds += capacity;

                    // count occupied beds using occupancy array
                    for (int b = 0; b < occupancy[r].length; b++) {
                        if (!occupancy[r][b].equals("EMPTY")) {
                            occupiedBeds++;
                        }
                    }
                }
            }

            int vacantBeds = totalBeds - occupiedBeds;

            // print row
            System.out.printf("%-6s %-10d %-10d %-10d %-10d%n", floor, totalRooms, totalBeds, occupiedBeds, vacantBeds);
        }
    }

    public static void studentsPerRoom() {
        clearScreen(); 
        System.out.println("Students per Room");

        // print header
        System.out.printf("%-8s %-6s %s%n", "Room", "Count", "Students");
        System.out.println("-----------------------------------------");

        for (int i = 0; i < rooms.length; i++) {
            String roomID = rooms[i][0];
            int count = 0;
            String studentsList = "";

            // Loop through allocations to find students in this room
            for (int j = 0; j < allocations.length; j++) {
                if (allocations[j][1].equals(roomID)) {
                    count++;
                    if (studentsList.isEmpty()) {
                        studentsList = allocations[j][0];
                    } else {
                        studentsList += ", " + allocations[j][0];
                    }
                }
            }

            // print each room's student info inside the loop
            System.out.printf("%-8s %-6d %s%n", roomID, count, studentsList);
        }
    }

    public static void overdueDues() {
        clearScreen(); 
        System.out.println("Overdue Dues\n");

        if (pastAllocations.length == 0) {
            System.out.println("No past allocations found.");
            return;
        }

        System.out.printf("%-12s %-8s %-12s %-9s %s%n", 
                "Student", "Room", "DaysOverdue", "Fee/Day", "EstimatedFine");
        System.out.println("---------------------------------------------------------");

        boolean hasOverdue = false;

        for (int i = 0; i < pastAllocations.length; i++) {
            // Only show records marked as "Overdue"
            if (pastAllocations[i][5].equals("Overdue")) {

                hasOverdue = true;

                String stuID = pastAllocations[i][0];
                String roomID = pastAllocations[i][1];
                double fine = Double.parseDouble(pastAllocations[i][4]);
                int roomIndex = getRoomIndex(roomID);
                double feePerDay = Double.parseDouble(rooms[roomIndex][4]);

                // Calculate overdue days from fine
                long overdueDays = (long) (fine / feePerDay);

                System.out.printf("%-12s %-8s %-12d %-9.2f %.2f%n",
                        stuID, roomID, overdueDays, feePerDay, fine);
            }
        }

        if (!hasOverdue) {
            System.out.println("No overdue dues found.");
        }
    }

    public static int getRoomIndex(String roomID) {
        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i][0].equalsIgnoreCase(roomID)) {
                return i;
            }
        }
        return -1; // not found
    }

    public static void revenueProjection() {
        clearScreen(); 
        System.out.println("Revenue Projection (Daily)");

        double totalRevenue = 0.0;

        for (int i = 0; i < allocations.length; i++) {
            String roomID = allocations[i][1];
            int roomIndex = getRoomIndex(roomID);
            double feePerDay = Double.parseDouble(rooms[roomIndex][4]);
            totalRevenue += feePerDay;
        }

        System.out.printf("             $ feePerDay for currently occupied beds = %.2f LKR%n",
                totalRevenue);
    }

    public static void exit() { 
        clearScreen(); 

        try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        System.exit(0);  // terminate the program
    }

    // === clearscreen between pages ===
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                new ProcessBuilder("clear").inheritIO().start().waitFor();
        } catch (Exception e) { 
            System.out.println(e); 
        }
    }

    public static void pauseAndClear() {
        try { Thread.sleep(3000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        clearScreen();
    }
}