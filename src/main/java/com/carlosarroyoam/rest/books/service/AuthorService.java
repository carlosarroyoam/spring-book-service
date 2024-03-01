package com.carlosarroyoam.rest.books.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.carlosarroyoam.rest.books.constant.AppMessages;
import com.carlosarroyoam.rest.books.dto.AuthorResponse;
import com.carlosarroyoam.rest.books.dto.CreateAuthorRequest;
import com.carlosarroyoam.rest.books.dto.UpdateAuthorRequest;
import com.carlosarroyoam.rest.books.entity.Author;
import com.carlosarroyoam.rest.books.mapper.AuthorMapper;
import com.carlosarroyoam.rest.books.repository.AuthorRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class AuthorService {

	private static final Logger log = LoggerFactory.getLogger(AuthorService.class);
	private final AuthorRepository authorRepository;
	private final AuthorMapper authorMapper;

	public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper) {
		this.authorRepository = authorRepository;
		this.authorMapper = authorMapper;
	}

	public List<AuthorResponse> findAll() {
		List<Author> authors = authorRepository.findAll();
		return authorMapper.toDtos(authors);
	}

	public AuthorResponse findById(Long authorId) {
		Author authorById = authorRepository.findById(authorId).orElseThrow(() -> {
			log.warn(AppMessages.AUTHOR_NOT_FOUND_EXCEPTION);
			return new ResponseStatusException(HttpStatus.NOT_FOUND, AppMessages.AUTHOR_NOT_FOUND_EXCEPTION);
		});

		return authorMapper.toDto(authorById);
	}

	@Transactional
	public AuthorResponse create(@Valid CreateAuthorRequest createAuthorRequest) {
		LocalDateTime now = LocalDateTime.now();
		Author author = authorMapper.toEntity(createAuthorRequest);
		author.setCreatedAt(now);
		author.setUpdatedAt(now);

		Author savedAuthor = authorRepository.save(author);
		return authorMapper.toDto(savedAuthor);
	}

	@Transactional
	public void update(Long authorId, @Valid UpdateAuthorRequest updateAuthorRequest) {
		Author authorById = authorRepository.findById(authorId).orElseThrow(() -> {
			log.warn(AppMessages.AUTHOR_NOT_FOUND_EXCEPTION);
			return new ResponseStatusException(HttpStatus.NOT_FOUND, AppMessages.AUTHOR_NOT_FOUND_EXCEPTION);
		});

		authorById.setName(updateAuthorRequest.getName());
		authorById.setUpdatedAt(LocalDateTime.now());

		authorRepository.save(authorById);
	}

	@Transactional
	public void deleteById(Long authorId) {
		boolean existsAuthorById = authorRepository.existsById(authorId);
		if (Boolean.FALSE.equals(existsAuthorById)) {
			log.warn(AppMessages.AUTHOR_NOT_FOUND_EXCEPTION);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, AppMessages.AUTHOR_NOT_FOUND_EXCEPTION);
		}

		authorRepository.deleteById(authorId);
	}

}
