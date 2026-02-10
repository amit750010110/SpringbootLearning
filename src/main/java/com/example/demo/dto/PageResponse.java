package com.example.demo.dto;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * PageResponse - A stable DTO for paginated API responses
 *
 * WHY THIS CLASS EXISTS:
 * Spring's PageImpl class structure can change between versions, causing instability
 * in your API responses. This DTO provides a guaranteed stable JSON structure.
 *
 * BENEFITS:
 * 1. Stable JSON structure (won't change with Spring Data updates)
 * 2. Eliminates serialization warnings from Spring Data
 * 3. Gives you control over what pagination metadata is exposed
 * 4. Clean, predictable API responses
 * 5. Easy to version and extend
 *
 * USAGE:
 * Instead of returning Page<T> directly, convert it to PageResponse<T>:
 * Page<User> page = userRepository.findAll(pageable);
 * return new PageResponse<>(page);
 *
 * @param <T> The type of data being paginated (e.g., User, Product, Order)
 */
public class PageResponse<T> {

    // The actual data/content for the current page
    private List<T> content;

    // Current page number (0-indexed: 0 = first page, 1 = second page)
    private int pageNumber;

    // Number of items per page (e.g., 10, 20, 50)
    private int pageSize;

    // Total number of items across all pages in the database
    private long totalElements;

    // Total number of pages
    private int totalPages;

    // Is this the first page? (pageNumber == 0)
    private boolean first;

    // Is this the last page? (pageNumber == totalPages - 1)
    private boolean last;

    // Does this page have any content?
    private boolean empty;

    /**
     * Default constructor (required for JSON deserialization)
     */
    public PageResponse() {
    }

    /**
     * Constructor to convert Spring Data Page to PageResponse
     *
     * This is the main constructor you'll use in your code.
     * It extracts all necessary pagination metadata from a Spring Data Page object.
     *
     * @param page - Spring Data Page object from repository query
     *
     * Example usage:
     * Page<User> userPage = userRepository.findAll(pageable);
     * PageResponse<User> response = new PageResponse<>(userPage);
     */
    public PageResponse(Page<T> page) {
        this.content = page.getContent();              // List of items in current page
        this.pageNumber = page.getNumber();            // Current page number (0-indexed)
        this.pageSize = page.getSize();                // Items per page
        this.totalElements = page.getTotalElements();  // Total items in database
        this.totalPages = page.getTotalPages();        // Total number of pages
        this.first = page.isFirst();                   // Is first page?
        this.last = page.isLast();                     // Is last page?
        this.empty = page.isEmpty();                   // Is empty?
    }

    // Getters and Setters

    /**
     * @return List of items in the current page
     */
    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    /**
     * @return Current page number (0-indexed)
     * Example: 0 = first page, 1 = second page, 2 = third page
     */
    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * @return Number of items per page
     * Example: If size=10, each page can have up to 10 items
     */
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @return Total number of items across all pages
     * Example: If totalElements=100 and pageSize=10, there are 10 pages
     */
    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    /**
     * @return Total number of pages
     * Calculated as: Math.ceil(totalElements / pageSize)
     */
    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * @return true if this is the first page (pageNumber == 0)
     */
    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    /**
     * @return true if this is the last page
     */
    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    /**
     * @return true if the page has no content
     */
    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    /**
     * Example JSON output structure:
     * {
     *   "content": [
     *     {"id": 1, "name": "John Doe", "city": "New York", "email": "john@example.com"},
     *     {"id": 2, "name": "Jane Smith", "city": "Boston", "email": "jane@example.com"}
     *   ],
     *   "pageNumber": 0,
     *   "pageSize": 10,
     *   "totalElements": 100,
     *   "totalPages": 10,
     *   "first": true,
     *   "last": false,
     *   "empty": false
     * }
     */
}