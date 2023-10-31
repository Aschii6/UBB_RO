package UI;

import Domain.User;
import Service.Service;

import java.util.Scanner;

public class Console {
    private Service service;

    public Console(Service service) {
        this.service = service;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        int nr;

        while (true){
            System.out.println("""
                0.Exit
                1.See users and friendships
                2.Add user(First Name, Last Name)
                3.Delete user(ID)
                4.Add friendship
                5.Delete friendship
                6.No of communities
                7.Biggest community""");

            try{
                nr = scanner.nextInt();
            } catch (Exception e){
                System.out.println("Command must be int.");
                scanner.nextLine();
                continue;
            }

            switch (nr){
                case 0:
                    return;
                case 1:
                    printAllUsersAndFriendships();
                    break;
                case 2:
                    addUser();
                    break;
                case 3:
                    deleteUser();
                    break;
                case 4:
                    addFriendship();
                    break;
                case 5:
                    removeFriendship();
                    break;
                case 6:
                    noOfCommunitites();
                    break;
                case 7:
                    biggestCommunity();
                    break;
                default:
                    System.out.println("Invalid command.");
            }
        }
    }

    private void printAllUsersAndFriendships(){
        service.getAllUsers().forEach(System.out::println);
    }

    private void addUser(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("First Name: ");
        String firstName = scanner.nextLine();
        System.out.println("Last Name: ");
        String lastName = scanner.nextLine();

        System.out.println("Optionally ID: (0 for implicit ID)");

        long id;
        try{
            id = scanner.nextLong();
        } catch (Exception e){
            System.out.println("ID must be long.");
            return;
        }

        try{
            service.addUser(firstName, lastName, id);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void deleteUser(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("ID: ");

        long id;
        try{
            id = scanner.nextLong();
        } catch (Exception e){
            System.out.println("ID must be long.");
            return;
        }

        try{
            service.deleteUser(id);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void addFriendship(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("ID1: ");

        long id1, id2;

        try{
            id1 = scanner.nextLong();
        } catch (Exception e){
            System.out.println("ID must be long.");
            return;
        }

        System.out.println("ID2: ");
        try{
            id2 = scanner.nextLong();
        } catch (Exception e){
            System.out.println("ID must be long.");
            return;
        }

        try{
            service.addFriendship(id1, id2);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void removeFriendship() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("ID1: ");

        long id1, id2;

        try{
            id1 = scanner.nextLong();
        } catch (Exception e){
            System.out.println("ID must be long.");
            return;
        }

        System.out.println("ID2: ");
        try{
            id2 = scanner.nextLong();
        } catch (Exception e){
            System.out.println("ID must be long.");
            return;
        }

        try{
            service.removeFriendship(id1, id2);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void noOfCommunitites() {
        int nr = service.noOfCommunities();
        System.out.println("No of communities: " + nr);
    }

    private void biggestCommunity() {
        Iterable<User> iterable = service.biggestCommunity();
        if (iterable == null){
            System.out.println("No communities.");
            return;
        }

        System.out.println("Biggest community: ");
        iterable.forEach(System.out::println);
    }
}
