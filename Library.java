/**
 * Library Management System
 *
 * A command-line application that allows users to manage a collection of books
 * within a library. The system supports adding books, searching by title and
 * author, checking books out, returning books, viewing inventory information,
 * and saving/loading library data from CSV files.
 *
 * Features:
 * - Add new books to the library
 * - Track multiple copies of the same book
 * - Search books by ISBN or title and author
 * - Check out and return books
 * - Save library data to a CSV file
 * - Load library data from a CSV file
 * - Display current library inventory
 *
 * The library stores books using ISBN numbers as unique identifiers and
 * maintains a secondary index for efficient title-and-author lookups.
 *
 * CSV Format:
 * copies, title, author, publicationYear, isbn
 *
 * Example:
 * 5, Star_Trek, Gene_Roddenberry, 1965, ISBN-1234
 *
 * Authors:
 * - Ngoc Hang
 * - J. Jesus Ibarra
 * - Arnav Pophale
 * - Harry Nguyen
 */

import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * A library management class. Has a simple shell that users can interact with to add/remove/checkout/list books in the library.
 * Also allows saving the library state to a file and reloading it from the file.
 */
public class Library {
	
	HashMap<String, Book> library = new HashMap<>();
	HashMap<String, String> ISBN = new HashMap<>();
	
    /**
     * @return the number of books (not number of copies) in the library.
     */
    public int getNumberOfBooks() {
        return library.size();
    }

    /**
     * Adds a book to the library. If the library already has this book then it
     * adds the number of copies the library has.
     * @author J. Jesus Ibarra
     * @param book the book to be added to the library
     */
    public void addBook(Book book) {
		String isbn = book.getIsbn();
		if(findByISBN(isbn) == null) {
			library.put(isbn, book);
			String titleAuthor = book.getTitle() + "-" + book.getAuthor();
			ISBN.put(titleAuthor, isbn);
		}
		else {
			findByISBN(isbn).addCopies(book.getNumberOfCopies());
			System.out.println("ISBN code exists, copies added to existing entry.");
		}
    }

    /**
     * Checks out the given book from the library. Throw the appropriate
     * exception if book doesnt exist or there are no more copies available.
     * @author Ngoc Hang
     */
    public void checkout(String isbn) {
            Book book = findByISBN(isbn);
            book.checkout();
    }

    /**
     * Returns a book to the library
     * @author Ngoc Hang
     */
    public void returnBook(String isbn) {
            Book book = findByISBN(isbn);
            book.checkin();
    }

    /**
     * Finds this book in the library. Throws appropriate exception if the book
     * doesnt exist.
     * @author Ngoc Hang
     */
    public Book findByTitleAndAuthor(String title, String author) {
        Book book = library.get(ISBN.get(title+"-"+author));
        if(book == null){
            throw new RuntimeException("the book is not in our system");
        }
        return book;
    }

    /**
     * Finds this book in the library. Throws appropriate exception if the book
     * doesnt exist.
     * @author Ngoc Hang
     */
    public Book findByISBN(String isbn) {
        Book book = library.get(isbn);
        if(book == null){
            return null;
        }
        return book;
    }

    /**
     * Saves the contents of this library to the given file.
     * @author J. Jesus Ibarra
     * @param filename the name of the file to be saved onto.
     */
    public void save(String filename) {
		try(BufferedWriter buff = new BufferedWriter(new FileWriter(filename + ".csv"))) {
			//buff.write("Library Contents");
			//buff.newLine();
			//buff.write("Format: Number Of Copies in Library, Title, Author, Publication Year, ISBN");
			for(Map.Entry<String, Book> entry : library.entrySet()) {
				Book page = entry.getValue();
				
				String book = "";
				book += page.getNumberOfCopies() + ", ";
				book += page.getTitle() + ", ";
				book += page.getAuthor() + ", ";
				book += page.getPublicationYear() + ", ";
				book += page.getIsbn();
				buff.write(book);
				buff.newLine();
			}
			buff.close();
		}
		catch (IOException e) {
			System.out.println("Error writing file.");
		}
	}

