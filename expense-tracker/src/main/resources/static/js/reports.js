if (!requireAuth()) throw new Error('Not authenticated');
setUserName();

const MONTHS = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
let currentYear = new Date().getFullYear();

async function init() {
    try {
        const years = await reportApi.getAvailableYears();
        const select = document.getElementById('yearSelect');
        select.innerHTML = years.map(y =>
            `<option value="${y}" ${y === currentYear ? 'selected' : ''}>${y}</option>`
        ).join('');
        currentYear = parseInt(select.value);
    } catch (err) { console.error('Could not load years'); }
    await loadReport();
}

async function loadReport() {
    currentYear = parseInt(document.getElementById('yearSelect').value);
    document.getElementById('loadingMsg').style.display  = 'block';
    document.getElementById('reportContent').style.display = 'none';
    document.getElementById('reportError').style.display   = 'none';
    try {
        const data = await reportApi.getMonthly(currentYear);
        renderReport(data);
        document.getElementById('reportContent').style.display = 'block';
    } catch (err) {
        document.getElementById('reportError').style.display = 'block';
    } finally {
        document.getElementById('loadingMsg').style.display = 'none';
    }
}

function renderReport(data) {
    document.getElementById('rptIncome').textContent  = fmt.format(data.totalIncome  || 0);
    document.getElementById('rptExpense').textContent = fmt.format(data.totalExpense || 0);
    document.getElementById('rptSavings').textContent = fmt.format(data.totalSavings || 0);

    const maxVal = Math.max(...data.months.map(m =>
        Math.max(parseFloat(m.income || 0), parseFloat(m.expense || 0))), 1);

    document.getElementById('monthBody').innerHTML = data.months.map((m, i) => {
        const inc     = parseFloat(m.income  || 0);
        const exp     = parseFloat(m.expense || 0);
        const sav     = parseFloat(m.savings || 0);
        const hasData = inc > 0 || exp > 0;
        const incPct  = (inc / maxVal * 100).toFixed(1);
        const expPct  = (exp / maxVal * 100).toFixed(1);
        return `
      <tr class="${hasData ? 'has-data' : ''}">
        <td><strong>${MONTHS[i]}</strong> ${currentYear}</td>
        <td class="tag-income">${inc > 0 ? fmt.format(inc) : '—'}</td>
        <td class="tag-expense">${exp > 0 ? fmt.format(exp) : '—'}</td>
        <td class="${sav >= 0 ? 'tag-savings' : 'tag-loss'}">${hasData ? fmt.format(sav) : '—'}</td>
        <td class="month-bar-cell">
          ${hasData ? `<div class="month-bar-wrap">
            <div class="month-bar income"  style="width:${incPct}%"></div>
            <div class="month-bar expense" style="width:${expPct}%"></div>
          </div>` : ''}
        </td>
      </tr>`;
    }).join('');
}

document.getElementById('yearSelect').addEventListener('change', loadReport);
init();