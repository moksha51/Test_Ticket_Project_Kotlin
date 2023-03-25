package com.cms.tickets

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.internal.matchers.GreaterThan
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class TicketsApplicationTests(
	@Autowired val mockMvc: MockMvc,
	@Autowired val objectMapper: ObjectMapper) {

	@Test
	fun `Assert test1 as the first item`() {
		mockMvc.get("/tickets") // 1
			.andExpect { // 2
				status { isOk() } // 3
				content { contentType(MediaType.APPLICATION_JSON) }
				jsonPath("$[0].id") { value(1) } // 4
				jsonPath("$[0].project") { value("SomethingCreative") }
				jsonPath("$[0].title") { value("SomethingFunny") }
				jsonPath("$[0].body") {value ("SomethingNonsensical") }
				jsonPath("$[0].completed") {value (false) }
				jsonPath("$[0].tags") {value ("SomethingNonsensical") }
				jsonPath("$.length()") { GreaterThan(1) }
			}
	}
	@Test
	fun `Assert that we can create a NFT`() {
		mockMvc.get("/NFT/3")
		.andExpect {
			status { isNotFound() }
		}
		val newTicket = com.cms.tickets.domain.Ticket(1, 1, "Project1", "Body", false)
		mockMvc.post("/tickets") {
			contentType = MediaType.APPLICATION_JSON
			content = objectMapper.writeValueAsString(newTicket)
		}
			.andExpect {
				status { isCreated() }
				content { contentType(MediaType.APPLICATION_JSON) }
				jsonPath("$[0].id") { value(1) } // 4
				jsonPath("$[0].project") { value("SomethingCreative") }
				jsonPath("$[0].title") { value("SomethingFunny") }
				jsonPath("$[0].body") {value ("SomethingNonsensical") }
				jsonPath("$[0].completed") {value (false) }
				jsonPath("$[0].tags") {value ("SomethingNonsensical") }
				jsonPath("$.length()") { GreaterThan(1) }
			}
		mockMvc.get("/nfts/6")
			.andExpect {
				status { isOk() }
				content { contentType(MediaType.APPLICATION_JSON) }
				jsonPath("$[0].id") { value(1) } // 4
				jsonPath("$[0].project") { value("SomethingCreative") }
				jsonPath("$[0].title") { value("SomethingFunny") }
				jsonPath("$[0].body") {value ("SomethingNonsensical") }
				jsonPath("$[0].completed") {value (false) }
				jsonPath("$[0].tags") {value ("SomethingNonsensical") }
				jsonPath("$.length()") { GreaterThan(1) }
			}
	}
}