     /**
      *	Loads the contents of this library from the given file. 
      * All existing data in this library is cleared before loading from the file.
      * @author Harry Nguyen
      */
    public void load(String filename) {
        library.clear();  // remove current contents to start anew

		try (BufferedReader reader = new BufferedReader(new FileReader(filename + ".csv"))) {
			String line;

			while ((line = reader.readLine()) != null) {

				// Expected format:
				// totalCopies, title, author, publicationYear, isbn
				String[] parts = line.split(",");

				if (parts.length != 5) {
					System.out.println("Skipping invalid line: " + line);
					continue;
				}
				
				// Add to the library
				try {
                        String str = parts[4].trim();
                        int num = Integer.parseInt(str.replaceAll("\\D", ""));
                        str = "ISBN-" + num;
                        if(parts[4].trim().equals(str)) {
                            Book book = new Book(parts[1].trim(), parts[2].trim(), parts[4].trim(), Integer.parseInt(parts[3].trim()), Integer.parseInt(parts[0].trim()));
							if(book.getPublicationYear() < 2026) {
								String isbn = book.getIsbn();
								if(findByISBN(isbn) == null) {
									library.put(isbn, book);
									String titleAuthor = book.getTitle() + "-" + book.getAuthor();
									ISBN.put(titleAuthor, isbn);
								}
								else {
									findByISBN(isbn).addCopies(book.getNumberOfCopies());
								}
							}
							
						}
                } catch(RuntimeException e) {
                }
			}

		} catch (IOException e) {
        System.out.println("Error loading file: " + e.getMessage());
		}
		System.out.println("If any lines weren't added, please recheck the format of the file.");
	}
	
