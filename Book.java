/**
 * Represents a book in the Library Management System.
 *
 * Each Book object stores information about a single title, including
 * its title, author, ISBN, publication year, total number of copies,
 * and currently available copies.
 *
 * The class also provides functionality for:
 * - Checking books out
 * - Returning books
 * - Tracking inventory counts
 * - Comparing books for equality
 * - Generating hash codes based on ISBN values
 *
 * Availability Rules:
 * - A checkout operation decreases the number of available copies.
 * - A check-in operation increases the number of available copies.
 * - A book cannot be checked out when no copies are available.
 * - A book cannot be checked in if all copies are already present.
 *
 * Example:
 * Book book = new Book(
 *     "Star Trek",
 *     "Gene Roddenberry",
 *     "ISBN-1234",
 *     1965,
 *     5
 * );
 *
 * @supervisor Balaji Srinivasan
 * @author Ngoc Hang
 * @author J. Jesus Ibarra
 */
public class Book {
    String title;
    String author;
    String isbn;
    int publicationYear;
    // number of copies in the library
    // NOTE: This is not the number of copies available in the library
    int numberOfCopies;
    int availableCopies;

    /**
     * Constructor. Most properties (except number of copies are read only)
     */
    public Book(String title, String author, String isbn, int publicationYear, int numberOfCopies) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.numberOfCopies = numberOfCopies;
        this.availableCopies = numberOfCopies;
    }

    /**
     * @return The title of the book.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return The author of the book.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @return the ISBN for this book.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * @return The publication year of this book.
     */
    public int getPublicationYear() {
        return publicationYear;
    }

    /**
     * @return The number of copies of this book.
     */
    public int getNumberOfCopies() {
        return numberOfCopies;
    }

    /**
     * @return The number of available copies of this book.
     */
    public int getAvailableCopies() {
        return this.availableCopies;
    }

    /**
     * Sets the number of available copies. Just used for testing.
     * @param int numCopies
     */
    public void setAvailableCopies(int numCopies) {
        this.availableCopies = numCopies;
    }

/**
 * Increases the total number of copies owned by the library.
 * @param numCopiesToAdd number of copies to add
 */
public void addCopies(int numCopiesToAdd)
    public void addCopies(int numCopiesToAdd) {
        numberOfCopies += numCopiesToAdd;
    }

/**
 * Checks out a copy of this book.
 * Decreases the number of available copies by one.
 * @throws IllegalStateException if no copies are currently available
 */
public void checkout()
    public void checkout() {
        if(availableCopies <= 0){
            throw new IllegalStateException("The book you want is not available.");
        }
        availableCopies--;
    } 

/**
 * Returns a copy of this book to the library.
 * Increases the number of available copies by one.
 * @throws IllegalStateException if no copies are currently checked out
 */
public void checkin()
    public void checkin() { 
        if(availableCopies == numberOfCopies){
            throw new IllegalStateException("A copy hasn't been checked out yet.");
        }
        availableCopies++;
    }
	
/**
 * Generates a hash code based on the numeric portion of the ISBN.
 * For an ISBN in the format ISBN-1234, the hash code returned is 1234.
 * @return hash code derived from the ISBN
 */
@Override
public int hashCode()
    @Override
    public int hashCode() {
		String[] parts = getIsbn().split("-");
		if(parts.length == 2) {
			return Integer.parseInt(parts[1]);
		}
		else {
			return Integer.parseInt(parts[0]);
		}
    }
	
/**
 * Determines whether two Book objects represent the same book.
 *
 * Two books are considered equal if they have the same:
 * - title
 * - author
 * - ISBN
 * - publication year
 *
 * @param that object to compare against
 * @return true if the books are equal, false otherwise
 */
@Override
public boolean equals(Object that)
    @Override
    public boolean equals(Object that) {
		if(if(that == null) {
			return false;
		}
        if(that instanceof Book) {
			Book b = (Book) that;
			if(this.author.equals(b.getAuthor())) { //Checks Author
				if(this.title.equals(b.getTitle())) { //Checks Title
					if(this.isbn.equals(b.getIsbn())) { // Checks Isbn
						if(this.publicationYear == b.getPublicationYear()) { //Checks Year
							return true;
						}
					}
				}
			}
		}
		return false;
    }
}
