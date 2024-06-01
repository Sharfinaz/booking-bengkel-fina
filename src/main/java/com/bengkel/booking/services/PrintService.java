package com.bengkel.booking.services;

import java.util.List;

import com.bengkel.booking.models.BookingOrder;
import com.bengkel.booking.models.Car;
import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.models.MemberCustomer;
import com.bengkel.booking.models.Vehicle;

public class PrintService {

    public static void printMenu(String[] listMenu, String title) {
        String line = "+---------------------------------+";
        int number = 1;
        String formatTable = " %-2d. %-25s %n";

        System.out.printf("%-25s %n", title);
        System.out.println(line);

        for (String data : listMenu) {
            if (number < listMenu.length) {
                System.out.printf(formatTable, number, data);
            } else {
                System.out.printf(formatTable, 0, data);
            }
            number++;
        }
    }

    public static void printVehicleList(List<Vehicle> vehicles) {
        String formatTable = "| %-2s | %-15s | %-10s | %-15s | %-15s | %-5s |%n";
        String line = "+----+-----------------+------------+-----------------+-----------------+-------+%n";
        System.out.format(line);
        System.out.format(formatTable, "No", "Vehicle ID", "Color", "Brand", "Transmission", "Year");
        System.out.format(line);
        int number = 1;
        for (Vehicle vehicle : vehicles) {
            String vehicleType = (vehicle instanceof Car) ? "Car" : "Motorcycle";
            System.out.format(formatTable, number, vehicle.getVehiclesId(), vehicle.getColor(), vehicle.getBrand(),
                    vehicle.getTransmisionType(), vehicle.getYearRelease());
            number++;
        }
        System.out.printf(line);
    }

    public static void showAllCustomers(List<Customer> customers) {
        for (Customer customer : customers) {
            System.out.println("+-----------------------------------------------------------+");
            System.out.println(customer instanceof MemberCustomer ? "Member" : "Non Member");
            System.out.println("               Customer Profile           ");
            System.out.println("+-----------------------------------------------------------+");
            System.out.printf("Customer ID    : %s\n", customer.getCustomerId());
            System.out.printf("Nama           : %s\n", customer.getName());
            System.out.printf("Customer Status: %s\n", (customer instanceof MemberCustomer) ? "Member" : "Non-Member");
            System.out.printf("Alamat         : %s\n", customer.getAddress());

            if (customer instanceof MemberCustomer) {
                MemberCustomer memberCustomer = (MemberCustomer) customer;
                System.out.printf("Saldo Koin     : %.0f\n", memberCustomer.getSaldoCoin());
            }

            System.out.println("\nList Kendaraan");
            PrintService.printVehicleList(customer.getVehicles());
            System.out.println();
        }
    }

    public static void showBookingInfo(List<BookingOrder> bookingOrders) {
        System.out.printf(
                "+-------------------------------------------------------------------------------------------------------------+\n");
        System.out.printf("| %-10s | %-15s | %-15s | %-15s | %-15s | %-20s | %-40s |\n", "ID Booking", "Nama Customer",
                "Total Harga Service", "Metode Pembayaran", "Total Pembayaran", "Tanggal Booking", "List Service");
        System.out.printf(
                "+-------------------------------------------------------------------------------------------------------------+\n");
        for (BookingOrder bookingOrder : bookingOrders) {
            StringBuilder serviceList = new StringBuilder();
            for (ItemService service : bookingOrder.getServices()) {
                serviceList.append(service.getServiceName()).append(", ");
            }
            if (serviceList.length() > 2) {
                serviceList.setLength(serviceList.length() - 2); // Remove the last comma and space
            }
            System.out.printf("| %-10s | %-15s | %-20.0f | %-15s | %-15.0f | %-20s | %-40s |\n",
                    bookingOrder.getBookingId(),
                    bookingOrder.getCustomer().getName(), bookingOrder.getTotalServicePrice(),
                    bookingOrder.getPaymentMethod(), bookingOrder.getTotalPayment(),
                    bookingOrder.getBookingDate().toString(), serviceList.toString());
        }
        System.out.printf(
                "+-------------------------------------------------------------------------------------------------------------+\n");
    }

    private static String getServiceList(List<ItemService> services) {
        StringBuilder sb = new StringBuilder();
        for (ItemService service : services) {
            sb.append(service.getServiceName()).append(", ");
        }
        // Remove the last comma and space
        if (sb.length() > 2) {
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

    public static void showAllItemServices(List<ItemService> itemServices) {
        System.out.println("+-----------------------------------------------------------+");
        System.out.printf("| %-10s | %-20s | %-10s |\n", "ID Service", "Nama Service", "Harga");
        System.out.println("+-----------------------------------------------------------+");
        for (ItemService itemService : itemServices) {
            System.out.printf("| %-10s | %-20s | %-10.0f |\n", itemService.getServiceId(), itemService.getServiceName(),
                    itemService.getPrice());
        }
        System.out.println("+-----------------------------------------------------------+");
    }
}
