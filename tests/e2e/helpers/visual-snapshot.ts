import type { Page, TestInfo } from '@playwright/test';

const MAX_SNAPSHOTS_PER_TEST = 6;
const snapshotCounts = new Map<string, number>();

export async function captureStep(page: Page, testInfo: TestInfo, stepName: string): Promise<void> {
  const key = testInfo.titlePath.join(' > ');
  const count = (snapshotCounts.get(key) ?? 0) + 1;
  snapshotCounts.set(key, count);

  if (count > MAX_SNAPSHOTS_PER_TEST) {
    return;
  }

  const safeName = stepName.replace(/[^a-z0-9-]+/gi, '-').toLowerCase();
  const fileName = `${String(count).padStart(2, '0')}-${safeName}.png`;

  await page.waitForLoadState('networkidle').catch(() => void 0);
  await page.screenshot({ path: testInfo.outputPath(fileName), fullPage: true });
}
