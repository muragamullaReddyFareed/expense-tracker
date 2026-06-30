if (!requireAuth()) throw new Error('Not authenticated');
setUserName();

let allCategories = [];

async function loadCategories() {
    try {
        allCategories = await categoryApi.getAll();
        renderCategories();
    } catch (err) {
        document.getElementById('catError').style.display = 'block';
    }
}

function renderCategories() {
    renderList('incomeList',  allCategories.filter(c => c.type === 'INCOME'),  'INCOME');
    renderList('expenseList', allCategories.filter(c => c.type === 'EXPENSE'), 'EXPENSE');
}

function renderList(containerId, items, type) {
    const container = document.getElementById(containerId);
    if (!items.length) {
        container.innerHTML = `
      <div class="empty-state" style="padding:24px">
        <p style="font-size:13px">No custom ${type.toLowerCase()} categories yet.</p>
        <span>Add one using the form above.</span>
      </div>`;
        return;
    }
    container.innerHTML = items.map(cat => `
    <div class="category-item">
      <span class="category-item__name">${cat.name}</span>
      <div style="display:flex;align-items:center;gap:10px">
        <span class="category-item__type ${type.toLowerCase()}">${type}</span>
        <button class="btn-danger btn-sm" onclick="deleteCategory(${cat.id})">✕</button>
      </div>
    </div>`).join('');
}

async function addCategory() {
    hideError('formError');
    const name = document.getElementById('catName').value.trim();
    const type = document.getElementById('catType').value;
    if (!name) { showError('formError', 'Category name is required'); return; }
    if (name.length > 50) { showError('formError', 'Name must be under 50 characters'); return; }
    try {
        const btn = document.getElementById('addBtn');
        btn.disabled = true; btn.textContent = 'Adding…';
        await categoryApi.create({ name, type });
        document.getElementById('catName').value = '';
        await loadCategories();
    } catch (err) {
        showError('formError', err.message || 'Could not add category');
    } finally {
        const btn = document.getElementById('addBtn');
        btn.disabled = false; btn.textContent = '+ Add Category';
    }
}

async function deleteCategory(id) {
    if (!confirm('Delete this category?')) return;
    try {
        await categoryApi.deleteById(id);
        await loadCategories();
    } catch (err) { alert('Could not delete category'); }
}

loadCategories();