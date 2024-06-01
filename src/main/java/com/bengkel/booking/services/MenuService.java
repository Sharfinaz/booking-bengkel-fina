package com.bengkel.booking.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.bengkel.booking.models.BookingOrder;
import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.repositories.CustomerRepository;
import com.bengkel.booking.repositories.ItemServiceRepository;

public class MenuService {
    private static List<Customer> listAllCustomers = CustomerRepository.getAllCustomer();
    private static List<ItemService> listAllItemService = ItemServiceRepository.getAllItemService();
    private static List<BookingOrder> listAllBookingOrder = new ArrayList<>();
    private static Scanner input = new Scanner(System.in);
    private static final int MAX_LOGIN_ATTEMPTS = 3;

    public static void run() {
        boolean isLooping = true;

        do {
            int choice = mainMenu();

            switch (choice) {
                case 1:
                    if (login()) {
                        bookingBengkelMenu();
                    }
                    break;
                case 2:
                    System.out.println("Goodbye!");
                    isLooping = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }
        } while (isLooping);
    }

    public static int mainMenu() {
        System.out.println("+----------------------------------------+");
        System.out.println("         Aplikasi Booking Bengkel    ");
        System.out.println("+----------------------------------------+");
        System.out.println("1 . Login");
        System.out.println("2 . Exit");
        System.out.println("+----------------------------------------+");
        return Validation.validasiNumberWithRange("Masukkan Pilihan Menu: ", "Input Harus Berupa Angka!", "^[0-9]+$", 2,
                1);
    }

    public static boolean login() {
        System.out.print("Masukkan ID Customer : ");
        String customerId = input.nextLine().trim();

        if (customerId.equalsIgnoreCase("exit")) {
            System.out.println("Goodbye");
            System.exit(0);
        }

        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer tidak ditemukan.");
            return false;
        }

        System.out.print("Masukkan Password    : ");
        String password = input.nextLine();

        int passwordAttempts = 1;
        while (!customer.getPassword().equals(password)) {
            if (passwordAttempts >= MAX_LOGIN_ATTEMPTS) {
                System.out.println("Anda telah melebihi jumlah maksimum percobaan login.");
                System.exit(0);
            }
            System.out.println("Password salah. Silakan coba lagi.");
            System.out.print("Masukkan Password: ");
            password = input.nextLine();
            passwordAttempts++;
        }
        System.out.println("+----------------------------------------+");
        System.out.println("Login berhasil! Selamat datang, " + customer.getName());
        return true;
    }

    public static void bookingBengkelMenu() {
        String[] listMenu = { "Informasi Customer", "Booking Bengkel", "Top Up Bengkel Coin", "Informasi Booking",
                "Logout" };
        int menuChoice = 0;
        boolean isLooping = true;

        do {
            System.out.println("+----------------------------------------+");
            PrintService.printMenu(listMenu, "        Booking Bengkel Menu    ");
            menuChoice = Validation.validasiNumberWithRange("Masukkan Pilihan Menu: ", "Input Harus Berupa Angka!",
                    "^[0-9]+$", listMenu.length, 1);

            switch (menuChoice) {
                case 1:
                    // panggil fitur Informasi Customer
                    PrintService.showAllCustomers(listAllCustomers);
                    break;
                case 2:
                    // panggil fitur Booking Bengkel
                    BengkelService.createBooking();
                    break;
                case 3:
                    // panggil fitur Top Up Saldo Coin
                    BengkelService.topUpSaldo();
                    break;
                case 4:
                    PrintService.showBookingInfo(listAllBookingOrder);
                    break;
                default:
                    System.out.println("Logout");
                    isLooping = false;
                    break;
            }
        } while (isLooping);
        System.out.println("Goodbye");
    }

    private static Customer findCustomerById(String customerId) {
        for (Customer customer : listAllCustomers) {
            if (customer.getCustomerId().equals(customerId)) {
                return customer;
            }
        }
        return null;
    }
}
