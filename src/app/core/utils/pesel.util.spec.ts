import {
  decodeBirthDateFromPesel,
  isPeselChecksumValid,
  validatePesel,
} from './pesel.util';

describe('pesel.util', () => {
  it('accepts a valid PESEL and returns the decoded date of birth', () => {
    expect(validatePesel('44051401359')).toEqual({
      isValid: true,
      dateOfBirth: '1944-05-14',
      errorCode: null,
    });
  });

  it('rejects a PESEL with an invalid checksum', () => {
    expect(isPeselChecksumValid('44051401358')).toBeFalse();
    expect(validatePesel('44051401358')).toEqual({
      isValid: false,
      dateOfBirth: null,
      errorCode: 'checksum',
    });
  });

  it('rejects non-numeric PESEL input', () => {
    expect(validatePesel('4405140135A')).toEqual({
      isValid: false,
      dateOfBirth: null,
      errorCode: 'format',
    });
  });

  it('rejects a PESEL with an impossible encoded date', () => {
    expect(validatePesel('02223000009')).toEqual({
      isValid: false,
      dateOfBirth: null,
      errorCode: 'date',
    });
  });

  it('decodes century offsets correctly for 2000+ and 1800+ dates', () => {
    expect(decodeBirthDateFromPesel('02210200005')).toBe('2002-01-02');
    expect(decodeBirthDateFromPesel('02891200000')).toBe('1802-09-12');
  });
});
