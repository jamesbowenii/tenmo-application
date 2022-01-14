package com.techelevator.view;


import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.User;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {

	private PrintWriter out;
	private Scanner in;


	public ConsoleService(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output, true);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		out.println();
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if (choice == null) {
			out.println(System.lineSeparator() + "*** " + userInput + " is not a valid option ***" + System.lineSeparator());
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options) {
		out.println();
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println(optionNum + ") " + options[i]);
		}
		out.print(System.lineSeparator() + "Please choose an option >>> ");
		out.flush();
	}

	public String getUserInput(String prompt) {
		out.print(prompt+": ");
		out.flush();
		return in.nextLine();
	}

	public Integer getUserInputInteger(String prompt) {
		Integer result = null;
		do {
			out.print(prompt+": ");
			out.flush();
			String userInput = in.nextLine();
			try {
				result = Integer.parseInt(userInput);
			} catch(NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(result == null);
		return result;
	}

	public Double getUserInputDouble(String prompt) {
		Double result = null;
		do {
			out.print(prompt+": ");
			out.flush();
			String userInput = in.nextLine();
			try {
				result = Double.parseDouble(userInput);
			} catch(NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(result == null);
		return result;
	}

	public String returnMoney (double money) {
		return NumberFormat.getCurrencyInstance().format(money);
	}

	public void printToScreen(String string) {
		System.out.println(string);
	}

	public void displayAllUsers(List<User> userList){
		System.out.println("       Users");
		System.out.printf("%-4s %20s\n", "ID", "Username");
		System.out.println("----------------------------------");
		for(User user: userList){
			System.out.printf("%-4d %20s\n", user.getId(), StringUtils.capitalize(user.getUsername()));
		}
		System.out.println("----------------------------------");
		System.out.println();

	}
	public void displayAllTransfers(List<TransferDTO> transferList, AuthenticatedUser currentUser) {
		String toFrom = "";
		System.out.println("Displaying All Transfers For This User");
		System.out.println("--------------------------------------- ");
		for (TransferDTO transferDTO : transferList) {
			if (currentUser.getUser().getUsername().equals(transferDTO.getUsernameFrom())) {
				toFrom = "To: " + StringUtils.capitalize(transferDTO.getUsernameTo());
			} else {
				toFrom = "From: " + StringUtils.capitalize(transferDTO.getUsernameFrom());
			}
			System.out.printf("%-8d %-15s %-7s %-15s\n", transferDTO.getTransferId(), toFrom ,"Amount:", returnMoney(transferDTO.getAmount()));
		}
		printToScreen("");
	}

	public void displayTransferDetails(TransferDTO transferDTO) {
		System.out.println("Transfer Details");
		System.out.println("-----------------");
		System.out.printf("%-13s %-5d\n", "Transfer ID:", transferDTO.getTransferId());
		System.out.printf("%-13s %-20s\n", "From:", StringUtils.capitalize(transferDTO.getUsernameFrom()));
		System.out.printf("%-13s %-20s\n", "To:", StringUtils.capitalize(transferDTO.getUsernameTo()));
		System.out.printf("%-13s %-20s\n", "Type:", "Send");
		System.out.printf("%-13s %-20s\n", "Status:",  "Approved");
		System.out.printf("%-13s %-20s\n", "Amount:",returnMoney(transferDTO.getAmount()));
		printToScreen("");
	}


}
