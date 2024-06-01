package com.bengkel.booking.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.bengkel.booking.models.BookingOrder;
import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.models.MemberCustomer;
import com.bengkel.booking.models.Vehicle;
import com.bengkel.booking.repositories.CustomerRepository;
import com.bengkel.booking.repositories.ItemServiceRepository;

public class BengkelService {
	private static List<Customer> listAllCustomers = CustomerRepository.getAllCustomer();
	private static List<ItemService> listAllItemService = ItemServiceRepository.getAllItemService();
	private static List<BookingOrder> listAllBookingOrder = new ArrayList<BookingOrder>();
	private static Scanner input = new Scanner(System.in);

	public static void createBooking() {
		System.out.println("=== Booking Bengkel ===");

		System.out.printf("Masukkan ID Customer  : ");
		String customerId = input.nextLine();
		Customer customer = findCustomerById(customerId);
		if (customer == null) {
			System.out.println("Customer tidak ditemukan. Silakan coba lagi.");
			return;
		}

		System.out.printf("Masukkan ID Kendaraan : ");
		String vehicleId = input.nextLine();
		Vehicle vehicle = findVehicleById(customer, vehicleId);
		if (vehicle == null) {
			System.out.println("Kendaraan tidak ditemukan. Silakan coba lagi.");
			return;
		}

		System.out.println("Daftar Layanan yang Tersedia:");
		List<ItemService> filteredServices = getServicesByVehicleType(vehicle);
		PrintService.showAllItemServices(filteredServices);

		List<ItemService> selectedServices = new ArrayList<>();
		boolean addMoreServices = true;

		while (addMoreServices) {
			System.out.printf("Masukkan ID Service: ");
			String serviceId = input.nextLine();
			ItemService service = findItemServiceById(serviceId, filteredServices);
			if (service == null) {
				System.out.println("Service tidak ditemukan. Silakan coba lagi.");
				continue;
			}
			selectedServices.add(service);
			System.out.println("Apakah anda ingin menambahkan service lainnya? (Y/T)");
			String response = input.nextLine();
			if (!response.equalsIgnoreCase("Y")) {
				addMoreServices = false;
			}
		}
		System.out.println("Masukkan Metode Pembayaran (Cash/Saldo Coin):");
		String paymentMethod = input.nextLine();

		LocalDate bookingDate = LocalDate.now();

		BookingOrder bookingOrder = new BookingOrder();
		bookingOrder.setBookingId("Booking-" + (listAllBookingOrder.size() + 1));
		bookingOrder.setCustomer(customer);
		bookingOrder.setServices(selectedServices);
		bookingOrder.setPaymentMethod(paymentMethod);
		double totalServicePrice = selectedServices.stream().mapToDouble(ItemService::getPrice).sum();
		bookingOrder.setTotalServicePrice(totalServicePrice);
		bookingOrder.calculatePayment();
		bookingOrder.setBookingDate(bookingDate);

		listAllBookingOrder.add(bookingOrder);

		System.out.println("Booking berhasil dibuat!");

		System.out.printf("Total Harga Service : Rp %.0f\n", bookingOrder.getTotalServicePrice());
		System.out.printf("Total Pembayaran    : Rp %.0f\n", bookingOrder.getTotalPayment());

		PrintService.showBookingInfo(listAllBookingOrder);
		System.out.println("Jumlah Booking Order: " + listAllBookingOrder.size());
	}

	public static void topUpSaldo() {
		// Implementasi top up saldo
		System.out.println("=== Top Up Bengkel Coin ===");

		System.out.println("Masukkan ID Customer:");
		String customerId = input.nextLine();
		Customer customer = findCustomerById(customerId);
		if (customer == null) {
			System.out.println("Customer tidak ditemukan. Silakan coba lagi.");
			return;
		}

		if (!(customer instanceof MemberCustomer)) {
			System.out.println("Maaf fitur ini hanya untuk member saja!");
			return;
		}

		MemberCustomer memberCustomer = (MemberCustomer) customer;

		System.out.println("Masukkan jumlah Top Up:");
		double amount = input.nextDouble();
		input.nextLine(); // Consume newline

		memberCustomer.setSaldoCoin(memberCustomer.getSaldoCoin() + amount);
		System.out.printf("Top up berhasil! Saldo Coin anda sekarang: %.0f\n", memberCustomer.getSaldoCoin());
	}

	private static Customer findCustomerById(String customerId) {
		for (Customer customer : listAllCustomers) {
			if (customer.getCustomerId().equals(customerId)) {
				return customer;
			}
		}
		return null;
	}

	private static ItemService findItemServiceById(String serviceId, List<ItemService> services) {
		for (ItemService service : services) {
			if (service.getServiceId().equals(serviceId)) {
				return service;
			}
		}
		return null;
	}

	private static Vehicle findVehicleById(Customer customer, String vehicleId) {
		for (Vehicle vehicle : customer.getVehicles()) {
			if (vehicle.getVehiclesId().equals(vehicleId)) {
				return vehicle;
			}
		}
		return null;
	}

	private static List<ItemService> getServicesByVehicleType(Vehicle vehicle) {
		List<ItemService> filteredServices = new ArrayList<>();
		for (ItemService service : listAllItemService) {
			if (service.getVehicleType().equalsIgnoreCase(vehicle.getVehicleType())) {
				filteredServices.add(service);
			}
		}
		return filteredServices;
	}

	private static String getServiceList(List<ItemService> services) {
		StringBuilder sb = new StringBuilder();
		for (ItemService service : services) {
			sb.append(service.getServiceName()).append(", ");
		}
		if (sb.length() > 2) {
			sb.setLength(sb.length() - 2);
		}
		return sb.toString();
	}
}