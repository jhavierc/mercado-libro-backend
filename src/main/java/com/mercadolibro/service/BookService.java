package com.mercadolibro.service;

import com.mercadolibro.dto.BookReqDTO;
import com.mercadolibro.dto.BookRespDTO;
import com.mercadolibro.dto.PageDTO;
import com.mercadolibro.exception.BookAlreadyExistsException;
import com.mercadolibro.exception.BookNotFoundException;
import com.mercadolibro.exception.NoBooksToShowException;

public interface BookService {

    /**
     * Updates a book identified by its ID.
     *
     * @param id The ID of the book to update.
     * @param bookReqDTO The BookReqDTO object containing updated book information.
     * @return The BookRespDTO representing the updated book.
     * @throws BookNotFoundException If the book with the specified ID is not found.
     * @throws BookAlreadyExistsException If a book with the same ISBN already exists.
     */
    BookRespDTO update(Long id, BookReqDTO bookReqDTO);

    /**
     * Partially updates a book identified by its ID.
     *
     * @param id The ID of the book to partially update.
     * @param bookRespDTO The BookRespDTO object containing partial book information.
     * @return The BookRespDTO representing the partially updated book.
     * @throws BookNotFoundException If the book with the specified ID is not found.
     * @throws BookAlreadyExistsException If a book with the same ISBN already exists.
     */
    BookRespDTO patch(Long id, BookRespDTO bookRespDTO);

    /**
     * Retrieves books. This search can be filtered and sorted in several ways.
     *
     * @param category Not required. It is used in case you want to filter by category.
     * @param publisher Not required. It is used in case you want to filter by publisher.
     * @param releases Not required. It is used when you want to filter the latest news of the month.
     * @param selection Not required. It is used if you want to sort from newer books to older ones or
     *                  from older books to newer ones.
     * @param sort Not required. It is used if you want to sort in ascending order or descending order.
     * @param page The page number being searched.
     * @return A list of BookRespDTO containing books, whether filtered, sorted, or all.
     * @throws NoBooksToShowException If no books are found.
     */
    PageDTO<BookRespDTO> findAll(String category, String publisher, boolean releases,
                                 String sort, String selection, short page);

    /**
     * Saves a new book.
     *
     * @param book The BookReqDTO object representing the book to be saved.
     * @return The BookRespDTO of the newly saved book.
     * @throws BookAlreadyExistsException If a book with the same ISBN already exists.
     */
    BookRespDTO save(BookReqDTO book);

    /**
     * Finds a book by its ID.
     *
     * @param id The ID of the book to retrieve.
     * @return The BookRespDTO representing the book with the given ID.
     * @throws BookNotFoundException If the book with the specified ID is not found.
     */
    BookRespDTO findByID(Long id);

    /**
     * Deletes a book by its ID.
     *
     * @param id The ID of the book to delete.
     * @throws BookNotFoundException If the book with the specified ID is not found.
     */
    void delete(Long id);
}
