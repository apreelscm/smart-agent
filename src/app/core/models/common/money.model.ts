/**
 * Represents a monetary value used across the application.
 *
 * Important: persisted and backend semantics remain PLN — all stored amounts
 * represent values in PLN. The widened currency union exists to allow
 * transient UI presentation (EUR / USD) while keeping stored semantics PLN.
 */
export interface Money {
  amount: number;
  currency: 'PLN' | 'EUR' | 'USD';
}
