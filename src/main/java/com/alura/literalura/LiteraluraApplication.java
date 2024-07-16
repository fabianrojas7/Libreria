package com.alura.literalura;

import com.alura.literalura.principal.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;

@SpringBootApplication
public class LiteraluraApplication {

	@Autowired
	private Menu menu;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Bean
	public CommandLineRunner run() {
		return args -> {
			Scanner scanner = new Scanner(System.in);
			boolean continueRunning = true;
			while (continueRunning) {
				menu.displayMenu();
				int option = scanner.nextInt();
				scanner.nextLine();  // Consumir la nueva línea
				continueRunning = menu.handleOption(option, scanner);
			}
			scanner.close();
		};
	}
}