	//Used for Testing, Written by J.
	public void printAll() {
		for(Map.Entry<String, Book> entry : library.entrySet()) {
			Book page = entry.getValue();
			System.out.print(page.getNumberOfCopies() + " copies of \"");
			System.out.print(page.getTitle() + "\" by ");
			System.out.print(page.getAuthor() + ". Written in the year ");
			System.out.print(page.getPublicationYear() + ". ISBN: ");
			System.out.println(page.getIsbn());
		}
	}
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Library lib = new Library();
		System.out.print("drop down menu: \n"+
							 "-add <Title> <Author> <ISBN-Number> <Year As Number> <Number of Copies>\n"+
							 "-findByTitleAndAuthor <title> <author>\n"+
							 "-return <isbn>\n"+
							 "-list <isbn>\n"+
							 "-save <filename>\n"+
							 "-load <filename>\n"+
							 "-exit to stop\n"+
							 "-print to print all books in the library\n\n");
		while (true) {
			System.out.print("library> ");
			String line = scanner.nextLine();
			String[] parts = line.split(" ");
			
			/**
             * @author J. Jesus Ibarra
             */
            if (line.startsWith("add")) {
				// Format: add <Title> <Author> <ISBN-Number> <Year As Number> <Number of Copies>
                if(parts.length != 6) {
                    System.out.println("Invalid Format or Missing Information");
                    System.out.println("Format: add <Title> <Author> <ISBN-Number> <Year As Number> <Number of Copies>");
                }
                if(parts.length == 6) {
                    try {
                        String str = parts[3];
                        int num = Integer.parseInt(str.replaceAll("\\D", ""));
                        str = "ISBN-" + num;
                        if(parts[3].equals(str)) {
                            Book book = new Book(parts[1], parts[2], parts[3], Integer.parseInt(parts[4]), Integer.parseInt(parts[5]));
                           if(book.getPublicationYear() < 2026) {
							lib.addBook(book);
							System.out.println(book.getTitle() + " succesfully added to the library.");
}
							else {
							System.out.println("Invalid year of publication \"" + book.getPublicationYear() + "\". Should be before 2026.");
							}
                        }
                        else {
                            System.out.println("ISBN input \"" + parts[3] + "\" is not in ISBN-Number format.");
                        }
                    } catch(RuntimeException e) {
                        System.out.println("Error: " + e.getMessage());
                        System.out.println("Format: add <Title> <Author> <ISBN-Number> <Year As Number> <Number of Copies>");
                    }
                }
                // e.g. add Star_Trek Gene_Roddenberry ISBN-1234 1965 10
            }
			
			/**
			 * @author Ngoc Hang
			 */
			else if (line.startsWith("checkout")) {
				// Format: checkout <isbn>
				try{
					if(parts.length !=2){
						throw new NumberFormatException("checkout <isbn>");
					} else {
						String[] splits= parts[1].split("-");
						if(!splits[0].equals("ISBN")){
							throw new NumberFormatException("ISBN-<digits>");
						}
						lib.checkout(parts[1]);
						System.out.println("\tCheck out completed");
					}
				} catch(Exception e){
					System.out.println("Error "+ e.getMessage());
				}
			}
            
            /**
			 * @author J. Jesus Ibarra
			 */
			else if (line.startsWith("findByTitleAndAuthor")) {
				// Format: findByTitleAndAuthor <title> <author>
				if (parts.length != 3) {
					System.out.println("Usage: findByTitleAndAuthor <title> <author>");
				}
				else {
					try {
						Book b = lib.findByTitleAndAuthor(parts[1], parts[2]);
						System.out.println("ISBN: " + b.getIsbn() + "\nAvailable Copies in Library: " +
							b.getAvailableCopies() + " out of " + b.getNumberOfCopies());
					} catch (RuntimeException e) {
						System.out.println("Error: " + e.getMessage());
				
					}
				}
			}
			
			/**
			 * @author Arnav Pophale @date 12/6/25
			 */
			else if (line.startsWith("return")) {
				// Format: return <isbn>
				if (parts.length != 2) {
					System.out.println("Usage: return <isbn>");
				} else {
					String isbn = parts[1];
					try {
						Book b = lib.findByISBN(isbn);
						b.checkin();
						System.out.println("Returned book: " + isbn);
					} catch (RuntimeException e) {
						System.out.println("Error: " + e.getMessage());
					}
				}
			}
			
			/**
			 * @author Arnav Pophale @date 12/6/25
			 */
			else if (line.startsWith("list")) {
				// Format: list <isbn>
				if (parts.length != 2) {
					System.out.println("Usage: list <isbn>");
				} else {
					String isbn = parts[1];
					try {
						Book b = lib.findByISBN(isbn);
						System.out.println(
							"ISBN: " + b.getIsbn() +
							", Total Copies: " + b.getNumberOfCopies() +
							", Available Copies: " + b.getAvailableCopies()
						);
					} catch (RuntimeException e) {
						System.out.println("Error: " + e.getMessage());
				
					}
				}
			
			}
			/**
			 * @author Arnav Pophale @date 12/6/25
			 */
			else if (line.startsWith("save")) {
				// Format: save <filename>
				if (parts.length != 2) {
					System.out.println("Usage: save <filename>");
				} else {
					lib.save(parts[1]);
					System.out.println("Library saved to " + parts[1]);
				}
			}
			
			/**
			 * @author Arnav Pophale @date 12/6/25
			 */
			else if (line.startsWith("load")) {
				// Format: load <filename>
				if (parts.length != 2) {
					System.out.println("Usage: load <filename>");
				} else {
					String file = parts[1] + ".csv";
					File f = new File(file);
					if(f.exists()) {
						lib.load(parts[1]);
						System.out.println("Library loaded from " + parts[1]);
					}
					else {
						System.out.println("File does not exist or cannot be accessed.");
					}
				}
			}
			else if (line.startsWith("exit")) {
				break;
			}
			
			// Used for testing. Written by J.
			else if(line.startsWith("print")) {
				lib.printAll();
				System.out.println();
			}
		}
	}
}
