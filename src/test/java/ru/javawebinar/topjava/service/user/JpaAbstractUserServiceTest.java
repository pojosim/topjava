package ru.javawebinar.topjava.service.user;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles(profiles = {Profiles.JPA})
public class JpaAbstractUserServiceTest extends AbstractUserServiceTest {
}
