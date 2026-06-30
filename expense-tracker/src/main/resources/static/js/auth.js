function showError(msg) {
    const el = document.getElementById('error');
    if (el) { el.textContent = msg; el.style.display = 'block'; }
}

async function handleLogin(e) {
    e.preventDefault();
    hideError('error');
    const email    = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    try {
        const data = await authApi.login({ email, password });
        localStorage.setItem('token', data.token);
        localStorage.setItem('user', JSON.stringify({ name: data.name, email: data.email }));
        window.location.href = '/dashboard.html';
    } catch (err) {
        showError(err.message || 'Invalid email or password');
    }
}

async function handleRegister(e) {
    e.preventDefault();
    hideError('error');
    const name     = document.getElementById('name').value.trim();
    const email    = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;
    if (password.length < 6) { showError('Password must be at least 6 characters'); return; }
    try {
        const data = await authApi.register({ name, email, password });
        localStorage.setItem('token', data.token);
        localStorage.setItem('user', JSON.stringify({ name: data.name, email: data.email }));
        window.location.href = '/dashboard.html';
    } catch (err) {
        showError(err.message || 'Could not create account');
    }
}