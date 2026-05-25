import { HttpErrorResponse, HttpInterceptorFn, HttpResponse } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { AuthLoginRequest, AuthLoginResponse } from './auth.service';

interface MockAuthUserRecord {
  password: string;
  response: AuthLoginResponse;
}

const AUTH_LOGIN_ENDPOINT = '/api/auth/login';
const AUTH_LOGOUT_ENDPOINT = '/api/auth/logout';

const MOCK_AUTH_USERS: Record<string, MockAuthUserRecord> = {
  askonieczny: {
    password: 'CzerwiecSierpien2023%',
    response: {
      token:
        'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2tvbmllY3pueSIsInJvbGVzIjpbIkVVTV9QSF9CWk9TIl0sImV4cCI6MTcxNjY1MDAwMH0.fake',
      refreshToken: 'rt_abc123def456',
      user: {
        username: 'askonieczny',
        firstName: 'Adam',
        lastName: 'Skonieczny',
        email: 'askonieczny@eservice.com.pl',
        sellerNumber: 'PH001',
        auwId: 12345,
        roles: ['EUM_PH_BZOS'],
      },
    },
  },
  mmelissa: {
    password: 'QQww123@456@789',
    response: {
      token: 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtbWVsaXNzYSIsInJvbGVzIjpbIkVVTV9aUkQiXSwiZXhwIjoxNzE2NjUwMDAwfQ.fake',
      refreshToken: 'rt_xyz789',
      user: {
        username: 'mmelissa',
        firstName: 'Małgorzata',
        lastName: 'Melissa',
        email: 'malgorzata.melissa@eservice.com.pl',
        sellerNumber: null,
        auwId: 67890,
        roles: ['EUM_ZRD'],
      },
    },
  },
};

export const authMockInterceptor: HttpInterceptorFn = (req, next) => {
  if (req.method === 'POST' && req.url === AUTH_LOGIN_ENDPOINT) {
    return handleLogin(req.body);
  }

  if (req.method === 'POST' && req.url === AUTH_LOGOUT_ENDPOINT) {
    return of(new HttpResponse<void>({ status: 204 }));
  }

  return next(req);
};

function handleLogin(body: unknown): Observable<HttpResponse<AuthLoginResponse>> {
  const credentials = normalizeCredentials(body);

  if (!credentials || credentials.username.length === 0 || credentials.password.length === 0) {
    return throwMockError(400, 'Bad Request', 'AUTH_BLANK_CREDENTIALS', 'Login i hasło są wymagane.');
  }

  if (credentials.username === 'ldap-down') {
    return throwMockError(
      503,
      'Service Unavailable',
      'AUTH_LDAP_UNAVAILABLE',
      'Usługa uwierzytelniania jest chwilowo niedostępna.',
    );
  }

  if (credentials.username === 'norole' && credentials.password === 'validpass') {
    return throwMockError(
      403,
      'Forbidden',
      'AUTH_NO_PERMISSION',
      'Użytkownik nie ma wymaganych uprawnień.',
    );
  }

  const userRecord = MOCK_AUTH_USERS[credentials.username];

  if (!userRecord || userRecord.password !== credentials.password) {
    return throwMockError(
      401,
      'Unauthorized',
      'AUTH_INVALID_CREDENTIALS',
      'Nieprawidłowy login lub hasło.',
    );
  }

  return of(
    new HttpResponse<AuthLoginResponse>({
      status: 200,
      body: userRecord.response,
    }),
  );
}

function normalizeCredentials(body: unknown): AuthLoginRequest | null {
  if (!body || typeof body !== 'object') {
    return null;
  }

  const candidate = body as Partial<AuthLoginRequest>;

  if (typeof candidate.username !== 'string' || typeof candidate.password !== 'string') {
    return null;
  }

  return {
    username: candidate.username.trim(),
    password: candidate.password.trim(),
  };
}

function throwMockError(
  status: number,
  statusText: string,
  errorCode: string,
  message: string,
): Observable<never> {
  return throwError(
    () =>
      new HttpErrorResponse({
        status,
        statusText,
        error: {
          error: errorCode,
          message,
        },
      }),
  );
}
