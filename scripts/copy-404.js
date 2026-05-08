#!/usr/bin/env node
'use strict';

const fs = require('fs').promises;
const path = require('path');

async function findIndexHtml(dir) {
  let entries;
  try {
    entries = await fs.readdir(dir, { withFileTypes: true });
  } catch (err) {
    return null;
  }

  for (const entry of entries) {
    const full = path.join(dir, entry.name);
    if (entry.isFile() && entry.name.toLowerCase() === 'index.html') {
      return full;
    }
  }

  for (const entry of entries) {
    if (entry.isDirectory()) {
      const found = await findIndexHtml(path.join(dir, entry.name));
      if (found) return found;
    }
  }
  return null;
}

(async () => {
  const distDir = path.join(process.cwd(), 'dist');
  const indexPath = await findIndexHtml(distDir);
  if (!indexPath) {
    console.log('No index.html found in dist/ - nothing to copy');
    process.exit(0);
  }

  const destDir = path.dirname(indexPath);
  const destPath = path.join(destDir, '404.html');
  try {
    await fs.copyFile(indexPath, destPath);
    console.log(`Copied ${indexPath} -> ${destPath}`);
    process.exit(0);
  } catch (err) {
    console.error('Failed to copy index.html to 404.html:', err);
    process.exit(2);
  }
})();

