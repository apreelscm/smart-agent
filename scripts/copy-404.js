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

async function copyDirRecursive(src, dest) {
  await fs.mkdir(dest, { recursive: true });
  const entries = await fs.readdir(src, { withFileTypes: true });
  for (const entry of entries) {
    const srcPath = path.join(src, entry.name);
    const destPath = path.join(dest, entry.name);
    if (entry.isDirectory()) {
      await copyDirRecursive(srcPath, destPath);
    } else if (entry.isFile()) {
      await fs.copyFile(srcPath, destPath);
    }
  }
}

(async () => {
  const cwd = process.cwd();
  const distDir = path.join(cwd, 'dist');
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
  } catch (err) {
    console.error('Failed to copy index.html to 404.html:', err);
    process.exit(2);
  }

  // Ensure compatibility with pipelines that expect dist/smart-agent/browser/index.html
  try {
    const aliasBrowserIndex = path.join(distDir, 'smart-agent', 'browser', 'index.html');
    // if alias exists, nothing to do
    try {
      await fs.access(aliasBrowserIndex);
      console.log('Alias path dist/smart-agent/browser/index.html already exists - nothing to do');
    } catch (e) {
      // alias missing - attempt to locate a generated browser dir (parent of indexPath if named 'browser')
      const srcBrowserDir = path.basename(path.dirname(indexPath)).toLowerCase() === 'browser'
        ? path.dirname(indexPath)
        : null;

      if (srcBrowserDir) {
        const destBrowserDir = path.join(distDir, 'smart-agent', 'browser');
        console.log(`Creating alias browser dir: ${destBrowserDir} from ${srcBrowserDir}`);
        await copyDirRecursive(srcBrowserDir, destBrowserDir);
        console.log(`Copied browser output to ${destBrowserDir}`);
      } else {
        // if we can't identify a browser dir, try to copy the whole project folder that contains index
        const projectRoot = path.relative(distDir, indexPath).split(path.sep)[0];
        if (projectRoot) {
          const srcProjectBrowser = path.join(distDir, projectRoot, 'browser');
          try {
            await fs.access(srcProjectBrowser);
            const destBrowserDir = path.join(distDir, 'smart-agent', 'browser');
            console.log(`Creating alias browser dir: ${destBrowserDir} from ${srcProjectBrowser}`);
            await copyDirRecursive(srcProjectBrowser, destBrowserDir);
            console.log(`Copied browser output to ${destBrowserDir}`);
          } catch (err) {
            // nothing more we can do safely
            console.log('No explicit browser directory found to create alias for dist/smart-agent - skipping alias creation');
          }
        }
      }
    }
  } catch (err) {
    console.error('Error while ensuring dist/smart-agent/browser alias:', err);
    // don't fail the whole build for alias creation
  }

