<script>
    const user = JSON.parse(localStorage.getItem("user")) || JSON.parse(localStorage.getItem("student"));

    if (!user) {
        window.location.href = "login.html";
    }

    async function loadBooks() {
        try {
            const res = await fetch('/api/library/books/available');
            const grid = document.getElementById('booksGrid');
            
            if (!res.ok) {
                grid.innerHTML = "<p>Error loading books.</p>";
                return;
            }
            const books = await res.json();
            
            if (books.length === 0) {
                grid.innerHTML = "<p>No books currently available.</p>";
            } else {
                grid.innerHTML = "";
                books.forEach(b => {
                    const card = document.createElement('div');
                    card.className = 'book-card';
                    card.innerHTML = `
                        <div>
                            <h4>${b.title}</h4>
                            <p><b>Author:</b> ${b.author}</p>
                        </div>
                        <button class="borrow-btn" onclick="borrowBook('${b.id}')">Borrow</button>
                    `;
                    grid.appendChild(card);
                });
            }
        } catch (err) {
            console.error("Error loading books", err);
            document.getElementById('booksGrid').innerHTML = "<p>Error communicating with server.</p>";
        }
    }

    async function loadHistory() {
        try {
            const res = await fetch(`/api/library/history/${user.id}`);
            const grid = document.getElementById('historyGrid');
            
            if (!res.ok) {
                grid.innerHTML = "<p>Error loading history.</p>";
                return;
            }
            const history = await res.json();
            
            if (history.length === 0) {
                grid.innerHTML = "<p>You haven't borrowed any books yet.</p>";
            } else {
                grid.innerHTML = "";
                history.forEach(h => {
                    const card = document.createElement('div');
                    card.className = 'book-card';
                    
                    const borrowedAt = new Date(h.borrowedAt).toLocaleDateString('en-GB');
                    const dueDate = new Date(h.dueDate).toLocaleDateString('en-GB');
                    
                    const detailsDiv = document.createElement("div");
                    
                    const titleEl = document.createElement("h4");
                    titleEl.textContent = h.bookTitle || "Unknown Title";
                    
                    const borrowP = document.createElement("p");
                    borrowP.innerHTML = `<b>Borrowed:</b> ${borrowedAt}`;
                    
                    const dueP = document.createElement("p");
                    dueP.innerHTML = `<b>Due:</b> ${dueDate}`;
                    
                    const statusP = document.createElement("p");
                    const statusBadge = document.createElement("span");
                    const safeStatus = h.status === 'RETURNED' ? 'RETURNED' : 'BORROWED';
                    statusBadge.className = `badge ${safeStatus}`;
                    statusBadge.textContent = h.status;
                    statusP.appendChild(statusBadge);
                    
                    detailsDiv.appendChild(titleEl);
                    detailsDiv.appendChild(borrowP);
                    detailsDiv.appendChild(dueP);
                    detailsDiv.appendChild(statusP);
                    card.appendChild(detailsDiv);

                    if (h.status === "BORROWED") {
                        const returnBtn = document.createElement("button");
                        returnBtn.className = "return-btn";
                        returnBtn.textContent = "Return";
                        returnBtn.onclick = () => returnBook(h.id);
                        card.appendChild(returnBtn);
                    }

                    grid.appendChild(card);
                });
            }
        } catch (err) {
            console.error("Error loading history", err);
            document.getElementById('historyGrid').innerHTML = "<p>Error communicating with server.</p>";
        }
    }

    function borrowBook(bookId) {
        const payload = {
            bookId: bookId,
            studentId: user.id,
            studentName: user.name
        };
        
        fetch('/api/library/borrow', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(payload)
        })
        .then(async res => {
            if (res.ok) {
                alert("Book borrowed successfully!");
                location.reload();
            } else {
                const text = await res.text();
                alert(text || "Failed to borrow book.");
            }
        })
        .catch(err => {
            alert("Network error occurred.");
            console.error(err);
        });
    }

    function returnBook(borrowId) {
        fetch(`/api/library/return/${borrowId}`, {
            method: 'POST'
        })
        .then(async res => {
            if (res.ok) {
                alert("Book returned successfully!");
                location.reload();
            } else {
                const text = await res.text();
                alert(text || "Failed to return book.");
            }
        })
        .catch(err => {
            alert("Network error occurred.");
            console.error(err);
        });
    }

    if (user) {
        loadBooks();
        loadHistory();
    }

</script>
