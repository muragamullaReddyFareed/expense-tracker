if (!requireAuth()) throw new Error('Not authenticated');
setUserName();

document.getElementById('txDate').value = new Date().toISOString().split('T')[0];

let expenseItems = [];

async function loadCategories() {
    try {
        const cats = await categoryApi.getByType('EXPENSE');
        const select       = document.getElementById('category');
        const filterSelect = document.getElementById('filterCategory');
        const defaults = ['Groceries','Rent','Utilities','Transport','Entertainment','Healthcare','Education','Shopping','Food','Other'];
        const allCats  = [...new Set([...defaults, ...cats.map(c => c.name)])];
        select.innerHTML       = allCats.map(c => `<option value="${c}">${c}</option>`).join('');
        filterSelect.innerHTML = '<option value="">All Categories</option>' +
            allCats.map(c => `<option value="${c}">${c}</option>`).join('');
    } catch (err) { console.error('Could not load categories'); }
}

async function loadExpenses(params = {}) {
    document.getElementById('loadingMsg').style.display = 'block';
    document.getElementById('expenseList').innerHTML = '';
    try {
        expenseItems = await transactionApi.getExpenses(buildQueryString(params));
        renderExpenses();
    } catch (err) {
        document.getElementById('expenseList').innerHTML =
            '<div class="error-banner" style="margin:16px 20px">Could not load expense entries.</div>';
    } finally {
        document.getElementById('loadingMsg').style.display = 'none';
    }
}

function renderExpenses() {
    const container = document.getElementById('expenseList');
    document.getElementById('resultCount').textContent = expenseItems.length + ' entries';
    if (!expenseItems.length) {
        container.innerHTML = `
      <div class="empty-state">
        <div class="empty-state__icon">📭</div>
        <p>No expense entries found</p>
        <span>Try adjusting your filters or add a new entry above.</span>
      </div>`;
        return;
    }
    container.innerHTML = `
    <table>
      <thead>
        <tr><th>Date</th><th>Category</th><th>Note</th><th class="amount">Amount</th><th></th></tr>
      </thead>
      <tbody>
        ${expenseItems.map(item => `
          <tr>
            <td>${formatDate(item.transactionDate)}</td>
            <td><span class="category-pill">${item.category}</span></td>
            <td class="desc-text">${item.description || '—'}</td>
            <td class="amount expense">− ${fmt.format(item.amount)}</td>
            <td><button class="btn-danger" onclick="removeExpense(${item.id})">Remove</button></td>
          </tr>`).join('')}
      </tbody>
    </table>`;
}

async function addExpense() {
    hideError('formError');
    const amount = parseFloat(document.getElementById('amount').value);
    if (!amount || amount <= 0) { showError('formError', 'Enter an amount greater than 0'); return; }
    const payload = {
        category:        document.getElementById('category').value,
        amount,
        description:     document.getElementById('description').value.trim(),
        transactionDate: document.getElementById('txDate').value,
    };
    try {
        const btn = document.getElementById('addBtn');
        btn.disabled = true; btn.textContent = 'Saving…';
        await transactionApi.addExpense(payload);
        document.getElementById('amount').value = '';
        document.getElementById('description').value = '';
        await loadExpenses();
    } catch (err) {
        showError('formError', err.message || 'Could not save entry');
    } finally {
        const btn = document.getElementById('addBtn');
        btn.disabled = false; btn.textContent = '+ Add Expense';
    }
}

async function removeExpense(id) {
    if (!confirm('Remove this expense entry?')) return;
    try {
        await transactionApi.deleteById(id);
        expenseItems = expenseItems.filter(i => i.id !== id);
        renderExpenses();
    } catch (err) { alert('Could not remove entry'); }
}

async function applyFilters() {
    await loadExpenses({
        keyword:   document.getElementById('search').value.trim(),
        category:  document.getElementById('filterCategory').value,
        startDate: document.getElementById('startDate').value,
        endDate:   document.getElementById('endDate').value,
    });
}

function clearFilters() {
    ['search','filterCategory','startDate','endDate'].forEach(id => {
        document.getElementById(id).value = '';
    });
    loadExpenses();
}

loadCategories();
loadExpenses();