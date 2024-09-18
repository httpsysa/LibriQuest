<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Book Management</title>

    <!-- Adding Bootstrap for styling -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <!-- Adding Font Awesome for icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

    <style>
        /* Blur effect for background when modal is active */
        .modal-backdrop {
            background-color: rgba(0, 0, 0, 0.3);
        }

        .blur-background {
            filter: blur(5px);
        }

        /* Status color */
        .status-available {
            background-color: green;
            color: white;
            border-radius: 4px;
            padding: 2px 5px;
        }

        .status-borrowed {
            background-color: orange;
            color: white;
            border-radius: 4px;
            padding: 2px 5px;
        }

        /* Custom table styling */
        table {
            border-collapse: collapse;
            width: 100%;
        }

        th,
        td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        th {
            cursor: pointer;
        }

        /* Alternating row colors */
        tbody tr:nth-child(odd) {
            background-color: #f2f2f2;
            /* Light gray for odd rows */
        }

        tbody tr:nth-child(even) {
            background-color: #ffffff;
            /* White for even rows */
        }

        .hidden {
            display: none;
        }

        /* Custom spacing */
        .table-wrapper {
            margin-bottom: 80px;
            /* Adjust space for footer */
        }

        .pagination-controls {
            display: flex;
            justify-content: flex-end;
            align-items: center;
            gap: 10px;
        }

        .page-number.active {
            font-weight: bold;
        }

        .sort-indicator {
            font-size: 12px;
            margin-left: 5px;
            cursor: pointer;
        }

        .custom-header {
            width: 100%;
            height: auto;
            /* This allows the image to scale based on its original size */
            position: fixed;
            top: 0;
            z-index: 10;
            /* Ensures the header stays above other content */
        }

        .custom-header img {
            width: 100%;
            /* Make the image fill the full width of the page */
            height: auto;
            /* Keep the image's aspect ratio */
            object-fit: cover;
            /* Make sure the image covers the header area without stretching */
        }
    </style>
</head>

