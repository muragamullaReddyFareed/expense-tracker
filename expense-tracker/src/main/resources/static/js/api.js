const API_BASE = 'http://localhost:8080/api';

function getToken() {
    return localStorage.getItem('token');
}

async function request(method, url, body = null) {
    const headers = { 'Content-Type': 'application/json' };
    const token = getToken();
    if (token) headers['Authorization'] = `Bearer ${token}`;

    const options = { method, headers };
    if (body) options.body = JSON.stringify(body);

    const response = await fetch(API_BASE + url, options);

    if (response.status === 401) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = 'index.html';
        return;
    }

    const data = await response.json();
    if (!response.ok) throw data;
    return data;
}

const authApi = {
    register: (payload) => request('POST', '/auth/register', payload),
    login:    (payload) => request('POST', '/auth/login',    payload),
};

const transactionApi = {
    addIncome:    (payload) => request('POST',   '/transactions/income',    payload),
    addExpense:   (payload) => request('POST',   '/transactions/expenses',  payload),
    getIncome:    ()        => request('GET',    '/transactions/income'),
    getExpenses:  ()        => request('GET',    '/transactions/expenses'),
    deleteById:   (id)      => request('DELETE', `/transactions/${id}`),
    getDashboard: ()        => request('GET',    '/dashboard'),
};