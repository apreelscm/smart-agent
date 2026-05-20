import { describe, expect, it } from 'vitest';
import {
  decodeDateOfBirthFromPesel,
  isPeselChecksumValid,
  sanitizePeselInput,
  validateAndDecodePesel,
} from './pesel.util';

describe('pesel.util', () => {
  it('sanitizes input to digits only', () => {
    expect(sanitizePeselInput('82a03-031 2340')).toBe('82030312340');
  });

  it('validates and decodes a correct pesel', () => {
    expect(isPeselChecksumValid('82030312340')).toBe(true);
    expect(validateAndDecodePesel('82030312340')).toBe('1982-03-03');
  });

  it('rejects pesel with invalid checksum', () => {
    expect(isPeselChecksumValid('82030312341')).toBe(false);
    expect(validateAndDecodePesel('82030312341')).toBeNull();
  });

  it('rejects pesel with impossible encoded date', () => {
    expect(isPeselChecksumValid('02223112346')).toBe(true);
    expect(validateAndDecodePesel('02223112346')).toBeNull();
  });

  it('decodes birth dates from different centuries', () => {
    expect(decodeDateOfBirthFromPesel('99923112347')).toBe('1899-12-31');
    expect(decodeDateOfBirthFromPesel('01320512343')).toBe('2001-12-05');
    expect(decodeDateOfBirthFromPesel('01610112343')).toBe('2201-01-01');
  });

  it('rejects invalid lengths', () => {
    expect(validateAndDecodePesel('123')).toBeNull();
    expect(validateAndDecodePesel('123456789012')).toBeNull();
  });
});
