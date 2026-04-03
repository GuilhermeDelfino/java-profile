package narciso.guilherme.github.profile;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ProfileApplicationTests extends AbstractIntegrationTest {

	@Test
	void contextLoads() {
	}

}
