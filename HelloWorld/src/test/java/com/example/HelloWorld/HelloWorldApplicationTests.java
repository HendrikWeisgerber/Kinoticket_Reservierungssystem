package com.example.HelloWorld;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import com.example.lib.Film;
import com.example.lib.Repositories.FilmRepository;

import static org.hamcrest.Matchers.containsString;

@SpringBootTest(classes = { com.example.lib.HelloWorldApplication.class })
@AutoConfigureMockMvc
class HelloWorldApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private FilmRepository filmrepository;

	@Test
	void contextLoads() {
	}

	@Test
	public void getAllFilms() throws Exception {
		// this.mockMvc.perform(MockMvcRequestBuilders.get("/bazahlen/bestellung/151/iban/DE-12345678901234567890"))
		ArrayList<String> filmNamen = new ArrayList<String>();
		long size = filmrepository.count();
		filmNamen.add("name");
		filmrepository.findAll().forEach(f -> {
			filmNamen.add(f.getName());
		});
		this.mockMvc.perform(MockMvcRequestBuilders.get("/film/all")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString(filmNamen.get((int) (Math.random() * size)))))
				.andExpect(content().string(containsString(filmNamen.get((int) (Math.random() * size)))))
				.andExpect(content().string(containsString(filmNamen.get((int) (Math.random() * size)))))
				.andExpect(content().string(containsString(filmNamen.get((int) (Math.random() * size)))))
				.andExpect(content().string(containsString(filmNamen.get((int) (Math.random() * size)))))
				.andExpect(content().string(containsString(filmNamen.get((int) (Math.random() * size)))))
				.andExpect(content().string(containsString(filmNamen.get((int) (Math.random() * size)))));
	}

	@Test
	public void getAFilm() throws Exception {
		// this.mockMvc.perform(MockMvcRequestBuilders.get("/bazahlen/bestellung/151/iban/DE-12345678901234567890"))
		ArrayList<Film> filme = new ArrayList<Film>();
		filmrepository.findAll().forEach(f -> {
			filme.add(f);
		});
		filme.stream().forEach(f -> {
			String call = "/film/" + f.getId();
			try {
				mockMvc.perform(MockMvcRequestBuilders.get(call)).andDo(print()).andExpect(status().isOk())
						.andExpect(content().string(containsString(f.getName())))
						.andExpect(content().string(containsString(f.getBeschreibung())))
						.andExpect(content().string(containsString(((Integer)f.getBewertung()).toString())))
						.andExpect(content().string(containsString(f.getGenre1().toString())));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

	}
	

}
