PZU-13 QA Checklist

1. Build & Tests
   - npm run build
   - npm test
   - Ensure new unit tests pass (CurrencyService & CurrencyDisplayPipe)

2. Dev server
   - ng serve
   - Open Offer Wizard -> Variants step:
     - By default currency = PLN; values should match previous behavior.
     - Use currency switch to select EUR / USD:
       - Prices in variant headers and payment rows convert from stored PLN.
       - A small helper text should appear showing approximate PLN equivalent and rate info.
     - Toggle between currencies repeatedly; observe stateless behavior and consistency.
   - Offers & Policies pages:
     - The header contains a currency switch.
     - Summary tiles and premium values convert according to selected currency.

3. Unavailable rates behavior
   - Rename or remove public/mock/currency-rates.json and reload:
     - If there is no cached rates, foreign currency buttons should be disabled and attempting to switch should keep PLN.
     - If there is cached but stale (>24h), the app should use cached rates and attempt background refresh; a console warning may indicate staleness.

4. Edge-case checks
   - Partial rates: modify mock JSON to remove USD; USD button should be disabled and show tooltip "Kurs niedostępny".
   - Large amounts and negative values:
     - Conversion should handle large values; PLN rounding integer, EUR/USD two decimals.
   - Payment plan splitting:
     - Ensure sums shown are derived from PLN totals (no persisted rounding shift).

Notes:
- The implementation uses public/mock/currency-rates.json in dev. Production URL and auth must be configured via environment variables for real NBP integration.
- Selected presentation currency is view-scoped and resets on navigation.
