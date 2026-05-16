import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SI2026Lab2Test {

    private Library createLibraryWithDefaultBooks() {
        Library library = new Library();
        library.addBook(new Book("Clean Code", "Robert C. Martin", "Programming"));
        library.addBook(new Book("Effective Java", "Joshua Bloch", "Programming"));
        library.addBook(new Book("The Hobbit", "J.R.R. Tolkien", "Fantasy"));
        library.addBook(new Book("1984", "George Orwell", "Dystopian"));
        return library;
    }

    @Test
    void searchBookEveryStatementTest() {
        Library invalidTitleLibrary = createLibraryWithDefaultBooks();
        IllegalArgumentException invalidTitleException = assertThrows(
                IllegalArgumentException.class,
                () -> invalidTitleLibrary.searchBookByTitle("")
        );
        assertEquals("Invalid title", invalidTitleException.getMessage());

        Library foundLibrary = createLibraryWithDefaultBooks();
        List<Book> foundBooks = foundLibrary.searchBookByTitle("Clean Code");
        assertNotNull(foundBooks);
        assertEquals(1, foundBooks.size());
        assertEquals("Clean Code", foundBooks.get(0).getTitle());
        assertFalse(foundBooks.get(0).isBorrowed());

        Library missingLibrary = createLibraryWithDefaultBooks();
        List<Book> missingBooks = missingLibrary.searchBookByTitle("Harry Potter");
        assertNull(missingBooks);
    }

    @Test
    void borrowBookEveryBranchTest() {
        Library invalidLibrary = createLibraryWithDefaultBooks();
        IllegalArgumentException invalidException = assertThrows(
                IllegalArgumentException.class,
                () -> invalidLibrary.borrowBook("", "Robert C. Martin")
        );
        assertEquals("Invalid search query", invalidException.getMessage());

        Library successLibrary = new Library();
        Book cleanCode = new Book("Clean Code", "Robert C. Martin", "Programming");
        successLibrary.addBook(cleanCode);
        assertDoesNotThrow(() -> successLibrary.borrowBook("Clean Code", "Robert C. Martin"));
        assertTrue(cleanCode.isBorrowed());

        Library alreadyBorrowedLibrary = new Library();
        Book alreadyBorrowedBook = new Book("The Hobbit", "J.R.R. Tolkien", "Fantasy");
        alreadyBorrowedBook.setBorrowed(true);
        alreadyBorrowedLibrary.addBook(alreadyBorrowedBook);
        RuntimeException alreadyBorrowedException = assertThrows(
                RuntimeException.class,
                () -> alreadyBorrowedLibrary.borrowBook("The Hobbit", "J.R.R. Tolkien")
        );
        assertEquals("Book is already borrowed.", alreadyBorrowedException.getMessage());

        Library notFoundLibrary = createLibraryWithDefaultBooks();
        RuntimeException notFoundException = assertThrows(
                RuntimeException.class,
                () -> notFoundLibrary.borrowBook("Harry Potter", "J.K. Rowling")
        );
        assertEquals("Book not found", notFoundException.getMessage());
    }

    @Test
    void searchBookMultipleConditionTest() {
        Library trueTrueLibrary = new Library();
        Book availableTarget = new Book("Target", "Author 1", "Genre");
        trueTrueLibrary.addBook(availableTarget);
        List<Book> trueTrueResult = trueTrueLibrary.searchBookByTitle("Target");
        assertNotNull(trueTrueResult);
        assertEquals(1, trueTrueResult.size());
        assertSame(availableTarget, trueTrueResult.get(0));

        Library trueFalseLibrary = new Library();
        Book borrowedTarget = new Book("Target", "Author 2", "Genre");
        borrowedTarget.setBorrowed(true);
        trueFalseLibrary.addBook(borrowedTarget);
        assertNull(trueFalseLibrary.searchBookByTitle("Target"));

        Library falseTrueLibrary = new Library();
        Book availableOther = new Book("Other", "Author 3", "Genre");
        falseTrueLibrary.addBook(availableOther);
        assertNull(falseTrueLibrary.searchBookByTitle("Target"));

        Library falseFalseLibrary = new Library();
        Book borrowedOther = new Book("Other", "Author 4", "Genre");
        borrowedOther.setBorrowed(true);
        falseFalseLibrary.addBook(borrowedOther);
        assertNull(falseFalseLibrary.searchBookByTitle("Target"));
    }

    @Test
    void borrowBookMultipleConditionTest() {
        Library trueTrueLibrary = createLibraryWithDefaultBooks();
        assertThrows(
                IllegalArgumentException.class,
                () -> trueTrueLibrary.borrowBook("", "")
        );

        Library trueFalseLibrary = createLibraryWithDefaultBooks();
        assertThrows(
                IllegalArgumentException.class,
                () -> trueFalseLibrary.borrowBook("", "Robert C. Martin")
        );

        Library falseTrueLibrary = createLibraryWithDefaultBooks();
        assertThrows(
                IllegalArgumentException.class,
                () -> falseTrueLibrary.borrowBook("Clean Code", "")
        );

        Library falseFalseLibrary = new Library();
        Book cleanCode = new Book("Clean Code", "Robert C. Martin", "Programming");
        falseFalseLibrary.addBook(cleanCode);
        assertDoesNotThrow(() -> falseFalseLibrary.borrowBook("Clean Code", "Robert C. Martin"));
        assertTrue(cleanCode.isBorrowed());
    }
}
