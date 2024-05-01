package net.consolejs.k8s.dashboard.utilities;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link HashUtility}
 */
public class TestHashUtility {

    @Test
    public void testThatItHashesAValidPassword() {
        // Given
        String password = "password";

        // When
        String result = HashUtility.getHashedPassword(password);

        // Then
        assertTrue(BCrypt.verifyer()
                           .verify(password.toCharArray(), result).verified);
    }

    @Test
    public void testThatItReturnsTrueForEqualPasswords() {
        // Given
        String password = "password";
        String hashed = HashUtility.getHashedPassword(password);

        // When & Then
        assertTrue(HashUtility.isPasswordEqualHash(password, hashed));
    }

    @Test
    public void testThatItReturnsFalseForNotEqualPasswords() {
        // Given
        String password = "password";
        String hashed = HashUtility.getHashedPassword(password);

        // When & Then
        assertFalse(HashUtility.isPasswordEqualHash("different", hashed));
    }
}
