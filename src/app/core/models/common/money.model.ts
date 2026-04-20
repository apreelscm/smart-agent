/**
 * Represents a monetary value used across the application.
 *
 * Important: persisted and backend semantics remain PLN — all stored amounts
 * represent values in PLN. This model represents stored Money values and thus
 * uses PLN as the canonical currency for persistence/values exchanged with the backend.
 */
export interface Money {
  amount: number;
  currency: 'PLN';
}
