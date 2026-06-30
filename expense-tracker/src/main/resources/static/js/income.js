if (!requireAuth()) throw new Error('Not authenticated');
setUserName();

document.getElementById('txDate').value = new Date().toISOString().split('T')[0];

let incomeItems = [];

async function loadCategories() {
    try {
        const cats = await categoryApi.getByType('INCOME');
        const select       = document.getElementById('category');
        const filterSelect = document.getElementById('filterCategory');
        const defaults = ['Salary','Freelance','Investment','Business','Rental','Gift','Bonus','Other'];
        const allCats  = [...new Set([...defaults, ...cats.map(c => c.name)])];
        select.innerHTML       = allCats.map(c => `<option value="${c}">${c}</option>`).join('');
        filterSelect.innerHTML = '<option value="">All Categories</option>' +
            allCats.map(c => `<option value="${c}">${c}</option>`).join('');
    } catch (err) { console.error('Could not load categories'); }
}

async function loadIncome(params = {}) {
    document.getElementById('loadingMsg').style.display = 'block';
    document.getElementById('incomeList').innerHTML = '';
    try {
        incomeItems = await transactionApi.getIncome(buildQueryString(params));
        renderIncome();
    } catch (err) {
        document.getElementById('incomeList').innerHTML =
            '<div class="error-banner" style="margin:16px 20px">Could not load income entries.</div>';
    } finally {
        document.getElementById('loadingMsg').style.display = 'none';
    }
}

function renderIncome() {
    const container = document.getElementById('incomeList');
    document.getElementById('resultCount').textContent = incomeItems.length + ' entries';
    if (!incomeItems.length) {
        container.innerHTML = `
      <div class="empty-state">
        <div class="empty-state__icon">📭</div>
        <p>No income entries found</p>
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
        ${incomeItems.map(item => `
          <tr>
            <td>${formatDate(item.transactionDate)}</td>
            <td><span class="category-pill">${item.category}</span></td>
            <td class="desc-text">${item.description || '—'}</td>
            <td class="amount income">+ ${fmt.format(item.amount)}</td>
            <td><button class="btn-danger" onclick="removeIncome(${item.id})">Remove</button></td>
          </tr>`).join('')}
      </tbody>
    </table>`;
}

async function addIncome() {
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
        await transactionApi.addIncome(payload);
        document.getElementById('amount').value = '';
        document.getElementById('description').value = '';
        await loadIncome();
    } catch (err) {
        showError('formError', err.message || 'Could not save entry');
    } finally {
        const btn = document.getElementById('addBtn');
        btn.disabled = false; btn.textContent = '+ Add Income';
    }
}

async function removeIncome(id) {
    if (!confirm('Remove this income entry?')) return;
    try {
        await transactionApi.deleteById(id);
        incomeItems = incomeItems.filter(i => i.id !== id);
        renderIncome();
    } catch (err) { alert('Could not remove entry'); }
}

async function applyFilters() {
    await loadIncome({
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
    loadIncome();
}

loadCategories();
loadIncome();