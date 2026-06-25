if (!localStorage.getItem('token')) window.location.href = 'index.html';

const user = JSON.parse(localStorage.getItem('user') || '{}');
document.getElementById('userName').textContent = user.name || '';
document.getElementById('date').value = new Date().toISOString().split('T')[0];

const fmt     = new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', maximumFractionDigits: 2 });
const dateFmt = new Intl.DateTimeFormat('en-IN', { day: '2-digit', month: 'short', year: 'numeric' });

let expenseItems = [];

async function loadExpenses() {
    try {
        expenseItems = await transactionApi.getExpenses();
        renderExpenses();
    } catch (err) {
        document.getElementById('expenseList').innerHTML =
            '<div class="error-banner">Could not load expense entries.</div>';
    }
}

function renderExpenses() {
    const container = document.getElementById('expenseList');
    if (expenseItems.length === 0) {
        container.innerHTML = `
      <div class="ledger">
        <div class="empty-state">
          <p>No expenses logged yet.</p>
          <span>Entries you add above will show up here.</span>
        </div>
      </div>`;
        return;
    }
    const rows = expenseItems.map(item => `
    <tr>
      <td>${dateFmt.format(new Date(item.transactionDate))}</td>
      <td><span class="category-badge">${item.category}</span></td>
      <td style="color:#8a8f8d">${item.description || '—'}</td>
      <td class="amount expense">− ${fmt.format(item.amount)}</td>
      <td><button class="btn-remove" onclick="removeExpense(${item.id})">Remove</button></td>
    </tr>`).join('');
    container.innerHTML = `
    <div class="ledger">
      <table>
        <thead>
          <tr>
            <th>Date</th><th>Category</th>
            <th>Note</th><th class="amount">Amount</th><th></th>
          </tr>
        </thead>
        <tbody>${rows}</tbody>
      </table>
    </div>`;
}

async function addExpense() {
    const errEl  = document.getElementById('formError');
    errEl.style.display = 'none';
    const amount = parseFloat(document.getElementById('amount').value);
    if (!amount || amount <= 0) {
        errEl.textContent = 'Enter an amount greater than 0';
        errEl.style.display = 'block';
        return;
    }
    const payload = {
        category:        document.getElementById('category').value,
        amount,
        description:     document.getElementById('description').value,
        transactionDate: document.getElementById('date').value,
    };
    try {
        const created = await transactionApi.addExpense(payload);
        expenseItems.unshift(created);
        renderExpenses();
        document.getElementById('amount').value      = '';
        document.getElementById('description').value = '';
    } catch (err) {
        errEl.textContent   = err.message || 'Could not save entry';
        errEl.style.display = 'block';
    }
}

async function removeExpense(id) {
    await transactionApi.deleteById(id);
    expenseItems = expenseItems.filter(i => i.id !== id);
    renderExpenses();
}

loadExpenses();