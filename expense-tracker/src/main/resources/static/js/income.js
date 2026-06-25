if (!localStorage.getItem('token')) window.location.href = 'index.html';

const user = JSON.parse(localStorage.getItem('user') || '{}');
document.getElementById('userName').textContent = user.name || '';
document.getElementById('date').value = new Date().toISOString().split('T')[0];

const fmt     = new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', maximumFractionDigits: 2 });
const dateFmt = new Intl.DateTimeFormat('en-IN', { day: '2-digit', month: 'short', year: 'numeric' });

let incomeItems = [];

async function loadIncome() {
    try {
        incomeItems = await transactionApi.getIncome();
        renderIncome();
    } catch (err) {
        document.getElementById('incomeList').innerHTML =
            '<div class="error-banner">Could not load income entries.</div>';
    }
}

function renderIncome() {
    const container = document.getElementById('incomeList');
    if (incomeItems.length === 0) {
        container.innerHTML = `
      <div class="ledger">
        <div class="empty-state">
          <p>No income logged yet.</p>
          <span>Entries you add above will show up here.</span>
        </div>
      </div>`;
        return;
    }
    const rows = incomeItems.map(item => `
    <tr>
      <td>${dateFmt.format(new Date(item.transactionDate))}</td>
      <td><span class="category-badge">${item.category}</span></td>
      <td style="color:#8a8f8d">${item.description || '—'}</td>
      <td class="amount income">+ ${fmt.format(item.amount)}</td>
      <td><button class="btn-remove" onclick="removeIncome(${item.id})">Remove</button></td>
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

async function addIncome() {
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
        const created = await transactionApi.addIncome(payload);
        incomeItems.unshift(created);
        renderIncome();
        document.getElementById('amount').value      = '';
        document.getElementById('description').value = '';
    } catch (err) {
        errEl.textContent   = err.message || 'Could not save entry';
        errEl.style.display = 'block';
    }
}

async function removeIncome(id) {
    await transactionApi.deleteById(id);
    incomeItems = incomeItems.filter(i => i.id !== id);
    renderIncome();
}

loadIncome();