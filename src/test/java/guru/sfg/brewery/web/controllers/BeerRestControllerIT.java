package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
public class BeerRestControllerIT extends BaseIT{

    // testing RestHeaderAuthFilter
    @Test
    public void deleteBeer() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/97df0c39-98c4-4ae0-b663-453e8e19c310")
        .header("Api-Key","spring").header("Api-Secret","guru")).andExpect(status().isOk());
    }

    @Test
    public void deleteBeerBadCredentials() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/97df0c39-98c4-4ae0-b663-453e8e19c310")
                .header("Api-Key","spring").header("Api-Secret","guruXXX"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void deleteBeerHttpBasic() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/97df0c39-98c4-4ae0-b663-453e8e19c310")
                .with(httpBasic("spring","guru")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void deleteBeerNoAuth() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/97df0c39-98c4-4ae0-b663-453e8e19c310"))
                .andExpect(status().isUnauthorized());
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
