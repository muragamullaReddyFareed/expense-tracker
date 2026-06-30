const API_BASE = 'http://localhost:8080/api';

function getToken() { return localStorage.getItem('token'); }

async function request(method, url, body = null) {
    const headers = { 'Content-Type': 'application/json' };
    const token = getToken();
    if (token) headers['Authorization'] = 'Bearer ' + token;
    const options = { method, headers };
    if (body) options.body = JSON.stringify(body);
    const response = await fetch(API_BASE + url, options);
    if (response.status === 401) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = '/index.html';
        return;
    }
    if (response.status === 204) return null;
    const data = await response.json();
    if (!response.ok) throw data;
    return data;
}

const authApi = {
    register: (p) => request('POST', '/auth/register', p),
    login:    (p) => request('POST', '/auth/login', p),
};

const transactionApi = {
    addIncome:   (p)        => request('POST', '/transactions/income', p),
    addExpense:  (p)        => request('POST', '/transactions/expenses', p),
    getIncome:   (params='') => request('GET', '/transactions/income'   + params),
    getExpenses: (params='') => request('GET', '/transactions/expenses' + params),
    deleteById:  (id)       => request('DELETE', '/transactions/' + id),
};

const dashboardApi = {
    getSummary: () => request('GET', '/dashboard'),
};

const categoryApi = {
    getAll:     ()     => request('GET',    '/categories'),
    getByType:  (type) => request('GET',    '/categories/type/' + type),
    create:     (p)    => request('POST',   '/categories', p),
    deleteById: (id)   => request('DELETE', '/categories/' + id),
};

const reportApi = {
    getMonthly:       (year) => request('GET', '/reports/monthly?year=' + year),
    getAvailableYears: ()    => request('GET', '/reports/years'),
};

function requireAuth() {
    if (!getToken()) { window.location.href = '/index.html'; return false; }
    return true;
}

function setUserName(id = 'userName') {
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    const el = document.getElementById(id);
    if (el) el.textContent = user.name || '';
}

function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    window.location.href = '/index.html';
}

const fmt = new Intl.NumberFormat('en-IN', {
    style: 'currency', currency: 'INR', maximumFractionDigits: 2
});

const dateFmt = new Intl.DateTimeFormat('en-IN', {
    day: '2-digit', month: 'short', year: 'numeric'
});

function formatDate(dateStr) {
    const [y, m, d] = dateStr.split('-');
    return dateFmt.format(new Date(y, m - 1, d));
}

function showError(id, msg) {
    const el = document.getElementById(id);
    if (el) { el.textContent = msg; el.style.display = 'block'; }
}

function hideError(id) {
    const el = document.getElementById(id);
    if (el) el.style.display = 'none';
}

function buildQueryString(params) {
    const qs = Object.entries(params)
        .filter(([, v]) => v !== null && v !== undefined && v !== '')
        .map(([k, v]) => k + '=' + encodeURIComponent(v))
        .join('&');
    return qs ? '?' + qs : '';
}