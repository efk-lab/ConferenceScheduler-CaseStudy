package com.n11.conference.scheduler.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.n11.conference.scheduler.conf.mongodb.MongoDBTestConfiguration;
import com.n11.conference.scheduler.document.Conference;

@ExtendWith(SpringExtension.class)
@DataMongoTest
@Import(MongoDBTestConfiguration.class)
public class ConferenceRepositoryTest {

	@Autowired
	private ConferenceRepository conferenceRepository;

	@BeforeEach
	public void setUp() {
		conferenceRepository.deleteAll();
	}

	@Test
	public void testSaveAndFindConference() {

		Conference conference = prepareConference();

		Conference savedConference = conferenceRepository.save(conference);
		Conference foundConference = conferenceRepository.findById(conference.getConferenceId()).get();

		assertThat(savedConference.getConferenceId()).isEqualTo(foundConference.getConferenceId());

	}

	private Conference prepareConference() {

		return Conference.builder().conferenceId(new ObjectId("62d322ddf9f5e01864bed242")).build();

	}

}
