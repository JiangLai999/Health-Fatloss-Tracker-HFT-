<template>
  <router-view />
</template>

<script setup>
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');

/* Global CSS Variables - Green Health Theme */
:root {
  --el-color-primary: #16c784;
  --el-color-primary-light-3: #3dd68c;
  --el-color-primary-light-5: #6ee7b7;
  --el-color-primary-light-7: #a7f3d0;
  --el-color-primary-light-8: #d1fae5;
  --el-color-primary-light-9: #ecfdf5;
  --el-color-primary-dark-2: #059669;
  --el-border-radius-base: 10px;

  --color-primary: #16c784;
  --color-primary-dark: #059669;
  --color-primary-light: #3dd68c;
  --color-primary-glow: rgba(22, 199, 132, 0.25);
  --color-bg-light: #ecfdf5;
  --color-border-light: #a7f3d0;

  --color-page-bg: #f0f4f8;
  --color-card-bg: #ffffff;
  --color-text-primary: #0f172a;
  --color-text-secondary: #475569;
  --color-text-muted: #94a3b8;
  --color-border: #e2e8f0;
  --color-border-soft: #f1f5f9;

  --shadow-sm: 0 1px 3px rgba(0,0,0,.06), 0 1px 2px rgba(0,0,0,.04);
  --shadow-md: 0 4px 16px rgba(0,0,0,.07), 0 1px 4px rgba(0,0,0,.04);
  --shadow-lg: 0 10px 40px rgba(0,0,0,.10), 0 2px 8px rgba(0,0,0,.05);
  --shadow-green: 0 8px 24px rgba(22, 199, 132, 0.22);

  --radius-sm: 8px;
  --radius-md: 12px;
  --radius-lg: 16px;
  --radius-xl: 20px;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Inter', 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', sans-serif;
  background-color: var(--color-page-bg);
  color: var(--color-text-primary);
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

#app {
  width: 100%;
  height: 100vh;
}

/* Scrollbar */
::-webkit-scrollbar { width: 6px; height: 6px; }
::-webkit-scrollbar-track { background: transparent; }
::-webkit-scrollbar-thumb { background: #cbd5e1; border-radius: 3px; }
::-webkit-scrollbar-thumb:hover { background: #94a3b8; }

/* Element Plus overrides */
.el-card {
  border: 1px solid var(--color-border) !important;
  border-radius: var(--radius-md) !important;
  box-shadow: var(--shadow-md) !important;
}

.el-card__header {
  padding: 16px 20px !important;
  border-bottom: 1px solid var(--color-border-soft) !important;
  font-weight: 600;
  color: var(--color-text-primary);
}

.el-button--primary {
  background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-primary-dark) 100%) !important;
  border-color: transparent !important;
  font-weight: 500;
  letter-spacing: 0.3px;
}

.el-button--primary:hover {
  box-shadow: var(--shadow-green) !important;
  transform: translateY(-1px);
}

.el-input__wrapper {
  border-radius: var(--radius-sm) !important;
}

.el-input__wrapper:hover,
.el-input__wrapper.is-focus {
  box-shadow: 0 0 0 1px var(--color-primary) inset !important;
}

.el-tag--success {
  background-color: var(--color-bg-light) !important;
  border-color: var(--color-border-light) !important;
  color: var(--color-primary-dark) !important;
}

.el-progress-bar__inner {
  border-radius: 4px !important;
}
</style>
