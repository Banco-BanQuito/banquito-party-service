package ec.edu.espe.banquito.party;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PartyServiceApplicationTests {

    @Test
    void contextLoads() {
        assertThat(PartyServiceApplication.class).isNotNull();
    }

}