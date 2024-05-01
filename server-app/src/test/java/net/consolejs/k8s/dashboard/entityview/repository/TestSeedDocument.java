package net.consolejs.k8s.dashboard.entityview.repository;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link SeedDocument}
 */
public class TestSeedDocument {
    private static final String NAME = "name";
    private static final ObjectId OBJECT_ID = mock(ObjectId.class);

    @Test
    public void shouldSuccessfullyBuild() {
        // When
        SeedDocument result = SeedDocument.newBuilder()
                .withObjectId(OBJECT_ID)
                .withName(NAME)
                .build();

        // Then
        assertEquals(result.getObjectId(), OBJECT_ID);
        assertEquals(result.getName(), NAME);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(SeedDocument.class)
                .usingGetClass()
                .verify();
    }
}
