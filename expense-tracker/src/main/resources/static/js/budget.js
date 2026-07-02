if (!requireAuth()) throw new Error('Not authenticated');
setUserName();

const now = new Date();
let selectedMonth = now.getMonth() + 1;
let selectedYear  = now.getFullYear();

// ── Init month/year selectors ────────────────────────────────────
function initSelectors() {
    document.getElementById('monthSelect').value = selectedMonth;

    const yearSelect = document.getElementById('yearSelect');
    for (let y = now.getFullYear() - 2; y <= now.getFullYear() + 1; y++) {
        const opt = document.createElement('option');
        opt.value = y;
        opt.textContent = y;
        if (y === selectedYear) opt.selected = true;
        yearSelect.appendChild(opt);
    }

    document.getElementById('monthSelect')
        .addEventListener('change', onPeriodChange);
    document.getElementById('yearSelect')
        .addEventListener('change', onPeriodChange);
}

function onPeriodChange() {
    selectedMonth = parseInt(document.getElementById('monthSelect').value);
    selectedYear  = parseInt(document.getElementById('yearSelect').value);
    loadBudget();
}

// ── Load budget for selected month ───────────────────────────────
async function loadBudget() {
    const container = document.getElementById('budgetSummary');
    try {
        const data = await budgetApi.getMonthly(selectedMonth, selectedYear);
        document.getElementById('budgetAmount').value = data.budgetAmount;
        renderSummary(container, data);
    } catch (err) {
        // 404 = no budget set yet for this month
        container.innerHTML = `
      <div class="form-card" style="text-align:center;padding:40px">
        <div style="font-size:36px;margin-bottom:12px">🎯</div>
        <p style="font-weight:600;color:var(--ink-soft);margin-bottom:4px">
          No budget set for this month
        </p>
        <span style="font-size:13px;color:var(--muted)">
          Enter an amount above and click Save Budget.
        </span>
      </div>`;
        document.getElementById('budgetAmount').value = '';
    }
}

// ── Render budget summary card ───────────────────────────────────
function renderSummary(container, data) {
    const spent     = parseFloat(data.spentAmount);
    const budget    = parseFloat(data.budgetAmount);
    const remaining = parseFloat(data.remainingAmount);
    const pct       = budget > 0
        ? Math.min((spent / budget) * 100, 100).toFixed(1)
        : 0;

    const MONTHS = ['','January','February','March','April','May','June',
        'July','August','September','October','November','December'];

    container.innerHTML = `
    <div class="form-card">
      <div style="display:flex;justify-content:space-between;
                  align-items:center;margin-bottom:20px">
        <h2 style="margin:0">
          ${MONTHS[data.month]} ${data.year} — Budget Overview
        </h2>
        <button class="btn-danger"
                onclick="deleteBudget(${data.id})">
          Remove Budget
        </button>
      </div>

      <!-- Three stat boxes -->
      <div class="summary-grid" style="margin-bottom:24px">
        <div class="summary-card income">
          <span class="summary-card__label">Budget Set</span>
          <span class="summary-card__amount">
            ${fmt.format(data.budgetAmount)}
          </span>
          <span class="summary-card__sub">Monthly limit</span>
        </div>
        <div class="summary-card expense">
          <span class="summary-card__label">Amount Spent</span>
          <span class="summary-card__amount">
            ${fmt.format(data.spentAmount)}
          </span>
          <span class="summary-card__sub">Total expenses this month</span>
        </div>
        <div class="summary-card savings">
          <span class="summary-card__label">Remaining</span>
          <span class="summary-card__amount"
                style="color:${remaining < 0
        ? 'var(--expense)' : 'var(--income)'}">
            ${fmt.format(Math.abs(remaining))}
            ${remaining < 0 ? ' over' : ' left'}
          </span>
          <span class="summary-card__sub">Budget minus spent</span>
        </div>
      </div>

      <!-- Status message -->
      <div style="margin-bottom:12px;
                  display:flex;justify-content:space-between;
                  font-size:14px">
        <span style="color:var(--ink-soft);font-weight:500">
          Spent ${pct}% of monthly budget
        </span>
        <span style="font-weight:700;color:${
        pct >= 100 ? 'var(--expense)'
            : pct >= 80 ? 'var(--accent)'
                : 'var(--income)'}">
          ${pct >= 100 ? '🔴 Exceeded'
        : pct >= 80 ? '🟡 Near limit'
            : '🟢 On track'}
        </span>
      </div>

      <!-- Progress bar -->
      <div style="background:var(--paper);border-radius:8px;
                  height:14px;overflow:hidden;
                  border:1px solid var(--border)">
        <div style="
          height:100%;
          width:${pct}%;
          border-radius:8px;
          background:${
        pct >= 100 ? 'var(--expense)'
            : pct >= 80 ? 'var(--accent)'
                : 'var(--income)'};
          transition:width 0.6s ease">
        </div>
      </div>
      <div style="display:flex;justify-content:space-between;
                  font-size:12px;color:var(--muted);margin-top:6px">
        <span>₹0</span>
        <span>${fmt.format(data.budgetAmount)}</span>
      </div>
    </div>`;
}

// ── Save budget ──────────────────────────────────────────────────
async function saveMonthlyBudget() {
    hideError('formError');
    const amount = parseFloat(
        document.getElementById('budgetAmount').value);

    if (!amount || amount <= 0) {
        showError('formError', 'Please enter a valid budget amount');
        return;
    }

    try {
        const btn = event.target;
        btn.disabled = true;
        btn.textContent = 'Saving…';

        await budgetApi.setMonthly({
            amount,
            month: selectedMonth,
            year: selectedYear,
            category: null
        });

        await loadBudget();
    } catch (err) {
        showError('formError', err.message || 'Could not save budget');
    } finally {
        const btn = event.target;
        btn.disabled = false;
        btn.textContent = 'Save Budget';
    }
}

// ── Delete budget ────────────────────────────────────────────────
async function deleteBudget(id) {
    if (!confirm('Remove this monthly budget?')) return;
    try {
        await budgetApi.deleteById(id);
        await loadBudget();
    } catch (err) {
        alert('Could not remove budget');
    }
}

// ── Init ─────────────────────────────────────────────────────────
initSelectors();
loadBudget();