if (!localStorage.getItem('token')) window.location.href = 'index.html';

const user = JSON.parse(localStorage.getItem('user') || '{}');
document.getElementById('userName').textContent = user.name || '';

const fmt = new Intl.NumberFormat('en-IN', {
    style: 'currency', currency: 'INR', maximumFractionDigits: 2
});

async function loadDashboard() {
    try {
        const data = await transactionApi.getDashboard();
        document.getElementById('totalIncome').textContent  = fmt.format(data.totalIncome);
        document.getElementById('totalExpense').textContent = fmt.format(data.totalExpense);
        document.getElementById('savings').textContent      = fmt.format(data.savings);
    } catch (err) {
        alert('Could not load dashboard. Make sure backend is running.');
    }
}

loadDashboard();