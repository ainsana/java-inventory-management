package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class SqlIdentifierValidatorTest {

    @Test
    void acceptsValidIdentifiers() {
        assertEquals(
                "prodotti_2026",
                SqlIdentifierValidator.requireValid("prodotti_2026")
        );
    }

    @Test
    void quotesValidIdentifiers() {
        assertEquals(
                "`prodotti`",
                SqlIdentifierValidator.quote("prodotti")
        );
    }

    @Test
    void rejectsIdentifierStartingWithNumber() {
        assertThrows(
                IllegalArgumentException.class,
                () -> SqlIdentifierValidator.requireValid("123prodotti")
        );
    }

    @Test
    void rejectsIdentifierContainingSpaces() {
        assertThrows(
                IllegalArgumentException.class,
                () -> SqlIdentifierValidator.requireValid("tabella prodotti")
        );
    }

    @Test
    void rejectsPotentialSqlInjection() {
        assertThrows(
                IllegalArgumentException.class,
                () -> SqlIdentifierValidator.requireValid(
                        "prodotti; DROP TABLE utenti"
                )
        );
    }

    @Test
    void rejectsNullIdentifier() {
        assertThrows(
                IllegalArgumentException.class,
                () -> SqlIdentifierValidator.requireValid(null)
        );
    }
}