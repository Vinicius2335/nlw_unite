package com.github.vinicius2335.passin;

import com.github.javafaker.Faker;
import com.github.vinicius2335.passin.domain.attendee.Attendee;
import com.github.vinicius2335.passin.domain.checkin.CheckIn;
import com.github.vinicius2335.passin.domain.event.Event;
import com.github.vinicius2335.passin.repositories.AttendeeRepository;
import com.github.vinicius2335.passin.repositories.CheckInRepository;
import com.github.vinicius2335.passin.repositories.EventRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@Log4j2
public class PassInApplication {

	public static void main(String[] args) {
		SpringApplication.run(PassInApplication.class, args);
	}

	@Bean
	CommandLineRunner seedDatabase(
			EventRepository eventRepository,
			AttendeeRepository attendeeRepository,
			CheckInRepository checkInRepository
	){
		Faker faker = new Faker(new Locale("pt-BR"));

		Event newEvent = new Event();
		newEvent.setTitle("Unite Summit");
		newEvent.setSlug("unite-summit");
		newEvent.setDetails("Um evento p/ devs apaixonados(as) por código!");
		newEvent.setMaximunAttendees(120);

		return args -> {
			// verifica se já existe um evento registrado, se não, irá preencher as tabelas do banco com dados aleátorios
			if (!eventRepository.findAll().isEmpty()) {
				return;
			}

			log.info("Seeding the database...");

			eventRepository.save(newEvent);

			for (int i = 1; i <= 120; i++){
				Attendee newAttendee = new Attendee();
				newAttendee.setName(faker.name().fullName());
				newAttendee.setEmail(faker.internet().emailAddress());
				newAttendee.setCreatedAt(generateCreatedAt(30, faker));
				newAttendee.setEvent(newEvent);

				attendeeRepository.save(newAttendee);

				CheckIn newCheckIn = new CheckIn();
				newCheckIn.setAttendee(newAttendee);
				newCheckIn.setCreatedAt(generateCreatedAt(8, faker));

				checkInRepository.save(newCheckIn);
			}
		};
	}

	private static OffsetDateTime generateCreatedAt(int atMost, Faker faker) {
		return OffsetDateTime.ofInstant(faker.date().past(atMost, TimeUnit.DAYS).toInstant(), ZoneId.systemDefault());
	}
}
