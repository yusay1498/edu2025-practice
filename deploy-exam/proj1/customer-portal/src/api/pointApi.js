// src/api/pointApi.js
const API_BASE = import.meta.env.VITE_API_BASE_URI || '';

async function fetchCustomers() {
  const resp = await fetch(`${API_BASE}/points`);
  if (!resp.ok) {
    const text = await resp.text();
    throw new Error(`HTTP ${resp.status}: ${text}`);
  }
  return resp.json();
}

async function fetchCustomerDetail(customerId) {
  const resp = await fetch(`${API_BASE}/points/${customerId}`);
  if (!resp.ok) {
    const text = await resp.text();
    throw new Error(`HTTP ${resp.status}: ${text}`);
  }
  return resp.json();
}

export { fetchCustomers, fetchCustomerDetail };
