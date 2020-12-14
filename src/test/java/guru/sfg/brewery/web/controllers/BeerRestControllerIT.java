package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest
public class BeerRestControllerIT extends BaseIT{

    // testing RestHeaderAuthFilter
    @Test
    public void deleteBeer() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/97df0c39-98c4-4ae0-b663-453e8e19c310")
        .header("Api-Key","spring").header("Api-Secret","guru")).andExpect(status().isOk());
    }


    @Test
    public void findBeers() throws Exception {
        mockMvc.perform(get("/api/v1/beer/"))
                .andExpect(status().isOk());
    }

    @Test
    public void findBeerById() throws Exception {
        mockMvc.perform(get("/api/v1/beer/97df0c39-98c4-4ae0-b663-453e8e19c310"))
                .andExpect(status().isOk());
    }


    @Test
    public void findBeerByUpc() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/8631234200036"))
                .andExpect(status().isOk());
    }


}
