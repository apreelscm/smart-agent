#!/bin/sh
set -e

# Find the first index.html under dist/ and copy it as 404.html next to it.
# Exits 0 if no index.html is found (safe for CI where dist/ may be absent).
index_path=$(find dist -type f -name index.html | head -n 1)
if [ -z "$index_path" ]; then
  echo "No index.html found in dist/ - nothing to copy"
  exit 0
fi

dest_dir=$(dirname "$index_path")
cp "$index_path" "$dest_dir/404.html"
echo "Copied $index_path -> $dest_dir/404.html"