<body>
    <header class="custom-header">
        <img src="images/header/LessVer.png" type="image/png">
    </header>

    <div class="container mt-5" id="contentWrapper">
        <div class="d-flex justify-content-between mb-4">
            <h1 class="mb-4">Administrator</h1>
            <button id="addBookBtn" class="btn btn-primary">
                <i class="fas fa-plus"></i> Add Book
            </button>
        </div>

        <div class="d-flex justify-content-between mb-3">
            <div class="d-flex align-items-center">
                <select id="pageLength" class="form-select me-2">
                    <option value="10">10</option>
                    <option value="25">25</option>
                    <option value="50">50</option>
                </select>
            </div>
            <div class="d-flex align-items-center">
                <input type="text" id="searchBar" placeholder="Search..." class="form-control">
            </div>
        </div>

        <div class="card table-wrapper" id="contentWrapper">
            <div class="card-header">
                <h4>Book Inventory Details List</h4>
            </div>
            <div class="card-body">
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th data-order="id">ID <span class="sort-indicator">↕️</span></th>
                            <th data-order="title">Title <span class="sort-indicator" id="titleSort">↕️</span></th>
                            <th data-order="author">Author <span class="sort-indicator" id="authorSort">↕️</span></th>
                            <th data-order="publisher">Publisher <span class="sort-indicator"
                                    id="publisherSort">↕️</span></th>
                            <th data-order="genre">Genre</th>
                            <th data-order="published_date">Published Date</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>

                    <tbody id="bookList">
                        <!-- Dynamic content goes here -->
                    </tbody>
                </table>
                <div class="d-flex justify-content-between">
                    <div class="page-length">
                        Showing <span id="currentBooks">0</span> out of <span id="totalBooks">0</span> books
                    </div>
                    <div class="pagination-controls">
                        <button id="prevPage" class="btn btn-secondary" disabled>Previous</button>
                        <div id="pageNumbers"></div>
                        <button id="nextPage" class="btn btn-secondary">Next</button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Add/Edit Book Modal -->
    <div class="modal fade" id="bookModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="modalTitle">Add/Edit Book</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="bookForm">
                        <input type="hidden" id="bookId">
                        <div class="mb-3">
                            <label class="form-label">Title</label>
                            <input type="text" id="title" class="form-control">
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Author</label>
                            <input type="text" id="author" class="form-control">
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Publisher</label>
                            <input type="text" id="publisher" class="form-control">
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Genre</label>
                            <select id="genre" class="form-select">
                                <option value="Non-Fiction">Non-Fiction</option>
                                <option value="Fiction">Fiction</option>
                                <option value="History">History</option>
                                <option value="Philippine History">Philippine History</option>
                                <option value="Other">Other: specify</option>
                            </select>
                        </div>

                        <!-- Input field that will be shown when 'Other: specify' is selected -->
                        <div class="mb-3 hidden" id="otherGenreWrapper">
                            <label class="form-label">Specify Other Genre</label>
                            <input type="text" id="otherGenre" class="form-control" placeholder="Enter genre">
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Published Date</label>
                            <input type="date" id="publishedDate" class="form-control">
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Status</label>
                            <select id="status" class="form-select">
                                <option value="Available">Available</option>
                                <option value="Borrowed">Borrowed</option>
                            </select>
                        </div>
                        <button type="submit" id="saveBook" class="btn btn-primary">Save</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>

    <script>
        $(document).ready(function () {
            // Function to toggle the visibility of the custom genre input field
            $('#genre').on('change', function () {
                if ($(this).val() === 'Other') {
                    $('#otherGenreWrapper').removeClass('hidden');
                } else {
                    $('#otherGenreWrapper').addClass('hidden');
                }
            });

            // Book form submission logic (add or edit)
            $('#bookForm').submit(function (e) {
                e.preventDefault();

                // Get the selected genre
                let genre = $('#genre').val();
                if (genre === 'Other') {
                    genre = $('#otherGenre').val();
                }

                const id = $('#bookId').val();
                const title = $('#title').val();
                const author = $('#author').val();
                const publisher = $('#publisher').val();
                const publishedDate = $('#publishedDate').val();
                const status = $('#status').val();

                const url = id ? 'update_book.php' : 'add_book.php';
                $.ajax({
                    url: url,
                    method: 'POST',
                    data: { id, title, author, publisher, genre, publishedDate, status },
                    success: function () {
                        $('#bookModal').modal('hide');
                        fetchBooks();  // Refresh the table after successful form submission
                    }
                });
            });

            // Variables for managing books and pagination
            let books = [];
            let currentPage = 1;
            let rowsPerPage = 10;
            let orderColumn = 'id';
            let orderDirection = 'asc';

            // Initial fetch to load the books
            fetchBooks();

            // Fetch books from the server
            function fetchBooks() {
                $.ajax({
                    url: 'get_books.php',
                    method: 'GET',
                    success: function (data) {
                        books = JSON.parse(data);
                        renderTable();  // Render table after fetching books
                    }
                });
            }

            // Render the table
            function renderTable() {
                const filteredBooks = filterBooks();
                const sortedBooks = sortBooks(filteredBooks);
                const paginatedBooks = paginateBooks(sortedBooks);

                let tableContent = '';

                // Clear the table first to avoid duplicates
                $('#bookList').empty();

                paginatedBooks.forEach(book => {
                    const statusClass = book.status === 'Available' ? 'status-available' : 'status-borrowed';
                    tableContent += `
                    <tr>
                        <td>${book.id}</td>
                        <td>${book.title}</td>
                        <td>${book.author}</td>
                        <td>${book.publisher}</td>
                        <td>${book.genre}</td>
                        <td>${book.published_date}</td>
                        <td><span class="${statusClass}">${book.status}</span></td>
                        <td>
                            <button onclick="editBook(${book.id})" class="btn btn-outline-primary btn-sm"><i class="fas fa-edit"></i></button>
                            <button onclick="deleteBook(${book.id})" class="btn btn-outline-danger btn-sm"><i class="fas fa-trash"></i></button>
                        </td>
                    </tr>
                `;
                });

                $('#bookList').html(tableContent);
                $('#currentBooks').text(paginatedBooks.length);
                $('#totalBooks').text(books.length);

                // Disable pagination buttons if necessary
                $('#prevPage').prop('disabled', currentPage === 1);
                $('#nextPage').prop('disabled', currentPage * rowsPerPage >= books.length);

                renderPageNumbers(); // Update pagination
            }

            // Filter books based on the search bar input
            function filterBooks() {
                const searchValue = $('#searchBar').val().toLowerCase();
                return books.filter(book =>
                    book.title.toLowerCase().includes(searchValue) ||
                    book.author.toLowerCase().includes(searchValue) ||
                    book.publisher.toLowerCase().includes(searchValue) ||
                    book.genre.toLowerCase().includes(searchValue) ||
                    book.published_date.toLowerCase().includes(searchValue) ||
                    book.status.toLowerCase().includes(searchValue)
                );
            }

            // Sort books based on the selected column and order
            function sortBooks(filteredBooks) {
                return filteredBooks.sort((a, b) => {
                    let comparison = 0;

                    if (orderColumn === 'id') {
                        comparison = b.id - a.id;  // Sort by ID by default (recent first)
                    } else if (orderColumn === 'title') {
                        comparison = a.title.localeCompare(b.title);
                    } else if (orderColumn === 'author') {
                        comparison = a.author.localeCompare(b.author);
                    } else if (orderColumn === 'publisher') {
                        comparison = a.publisher.localeCompare(b.publisher);
                    }

                    return orderDirection === 'asc' ? comparison : -comparison;
                });
            }

            // Paginate the sorted books
            function paginateBooks(sortedBooks) {
                const startIndex = (currentPage - 1) * rowsPerPage;
                const endIndex = startIndex + rowsPerPage;
                return sortedBooks.slice(startIndex, endIndex);
            }

            // Render pagination buttons
            function renderPageNumbers() {
                const totalPages = Math.ceil(books.length / rowsPerPage);
                const pageNumbers = [];

                for (let i = 1; i <= totalPages; i++) {
                    pageNumbers.push(`<button class="btn btn-outline-secondary page-number ${i === currentPage ? 'active' : ''}" data-page="${i}">${i}</button>`);
                }

                $('#pageNumbers').html(pageNumbers.join(' '));
            }

            // Event listeners for searching and pagination
            $('#searchBar').on('keyup', renderTable);

            $('#pageLength').on('change', function () {
                rowsPerPage = parseInt($(this).val());
                renderTable();
            });

            $('#prevPage').on('click', function () {
                if (currentPage > 1) {
                    currentPage--;
                    renderTable();
                }
            });

            $('#nextPage').on('click', function () {
                if (currentPage * rowsPerPage < books.length) {
                    currentPage++;
                    renderTable();
                }
            });

            $('#pageNumbers').on('click', '.page-number', function () {
                currentPage = $(this).data('page');
                renderTable();
            });

            // Column sorting logic
            $('th').on('click', function () {
                const column = $(this).data('order');
                if (column) {
                    if (orderColumn === column) {
                        orderDirection = orderDirection === 'asc' ? 'desc' : 'asc';  // Toggle sorting direction
                    } else {
                        orderColumn = column;
                        orderDirection = 'asc';  // Default to ascending when switching columns
                    }

                    updateSortIndicators();  // Update the sorting arrows
                    renderTable();  // Re-render the table with the new sorting
                }
            });

            // Update sorting indicators (arrows)
            function updateSortIndicators() {
                $('.sort-indicator').text('↕️');  // Reset all to default

                const sortIcon = orderDirection === 'asc' ? '↑' : '↓';
                if (orderColumn === 'title') {
                    $('#titleSort').text(sortIcon);
                } else if (orderColumn === 'author') {
                    $('#authorSort').text(sortIcon);
                } else if (orderColumn === 'publisher') {
                    $('#publisherSort').text(sortIcon);
                }
            }

            // Open the "Add Book" modal and reset the form
            $('#addBookBtn').click(function () {
                $('#bookForm')[0].reset();
                $('#bookModal').modal('show');
                $('#bookId').val('');
                $('#modalTitle').text('Add Book');
            });

            // Modal background blur effect
            $('#bookModal').on('show.bs.modal', function () {
                $('#contentWrapper').addClass('blur-background');
            });

            $('#bookModal').on('hidden.bs.modal', function () {
                $('#contentWrapper').removeClass('blur-background');
            });

            // Edit book function (loads book data into the form)
            window.editBook = function (id) {
                $('#modalTitle').text('Edit Book');
                $.ajax({
                    url: 'get_books.php',
                    method: 'GET',
                    success: function (data) {
                        const book = JSON.parse(data).find(b => b.id == id);
                        $('#bookId').val(book.id);
                        $('#title').val(book.title);
                        $('#author').val(book.author);
                        $('#publisher').val(book.publisher);
                        $('#genre').val(book.genre);
                        $('#publishedDate').val(book.published_date);
                        $('#status').val(book.status);
                        $('#bookModal').modal('show');
                    }
                });
            };

            // Delete book function
            window.deleteBook = function (id) {
                if (confirm('Are you sure you want to delete this book?')) {
                    $.ajax({
                        url: 'delete_book.php',
                        method: 'POST',
                        data: { id },
                        success: function () {
                            fetchBooks();
                        }
                    });
                }
            };
        });
    </script>


</body>

</html>