if (!requireAuth()) throw new Error('Not authenticated');
setUserName();

async function loadDashboard() {
    try {
        const data = await dashboardApi.getSummary();

        document.getElementById('totalIncome').textContent  = fmt.format(data.totalIncome  || 0);
        document.getElementById('totalExpense').textContent = fmt.format(data.totalExpense || 0);
        document.getElementById('savings').textContent      = fmt.format(data.savings      || 0);

        if (data.savings < 0) {
            document.querySelector('#savingsCard .summary-card__label').textContent = 'DEFICIT';
        }

        renderBreakdown('incomeBreakdown',  data.incomeByCategory  || [], 'income');
        renderBreakdown('expenseBreakdown', data.expenseByCategory || [], 'expense');

    } catch (err) {
        document.getElementById('dashError').style.display = 'block';
    }
}

function renderBreakdown(containerId, items, type) {
    const container = document.getElementById(containerId);
    if (!items.length) {
        container.innerHTML = '<p style="color:var(--muted);font-size:13px;padding:8px 0">No data yet.</p>';
        return;
    }
    const maxAmount = Math.max(...items.map(i => parseFloat(i.amount)));
    container.innerHTML = items.slice(0, 6).map(item => {
        const pct = maxAmount > 0 ? (parseFloat(item.amount) / maxAmount * 100) : 0;
        return `
      <div class="breakdown-item">
        <span class="breakdown-item__label">${item.category}</span>
        <div class="breakdown-item__bar-wrap">
          <div class="breakdown-item__bar ${type}" style="width:${pct}%"></div>
        </div>
        <span class="breakdown-item__amount">${fmt.format(item.amount)}</span>
      </div>`;
    }).join('');
}

loadDashboard();