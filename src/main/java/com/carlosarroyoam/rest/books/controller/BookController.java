package com.carlosarroyoam.rest.books.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.carlosarroyoam.rest.books.config.OpenApiConfig;
import com.carlosarroyoam.rest.books.dto.AuthorResponse;
import com.carlosarroyoam.rest.books.dto.BookResponse;
import com.carlosarroyoam.rest.books.dto.CreateBookRequest;
import com.carlosarroyoam.rest.books.dto.UpdateBookRequest;
import com.carlosarroyoam.rest.books.service.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/books")
@Tag(name = "Books", description = "Operations about books")
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
public class BookController {
	private final BookService bookService;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@GetMapping(produces = "application/json")
	@Operation(summary = "Gets the list of books")
	public ResponseEntity<List<BookResponse>> findAll(@RequestParam(required = false, defaultValue = "0") Integer page,
			@RequestParam(required = false, defaultValue = "25") Integer size) {
		List<BookResponse> books = bookService.findAll(page, size);
		return ResponseEntity.ok(books);
	}

	@GetMapping(value = "/{bookId}", produces = "application/json")
	@Operation(summary = "Gets a book by its id")
	public ResponseEntity<BookResponse> findById(@PathVariable Long bookId) {
		BookResponse bookById = bookService.findById(bookId);
		return ResponseEntity.ok(bookById);
	}

	@PostMapping(consumes = "application/json")
	@Operation(summary = "Creates a new book")
	public ResponseEntity<Void> create(@Valid @RequestBody CreateBookRequest createBookRequest,
			UriComponentsBuilder builder) {
		BookResponse createdBook = bookService.create(createBookRequest);
		UriComponents uriComponents = builder.path("/books/{bookId}").buildAndExpand(createdBook.getId());
		return ResponseEntity.created(uriComponents.toUri()).build();
	}

	@PutMapping(value = "/{bookId}", consumes = "application/json")
	@Operation(summary = "Updates a book by its id")
	public ResponseEntity<Void> update(@PathVariable Long bookId,
			@Valid @RequestBody UpdateBookRequest updateBookRequest) {
		bookService.update(bookId, updateBookRequest);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{bookId}")
	@Operation(summary = "Deletes a book by its id")
	public ResponseEntity<Void> deleteById(@PathVariable Long bookId) {
		bookService.deleteById(bookId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping(path = "/{bookId}/authors", produces = "application/json")
	public ResponseEntity<List<AuthorResponse>> findBookAuthors(@PathVariable Long bookId) {
		List<AuthorResponse> authors = bookService.findAuthorsByBookId(bookId);
		return ResponseEntity.ok(authors);
	}
}
