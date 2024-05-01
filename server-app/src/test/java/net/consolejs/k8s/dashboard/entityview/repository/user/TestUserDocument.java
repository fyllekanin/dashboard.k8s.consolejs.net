package net.consolejs.k8s.dashboard.entityview.repository.user;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link UserDocument}
 */
public class TestUserDocument {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final ObjectId OBJECT_ID = mock(ObjectId.class);

    @Test
    public void shouldSuccessfullyBuild() {
        // When
        UserDocument result = UserDocument.newBuilder()
                .withObjectId(OBJECT_ID)
                .withUsername(USERNAME)
                .withPassword(PASSWORD)
                .build();

        // Then
        assertEquals(result.getObjectId(), OBJECT_ID);
        assertEquals(result.getUsername(), USERNAME);
        assertEquals(result.getPassword(), PASSWORD);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(UserDocument.class)
                .usingGetClass()
                .verify();
    }
}
